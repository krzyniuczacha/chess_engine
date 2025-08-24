package main;

import gui.ChessGUI;
import pieces.*;
import players.Player;
import util.Move;

import static util.FEN.fenToBoard;

import java.util.*;

public class Board {
    public final static int MAX_ROW = 8;
    public final static int MAX_COL = 8;
    public Piece[][] board;
    private  Stack<Move> moveHistory;
    public static final String initFenString = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR";
    public static final String testPromotion = "4k3/8/8/8/8/8/7P/4K3";
    public int colorToMove;
    public int movesWithoutCapture, movesNotWithPawns;
    public Player whitePlayer, blackPlayer;

    public Board(){
        board = fenToBoard(testPromotion);
        moveHistory = new Stack<>();
        colorToMove = Piece.WHITE;
    }

    public Board(Board board1){
        board = new Piece[MAX_COL][MAX_ROW];
        for (int i = 0; i < MAX_ROW; i++){
            for (int j = 0; j < MAX_COL; j++){
                board[i][j] = board1.getBoard()[i][j];
            }
        }
        moveHistory = new Stack<>();
        colorToMove = Piece.WHITE;
    }

    public Piece[][] getBoard(){
        return board;
    }

    public Piece getPieceAtSquare(int row, int col){
        if (row < 0 || row >= MAX_ROW || col < 0 || col >= MAX_COL) return null;
        return board[row][col];
    }

    public boolean isSquareTaken(int row, int col){
        return getPieceAtSquare(row, col) != null;
    }

    public void addMoveToHistory(Piece piece, int toRow, int toCol, boolean isCapture, Piece capturedPiece) {
        moveHistory.push(new Move(piece, toRow, toCol, isCapture, capturedPiece));
    }

    public boolean isKingInCheck(int color) {
        King king = findKing(color);
        if (king == null) return false;
        return isSquareAttacked(king.getRow(), king.getCol(), 1 - color);
    }

