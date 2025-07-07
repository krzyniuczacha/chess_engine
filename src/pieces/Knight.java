package pieces;

import static main.Board.*;

public class Knight extends Piece {

    public Knight(int color,int row, int col) {
        super(color, row, col);
    }


    @Override
    public boolean isMoveAllowed(int row, int col) {
        int deltaRow = row-getRow();
        int deltaCol = col-getCol();

        if (deltaRow == 0 || deltaCol == 0) return false;

        if (row < 0 || row >7 || col < 0 || col >7) return false;

        if (Math.abs(deltaRow) + Math.abs(deltaCol) == 3) return false;

        if (isSquareTaken(row, col)){
            if (((this.getColor()) ^ (getPieceAtSquare(row,col).getColor())) == 0) return false;
        }

        return true;
    }
}
