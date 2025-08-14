package pieces;

import main.Board;
import util.Square;

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

    public Square getSquare(){
        return new Square(row,col);
    }

    public void setPiecePosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public boolean move(int row, int col){
        if (!isMoveValid(row, col)) return false;

        int originalRow = getRow();
        int originalCol = getCol();
        Piece capturedPiece = getPieceAtSquare(row, col);

        movesWithoutCapture = (capturedPiece == null) ? movesWithoutCapture + 1 : 0;
        movesNotWithPawns = (this.getClass() != Pawn.class) ? movesNotWithPawns + 1 : 0;

        addMoveToHistory(this, row, col, capturedPiece != null, capturedPiece);

        board[row][col] = this;
        board[originalRow][originalCol] = null;
        setPiecePosition(row, col);

        if (isKingInCheck(this.getColor())) {
            board[originalRow][originalCol] = this;
            board[row][col] = capturedPiece;
            setPiecePosition(originalRow, originalCol);
            return false;
        }

        if (this.getClass() == Rook.class) ((Rook) this).wasMoved = true;
        if (this.getClass() == King.class) ((King) this).wasMoved = true;
        if (this.getClass() == Pawn.class) ((Pawn) this).wasMoved = true;

        Board.colorToMove = 1 - Board.colorToMove;
        return true;
    }

    public boolean isMoveValid(int row, int col) {
        if (isKingInCheck(getColor())) {
            if (!canPieceBlockCheck(row, col, getColor())) return false;
        }

        if (!canPieceAttackSquare(row, col)) return false;

        return true;
    }

    public boolean canPieceBlockCheck(int row, int col, int color){
        int originalRow = this.getRow();
        int originalCol = this.getCol();
        Piece capturedPiece = getPieceAtSquare(row, col);

        board[row][col] = this;
        board[originalRow][originalCol] = null;
        this.setPiecePosition(row, col);

        boolean stillInCheck = isKingInCheck(color);

        board[originalRow][originalCol] = this;
        board[row][col] = capturedPiece;
        this.setPiecePosition(originalRow, originalCol);

        return !stillInCheck;
    }

    public abstract boolean canPieceAttackSquare(int row, int col);
}