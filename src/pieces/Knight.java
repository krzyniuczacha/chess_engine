package pieces;

import main.Board;

public class Knight extends Piece {

    public Knight(int color, int row, int col) {
        super(color, row, col);
    }

    @Override
    public boolean canAttackSquare(int row, int col, Board board) {
        if (row < 0 || row > 7 || col < 0 || col > 7) return false;

        int deltaRow = Math.abs(row - getRow());
        int deltaCol = Math.abs(col - getCol());

        if (!((deltaRow == 2 && deltaCol == 1) || (deltaRow == 1 && deltaCol == 2))) {
            return false;
        }

        if (board.isSquareTaken(row, col)) {
            return board.getPieceAtSquare(row, col).getColor() != this.getColor();
        }
        return true;
    }
}