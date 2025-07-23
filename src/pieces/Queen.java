package pieces;

import static main.Board.*;

public class Queen extends Piece {

    public Queen(int color,int row, int col) {
        super(color, row, col);
    }

    @Override
    public boolean isMoveValid(int row, int col) {
        if (row < 0 || row > 7 || col < 0 || col > 7) return false;

        int deltaRow = row - getRow();
        int deltaCol = col - getCol();

        if (deltaRow == 0 && deltaCol == 0) return false;

        boolean isRookMove = deltaRow == 0 || deltaCol == 0;
        boolean isBishopMove = Math.abs(deltaRow) == Math.abs(deltaCol);

        if (!isRookMove && !isBishopMove) return false;

        if (isSquareTaken(row, col)) {
            if (getPieceAtSquare(row, col).getColor() == this.getColor()) {
                return false;
            }
        }

        if (isRookMove) {
            if (deltaRow == 0) {
                int step = (deltaCol > 0) ? 1 : -1;
                for (int i = 1; i < Math.abs(deltaCol); i++) {
                    if (isSquareTaken(getRow(), getCol() + i * step)) return false;
                }
            } else {
                int step = (deltaRow > 0) ? 1 : -1;
                for (int i = 1; i < Math.abs(deltaRow); i++) {
                    if (isSquareTaken(getRow() + i * step, getCol())) return false;
                }
            }
        } else {
            int rowStep = (deltaRow > 0) ? 1 : -1;
            int colStep = (deltaCol > 0) ? 1 : -1;
            for (int i = 1; i < Math.abs(deltaRow); i++) {
                if (isSquareTaken(getRow() + i * rowStep, getCol() + i * colStep)) return false;
            }
        }

        if (isKingInCheck(getColor())) {
            if (!canPieceBlockCheck(row, col, getColor())) return false;
        }

        return true;
    }
}