/**
 * Responsible for displaying the map and player surroundings.
 */
public class Renderer {

    /**
     * Displays the entire map.
     */
    public void displayMap(char[][] map) {
        for (char[] row : map) {
            for (char tile : row) {
                System.out.print(tile);
            }
            System.out.println();
        }
    }

    /**
     * Displays a 5x5 area centered around the player.
     * If the area goes outside the map bounds, walls ('#') are shown.
     */
    public void displayLook(char[][] map, int playerX, int playerY) {
        int topX = playerX - 2;
        int leftY = playerY - 2;

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                int x = topX + i;
                int y = leftY + j;

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
