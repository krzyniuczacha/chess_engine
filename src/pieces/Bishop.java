package pieces;

import static main.Board.getPieceAtSquare;
import static main.Board.isSquareTaken;

public class Bishop extends Piece {

    public Bishop(int color,int row, int col) {
        super(color, row, col);
    }

    @Override
    public boolean isMoveValid(int row, int col) {
        if (row < 0 || row > 7 || col < 0 || col > 7) return false;

        int deltaRow = row - getRow();
        int deltaCol = col - getCol();

        if (Math.abs(deltaRow) != Math.abs(deltaCol)) return false;
        if (deltaRow == 0) return false;

        if (isSquareTaken(row, col)) {
            if (getPieceAtSquare(row, col).getColor() == this.getColor()) {
                return false;
            }
        }

        int rowStep = (deltaRow > 0) ? 1 : -1;
        int colStep = (deltaCol > 0) ? 1 : -1;

        for (int i = 1; i < Math.abs(deltaRow); i++) {
            if (isSquareTaken(getRow() + i * rowStep, getCol() + i * colStep)) {
                return false;
            }
        }
        return true;
    }
}