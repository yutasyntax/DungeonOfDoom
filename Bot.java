import java.util.List;
import java.util.Random;

public class Bot {
    private int x;
    private int y;

    public Bot(int startX, int startY) {
        this.x = startX;
        this.y = startY;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void move(char[][] map, List<int[]> goldPositions, List<int[]> exitPositions) {
        Random rand = new Random();
        int newX = x;
        int newY = y;

        while (true) {
            newX = x;
            newY = y;

            int botDirection = rand.nextInt(4);
            if (botDirection == 0) newX = x - 1; // 北
            else if (botDirection == 1) newX = x + 1; // 南
            else if (botDirection == 2) newY = y + 1; // 東
            else if (botDirection == 3) newY = y - 1; // 西

            if (newX >= 0 && newX < map.length && newY >= 0 && newY < map[newX].length && map[newX][newY] != '#') {
                break;
            }
        }

        // 現在の位置を元の状態に戻す
        if (isGoldPosition(x, y, goldPositions)) {
            map[x][y] = 'G';
        } else if (isExitPosition(x, y, exitPositions)) {
            map[x][y] = 'E';
        } else {
            map[x][y] = '.';
        }

        // 新しい位置に移動
        x = newX;
        y = newY;
        map[x][y] = 'B';
    }

    private boolean isGoldPosition(int x, int y, List<int[]> goldPositions) {
        for (int[] gold : goldPositions) {
            if (gold[0] == x && gold[1] == y) {
                return true;
            }
        }
        return false;
    }

    private boolean isExitPosition(int x, int y, List<int[]> exitPositions) {
        for (int[] exit : exitPositions) {
            if (exit[0] == x && exit[1] == y) {
                return true;
            }
        }
        return false;
    }
}
