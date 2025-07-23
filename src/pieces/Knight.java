package pieces;

import static main.Board.*;

public class Knight extends Piece {

    public Knight(int color, int row, int col) {
        super(color, row, col);
    }

    @Override
    public boolean isMoveValid(int row, int col) {
        if (row < 0 || row > 7 || col < 0 || col > 7) return false;

        int deltaRow = Math.abs(row - getRow());
        int deltaCol = Math.abs(col - getCol());

        if (!((deltaRow == 2 && deltaCol == 1) || (deltaRow == 1 && deltaCol == 2))) {
            return false;
        }

        if (isSquareTaken(row, col)) {
            return getPieceAtSquare(row, col).getColor() != this.getColor();
        }

        return true;
    }
}