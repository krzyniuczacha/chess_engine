package pieces;

import gui.ChessGUI;
import main.Board;

import static main.Board.*;
import static gui.ChessGUI.getPromotionChoice;

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
            setPiecePosition(row, col);

            if (isKingInCheck(this.getColor())) {
                board[originalRow][originalCol] = this;
                board[row][col] = null;
                board[originalRow][col] = capturedPawn;
                setPiecePosition(originalRow, originalCol);
                return false;
            }
            this.wasMoved = true;
            Board.colorToMove = 1 - Board.colorToMove;
            return true;
        }

        boolean isPromotion = (row == 7 && getColor() == WHITE) || (row == 0 && getColor() == BLACK);

        if (isPromotion) {
            int originalRow = getRow();
            int originalCol = getCol();
            Piece capturedPiece = getPieceAtSquare(row, col);

            addMoveToHistory(this, row, col, capturedPiece != null, capturedPiece);

            Piece promoted = getPromotionPiece(colorToMove, row, col);
            board[row][col] = promoted;
            board[originalRow][originalCol] = null;
            setPiecePosition(row, col);

            if (isKingInCheck(this.getColor())) {
                board[originalRow][originalCol] = this;
                board[row][col] = capturedPiece;
                setPiecePosition(originalRow, originalCol);
                return false;
            }

            this.wasMoved = true;
            Board.colorToMove = 1 - Board.colorToMove;
            return true;
        }
        return super.move(row, col);
    }

    @Override
    public boolean isMoveValid(int row, int col) {
        int deltaRow = row - getRow();
        int deltaCol = Math.abs(col - getCol());
        int direction = (getColor() == WHITE) ? 1 : -1;

        if (row < 0 || row > 7 || col < 0 || col > 7) return false;

        if (isKingInCheck(getColor())) {
            if (!canPieceBlockCheck(row, col, getColor())) return false;
        }

        if (deltaCol != 0){
            if (canPieceAttackSquare(row, col)) return true;
        }

        if (isSquareTaken(row, col) || deltaCol != 0) return false;

        if (deltaRow == direction) {
            return true;
        }

        if (!wasMoved && deltaRow == 2 * direction) {
            return !isSquareTaken(getRow() + direction, col);
        }

        return false;
    }


    @Override
    public boolean canPieceAttackSquare(int row, int col) {
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

        return false;
    }

    public static Piece getPromotionPiece(int color, int row, int col) {
        Piece promotedPiece = null;
        String piece = ChessGUI.getPromotionChoice();

        switch (piece) {
            case "Queen":
                promotedPiece = new Queen(color, row, col);
                break;
            case "Rook":
                promotedPiece = new Rook(color, row, col);
                break;
            case "Knight":
                promotedPiece = new Knight(color, row, col);
                break;
            case "Bishop":
                promotedPiece = new Bishop(color, row, col);
                break;
            default:
                promotedPiece = new Queen(color, row, col);
        }


        return promotedPiece;
    }
}