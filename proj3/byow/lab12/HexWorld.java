package byow.lab12;
import edu.neu.ccs.util.Hex;
import org.junit.Test;
import static org.junit.Assert.*;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    private static int HEX_WIDTH;
    private static int WIDTH;
    private static int HEIGHT;
    private static int INITIAL_X;
    private static final Random RANDOM = new Random();
    HexWorld(int w){
        HEX_WIDTH = w;
        WIDTH = 11 * w;
        HEIGHT = 11 * w;
        INITIAL_X = (WIDTH - HEX_WIDTH)/2;
    }

    private static class Hexagon {
        private int width;
        private TETile type;
        Hexagon(int w, TETile t){
            width = w;
            type = t;
        }
    }

    private static void addHexagon(int x, int y, Hexagon h, TETile[][] world){
        for (int line = 0; line < 2 * h.width; line++){
            int add_len;
            if (line < h.width){
                add_len = line;
            }
            else {
                add_len = 2 * h.width - 1 - line;
            }
            addLine(x - add_len, y, h.width  + 2 * add_len, line, h.type, world);
        }
    }

    private static void addLine(int x, int y, int length, int line, TETile type, TETile[][] world){
        for (int i = 0; i < length; i++){
            world[x + i][y + line] = type;
        }
    }

    private static TETile randomTile() {
        int tileNum = RANDOM.nextInt(3);
        switch (tileNum) {
            case 0: return Tileset.WALL;
            case 1: return Tileset.FLOWER;
            case 2: return Tileset.GRASS;
            default: return Tileset.NOTHING;
        }
    }

    private static void addHexagons(int line, int num, TETile[][] world){
        int y = HEX_WIDTH * line;
        switch (num){
            case 1: addHexagon(INITIAL_X, y, new Hexagon(HEX_WIDTH, randomTile()), world);
                break;
            case 2: addHexagon(INITIAL_X - 2 * HEX_WIDTH + 1, y, new Hexagon(HEX_WIDTH, randomTile()), world);
                addHexagon(INITIAL_X + 2 * HEX_WIDTH - 1, y, new Hexagon(HEX_WIDTH, randomTile()), world);
                break;
            case 3: addHexagon(INITIAL_X - 4 * HEX_WIDTH + 2, y, new Hexagon(HEX_WIDTH, randomTile()), world);
                addHexagon(INITIAL_X, y, new Hexagon(HEX_WIDTH, randomTile()), world);
                addHexagon(INITIAL_X + 4 * HEX_WIDTH - 2, y, new Hexagon(HEX_WIDTH, randomTile()), world);
                break;
        }
    }

    public static void fillWithRandomHexagons(TETile[][] world){
        addHexagons(0, 1, world);
        addHexagons(1, 2, world);
        addHexagons(2, 3, world);
        addHexagons(3, 2, world);
        addHexagons(4, 3, world);
        addHexagons(5, 2, world);
        addHexagons(6, 3, world);
        addHexagons(7, 2, world);
        addHexagons(8, 1, world);
    }

    public static void main(String[] args) {
        // initialize the tile rendering engine with a window of size WIDTH x HEIGHT
        new HexWorld(3);
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        // initialize tiles
        TETile[][] HexWorld = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                HexWorld[x][y] = Tileset.NOTHING;
            }
        }
        // fills in a block with hexagons
        fillWithRandomHexagons(HexWorld);
        // draws the world to the screen
        ter.renderFrame(HexWorld);
    }
}


