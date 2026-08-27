package byow.Core;

public class Light {
    private int X;
    private int Y;
    private boolean on;
    private Room room;

    Light(int x, int y, Room r){
        X = x;
        Y = y;
        room = r;
    }

    public void turn(){
        on = !on;
    }

    public Room getRoom(){
        return room;
    }

    public boolean getStatus(){return on;}

    public int getX(){return X;}

    public int getY(){return Y;}
}
