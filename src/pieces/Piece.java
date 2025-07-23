package pieces;

import main.Board;

import static main.Board.*;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public abstract class Piece {
    private int row,col,color;
    public static final int WHITE = 1;
    public static final int BLACK = 0;

    public Piece(int color, int row, int col){
        this.color = color;
        this.row = row;
        this.col = col;
    }

    public BufferedImage getImage(String name) {
        BufferedImage image = null;
        try {
            File file = new File("res/pieces/" + name + ".png");
            if (!file.exists()) {
                System.err.println("Image file not found: " + file.getAbsolutePath());
                return null;
            }
            image = ImageIO.read(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }

    public int getColor() {
        return color;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void updatePositionOnly(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public boolean move(int row, int col){
        if (!isMoveValid(row, col)) return false;

        int originalRow = getRow();
        int originalCol = getCol();
        Piece capturedPiece = getPieceAtSquare(row, col);

        addMoveToHistory(this, row, col, capturedPiece != null, capturedPiece);

        board[row][col] = this;
        board[originalRow][originalCol] = null;
        updatePositionOnly(row, col);

        if (isKingInCheck(this.getColor())) {
            board[originalRow][originalCol] = this;
            board[row][col] = capturedPiece;
            updatePositionOnly(originalRow, originalCol);
            return false;
        }

        if (this instanceof Rook) ((Rook) this).wasMoved = true;
        if (this instanceof King) ((King) this).wasMoved = true;
        if (this instanceof Pawn) ((Pawn) this).wasMoved = true;

        Board.colorToMove = 1 - Board.colorToMove;
        return true;
    }

    public abstract boolean isMoveValid(int row, int col);

    public boolean canPieceBlockCheck(int row, int col, int color){
        int originalRow = this.getRow();
        int originalCol = this.getCol();
        Piece capturedPiece = getPieceAtSquare(row, col);

        board[row][col] = this;
        board[originalRow][originalCol] = null;
        this.updatePositionOnly(row, col);

        boolean stillInCheck = isKingInCheck(color);

        board[originalRow][originalCol] = this;
        board[row][col] = capturedPiece;
        this.updatePositionOnly(originalRow, originalCol);

        return !stillInCheck;
    }
}