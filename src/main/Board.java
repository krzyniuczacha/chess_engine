package main;

import pieces.*;

public class Board {
    final static int MAX_ROW = 8;
    final static int MAX_COL = 8;
    private static Piece[][] board;


    public Board(){
        this.board = new Piece[MAX_ROW][MAX_COL];
        //createBoard(this);
    }


    public static Piece getPieceAtSquare(int row, int col){
        return board[row][col];
    }

    public static boolean isSquareTaken(int row, int col){
        return board[row][col] != null;
    }

    public static void setPieceAtSquare(int row, int col, Piece piece){
        int row1 = piece.getRow();
        int col1 = piece.getCol();
        board[row][col] = piece;
        board[row1][col1] = null;
    }

    public static boolean isCheckableAfter(Piece piece, int row, int col){
        Piece[][] temp = board;
        int row1 = piece.getRow();
        int col1 = piece.getCol();
        temp[row][col] = piece;
        temp[row1][col1] = null;

        for (int i = 0; i < MAX_ROW; i++ ){
            for (int j = 0; j < MAX_COL; j++){
                 if (temp[i][j].isMoveAllowed(row, col)) return true;
            }
        }

        return false;
    }

    public static boolean checkForCastling(Piece piece, int row, int col){
        Piece rook = getPieceAtSquare(row, col);

        if (col ==7){
        for (int i = piece.getCol(); i < col; i++){
            if (isSquareTaken(row, i)) return false;
            if (isCheckableAfter(piece, row, i)) return false;
            }
        }

        if (col ==0) {
            for (int i = piece.getCol(); i > col; i--) {
                if (isSquareTaken(row, i)) return false;
                if (isCheckableAfter(piece, row, i)) return false;
            }
        }

        if (rook == null) return false;
        if (rook.getClass() != Rook.class) return false;
        if (((Rook) rook).wasMoved) return false;

        

        return true;
    }







}

