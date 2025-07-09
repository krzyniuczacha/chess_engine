package pieces;

import static main.Board.*;

public class King extends Piece {
    boolean wasMoved;
    boolean isCastling;

    public King(int color,int row, int col) {
        super(color, row, col);
        wasMoved = false;
    }

    @Override
    public boolean move(int row, int col) {
        if (!isMoveAllowed(row, col)) return false;
        if (isCastling) castlingMove(row, col);
        else setPosition(row, col);
        wasMoved = true;
        return true;
    }

    @Override
    public boolean isMoveAllowed(int row, int col) {
        int deltaRow = row-getRow();
        int deltaCol = col-getCol();

        if (row < 0 || row >7 || col < 0 || col >7) return false;

        if ((row == 0 || row == 7) && (col == 0 || col == 7) && !wasMoved) {
            if (!checkForCastling(this, row, col)) return false;
            else isCastling = true;
        }
        else if (Math.abs(deltaCol) > 1 || Math.abs(deltaRow) > 1) return false;

        if (deltaRow == 0 && deltaCol == 0) return false;

        if (isSquareTaken(row , col)){
            if (((this.getColor()) ^ (getPieceAtSquare(row,col).getColor())) == 0) return false;
        }

        if (isCheckableAfter(this ,row, col)) return false;

        return true;
    }

    public void castlingMove(int row, int col) {
        Piece rook = getPieceAtSquare(row,col);

        if (col == 7){
            this.setPosition(row,col - 1);
            rook.setPosition(row,col - 2);
        }
        else if (col == 0){
            this.setPosition(row,col + 1);
            rook.setPosition(row,col + 2);
        }
    }
}
