package util;

import pieces.King;
import pieces.Pawn;
import pieces.Piece;

public class Move {
    Piece pieceMoved;
    int rowFrom;
    int colFrom;
    int rowTo;
    int colTo;
    boolean wasPieceCaptured;
    Piece otherPiece;

    public Move(Piece pieceMoved, int rowTo, int colTo, boolean wasPieceCaptured, Piece otherPiece) {
        this.pieceMoved = pieceMoved;
        rowFrom = pieceMoved.getRow();
        colFrom = pieceMoved.getCol();
        this.rowTo = rowTo;
        this.colTo = colTo;
        this.wasPieceCaptured = wasPieceCaptured;
        this.otherPiece = otherPiece;
    }

    public Piece getPieceMoved() {
        return pieceMoved;
    }

    public int getRowFrom() {
        return rowFrom;
    }

    public int getColFrom() {
        return colFrom;
    }

    public int getRowTo() {
        return rowTo;
    }

    public int getColTo() {
        return colTo;
    }

    public boolean wasPieceCaptured() {
        return wasPieceCaptured;
    }

    public Piece getOtherPiece() {
        return otherPiece;
    }

    public boolean didPawnMoveTwoSquares() {
        if (pieceMoved.getClass() != Pawn.class) return false;
        return (Math.abs(rowFrom - rowTo) == 2);
    }

    public boolean wasCastling() {
        if (pieceMoved.getClass() != King.class) return false;
        return (Math.abs(colFrom - colTo) == 2);
    }
}
