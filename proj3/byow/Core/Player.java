package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

public class Player {
    private int X;
    private int Y;
    private int HQ;
    private String Name;
    private int vision;

    Player(int x, int y, int h, String N){
        X = x;
        Y = y;
        HQ = h;
        Name = N;
        vision = 5;
    }

    public void move(char direction, World w){
        int newX = X;
        int newY = Y;
        switch (direction) {
            case 'w':
                newY += 1;
                break;
            case 's':
                newY -= 1;
                break;
            case 'd':
                newX += 1;
                break;
            case 'a':
                newX -= 1;
                break;
        }
        if (checkValidMove(newX, newY, w.getWorld())){
            X = newX;
            Y = newY;
        }
    }

    private boolean checkValidMove(int x, int y, TETile[][] world){
        if (world[x][y].equals(Tileset.WALL) || world[x][y].equals(Tileset.NOTHING)){
            return false;
        }
        return true;
    }

    public boolean inSight(int x, int y){
        return (Math.abs(x - X) + Math.abs(y - Y) < vision);
    }

    public void setName(String n){
        Name = n;
    }

    public int getX(){return X;}

    public int getY(){return Y;}

    public int getHQ(){return HQ;}

    public String getName(){return Name;}
}
