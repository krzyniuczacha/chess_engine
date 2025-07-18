package util;

import main.Board;
import pieces.*;

import static java.lang.Character.toUpperCase;
import static main.Board.MAX_COL;
import static main.Board.MAX_ROW;

public class FEN {

    static final char rook = 'r';
    static final char knight = 'n';
    static final char bishop = 'b';
    static final char queen = 'q';
    static final char king = 'k';
    static final char pawn = 'p';

    static final char white = 'w';
    static final char black = 'b';

    static final char none = '-';
    static final char rankSeparator = '/';


    public static String boardToFen(Board board){
        String fen = "";
        Piece[][] board1 = board.getBoard();

        for (int i = 7; i >= 0; i--) {
            int countEmpty = 0;
            String rankString = "";

            for (int j = 0; j < 8; j++) {
                if (board1[i][j] == null) countEmpty++;
                else {
                    if (countEmpty != 0) rankString += countEmpty;
                    rankString += pieceToChar(board1[i][j]);
                    countEmpty = 0;
                }
            }
            if (countEmpty != 0) rankString += countEmpty;
            fen += rankString;

            if (!(i == 0)) fen += rankSeparator;
            else fen += " ";
        }

        return fen;
    }

    public static char pieceToChar(Piece piece){
        char result = 0;

        if (piece.getClass() == Knight.class){
            if (piece.getColor() == Piece.WHITE) result = toUpperCase(knight);
            else result = knight;
        }
        if (piece.getClass() == Bishop.class) {
            if (piece.getColor() == Piece.WHITE) result = toUpperCase(bishop);
            else result = bishop;
        }
        if (piece.getClass() == Queen.class) {
            if (piece.getColor() == Piece.WHITE) result = toUpperCase(queen);
            else result = queen;
        }
        if (piece.getClass() == King.class) {
            if (piece.getColor() == Piece.WHITE) result = toUpperCase(king);
            else result = king;
        }
        if (piece.getClass() == Pawn.class) {
            if (piece.getColor() == Piece.WHITE) result = toUpperCase(pawn);
            else result = pawn;
        }
        if (piece.getClass() == Rook.class) {
            if (piece.getColor() == Piece.WHITE) result = toUpperCase(rook);
            else result = rook;
        }

        return result;
    }

    public static Piece charToPiece(char piece, int row , int col){
        Piece result = null;

        switch (piece){
            case rook:
                result = new Rook(Piece.BLACK, row, col);
                break;
            case 'R':
                result = new Rook(Piece.WHITE, row, col);
                break;
            case knight:
                result = new Knight(Piece.BLACK, row, col);
                break;
            case 'N':
                result = new Knight(Piece.WHITE, row, col);
                break;
            case bishop:
                result = new Bishop(Piece.BLACK, row, col);
                break;
            case 'B':
                result = new Bishop(Piece.WHITE, row, col);
                break;
            case queen:
                result = new Queen(Piece.BLACK, row, col);
                break;
            case 'Q':
                result = new Queen(Piece.WHITE, row, col);
                break;
            case king:
                result = new King(Piece.BLACK, row, col);
                break;
            case 'K':
                result = new King(Piece.WHITE, row, col);
                break;
            case pawn:
                result = new Pawn(Piece.BLACK, row, col);
                break;
            case 'P':
                result = new Pawn(Piece.WHITE, row, col);
                break;
        }

        return result;
    }

    public static Piece[][] fenToBoard(String fen){
        Piece[][] board = new Piece[MAX_COL][MAX_ROW];
        String[] parts = fen.split(" ");
        String[] rows = parts[0].split("/");

        for (int fenRow = 0; fenRow < 8; fenRow++) {
            int row = 7 - fenRow;
            int col = 0;

            for (int i = 0; i < rows[fenRow].length(); i++) {
                char c = rows[fenRow].charAt(i);

                if (Character.isDigit(c)) {
                    col += c - '0';
                } else {
                    Piece piece = charToPiece(c, row, col);
                    board[row][col] = piece;
                    col++;
                }
            }
        }

        return board;
    }

}


