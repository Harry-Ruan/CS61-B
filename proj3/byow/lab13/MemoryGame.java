package byow.lab13;

import byow.Core.RandomUtils;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;
import java.util.Random;

public class MemoryGame {
    /** The width of the window of this game. */
    private int width;
    /** The height of the window of this game. */
    private int height;
    /** The current round the user is on. */
    private int round;
    /** The Random object used to randomly generate Strings. */
    private Random rand;
    /** Whether or not the game is over. */
    private boolean gameOver;
    /** Whether or not it is the player's turn. Used in the last section of the
     * spec, 'Helpful UI'. */
    private boolean playerTurn;
    /** The characters we generate random Strings from. */
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    /** Encouraging phrases. Used in the last section of the spec, 'Helpful UI'. */
    private static final String[] ENCOURAGEMENT = {"You can do this!", "I believe in you!",
                                                   "You got this!", "You're a star!", "Go Bears!",
                                                   "Too easy for you!", "Wow, so impressive!"};

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please enter a seed");
            return;
        }

        long seed = Long.parseLong(args[0]);
        MemoryGame game = new MemoryGame(40, 40, seed);
        game.startGame();
    }

    public MemoryGame(int width, int height, long seed) {
        /* Sets up StdDraw so that it has a width by height grid of 16 by 16 squares as its canvas
         * Also sets up the scale so the top left is (0,0) and the bottom right is (width, height)
         */
        this.width = width;
        this.height = height;
        this.rand = new Random(seed);
        StdDraw.setCanvasSize(this.width * 16, this.height * 16);
        Font font = new Font("Arial", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();

        //TODO: Initialize random number generator
    }

    public String generateRandomString(int n) {
        //TODO: Generate random string of letters of length n
        String randomString = "";
        int charNum = CHARACTERS.length;
        for (int i = 0; i < n; i++){
            randomString += Character.toString(CHARACTERS[rand.nextInt(charNum)]);
        }
        return randomString;
    }

    public void drawFrame(String s) {
        //TODO: Take the string and display it in the center of the screen
        //TODO: If game is not over, display relevant game information at the top of the screen
        StdDraw.clear(Color.black);
        StdDraw.setPenColor(Color.white);
        StdDraw.text(width / 2, height / 2, s);
        StdDraw.show();
    }

    public void flashSequence(String letters) {
        //TODO: Display each character in letters, making sure to blank the screen between letters
        Font font = new Font("Arial", Font.BOLD, 30);
        StdDraw.setFont(font);
        int len = letters.length();
        String temp;
        for (int i = 0; i < len; i++){
            temp = letters.substring(i, i + 1);
            drawFrame(temp);
            StdDraw.pause(1000);
            StdDraw.clear(Color.black);
            StdDraw.pause(500);
        }
    }

    public String solicitNCharsInput(int n) {
        //TODO: Read n letters of player input
        StdDraw.clear(Color.BLACK);
        String input = "";
        while (input.length() < n){
            if (StdDraw.hasNextKeyTyped()){
                char c = Character.toLowerCase(StdDraw.nextKeyTyped());
                input += c;
                StdDraw.text(width / 2, height / 3, input);
                StdDraw.show();
            }
        }
        return input;
    }

    public void startGame() {
        //TODO: Set any relevant variables before the game starts
        round = 0;
        gameOver = false;
        while (gameOver == false){
            round += 1;
            startRound(round);
        }
        displayOver(round);
        //TODO: Establish Engine loop
    }

    private void displayRound(int round){
        Font font = new Font("Arial", Font.BOLD, 50);
        StdDraw.setFont(font);
        StdDraw.clear(Color.black);
        StdDraw.text(width / 2, height / 3, String.format("Round: %d", round));
        StdDraw.show();
        StdDraw.pause(1000);
    }

    private void startRound(int round){
        displayRound(round);
        String question = generateRandomString(round);
        flashSequence(question);
        if (!solicitNCharsInput(round).equals(question)){
            gameOver = true;
        }
    }

    private void displayOver(int round){
        StdDraw.clear(Color.black);
        StdDraw.text(width / 2, height / 3, String.format("Game Over, You Made It to Round %d", round));
        StdDraw.show();
    }
}
