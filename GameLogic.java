import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

/**
 * Handles the core logic of the Dungeon of Doom game:
 * map loading, player and bot placement, and game mechanics.
 */
public class GameLogic {
    private char [][] map;
    private Player player;
    private int playerX, playerY;

    private Bot bot;
    private int botX, botY;

    private int G_toWin = 0;
    private List<int[]> G_Posi = new ArrayList<>();
    private List<int[]> E_Posi = new ArrayList<>();

    private Renderer renderer = new Renderer();

    /**
     * Initializes the game by loading the map and placing the player and bot.
     */
    public void initializeGame(Scanner scanner) {
        inquireMap(scanner);
        positioningPandB();
    }

    /**
     * Updates the game state after each turn.
     * Moves the bot and checks if it has captured the player.
     */
    public void updateGameState() {
        moveBot();
        captured(bot.getX(), bot.getY());
    }

    /**
     * Asks the player to input a valid map file path until one is successfully loaded.
     */
    public void inquireMap(Scanner s) {
        while (true) {
            System.out.println("Input the path (e.g., dungeon.txt):");
            String fileName = s.nextLine();

            if (loadMap(fileName)) {
                break;
            }
        }
    }

    /**
     * Loads the map from the given file and extracts gold and exit positions.
     */
    public boolean loadMap(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            System.out.println("Error: file is not provided.");
            return false;
        }

        try {
            List<char[]> list = new ArrayList<>();
            FileReader fr = new FileReader(fileName);
            BufferedReader br = new BufferedReader(fr);

            String line;
            int lineNum = 0;

            while ((line = br.readLine()) != null) {
                lineNum++;
                if (lineNum == 2 && line.startsWith("win ")) {
                    String num = line.substring(4).trim();
                    G_toWin = Integer.parseInt(num);
                } else if (lineNum == 2) {
                    System.out.println("No win condition in second line. Set to 0.");
                    G_toWin = 0;
                }
                if (lineNum > 2) list.add(line.toCharArray());
            }

            br.close();
            map = list.toArray(new char[list.size()][]);

            // Check if tha map is empty
            if (map == null || map.length == 0) {
                System.out.println("Error: The map is empty. Please make sure the file contains a valid map layout.\n");
                return false;
            }

            // Extract gold and exit positions from the map
            for (int i = 0; i < map.length; i++) {
                for (int j = 0; j < map[i].length; j++) {
                    if (map[i][j] == 'G') G_Posi.add(new int[]{i, j});
                    else if (map[i][j] == 'E') E_Posi.add(new int[]{i, j});
                }
            }
            return true;

        } catch (IOException e) {
            System.out.println("Failed to load map: '" + fileName + "'");
            System.out.println("Please make sure the file exists in the correct directory and try again.\n");
            return false;
        }
    }

    /**
     * Randomly places the player and bot on valid positions on the map.
     */
    public void positioningPandB() {
        Random r = new Random();

        // Place player
        do {
            playerX = r.nextInt(map.length - 2) + 2;
            playerY = r.nextInt(map[playerX].length);
        } while (map[playerX][playerY] == '#' || map[playerX][playerY] == 'G');

        map[playerX][playerY] = 'P';
        player = new Player(playerX, playerY);

        // Place bot
        do {
            botX = r.nextInt(map.length - 2) + 2;
            botY = r.nextInt(map[botX].length);
        } while (map[botX][botY] == '#' || (botX == playerX && botY == playerY));

        map[botX][botY] = 'B';
        bot = new Bot(botX, botY);
    }

    /**
     * Moves the player in the given direction and checks for capture.
     */
    public void movePlayer(String direction) {
        player.move(direction, map, G_Posi, E_Posi);
        playerX = player.getX();
        playerY = player.getY();
        captured(playerX, playerY);
    }

    /**
     * Moves the bot and checks if it captured the player.
     */
    public void moveBot() {
        bot.move(map, G_Posi, E_Posi);
        botX = bot.getX();
        botY = bot.getY();
        captured(botX, botY);
    }

    /**
     * Displays the amount of gold required to win.
     */
    public void hello() {
        System.out.println("Gold to win: " + G_toWin);
    }

    /**
     * Displays the amount of gold the player currently owns.
     */
    public void displayG() {
        System.out.println("Gold Owned: " + player.getGcount());
    }

    /**
     * Attempts to pick up gold at the player's current position.
     */
    public void pickupG() {
        if (player.pickup(G_Posi, map)) {
            System.out.println("Success. Gold owned: " + player.getGcount());
        } else {
            System.out.println("Fail. Gold owned: " + player.getGcount());
        }
    }

    /**
     * Ends the game. Checks if the player has met the win condition.
     */
    public void quit() {
        if (player.is_EPosi(playerX, playerY, E_Posi) && player.getGcount() >= G_toWin) {
            System.out.println("WIN. You became a millionaire!");
        } else {
            System.out.println("LOSE.");
        }
        System.exit(0);
    }

    /**
     * Checks if the player has been captured by the bot.
     */
    public void captured(int currentX, int currentY) {
        if (playerX == botX && playerY == botY) {
            System.out.println("LOSE. You are captured.");
            map[botX][botY] = '*';
            displayMap();
            System.exit(0);
        }
    }

    /**
     * Displays the full map.
     */
    public void displayMap() {
        renderer.displayMap(map);
    }

    /**
     * Displays a 5x5 area centered on the player's position.
     */
    public void look() {
        renderer.displayLook(map, playerX, playerY);
    }
}
