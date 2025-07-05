package pieces;

import static main.Board.*;

public class Rook extends Piece {

    public Rook(int color,int row, int col) {
        super(color, row, col);
    }

    @Override
    public boolean move(int row, int col) {
        if (!isMoveAllowed(row, col)) return false;

    }

    @Override
    public boolean isMoveAllowed(int row, int col) {
        int deltaRow = row-getRow();
        int deltaCol = col-getCol();

        int direction = 0;

        if (deltaRow == 0 && deltaCol == 0) return false;

        if (!(deltaRow == 0) && !(deltaCol == 0)) return false;

        if (row < 0 || row >7 || col < 0 || col >7) return false;


        if (deltaRow == 0 && !(deltaCol == 0)){
            if (deltaCol > 0) direction = 1;
            else direction = 2;
        }

        if (deltaCol == 0 && !(deltaRow == 0)){
            if (deltaRow > 0 ) direction = 3;
            else direction = 4;
        }

        switch (direction){
            case 1:
                for (int length = 0; length < Math.abs(deltaRow) + Math.abs(deltaCol); length++) {
                    if (isSquareTaken(getRow(), getCol() + length)) return false;
                }
                break;
            case 2:
                for (int length = 0; length < Math.abs(deltaRow) + Math.abs(deltaCol); length++) {
                    if (isSquareTaken(getRow(), getCol() - length)) return false;
                }
                break;
            case 3:
                for (int length = 0; length < Math.abs(deltaRow) + Math.abs(deltaCol); length++) {
                    if (isSquareTaken(getRow() + length, getCol())) return false;
                }
                break;
            case 4:
                for (int length = 0; length < Math.abs(deltaRow) + Math.abs(deltaCol); length++) {
                    if (isSquareTaken(getRow() - length, getCol())) return false;
                }
                break;
            default: return false;
        }
        return true;
    }
}
