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

    public int getStartCol() {
        return startCol;
    }

    public int getEndCol(){
        return endCol;
    }

    public Piece getPieceCaptured() {
        return pieceCaptured;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Move)) return false;
        Move other = (Move) obj;
        return this.startRow == other.startRow
                && this.startCol == other.startCol
                && this.endRow == other.endRow
                && this.endCol == other.endCol
                && this.pieceMoved != null && other.pieceMoved != null
                && this.pieceMoved.getClass() == other.pieceMoved.getClass()
                && this.pieceMoved.getColor() == other.pieceMoved.getColor();
    }
}