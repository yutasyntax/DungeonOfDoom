import java.util.List;

/**
 * Represents the player in the dungeon.
 * Handles position, movement, and gold collection.
 */
public class Player {
    private int x, y;          // Current position of the player
    private int Gcounter = 0; // Number of gold pieces collected

    /**
     * Creates a new player at the given coordinates.
     */
    public Player(int playerX, int playerY) {
        this.x = playerX;
        this.y = playerY;
    }

    /**
     * Moves the player in the given direction if the destination is valid.
     * Updates the map and restores underlying tile content (G/E/.).
     */
    public void move(String direction, char[][] map, List<int[]> G_Posi, List<int[]> E_Posi) {
        int newX = x;
        int newY = y;

        switch (direction) {
            case "N": newX = x - 1; break;
            case "S": newX = x + 1; break;
            case "E": newY = y + 1; break;
            case "W": newY = y - 1; break;
            default:
                System.out.println("Error: Invalid input.");
                return;
        }

        if (isValidArea(newX, newY, map)) {
            // Restore the original tile where the player was standing
            if (is_GPosi(x, y, G_Posi)) {
                map[x][y] = 'G';
            } else if (is_EPosi(x, y, E_Posi)) {
                map[x][y] = 'E';
            } else {
                map[x][y] = '.';
            }

            // Move the player to the new position
            x = newX;
            y = newY;
            map[x][y] = 'P';

            System.out.println("Success");
        } else {
            System.out.println("Fail");
        }
    }

    /**
     * Checks if the destination tile is not a wall.
     */
    private boolean isValidArea(int x, int y, char[][] map) {
        return x >= 0 && x < map.length &&
        y >= 0 && y < map[x].length &&
        map[x][y] != '#';
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
     * If the player is on a gold tile, pick it up and remove it from the map.
     */
    public boolean pickup(List<int[]> G_Posi, char[][] map) {
        for (int i = 0; i < G_Posi.size(); i++) {
            int[] pos = G_Posi.get(i);
            if (pos[0] == x && pos[1] == y) {
                G_Posi.remove(i);
                Gcounter++;
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the amount of gold the player has collected.
     */
    public int getGcount() {
        return Gcounter;
    }

    /**
     * Returns the current X coordinate of the player.
     */
    public int getX() {
        return x;
    }

    /**
     * Returns the current Y coordinate of the player.
     */
    public int getY() {
        return y;
    }
}
