package pieces;

import main.Board;

import static main.Board.*;

public class Pawn extends Piece {
    public boolean wasMoved;

    public Pawn(int color, int row, int col) {
        super(color, row, col);
        wasMoved = false;
    }

    @Override
    public boolean move(int row, int col) {
        if (!isMoveValid(row, col)) return false;

        boolean isEnPassant = (Math.abs(col - getCol()) == 1) && !isSquareTaken(row, col);

        if (isEnPassant) {
            int originalRow = getRow();
            int originalCol = getCol();
            Piece capturedPawn = getPieceAtSquare(originalRow, col);

            addMoveToHistory(this, row, col, true, capturedPawn);

            board[row][col] = this;
            board[originalRow][originalCol] = null;
            board[originalRow][col] = null;
            updatePositionOnly(row, col);

            if (isKingInCheck(this.getColor())) {
                board[originalRow][originalCol] = this;
                board[row][col] = null;
                board[originalRow][col] = capturedPawn;
                updatePositionOnly(originalRow, originalCol);
                return false;
            }
            this.wasMoved = true;
            Board.colorToMove = (Board.colorToMove == Piece.WHITE) ? Piece.BLACK : Piece.WHITE;
            return true;
        }

        return super.move(row, col);
    }

    @Override
    public boolean isMoveValid(int row, int col) {
        if (row < 0 || row > 7 || col < 0 || col > 7) return false;

        int deltaRow = row - getRow();
        int deltaCol = Math.abs(col - getCol());
        int direction = (getColor() == WHITE) ? 1 : -1;

        if (deltaCol == 1 && deltaRow == direction) {
            if (isSquareTaken(row, col) && getPieceAtSquare(row, col).getColor() != getColor()) {
                return true;
            }
            return checkForEnPassant(this, row, col);
        }

        if (isSquareTaken(row, col) || deltaCol != 0) return false;

        if (deltaRow == direction) {
            return true;
        }

        if (!wasMoved && deltaRow == 2 * direction) {
            return !isSquareTaken(getRow() + direction, col);
        }

        if (isKingInCheck(getColor())) {
            if (!canPieceBlockCheck(row, col, getColor())) return false;
        }


        return false;
    }
}