package byow.Core;

public class Room{
    int length;
    int width;
    int X;
    int Y;
    int index;
    Room(int l, int w, int x, int y, int i){
        length = l;
        width = w;
        X = x;
        Y = y;
        index = i;
    }

    public void changeIndex(int newIndex){
        index = newIndex;
    }

    public boolean contains(int x, int y){
        return (x >= X && x < X + length && y >= Y && y < Y + width);
    }
}
