import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

public class GameLogic {
    private char [][] map; // Stores 2D map data
    private Player player; // Object variable of Player
    private int playerX, playerY; // Variables of Player position
    private Bot bot; // Object variable of Bot
    private int botX, botY; // Variables of Bot position
    private int G_toWin = 0; //winning condition
    private List<int[]> G_Posi = new ArrayList<>(); // list to store Gold positions
    private List<int[]> E_Posi = new ArrayList<>(); // list to store Exit position(s)

    /* Check if map format is correct */
    public void inquireMap(Scanner s){
        while (true) {
            System.out.println("Input the pass (e.g., DungeonOfDoom/dungeon.txt):");
            String fileName = s.nextLine(); // Assign user input to fileName

            /* Load the map. Repeat until it loads successfully */
            if (loadMap(fileName)) {
                break;
            } else {
                System.out.println("Please try again.");
            }
        }    
    }

    /* Load map */
    public boolean loadMap(String fileName) {

        /* Error if no file is passed */
        if (fileName == null || fileName.isEmpty()) {
            System.out.println("Error: file is not provided.");
            return false;
        }

        try {
            List<char[]> list = new ArrayList<>(); // Object for storing map (array of char)
            FileReader fr = new FileReader(fileName); // Object for reading characters from a file
            BufferedReader br = new BufferedReader(fr); // Object for reading characters line by line
             
            /* Read number in the 2nd line. Read the map from the 3rd line onwards*/
            String line;
            int lineNum = 0;

            while ((line = br.readLine()) != null) { 
                lineNum++;
                if (lineNum == 2) { 
                    if (line.startsWith("win ")) {
                        String num = line.substring(4).trim(); // Trim "win_" to get only the number
                        G_toWin = Integer.parseInt(num);
                        System.out.println(); //Add a new line
                    } else {
                        System.out.println("There is no Win condition written on the second line. Win condition is set to 0."); //Error Handling
                        G_toWin = 0; 
                    }
                }
                if (lineNum > 2) {
                    list.add(line.toCharArray()); //Convert string to char[] and add to list
                }
            }
            br.close(); 
            map = list.toArray(new char[list.size()][]); // Convert list set to 2D array

            /*Get the Gold and Exit positions from the 2D array*/
            for (int i = 0; i < map.length; i++) {
                for (int j = 0; j < map[i].length; j++) {
                    if (map[i][j] == 'G') {
                        G_Posi.add(new int[]{i, j});
                    } else if (map[i][j] == 'E') {
                        E_Posi.add(new int[]{i, j});
                    }
                }
            }
            return true;

        /* Handling IOException errors */
        } catch (IOException e) {
            System.out.println("Error: ");
            e.printStackTrace(); //Print error details
            return false;
        }
    }

    /* Place Player(P) and Bot(B) randomly */
    public void positioningPandB() {
        Random r = new Random();
        
        /*
        Place the player. 
        Since the first 2 lines of .txt are metadata, a random number should be generated between the 3rd line and the last line. 
        Repeat until a location without the wall(#) is specified.
        */
        do {
            playerX = r.nextInt(map.length -2) +2; // Randomly select a row
            playerY = r.nextInt(map[playerX].length); // Randomly select a column within a specified row
        } while (map[playerX][playerY] == '#' || map[playerX][playerY] == 'G');
        
        map[playerX][playerY] = 'P'; // Show P at a specified location
        player = new Player(playerX, playerY);  // Create a player object and update the location


        /* Place the bot. Repeat until a location without # and P is specified. */
        do {
            botX = r.nextInt(map.length -2) +2;
            botY = r.nextInt(map[botX].length);
        } while (map[botX][botY] == '#' || (botX == playerX && botY == playerY));
        
        map[botX][botY] = 'B'; // Show B at a specified location
        bot = new Bot(botX, botY); // Create a bot object and update the location
    }

    /* Player Movement */
    public void movePlayer(String direction) {
        player.move(direction, map, G_Posi, E_Posi);
        playerX = player.getX(); // Update the player's position (x)
        playerY = player.getY(); // Update the player's position (y)
        captured(player.getX(), player.getY()); // Check if the player was caught by a bot
    }

    /* Bot Movement */
    public void moveBot() {
        bot.move(map, G_Posi, E_Posi);
        botX = bot.getX(); // Update the player's position (x)
        botY = bot.getY(); // Update the player's position (y)
        captured(bot.getX(), bot.getY()); // Check if the player was caught by a bot
    }

    /* HELLO */
    public void hello() {
        System.out.println("Gold to win: " + G_toWin);
    }

    /* GOLD */
    public void displayG() {
        System.out.println("Gold Owned: " + player.getGcount());
    }

    /* PICKUP */
    public void pickupG() {
        if (player.pickup(G_Posi, map)) {
            System.out.println("Success. Gold owned: " + player.getGcount());
        } else {
            System.out.println("Fail. Gold owned: " + player.getGcount());
        }
    }

    /* QUIT: Check if P is at the same coordinate as E and has G greater than or equal to G_toWin */
    public void quit() {
        if (player.is_EPosi(playerX, playerY, E_Posi) && player.getGcount() >= G_toWin) {
            System.out.println("WIN. You became a millionaire!");
        } else {
            System.out.println("LOSE.");
        }
        System.exit(0);
    }


    /* Collision judgment */
    public void captured(int currentX, int currentY) {
        if (playerX == botX && playerY == botY) {
            System.out.println("LOSE. You are captured.");
            map[botX][botY] = '*'; // collision point
            displayMap();
            System.exit(0); // quit the game
        }
    }

    /* Display the whole map */
    public void displayMap() {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++){
                System.out.print(map[i][j]);
            }
            System.out.println();
        }
    }

    /* LOOK */
    public void look() {
        int topX = playerX - 2; // 2 rows above P
        int leftY = playerY - 2; // 2 columns left from P
    
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                int x = topX + i;
                int y = leftY + j;
    
                /* If x and y are outside the range of 0~.length, # is drawn. Others are displayed as is */
                if (x < 0 || x >= map.length || y < 0 || y >= map[x].length) {
                    System.out.print("#");
                } else {
                    System.out.print(map[x][y]);
                }
            }
            System.out.println();
        }
    }
}
