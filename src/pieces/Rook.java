package pieces;

import main.Board;

public class Rook extends Piece {
    public boolean wasMoved;

    public Rook(int color,int row, int col) {
        super(color, row, col);
        wasMoved = false;
    }

    @Override
    public boolean canAttackSquare(int row, int col, Board board) {
        if (row < 0 || row > 7 || col < 0 || col > 7) return false;

        int deltaRow = row - getRow();
        int deltaCol = col - getCol();

        if (deltaRow == 0 && deltaCol == 0) return false;
        if (deltaRow != 0 && deltaCol != 0) return false;

        if (board.isSquareTaken(row, col)) {
            if (board.getPieceAtSquare(row, col).getColor() == this.getColor()) {
                return false;
            }
        }

        if (deltaRow == 0) {
            int step = (deltaCol > 0) ? 1 : -1;
            for (int i = 1; i < Math.abs(deltaCol); i++) {
                if (board.isSquareTaken(getRow(), getCol() + i * step)) return false;
            }
        } else {
            int step = (deltaRow > 0) ? 1 : -1;
            for (int i = 1; i < Math.abs(deltaRow); i++) {
                if (board.isSquareTaken(getRow() + i * step, getCol())) return false;
            }
        }
        return true;
    }
}