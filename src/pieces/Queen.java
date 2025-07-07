package pieces;

import static main.Board.getPieceAtSquare;
import static main.Board.isSquareTaken;

public class Queen extends Piece {

    public Queen(int color,int row, int col) {
        super(color, row, col);
    }


    @Override
    public boolean isMoveAllowed(int row, int col) {
        int deltaRow = row-getRow();
        int deltaCol = col-getCol();
        int direction = 0;

        if (deltaRow == 0 && deltaCol == 0) return false;

        if (!(deltaRow == 0) && !(deltaCol == 0)) {
            if (!(Math.abs(deltaRow) == Math.abs(deltaCol))) return false;
        }

        if (row < 0 || row >7 || col < 0 || col >7) return false;

        if (isSquareTaken(row , col)){
            if (((this.getColor()) ^ (getPieceAtSquare(row,col).getColor())) == 0) return false;
        }

        if (((this.getColor()) ^ (getPieceAtSquare(row,col).getColor())) == 0) return false;

        if (deltaRow == 0 && !(deltaCol == 0)){
            if (deltaCol > 0) direction = 1;
            else direction = 5;
        }

        if (deltaCol == 0 && !(deltaRow == 0)){
            if (deltaRow > 0) direction = 3;
            else direction = 7;
        }

        if (deltaRow > 0){
            if (deltaCol > 0) direction = 2;
            else direction = 4;
        }
        else {
            if (deltaCol > 0) direction = 8;
            else direction = 6;
        }


        switch (direction){
            case 1:
                for (int length = 0; length < deltaCol; length++) {
                    if (isSquareTaken(getRow(), getCol() + length)) return false;
                }
                break;
            case 2:
                for (int length = 0; length < deltaRow; length++) {
                    if (isSquareTaken(getRow() + length, getCol() + length)) return false;
                }
                break;
            case 3:
                for (int length = 0; length < deltaRow; length++) {
                    if (isSquareTaken(getRow() + length, getCol())) return false;
                }
                break;
            case 4:
                for (int length = 0; length < deltaRow; length++) {
                    if (isSquareTaken(getRow() + length, getCol() - length)) return false;
                }
                break;
            case 5:
                for (int length = 0; length < Math.abs(deltaCol); length++) {
                    if (isSquareTaken(getRow(), getCol() - length)) return false;
                }
                break;
            case 6:
                for (int length = 0; length < deltaRow; length++) {
                    if (isSquareTaken(getRow() - length, getCol() - length)) return false;
                }
                break;
            case 7:
                for (int length = 0; length < Math.abs(deltaRow); length++) {
                    if (isSquareTaken(getRow() - length, getCol())) return false;
                }
            case 8:
                for (int length = 0; length < deltaRow; length++) {
                    if (isSquareTaken(getRow() - length, getCol() + length)) return false;
                }
                break;
            default: return false;
        }

        return true;
    }
}
