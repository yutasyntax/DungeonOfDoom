import java.util.List;
import java.util.Random;

public class Bot {
    private int x, y; // Bot position


    /* constructor */
    public Bot(int botX, int botY) {
        this.x = botX;
        this.y = botY;
    }

    /* bot movement */
    public void move(char[][] map, List<int[]> G_Posi, List<int[]> E_Posi) {
        Random r = new Random();
        int newX = x;
        int newY = y;

        /* Update newX and newY according to a randomly generated number between 0 and 3 */
        while (true) {
            int rnd = r.nextInt(4);
            if (rnd == 0) {
                newX = x - 1; // North
            } else if (rnd == 1) {
                newX = x + 1; // South
            } else if (rnd == 2) {
                newY = y + 1; // East
            } else if (rnd == 3) {
                newY = y - 1; // West
            }

            /* Check if the destination is not '#' */
            if (map[newX][newY] != '#') {
                break;
            }
        }


        /* Return B's location to the original tile (G,E,.) */
        if (is_GPosi(x, y, G_Posi)) {
            map[x][y] = 'G';
        } else if (is_EPosi(x, y, E_Posi)) {
            map[x][y] = 'E';
        } else {
            map[x][y] = '.';
        }

        /* Place Bot */
        x = newX;
        y = newY;
        map[x][y] = 'B';
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

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }


}
