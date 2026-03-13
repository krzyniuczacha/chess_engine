package pieces;

import main.Board;
import util.Square;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public abstract class Piece {
    private int row,col,color;
    public static final int WHITE = 1;
    public static final int BLACK = 0;

    public Piece(int color, int row, int col){
        this.color = color;
        this.row = row;
        this.col = col;
    }

    public BufferedImage getImage(String name) {
        BufferedImage image = null;
        try {
            File file = new File("res/pieces/" + name + ".png");
            if (!file.exists()) {
                System.err.println("Image file not found: " + file.getAbsolutePath());
                return null;
            }
            image = ImageIO.read(file);
        } catch (Exception e) {
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

    public Square getSquare(){
        return new Square(row,col);
    }

    public void setPiecePosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;

        Piece o = (Piece) other;

        return row == o.row && col == o.col &&  color == o.color;
    }

    public abstract boolean canAttackSquare(int row, int col, Board board);
}