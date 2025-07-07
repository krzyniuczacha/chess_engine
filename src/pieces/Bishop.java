package pieces;

import static main.Board.getPieceAtSquare;
import static main.Board.isSquareTaken;

public class Bishop extends Piece {

    public Bishop(int color,int row, int col) {
        super(color, row, col);
    }


    @Override
    public boolean isMoveAllowed(int row, int col) {
        int deltaRow = row-getRow();
        int deltaCol = col-getCol();

        int direction = 0;

        if (deltaRow == 0 || deltaCol == 0) return false;

        if (!(deltaRow == 0) && !(deltaCol == 0)) return false;

        if (row < 0 || row >7 || col < 0 || col >7) return false;

        if (((this.getColor()) ^ (getPieceAtSquare(row,col).getColor())) == 0) return false;


        if (deltaRow > 0){
            if (deltaCol > 0) direction = 1;
            else direction = 2;
        }
        else {
            if (deltaCol < 0) direction = 3;
            else direction = 4;
        }

        switch (direction){
            case 1:
                for (int length = 0; length < Math.abs(deltaRow); length++) {
                    if (isSquareTaken(getRow() + length, getCol() + length)) return false;
                }
                break;
            case 2:
                for (int length = 0; length < deltaRow; length++) {
                    if (isSquareTaken(getRow() + length, getCol() - length)) return false;
                }
                break;
            case 3:
                for (int length = 0; length < Math.abs(deltaRow); length++) {
                    if (isSquareTaken(getRow() - length, getCol() - length)) return false;
                }
                break;
            case 4:
                for (int length = 0; length < deltaCol; length++) {
                    if (isSquareTaken(getRow() - length, getCol() + length)) return false;
                }
                break;
            default: return false;
        }
        return true;
    }
}

