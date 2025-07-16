package pieces;

import main.Board;

import static main.Board.*;

public class Pawn extends Piece {
    boolean wasMoved;
    boolean isEnPassant;

    public Pawn (int color,int row, int col) {
        super(color, row, col);
        wasMoved = false;
        isEnPassant = false;
    }

    @Override
    public boolean move(int row, int col) {
        if (!isMoveAllowed(row, col)) return false;
        if (isEnPassant) enPassantMove(row, col);
        else setPosition(row, col);
        wasMoved = true;
        return true;
    }

    @Override
    public boolean isMoveAllowed(int row, int col) {
        int deltaRow = row-getRow();
        int deltaCol = col-getCol();
        int color = getColor();


        if (wasMoved && deltaRow != 1) return false;

        if (deltaRow == 1 && Math.abs(deltaCol) == 1){
            if (getPieceAtSquare(row,col) != null) {
                if (((this.getColor()) ^ (getPieceAtSquare(row, col).getColor())) == 0) return false;
            }
            else {
                if (!wasMoved) {
                    if (!checkForEnPassant(this, row, col)) return false;
                    else isEnPassant = true;
                }
            }
        }

        if (deltaRow == 0 || !(deltaCol == 0)) return false;

        if (row < 0 || row >7 || col < 0 || col >7) return false;

        if (color == WHITE) {
             if (deltaRow > 2) return false;
        }
        else {
            if (deltaRow < -2) return false;
        }

        for (int i = 1; i < deltaRow; i++) {
            if (isSquareTaken(getRow() + i, getCol())) return false;
        }

        return true;
    }

    public void enPassantMove(int row, int col) {
        int offset;
        if (getColor() == WHITE) {
             offset= -1;
        }
        else {
             offset = 1;
        }

        setPieceAtSquare(row + offset, col, null);
        setPosition(row,col);

        isEnPassant = false;
    }
}
