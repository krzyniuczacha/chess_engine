package gui;

import main.Board;
import pieces.Piece;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class ChessGUI extends JFrame {
    private final int TILE_SIZE = 80;
    private final Board board;
    private JPanel boardPanel;

    private Piece draggedPiece;
    private int dragOffsetX, dragOffsetY;
    private int selectedRow = -1;
    private int selectedCol = -1;
    private List<Point> validMoves = new ArrayList<>();
    private boolean gameEnded = false;
    private boolean gameDrawn = false;

    public ChessGUI() {
        board = new Board();
        initializeGUI();
    }

    private void initializeGUI() {
        setTitle("Java Chess");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        boardPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawBoardAndHighlights(g);
                drawPieces(g);
                if (draggedPiece != null) {
                    Point mousePos = boardPanel.getMousePosition();
                    if (mousePos != null) {
                        BufferedImage img = draggedPiece.getImage(getImageName(draggedPiece));
                        g.drawImage(img, mousePos.x - dragOffsetX, mousePos.y - dragOffsetY, TILE_SIZE, TILE_SIZE, null);
                    }
                }
            }
        };

        boardPanel.setPreferredSize(new Dimension(TILE_SIZE * 8, TILE_SIZE * 8));
        MouseHandler mouseHandler = new MouseHandler();
        boardPanel.addMouseListener(mouseHandler);
        boardPanel.addMouseMotionListener(mouseHandler);

        add(boardPanel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private class MouseHandler extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            if (gameEnded) return;

            int col = e.getX() / TILE_SIZE;
            int guiRow = e.getY() / TILE_SIZE;
            int row = 7 - guiRow;

            Piece clickedPiece = board.getPieceAtSquare(row, col);

            if (clickedPiece != null && clickedPiece.getColor() == board.colorToMove) {
                draggedPiece = clickedPiece;
                selectedRow = row;
                selectedCol = col;
                dragOffsetX = e.getX() - (col * TILE_SIZE);
                dragOffsetY = e.getY() - (guiRow * TILE_SIZE);
                computeValidMoves(clickedPiece);
            } else {
                draggedPiece = null;
                selectedRow = -1;
                selectedCol = -1;
                validMoves.clear();
            }

            boardPanel.repaint();
        }


        @Override
        public void mouseDragged(MouseEvent e) {
            if (draggedPiece != null) {
                boardPanel.repaint();
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (draggedPiece != null) {
                int col = e.getX() / TILE_SIZE;
                int guiRow = e.getY() / TILE_SIZE;
                int row = 7 - guiRow;

                boolean validMoveFound = false;
                for (Point p : validMoves) {
                    if (p.y == row && p.x == col) {
                        validMoveFound = true;
                        break;
                    }
                }

                boolean moveSucceeded = false;
                if (validMoveFound) {
                    if (board.makeMove(draggedPiece, row, col)) {
                        moveSucceeded = true;
                        if (board.isCheckmate(board.colorToMove)) gameEnded = true;
                        if (board.isDraw()) gameDrawn = true;
                    }
                }

                draggedPiece = null;

                if (moveSucceeded) {
                    selectedRow = row;
                    selectedCol = col;
                    validMoves.clear();
                }

                boardPanel.repaint();

                String winner = (board.colorToMove == Piece.WHITE) ? "Black" : "White";
                if (gameEnded) JOptionPane.showMessageDialog(boardPanel, "Checkmate! " + winner + " wins.", "Game Over", JOptionPane.INFORMATION_MESSAGE);
                if (gameDrawn) JOptionPane.showMessageDialog(boardPanel, "Draw!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    public static String getPromotionChoice(Board board){
        AtomicReference<String> selectedPiece = new AtomicReference<>(null);
        JDialog dialog = new JDialog((Frame) null, "Pawn Promotion", true);
        dialog.setLayout(new FlowLayout());

        String color = (board.colorToMove == Piece.WHITE) ? "white" : "black";

        ImageIcon queenIcon = new ImageIcon("res/pieces/" + color + "-queen.png");
        ImageIcon rookIcon = new ImageIcon("res/pieces/" + color + "-rook.png");
        ImageIcon bishopIcon = new ImageIcon("res/pieces/" + color + "-bishop.png");
        ImageIcon knightIcon = new ImageIcon("res/pieces/" + color + "-knight.png");

        JButton queenButton = new JButton(queenIcon);
        queenButton.addActionListener(e -> {
            selectedPiece.set("Queen");
            dialog.dispose();
        });

        JButton rookButton = new JButton(rookIcon);
        rookButton.addActionListener(e -> {
            selectedPiece.set("Rook");
            dialog.dispose();
        });

        JButton bishopButton = new JButton(bishopIcon);
        bishopButton.addActionListener(e -> {
            selectedPiece.set("Bishop");
            dialog.dispose();
        });

        JButton knightButton = new JButton(knightIcon);
        knightButton.addActionListener(e -> {
            selectedPiece.set("Knight");
            dialog.dispose();
        });


        dialog.add(queenButton);
        dialog.add(rookButton);
        dialog.add(bishopButton);
        dialog.add(knightButton);

        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);

        return (selectedPiece.get() != null) ? selectedPiece.get() : "Queen";
    }

    private void computeValidMoves(Piece piece) {
        validMoves.clear();
        for (int r = 0; r < Board.MAX_ROW; r++) {
            for (int c = 0; c < Board.MAX_COL; c++) {
                if (board.isMoveValid(piece, r, c)) {
                    validMoves.add(new Point(c, r));
                }
            }
        }
    }

    private void drawBoardAndHighlights(Graphics g) {
        for (int row = 0; row < Board.MAX_ROW; row++) {
            for (int col = 0; col < Board.MAX_COL; col++) {
                int guiRow = 7 - row;
                boolean isLight = (row + col) % 2 == 1;
                g.setColor(isLight ? new Color(240, 217, 181) : new Color(181, 136, 99));
                g.fillRect(col * TILE_SIZE, guiRow * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        }

        if (selectedRow != -1) {
            int guiRow = 7 - selectedRow;
            g.setColor(new Color(0, 0, 0, 40));
            g.fillRect(selectedCol * TILE_SIZE, guiRow * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }

        g.setColor(new Color(97, 185, 97, 125)); // Semi-transparent green
        for (Point p : validMoves) {
            int guiRow = 7 - p.y;
            g.fillRect(p.x * TILE_SIZE, guiRow * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }

    }

    private void drawPieces(Graphics g) {
        for (int row = 0; row < Board.MAX_ROW; row++) {
            for (int col = 0; col < Board.MAX_COL; col++) {
                Piece p = board.getPieceAtSquare(row, col);
                if (p != null && p != draggedPiece) {
                    BufferedImage img = p.getImage(getImageName(p));
                    if (img != null) {
                        int guiRow = 7 - row;
                        g.drawImage(img, col * TILE_SIZE, guiRow * TILE_SIZE, TILE_SIZE, TILE_SIZE, null);
                    }
                }
            }
        }
    }

    private String getImageName(Piece piece) {
        String color = (piece.getColor() == Piece.WHITE) ? "white" : "black";
        String pieceType = piece.getClass().getSimpleName().toLowerCase();
        return color + "-" + pieceType;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ChessGUI::new);
    }
}