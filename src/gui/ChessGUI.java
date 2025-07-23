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

            Piece clickedPiece = Board.getPieceAtSquare(row, col);

            if (clickedPiece != null && clickedPiece.getColor() == Board.colorToMove) {
                draggedPiece = clickedPiece;
                selectedRow = row;
                selectedCol = col;
                dragOffsetX = e.getX() - (col * TILE_SIZE);
                dragOffsetY = e.getY() - (guiRow * TILE_SIZE);
                computeValidMoves(clickedPiece);
                boardPanel.repaint();
            }
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

                if (validMoveFound) {
                    if (draggedPiece.move(row, col)) {
                        if (Board.isCheckmate(Board.colorToMove)) gameEnded = true;
                    }
                }

                draggedPiece = null;
                selectedRow = -1;
                selectedCol = -1;
                validMoves.clear();
                boardPanel.repaint();

                String winner = (Board.colorToMove == Piece.WHITE) ? "Black" : "White";
                if (gameEnded) JOptionPane.showMessageDialog(boardPanel, "Checkmate! " + winner + " wins.", "Game Over", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private void computeValidMoves(Piece piece) {
        validMoves.clear();
        for (int r = 0; r < Board.MAX_ROW; r++) {
            for (int c = 0; c < Board.MAX_COL; c++) {
                if (piece.isMoveValid(r, c)) {
                    validMoves.add(new Point(c, r));
                }
            }
        }
    }

    private void drawBoardAndHighlights(Graphics g) {
        for (int row = 0; row < Board.MAX_ROW; row++) {
            for (int col = 0; col < Board.MAX_COL; col++) {
                int guiRow = 7 - row;
                boolean isLight = (row + col) % 2 == 0;
                g.setColor(isLight ? new Color(240, 217, 181) : new Color(181, 136, 99));
                g.fillRect(col * TILE_SIZE, guiRow * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        }

        if (selectedRow != -1) {
            int guiRow = 7 - selectedRow;
            g.setColor(new Color(100, 200, 100, 125));
            g.fillRect(selectedCol * TILE_SIZE, guiRow * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }

        g.setColor(new Color(0, 0, 0, 40));
        for (Point p : validMoves) {
            int guiRow = 7 - p.y;
            int centerX = p.x * TILE_SIZE + TILE_SIZE / 2;
            int centerY = guiRow * TILE_SIZE + TILE_SIZE / 2;
            int radius = TILE_SIZE / 6;
            g.fillOval(centerX - radius, centerY - radius, radius * 2, radius * 2);
        }
    }

    private void drawPieces(Graphics g) {
        for (int row = 0; row < Board.MAX_ROW; row++) {
            for (int col = 0; col < Board.MAX_COL; col++) {
                Piece p = Board.getPieceAtSquare(row, col);
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