package pieces;

import main.Board;

public class Pawn extends Piece {
    public boolean wasMoved;

    public Pawn(int color, int row, int col) {
        super(color, row, col);
        wasMoved = false;
    }

    public boolean isMoveValid(int row, int col, Board board) {
        int deltaRow = row - getRow();
        int deltaCol = Math.abs(col - getCol());
        int direction = (getColor() == WHITE) ? 1 : -1;

        if (row < 0 || row > 7 || col < 0 || col > 7) return false;

        if (board.isKingInCheck(getColor())) {
            if (!board.canPieceBlockCheck(this, row, col, getColor())) return false;
        }

        if (deltaCol != 0){
            if (canAttackSquare(row, col, board)) return true;
        }

        if (board.isSquareTaken(row, col) || deltaCol != 0) return false;

        if (deltaRow == direction) {
            return true;
        }

        if (!wasMoved && deltaRow == 2 * direction) {
            return !board.isSquareTaken(getRow() + direction, col);
        }

        return false;
    }


    @Override
    public boolean canAttackSquare(int row, int col, Board board) {
        if (row < 0 || row > 7 || col < 0 || col > 7) return false;

        int deltaRow = row - getRow();
        int deltaCol = Math.abs(col - getCol());
        int direction = (getColor() == WHITE) ? 1 : -1;

        if (deltaCol == 1 && deltaRow == direction) {
            if (board.isSquareTaken(row, col) && board.getPieceAtSquare(row, col).getColor() != getColor()) {
                return true;
            }
            return board.checkForEnPassant(this, row, col);
        }

        return false;
    }


}