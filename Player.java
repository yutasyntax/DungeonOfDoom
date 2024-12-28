import java.util.List;

public class Player {
    private int x, y; // Player position
    private int Gcounter = 0; // The number of G the player has

    /* constructor */ 
    public Player(int playerX, int playerY) {
        this.x = playerX;
        this.y = playerY;
    }

    /* player movement */
    public void move(String direction, char[][] map, List<int[]> G_Posi, List<int[]> E_Posi) {

        int newX = x;
        int newY = y;

        /* Update newX or newY according to direction passed from Main class*/
        if (direction.equals("N")) {
            newX = x - 1; // North
        } else if (direction.equals("S")) {
            newX = x + 1; // South
        } else if (direction.equals("E")) {
            newY = y + 1; // East
        } else if (direction.equals("W")) {
            newY = y - 1; // West
        } else {
            System.out.println("Error: Invalid input.");
            return;
        }

        if (isValidArea(newX, newY, map)) {
            /* Return P's location to the original tile (G,E,.) */
            if (is_GPosi(x, y, G_Posi)) {
                map[x][y] = 'G';
            } else if (is_EPosi(x, y, E_Posi)){
                map[x][y] = 'E';
            } else {
                map[x][y] = '.';
            }

            /* Place Player */
            x = newX;
            y = newY;
            map[x][y] = 'P';
            System.out.println("Success");   
        } else {
            System.out.println("Fail");
        }
    }

    /* Check if the destination is not '#' */
    private boolean isValidArea(int x, int y, char[][] map) {
        return map[x][y] != '#';
    }   

    /* Check if [x][y] matches Gold's position */ 
    private boolean is_GPosi(int x, int y, List<int[]> G_Posi) {
        for (int i = 0; i < G_Posi.size(); i++) {
            if (G_Posi.get(i)[0] == x && G_Posi.get(i)[1] == y) {
                return true;
            }
        }
        return false;
    }

    /* Check if [x][y] matches Exit's position */
    public boolean is_EPosi(int x, int y, List<int[]> E_Posi) {
        for (int i = 0; i < E_Posi.size(); i++) {
            if (E_Posi.get(i)[0] == x && E_Posi.get(i)[1] == y) {
                return true;
            }
        }
        return false;
    }

    /* If P is in the same position as G, increase 'Gcounter' and remove it from 'G_Posi'.*/
    public boolean pickup(List<int[]> G_Posi, char[][] map) {
        for (int i = 0; i < G_Posi.size(); i++) {
            int[] list = G_Posi.get(i);
            if (list[0] == x && list[1] == y ) {
                G_Posi.remove(i); // Remove gold locations from the map
                Gcounter++; // Increase Gold holdings
                return true;
            }
        }
        return false;
    }

    public int getGcount() {
        return Gcounter;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}