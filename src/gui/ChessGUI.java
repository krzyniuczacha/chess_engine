package gui;

import main.Board;
import pieces.Piece;
import util.Move;

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
    private java.util.function.Consumer<util.Move> onHumanMove;
    private final int humanColor = Piece.WHITE;

    private Piece draggedPiece;
    private int dragOffsetX, dragOffsetY;
    private int selectedRow = -1;
    private int selectedCol = -1;
    private List<Point> validMoves = new ArrayList<>();
    private boolean gameEnded = false;
    private boolean gameDrawn = false;
    private util.Move lastMove = null;

    public ChessGUI(Board board) {
        this.board = board;
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

            if (board.colorToMove != humanColor) {
                draggedPiece = null;
                selectedRow = -1;
                selectedCol = -1;
                validMoves.clear();
                boardPanel.repaint();
                return;
            }

            int col = e.getX() / TILE_SIZE;
            int guiRow = e.getY() / TILE_SIZE;
            int row = 7 - guiRow;

            Piece clickedPiece = board.getPieceAtSquare(row, col);

            if (clickedPiece != null && clickedPiece.getColor() == humanColor) {
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
                Point mousePos = e.getPoint();
                int repaintSize = TILE_SIZE * 2;
                boardPanel.repaint(mousePos.x - repaintSize/2, mousePos.y - repaintSize/2,
                        repaintSize, repaintSize);
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (draggedPiece != null) {
                int col = e.getX() / TILE_SIZE;
                int guiRow = e.getY() / TILE_SIZE;
                int row = 7 - guiRow;

                boolean isHumanTurn = board.colorToMove == humanColor && draggedPiece.getColor() == humanColor;
                if (!isHumanTurn) {
                    draggedPiece = null;
                    validMoves.clear();
                    boardPanel.repaint();
                    return;
                }

                boolean validMoveFound = false;
                for (Point p : validMoves) {
                    if (p.y == row && p.x == col) {
                        validMoveFound = true;
                        break;
                    }
                }

                if (validMoveFound) {
                    Piece capturedPiece = board.getPieceAtSquare(row, col);
                    util.Move move = new util.Move(draggedPiece, row, col, capturedPiece != null, capturedPiece);

                    if (onHumanMove != null) {
                        onHumanMove.accept(move);
                    }
                }

                draggedPiece = null;
                validMoves.clear();
                boardPanel.repaint();
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

        List<util.Move> moves = board.getAvailableMoves(piece);
        for (util.Move move : moves) {
            validMoves.add(new Point(move.getEndCol(), move.getEndRow()));
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

        if (lastMove != null) {
            g.setColor(new Color(255, 255, 0, 100));

            int startRow = lastMove.getStartRow();
            int startCol = lastMove.getStartCol();
            int guiStartRow = 7 - startRow;
            g.fillRect(startCol * TILE_SIZE, guiStartRow * TILE_SIZE, TILE_SIZE, TILE_SIZE);

            int endRow = lastMove.getEndRow();
            int endCol = lastMove.getEndCol();
            int guiEndRow = 7 - endRow;
            g.fillRect(endCol * TILE_SIZE, guiEndRow * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }

        if (selectedRow != -1) {
            int guiRow = 7 - selectedRow;
            g.setColor(new Color(0, 0, 0, 40));
            g.fillRect(selectedCol * TILE_SIZE, guiRow * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }

        g.setColor(new Color(97, 185, 97, 125));
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
    public void setOnHumanMove(java.util.function.Consumer<util.Move> onHumanMove) {
        this.onHumanMove = onHumanMove;
    }

    public void setLastMove(util.Move lastMove) {
        this.lastMove = lastMove;
    }

    public void repaintBoard() {
        boardPanel.repaint();
    }

    public void showGameOver(main.Board b) {
        boolean draw = b.isDraw();
        boolean mateW = b.isCheckmate(pieces.Piece.WHITE);
        boolean mateB = b.isCheckmate(pieces.Piece.BLACK);

        if (draw) {
            javax.swing.JOptionPane.showMessageDialog(boardPanel, "Draw!", "Game Over",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE);
        } else if (mateW || mateB) {
            String winner = mateW ? "Black" : "White";
            javax.swing.JOptionPane.showMessageDialog(boardPanel, "Checkmate! " + winner + " wins.",
                    "Game Over", javax.swing.JOptionPane.INFORMATION_MESSAGE);
        }
    }
}