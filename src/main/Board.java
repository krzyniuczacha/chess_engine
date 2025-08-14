package main;

import pieces.*;
import util.Move;

import static util.FEN.fenToBoard;

import java.util.*;

public class Board {
    public final static int MAX_ROW = 8;
    public final static int MAX_COL = 8;
    public static Piece[][] board;
    private static Stack<Move> moveHistory;
    public static String initFenString = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR";
    public static String testPromotion = "4k3/8/8/8/8/8/7P/4K3";
    public static int colorToMove;
    public static int movesWithoutCapture, movesNotWithPawns;

    public Board(){
        board = fenToBoard(initFenString);
        moveHistory = new Stack<>();
        colorToMove = Piece.WHITE;
    }

    public Piece[][] getBoard(){
        return board;
    }

    public static Piece getPieceAtSquare(int row, int col){
        if (row < 0 || row >= MAX_ROW || col < 0 || col >= MAX_COL) return null;
        return board[row][col];
    }

    public static boolean isSquareTaken(int row, int col){
        return getPieceAtSquare(row, col) != null;
    }

    public static void addMoveToHistory(Piece piece, int toRow, int toCol, boolean isCapture, Piece capturedPiece) {
        moveHistory.push(new Move(piece, toRow, toCol, isCapture, capturedPiece));
    }

    public static boolean isKingInCheck(int color) {
        King king = findKing(color);
        if (king == null) return false;
        return isSquareAttacked(king.getRow(), king.getCol(), 1 - color);
    }

    public static King findKing(int color) {
        for (int r = 0; r < MAX_ROW; r++) {
            for (int c = 0; c < MAX_COL; c++) {
                Piece p = board[r][c];
                if (p instanceof King && p.getColor() == color) {
                    return (King) p;
                }
            }
        }
        return null;
    }

    public static boolean isSquareAttacked(int row, int col, int attackerColor) {
        for (int r = 0; r < MAX_ROW; r++) {
            for (int c = 0; c < MAX_COL; c++) {
                Piece p = board[r][c];
                if (p != null && p.getColor() == attackerColor) {
                    if (p.canPieceAttackSquare(row, col)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean checkForCastling(King king, int row, int col){
        if (king.wasMoved || isKingInCheck(king.getColor())) return false;

        int rookCol = col > king.getCol() ? 7 : 0;
        Piece rook = getPieceAtSquare(row, rookCol);
        if (!(rook instanceof Rook) || ((Rook) rook).wasMoved) return false;

        int step = col > king.getCol() ? 1 : -1;
        for (int c = king.getCol() + step; c != rookCol; c += step) {
            if (isSquareTaken(row, c)) return false;
        }

        for (int c = king.getCol(); c != king.getCol() + (3*step); c += step) {
            if (isSquareAttacked(row, c, 1 - king.getColor())) return false;
            if (c == col) break;
        }

        return true;
    }

    public static boolean checkForEnPassant(Pawn pawn, int row, int col) {
        if (moveHistory.isEmpty()) return false;

        int expectedRank = pawn.getColor() == Piece.WHITE ? 4 : 3;
        if (pawn.getRow() != expectedRank) return false;

        Move lastMove = moveHistory.peek();
        Piece lastMovedPiece = lastMove.getPieceMoved();
        if (!(lastMovedPiece instanceof Pawn) || lastMovedPiece.getColor() == pawn.getColor()) return false;

        if (Math.abs(lastMove.getStartRow() - lastMove.getEndRow()) != 2) return false;

        return lastMove.getEndRow() == expectedRank && lastMove.getPieceMoved().getCol() == col;
    }

    public static boolean isCheckmate(int color) {
        if (!isKingInCheck(color)) {
            return false;
        }

        for (int row = 0; row < MAX_ROW; row++) {
            for (int col = 0; col < MAX_COL; col++) {
                if (canCheckBeBlocked(row, col, color)) return false;
            }
        }

        return true;
    }

    public static boolean isDraw(){
        boolean isDraw = false;
        List <Piece> piecesLeft = new ArrayList<>();

        if (movesWithoutCapture == 100 || movesWithoutCapture == 100) return true;

        for (int row = 0; row < MAX_ROW; row++) {
            for (int col = 0; col < MAX_COL; col++) {
                Piece piece = getPieceAtSquare(row, col);
                if (piece != null) piecesLeft.add(piece);
            }
        }

        boolean isStalemate = true;
        for (Piece piece : piecesLeft) {
            if (isThereValidMove(piece)) isStalemate = false;
        }

        if (isStalemate && !isKingInCheck(colorToMove)) return true;

        List <Piece> knightsLeft = new ArrayList<>();
        List <Piece> bishopsLeft = new ArrayList<>();
        List <Piece> pawnsLeft = new ArrayList<>();

        for (Piece piece : piecesLeft) {
            if (piece.getClass() == Pawn.class) pawnsLeft.add(piece);
            if (piece.getClass() == Knight.class) knightsLeft.add(piece);
            if (piece.getClass() == Bishop.class) bishopsLeft.add(piece);
            if (piece.getClass() == Queen.class) return false;
        }

        if (pawnsLeft.isEmpty()) {
            if (knightsLeft.isEmpty() && bishopsLeft.isEmpty()) return true;
            if (knightsLeft.isEmpty() && bishopsLeft.size() == 1) return true;
            if (knightsLeft.size() == 1 && bishopsLeft.isEmpty()) return true;
            if (knightsLeft.isEmpty() && bishopsLeft.size() == 2) {
                Bishop bishop1 = (Bishop) bishopsLeft.get(0);
                Bishop bishop2 = (Bishop) bishopsLeft.get(1);

                if (bishop1.getBishopColor() == bishop2.getBishopColor()) return true;
            }
        }
        /*
        // TODO: dead position draw

        boolean pawnGridlock = true;
        for (Piece pawn : pawnsLeft) {
            if (isThereValidMove(pawn)) pawnGridlock = false;
        }

        boolean isKingTrapped = true;
        King king = findKing(colorToMove);
        List <Square> visitedSquares = new ArrayList<>();
        visitedSquares.add(king.getSquare());
        Queue<Square> availableSquaresQueue = new LinkedList<>();
        availableSquaresQueue.add(king.getSquare());
        while (!availableSquaresQueue.isEmpty()) {
            Square square = availableSquaresQueue.poll();

        }

         */
        return isDraw;
    }

    public static boolean isThereValidMove(Piece piece) {
        for (int row = 0; row < Board.MAX_ROW; row++) {
            for (int col = 0; col < Board.MAX_COL; col++) {
                if (piece.isMoveValid(row, col)) {
                    return true;
                }
            }
        }
        return false;
    }


    public static boolean canCheckBeBlocked(int row, int col, int color) {
        Piece piece = getPieceAtSquare(row, col);
        if (piece != null && piece.getColor() == color) {
            for (int destR = 0; destR < MAX_ROW; destR++) {
                for (int destC = 0; destC < MAX_COL; destC++) {
                    if (piece.isMoveValid(destR, destC)) {
                       if (piece.canPieceBlockCheck(destR, destC, color)) return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean isGameOver(int color){
        if (isCheckmate(color)) return true;
        if (isDraw()) return true;
        return false;
    }

}
