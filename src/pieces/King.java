package pieces;

import main.Board;

import static main.Board.*;

public class King extends Piece {
    public boolean wasMoved;

    public King(int color,int row, int col) {
        super(color, row, col);
        wasMoved = false;
    }

    @Override
    public boolean move(int row, int col) {
        if (Math.abs(col - getCol()) == 2 && isMoveValid(row, col)) {
            castlingMove(row, col);
            return true;
        }
        return super.move(row, col);
    }

    @Override
    public boolean isMoveValid(int row, int col) {
        if (row < 0 || row > 7 || col < 0 || col > 7) return false;
        int deltaRow = Math.abs(row - getRow());
        int deltaCol = Math.abs(col - getCol());

        if (deltaRow == 0 && deltaCol == 2 && !wasMoved) {
            return checkForCastling(this, row, col);
        }

        if (deltaRow > 1 || deltaCol > 1) return false;
        if (deltaRow == 0 && deltaCol == 0) return false;

        if (isSquareTaken(row, col)) {
            return getPieceAtSquare(row, col).getColor() != this.getColor();
        }

        if (isSquareAttacked(row, col, 1 - getColor())) return false;

        return true;
    }

    public void castlingMove(int row, int col) {
        int rookStartCol = col > getCol() ? 7 : 0;
        int rookEndCol = col > getCol() ? 5 : 3;
        Piece rook = getPieceAtSquare(row, rookStartCol);

        addMoveToHistory(this, row, col, false, null);

        board[row][col] = this;
        board[getRow()][getCol()] = null;
        setPiecePosition(row, col);

        board[row][rookEndCol] = rook;
        board[row][rookStartCol] = null;
        rook.setPiecePosition(row, rookEndCol);

        this.wasMoved = true;
        ((Rook) rook).wasMoved = true;
        Board.colorToMove = (Board.colorToMove == Piece.WHITE) ? Piece.BLACK : Piece.WHITE;
    }

    @Override
    public boolean canPieceAttackSquare(int row, int col) {
        return isMoveValid(row, col);
    }
}