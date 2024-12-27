import java.util.List;

public class Player {
    private int x, y; // プレイヤーの位置
    private int Gcounter = 0; // 持ってるGの数




    // コンストラクタで初期位置を設定
    public Player(int startX, int startY) {
        this.x = startX;
        this.y = startY;
    }


//     public void movePlayer(String direction, char[][] map, List<int[]> G_Posi, GameLogic gameLogic) {
//         this.move(direction, map, G_Posi); // プレイヤーの移動処理
//         gameLogic.updatePlayerPosition(this.getX(), this.getY()); // GameLogic にプレイヤーの位置更新を通知
//         gameLogic.captured(this.getX(), this.getY()); // Botに捕まったかどうか確認
// }

    

    // プレイヤーを移動するメソッド
    public void move(String direction, char[][] map, List<int[]> G_Posi) {

        int newX = x;
        int newY = y;

        if (direction.equals("N")) {
            newX = x - 1; // 北
        } else if (direction.equals("S")) {
            newX = x + 1; // 南
        } else if (direction.equals("E")) {
            newY = y + 1; // 東
        } else if (direction.equals("W")) {
            newY = y - 1; // 西
        } else {
            System.out.println("Invalid input. Please enter N, E, W, or S.");
            return; // 入力が無効なら再入力を促す
        }

        // 移動可能か判定
        if (isValidMove(newX, newY, map)) {

            if (is_GPosi(x, y, G_Posi)) {
                map[x][y] = 'G'; //その地点がGのリストと一致する場合にはGに戻す
            } else {
                map[x][y] = '.'; // それ以外の場所なら通常の床に戻す
            }


            // 新しい位置にプレイヤーを配置
            x = newX;
            y = newY;
            map[x][y] = 'P';

            System.out.println("Success"); //移動成功のメッセージ。            

        } else {
            System.out.println("Fail"); //移動失敗のメッセージ。
        }
    }

    // 移動可能かどうかを判定するメソッド
    private boolean isValidMove(int x, int y, char[][] map) {
        return x >= 0 && x < map.length && y >= 0 && y < map[x].length && map[x][y] != '#';
    }

    // そこがGの場所かどうかを判定するメソッド
    private boolean is_GPosi(int x, int y, List<int[]> G_Posi) {
        for (int[] gold : G_Posi) {
            if (gold[0] == x && gold[1] == y) {
                return true;
            }
        }
        return false;
    }

    // そこがExitかどうかを判定するメソッド
    public boolean is_EPosi(int x, int y, List<int[]> E_Posi) {
        for (int[] exit : E_Posi) {
            if (exit[0] == x && exit[1] == y) {
                return true;
            }
        }
        return false;
    }


    //Pickupに関連するメソッド
    public boolean pickup(List<int[]> G_Posi, char[][] map) {
        for (int i = 0; i < G_Posi.size(); i++) {
            int[] gold = G_Posi.get(i);
            if (gold[0] == x && gold[1] == y ) {
                G_Posi.remove(i); // ゴールド位置をリストから削除
                map[x][y] = 'P'; // マップを更新
                Gcounter++;
                // System.out.println("Success. Gold owned: " + Gcounter);
                return true;
            }
        }
        // System.out.println("Fail. Gold owned: " + Gcounter);
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
