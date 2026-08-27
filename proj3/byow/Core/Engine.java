package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.Core.RandomUtils;
import byow.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;


public class Engine {
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 60;
    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() throws FileNotFoundException {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        StdDraw.setPenColor(Color.white);
        Font font = new Font("Arial", Font.BOLD, 15);
        String SEED = "";
        String name = "";
        boolean fogOn = true;
        World world = null;
        while(world == null){
            switch (menuCommand()){
                case 'n':
                    StdDraw.clear(Color.black);
                    StdDraw.show();
                    char nextCommand;
                    while(true){
                        if (StdDraw.hasNextKeyTyped()){
                            nextCommand = Character.toLowerCase(StdDraw.nextKeyTyped());
                            if (nextCommand == 's'){
                                break;
                            }
                            SEED += nextCommand;
                            StdDraw.clear(Color.black);
                            StdDraw.text(WIDTH / 2, HEIGHT / 3, SEED);
                            StdDraw.show();
                        }
                    }
                    world = new World(Long.parseLong(SEED), WIDTH, HEIGHT);
                    world.generateWorld();
                    world.addLightsOnWorld();
                    world.addPlayer(name);
                    break;
                case 'l': world = load();
                    break;
                case 'c':
                    StdDraw.clear(Color.black);
                    StdDraw.show();
                    char nextChar;
                    while(true) {
                        if (StdDraw.hasNextKeyTyped()) {
                            nextChar = Character.toLowerCase(StdDraw.nextKeyTyped());
                            if (nextChar == '\n') {
                                break;
                            }
                            name += nextChar;
                            StdDraw.clear(Color.black);
                            StdDraw.text(WIDTH / 2, HEIGHT / 3, name);
                            StdDraw.show();
                        }
                    }
                    world = load();
                    if (world != null) {
                        world.getPlayer().setName(name);
                        save(world);
                    }
                    world = null;
                    break;
                case 'v':
                    fogOn = !fogOn;
                    break;
                case 'q': return;
            }
        }
        StdDraw.setFont(font);
        while(true){
            if (fogOn){
                renderWithFog(world);
            }
            else{
                render(world);
            }
            if (StdDraw.hasNextKeyTyped()) {
                char command = Character.toLowerCase(StdDraw.nextKeyTyped());
                switch (command) {
                    case ':':
                        while (!StdDraw.hasNextKeyTyped()) {
                            renderWithFog(world);
                        }
                        if (Character.toLowerCase(StdDraw.nextKeyTyped()) == 'q') {
                            save(world);
                            return;
                        }
                        break;
                    case 't': world.playerTurnLight();
                    break;
                    default: world.movePlayer(command);
                }
            }
            StdDraw.pause(16);
        }
    }


    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, both of these calls:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) throws FileNotFoundException {
        int endIndex = 1;
        World world = null;
        String name = "";
        switch (Character.toLowerCase(input.charAt(0))){
            case 'n': while (Character.toLowerCase(input.charAt(endIndex)) != 's'){
                endIndex += 1;
            }
                Long SEED = Long.parseLong(input.substring(1, endIndex));
                world = new World(SEED, WIDTH, HEIGHT);
                world.generateWorld();
                world.addLightsOnWorld();
                world.addPlayer(name);
                endIndex += 1;
                break;
            case 'l':
                world = load();
                break;
        }
        String otherInput = input.substring(endIndex);
        int inputLen = otherInput.length();
        for (int i = 0; i < inputLen; i++){
            char command = Character.toLowerCase(otherInput.charAt(i));
            switch (command){
                case ':': if (i + 1 < inputLen && Character.toLowerCase(otherInput.charAt(i + 1)) == 'q') {
                        save(world);
                        return world.getFinalWorld();
                        }
                        i += 1;
                    break;
                case 't': world.playerTurnLight();
                break;
                default: world.movePlayer(command);
            }
        }
        // TODO: Fill out this method so that it run the engine using the input
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.
        TETile[][] finalWorldFrame = world.getFinalWorld();
        return finalWorldFrame;
    }

    public static void main(String[] args) throws FileNotFoundException {

        Engine engine = new Engine();

        TETile[][] world = engine.interactWithInputString("N12345SWDWWWD:Q");

        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        ter.renderFrame(world);

        TETile[][] world1 = engine.interactWithInputString("L");
        ter.initialize(WIDTH, HEIGHT);
        ter.renderFrame(world1);
    }

    public void save(World world) throws FileNotFoundException {
        PrintWriter out = new PrintWriter("savefile.txt");
        out.println(world.getSEED());
        out.println(world.getPlayer().getX());
        out.println(world.getPlayer().getY());
        out.println(world.getPlayer().getHQ());
        out.println(world.getPlayer().getName());
        out.close();
    }

    public World load() {
        try {
            Scanner scanner = new Scanner(new File("savefile.txt"));
            long seed = scanner.nextLong();
            int x = scanner.nextInt();
            int y = scanner.nextInt();
            int h = scanner.nextInt();
            scanner.nextLine();
            String name = scanner.nextLine();
            scanner.close();
            World world = new World(seed, WIDTH, HEIGHT);
            world.generateWorld();
            world.addLightsOnWorld();
            world.setPlayer(new Player(x, y, h, name));
            return world;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private char menuCommand(){
        Font font = new Font("Arial", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.clear(Color.black);
        StdDraw.text(WIDTH / 2, HEIGHT * 0.7, "New Game (n)");
        StdDraw.text(WIDTH / 2, HEIGHT * 0.5, "Load Game (l)");
        StdDraw.text(WIDTH / 2, HEIGHT * 0.3, "Change Name (c)");
        StdDraw.text(WIDTH / 2, HEIGHT * 0.1, "Quit (q)");
        StdDraw.show();
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                return Character.toLowerCase(StdDraw.nextKeyTyped());
            }
        }
    }

    private void drawHUD(World w){
        StdDraw.setPenColor(Color.WHITE);
        int mouseX = (int) StdDraw.mouseX();
        int mouseY = (int) StdDraw.mouseY();
        if (mouseX >= 0 && mouseX < WIDTH && mouseY >= 0 && mouseY < HEIGHT && w.getPlayer().inSight(mouseX, mouseY)) {
            StdDraw.text(2, HEIGHT - 2, w.getWorld()[mouseX][mouseY].description());
        }
        StdDraw.text(2, HEIGHT - 1, String.format("Name: %s", w.getPlayer().getName()));
    }

    private void drawWorld(World w){
        TETile[][] world = w.getWorld();
        int numXTiles = world.length;
        int numYTiles = world[0].length;
        for (int x = 0; x < numXTiles; x += 1) {
            for (int y = 0; y < numYTiles; y += 1) {
                if (world[x][y] == null) {
                    throw new IllegalArgumentException("Tile at position x=" + x + ", y=" + y
                            + " is null.");
                }
                world[x][y].draw(x, y);
            }
        }
    }

    private void drawPlayer(World w){
        int playerX = w.getPlayer().getX();
        int playerY = w.getPlayer().getY();
        Tileset.AVATAR.draw(playerX, playerY);
    }

    private void drawFog(World w){
        TETile[][] world = w.getWorld();
        int numXTiles = world.length;
        int numYTiles = world[0].length;
        for (int x = 0; x < numXTiles; x += 1) {
            for (int y = 0; y < numYTiles; y += 1) {
                if (!w.getPlayer().inSight(x, y)){
                    Tileset.NOTHING.draw(x, y);
                }
            }
        }
    }

    private void render(World world){
        StdDraw.clear(Color.black);
        drawWorld(world);
        drawPlayer(world);
        drawHUD(world);
        StdDraw.show();
    }

    private void renderWithFog(World world){
        StdDraw.clear(Color.black);
        drawWorld(world);
        drawPlayer(world);
        drawFog(world);
        drawHUD(world);
        StdDraw.show();
    }
}