    public King findKing(int color) {
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

    public boolean isSquareAttacked(int row, int col, int attackerColor) {
        for (int r = 0; r < MAX_ROW; r++) {
            for (int c = 0; c < MAX_COL; c++) {
                Piece p = board[r][c];
                if (p != null && p.getColor() == attackerColor) {
                    if (p.canAttackSquare(row, col, this)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean checkForCastling(King king, int row, int col){
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

    public boolean checkForEnPassant(Pawn pawn, int row, int col) {
        if (moveHistory.isEmpty()) return false;

        int expectedRank = pawn.getColor() == Piece.WHITE ? 4 : 3;
        if (pawn.getRow() != expectedRank) return false;

        Move lastMove = moveHistory.peek();
        Piece lastMovedPiece = lastMove.getPieceMoved();
        if (!(lastMovedPiece instanceof Pawn) || lastMovedPiece.getColor() == pawn.getColor()) return false;

        if (Math.abs(lastMove.getStartRow() - lastMove.getEndRow()) != 2) return false;

        return lastMove.getEndRow() == expectedRank && lastMove.getPieceMoved().getCol() == col;
    }

    public boolean isCheckmate(int color) {
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

    public boolean isDraw(){
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

    public boolean isThereValidMove(Piece piece) {
        return getAvailableMoves(piece) != null;
    }

    public boolean canCheckBeBlocked(int row, int col, int color) {
        Piece piece = getPieceAtSquare(row, col);
        if (piece != null && piece.getColor() == color) {
            for (int destR = 0; destR < MAX_ROW; destR++) {
                for (int destC = 0; destC < MAX_COL; destC++) {
                    if (isMoveValid(piece, destR, destC)) {
                       if (canPieceBlockCheck(piece, destR, destC, color)) return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isGameOver(int color){
        if (isCheckmate(color)) return true;
        if (isDraw()) return true;
        return false;
    }

    public List<Move> getAvailableMoves(int color){
        List<Move> allMoves = new ArrayList<>();
        for (int row = 0; row < MAX_ROW; row++) {
            for (int col = 0; col < MAX_COL; col++) {
                Piece piece = getPieceAtSquare(row, col);
                if (piece != null && piece.getColor() == color) {
                    allMoves.addAll(getAvailableMoves(piece));
                }
            }
        }
        return allMoves;
    }

    public List<Move> getAvailableMoves(Piece piece){
        List<Move> allMoves = new ArrayList<>();
        for (int row = 0; row < MAX_ROW; row++) {
            for (int col = 0; col < MAX_COL; col++) {
                if (isMoveValid(piece, row, col)) {
                    Piece capturedPiece = getPieceAtSquare(row, col);
                    Move move = new Move(piece, row, col,capturedPiece != null, capturedPiece);
                    allMoves.add(move);
                }
            }
        }
        return allMoves;
    }

    public void makeMove(Move move){
        this.makeMove(move.getPieceMoved(), move.getEndRow(), move.getEndCol());
    }

    public boolean makeMove(Piece piece, int row, int col){
        if (piece == null) return false;
        if (!isMoveValid(piece,row, col)) return false;

        int originalRow = piece.getRow();
        int originalCol = piece.getCol();
        Piece capturedPiece = getPieceAtSquare(row, col);

        if (piece.getClass() == Pawn.class) {
            boolean isEnPassant = (Math.abs(col - piece.getCol()) == 1) && !isSquareTaken(row, col);

            if (isEnPassant) {
                capturedPiece = getPieceAtSquare(originalRow, col);
                addMoveToHistory(piece, row, col, true, capturedPiece);

                board[row][col] = piece;
                board[originalRow][originalCol] = null;
                board[originalRow][col] = null;
                piece.setPiecePosition(row, col);

                if (isKingInCheck(piece.getColor())) {
                    board[originalRow][originalCol] = piece;
                    board[row][col] = null;
                    board[originalRow][col] = capturedPiece;
                    piece.setPiecePosition(originalRow, originalCol);
                    return false;
                }
                ((Pawn) piece).wasMoved = true;
                colorToMove = 1 - colorToMove;
                return true;
            }

            boolean isPromotion = (row == 7 && piece.getColor() == Piece.WHITE) || (row == 0 && piece.getColor() == Piece.BLACK);

            if (isPromotion) {
                addMoveToHistory(piece, row, col, capturedPiece != null, capturedPiece);

                Piece promoted = getPromotionPiece(colorToMove, row, col);
                board[row][col] = promoted;
                board[originalRow][originalCol] = null;
                piece.setPiecePosition(row, col);

                if (isKingInCheck(piece.getColor())) {
                    board[originalRow][originalCol] = piece;
                    board[row][col] = capturedPiece;
                    piece.setPiecePosition(originalRow, originalCol);
                    return false;
                }

                ((Pawn) piece).wasMoved = true;
                colorToMove = 1 - colorToMove;
                return true;
            }
        }

        if (piece.getClass() == King.class) {
            if (originalRow - row == 0 && Math.abs(originalCol - col) == 2 && !((King) piece).wasMoved) {
                if (checkForCastling(((King) piece), row, col)){
                    int rookStartCol = col > piece.getCol() ? 7 : 0;
                    int rookEndCol = col > piece.getCol() ? 5 : 3;
                    Piece rook = getPieceAtSquare(row, rookStartCol);

                    addMoveToHistory(piece, row, col, false, null);

                    board[row][col] = piece;
                    board[piece.getRow()][piece.getCol()] = null;
                    piece.setPiecePosition(row, col);

                    board[row][rookEndCol] = rook;
                    board[row][rookStartCol] = null;
                    rook.setPiecePosition(row, rookEndCol);

                    ((King) piece).wasMoved = true;
                    ((Rook) rook).wasMoved = true;
                    colorToMove = 1 - colorToMove;
                    return true;
                }
            }
        }

        addMoveToHistory(piece, row, col, capturedPiece != null, capturedPiece);

        board[row][col] = piece;
        board[originalRow][originalCol] = null;
        piece.setPiecePosition(row, col);

        if (isKingInCheck(piece.getColor())) {
            board[originalRow][originalCol] = piece;
            board[row][col] = capturedPiece;
            piece.setPiecePosition(originalRow, originalCol);
            return false;
        }

        movesWithoutCapture = (capturedPiece == null) ? movesWithoutCapture + 1 : 0;
        movesNotWithPawns = (piece.getClass() != Pawn.class) ? movesNotWithPawns + 1 : 0;

        if (piece.getClass() == Rook.class) ((Rook) piece).wasMoved = true;
        if (piece.getClass() == King.class) ((King) piece).wasMoved = true;
        if (piece.getClass() == Pawn.class) ((Pawn) piece).wasMoved = true;

        colorToMove = 1 - colorToMove;
        return true;
    }

    public boolean isMoveValid(Piece piece, int row, int col) {
        if (isKingInCheck(piece.getColor())) {
            if (!canPieceBlockCheck(piece ,row, col, piece.getColor())) return false;
        }

        if (piece.getClass() == Pawn.class){
            if (!((Pawn) piece).isMoveValid(row, col, this)) return false;
        } else if (piece.getClass() == King.class){
            if (!((King) piece).isMoveValid(row, col, this)) return false;
        } else if (!piece.canAttackSquare(row, col, this)) return false;

        return true;
    }

    public Piece getPromotionPiece(int color, int row, int col) {
        Piece promotedPiece = null;
        String piece = ChessGUI.getPromotionChoice(this);

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

    public boolean canPieceBlockCheck(Piece piece,int row, int col, int color){
        int originalRow = piece.getRow();
        int originalCol = piece.getCol();
        Piece capturedPiece = getPieceAtSquare(row, col);

        board[row][col] = piece;
        board[originalRow][originalCol] = null;
        piece.setPiecePosition(row, col);

        boolean stillInCheck = isKingInCheck(color);

        board[originalRow][originalCol] = piece;
        board[row][col] = capturedPiece;
        piece.setPiecePosition(originalRow, originalCol);

        return !stillInCheck;
    }

}
