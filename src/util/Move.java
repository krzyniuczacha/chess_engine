package util;

import pieces.Piece;

public class Move {
    Piece pieceMoved;
    int startRow, startCol, endRow, endCol;
    boolean wasCapture;
    Piece pieceCaptured;

    public Move(Piece pieceMoved, int endRow, int endCol, boolean wasCapture, Piece pieceCaptured) {
        this.pieceMoved = pieceMoved;
        if (pieceMoved != null) {
            this.startRow = pieceMoved.getRow();
            this.startCol = pieceMoved.getCol();
        }
        this.endRow = endRow;
        this.endCol = endCol;
        this.wasCapture = wasCapture;
        this.pieceCaptured = pieceCaptured;
    }

    public Piece getPieceMoved() {
        return pieceMoved;
    }

    public int getStartRow() {
        return startRow;
    }

    public int getEndRow() {
        return endRow;
    }
}