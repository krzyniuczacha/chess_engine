package pieces;

import static main.Board.*;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public abstract class Piece {
    private int row,col,color;
    public static final int WHITE = 1;
    public static final int BLACK = 0;

    public Piece(int color, int col, int row){
        this.color = color;
        this.col = col;
        this.row = row;
    }

    public BufferedImage getImage(String path){
        BufferedImage image = null;
        try{
            image = ImageIO.read(getClass().getResourceAsStream(path + ".png"));
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return image;
    }

    public int getColor() {
        return color;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void setPosition(int row, int col){
        setPieceAtSquare(row, col, this);

        this.row = row;
        this.col = col;
    }

    public boolean move(int row, int col){
        if (!isMoveAllowed(row, col)) return false;
        setPosition(row,col);
        return true;
    }

    public abstract boolean isMoveAllowed(int row, int col);


}
