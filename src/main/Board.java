package main;

import pieces.*;

public class Board {
    final int MAX_ROW = 8;
    final int MAX_COL = 8;
    private static Piece[][] Board;


    public Board(){
        this.Board = new Piece[MAX_ROW][MAX_COL];
        //createBoard(this);
    }


    public static Piece getPieceAtSquare(int row, int col){
        return Board[row][col];
    }

    public static boolean isSquareTaken(int row, int col){
        return Board[row][col] == null;
    }





}

