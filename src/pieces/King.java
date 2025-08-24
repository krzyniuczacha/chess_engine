package pieces;

import main.Board;

public class King extends Piece {
    public boolean wasMoved;

    public King(int color,int row, int col) {
        super(color, row, col);
        wasMoved = false;
    }

    public boolean isMoveValid(int row, int col, Board board) {
        if (row < 0 || row > 7 || col < 0 || col > 7) return false;
        int deltaRow = Math.abs(row - getRow());
        int deltaCol = Math.abs(col - getCol());

        if (deltaRow == 0 && deltaCol == 2 && !wasMoved && !board.isKingInCheck(getColor())) {
            return board.checkForCastling(this, row, col);
        }

        if (deltaRow > 1 || deltaCol > 1) return false;
        if (deltaRow == 0 && deltaCol == 0) return false;

        if (board.isSquareTaken(row, col)) {
            return board.getPieceAtSquare(row, col).getColor() != this.getColor();
        }

        if (board.isSquareAttacked(row, col, 1 - getColor())) return false;

        return true;
    }

    @Override
    public boolean canAttackSquare(int row, int col, Board board) {
        int dr = Math.abs(row - this.getRow());
        int dc = Math.abs(col - this.getCol());
        return (dr <= 1 && dc <= 1 && !(dr == 0 && dc == 0));
    }



}