import java.util.Scanner; //??????

public class Player {
    private int x, y; // プレイヤーの位置

    // コンストラクタで初期位置を設定
    public Player(int startX, int startY) {
        this.x = startX;
        this.y = startY;
    }

    // プレイヤーを移動するメソッド
    public void move(char[][] map) {
        Scanner scanner = new Scanner(System.in); // ?????
        System.out.println("Enter direction to move (N, E, W, S):");
        String input = scanner.nextLine().toUpperCase(); // 入力を大文字に変換

        int newX = x;
        int newY = y;

        switch (input) {
            case "N": // 北
                newX = x - 1;
                break;
            case "S": // 南
                newX = x + 1;
                break;
            case "E": // 東
                newY = y + 1;
                break;
            case "W": // 西
                newY = y - 1;
                break;
            default:
                System.out.println("Invalid input. Please enter N, E, W, or S.");
                return; // 入力が無効なら再入力を促す
        }

        // 移動可能か判定
        if (isValidMove(newX, newY, map)) {
            // 元の位置を空白に変更
            map[x][y] = '.';

            // 新しい位置にプレイヤーを配置
            x = newX;
            y = newY;
            map[x][y] = 'P';

            System.out.println("Player moved to (" + x + ", " + y + ")");
        } else {
            System.out.println("Cannot move to that location. Try a different direction.");
        }
    }

    // 移動可能かどうかを判定するメソッド
    private boolean isValidMove(int x, int y, char[][] map) {
        return x >= 0 && x < map.length && y >= 0 && y < map[x].length && map[x][y] != '#';
    }
}
