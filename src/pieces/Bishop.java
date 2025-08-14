package pieces;

import static main.Board.*;

public class Bishop extends Piece {
    public static final int lightSquare = 1;
    public static final int darkSquare = 0;

    public Bishop(int color,int row, int col) {
        super(color, row, col);
    }

    public int getBishopColor(){
        return ((getRow() + getCol()) % 2 == 1) ? lightSquare : darkSquare;
    }

    @Override
    public boolean canPieceAttackSquare(int row, int col) {
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
