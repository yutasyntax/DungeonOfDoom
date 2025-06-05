import java.util.List;
import java.util.Random;

/**
 * Represents a bot (enemy) in the dungeon.
 * Handles movement and map interactions.
 */
public class Bot {
    private int x, y; // Current position of the bot

    /**
     * Creates a new bot at the given coordinates.
     */
    public Bot(int botX, int botY) {
        this.x = botX;
        this.y = botY;
    }

    /**
     * Moves the bot one step in a random valid direction.
     * Restores the tile it previously stood on, and updates the map.
     */
    public void move(char[][] map, List<int[]> G_Posi, List<int[]> E_Posi) {
        Random r = new Random();
        int newX = x;
        int newY = y;

        // Randomly pick a valid direction until a non-wall tile is found
        while (true) {
            newX = x;
            newY = y;

            int rnd = r.nextInt(4);
            if (rnd == 0) newX = x - 1; // North
            else if (rnd == 1) newX = x + 1; // South
            else if (rnd == 2) newY = y + 1; // East
            else newY = y - 1; // West

            if (newX >= 0 && newX < map.length &&
                newY >= 0 && newY < map[newX].length &&
                map[newX][newY] != '#') {
                break;
            }
        }

        // Restore the original tile under the bot
        if (is_GPosi(x, y, G_Posi)) {
            map[x][y] = 'G';
        } else if (is_EPosi(x, y, E_Posi)) {
            map[x][y] = 'E';
        } else {
            map[x][y] = '.';
        }

        // Move the bot to the new location
        x = newX;
        y = newY;
        map[x][y] = 'B';
    }

    /**
     * Checks if the given coordinates match a gold position.
     */
    private boolean is_GPosi(int x, int y, List<int[]> G_Posi) {
        for (int[] pos : G_Posi) {
            if (pos[0] == x && pos[1] == y) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the given coordinates match an exit position.
     */
    public boolean is_EPosi(int x, int y, List<int[]> E_Posi) {
        for (int[] pos : E_Posi) {
            if (pos[0] == x && pos[1] == y) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the current X coordinate of the bot.
     */
    public int getX() {
        return x;
    }

    /**
     * Returns the current Y coordinate of the bot.
     */
    public int getY() {
        return y;
    }
}
