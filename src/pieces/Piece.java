package pieces;

import static main.Board.*;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public abstract class Piece {
    private int row,col,color;
    final int WHITE = 1;
    final int BLACK = 0;

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

    public int setPosition(int row, int col){
        this.row = row;
        this.col = col;
    }

    public abstract boolean move(int row, int col);

    public abstract boolean isMoveAllowed(int row, int col);




}
