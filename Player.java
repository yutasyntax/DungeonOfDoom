import java.util.List;


public class Player {
    private int x, y; // プレイヤーの位置
    private int Gcounter = 0; // プレイヤーが保有するGの数

    /* コンストラクタ */ 
    public Player(int playerX, int playerY) {
        this.x = playerX;
        this.y = playerY;
    }

    /* プレイヤーの移動 */
    public void move(String direction, char[][] map, List<int[]> G_Posi, List<int[]> E_Posi) {

        int newX = x;
        int newY = y;

        /* Mainクラスから渡された direction に応じてnewXとnewYを更新*/
        if (direction.equals("N")) {
            newX = x - 1; // 北
        } else if (direction.equals("S")) {
            newX = x + 1; // 南
        } else if (direction.equals("E")) {
            newY = y + 1; // 東
        } else if (direction.equals("W")) {
            newY = y - 1; // 西
        } else {
            System.out.println("Error: Invalid input."); //エラーハンドリング（通常は使用しない）
            return;
        }

        
        if (isValidArea(newX, newY, map)) {
            /* Pが居た場所を元のタイル(G,E,.)に戻す */
            if (is_GPosi(x, y, G_Posi)) {
                map[x][y] = 'G';
            } else if (is_EPosi(x, y, E_Posi)){
                map[x][y] = 'E';
            } else {
                map[x][y] = '.';
            }

            /* プレイヤーを配置 */
            x = newX;
            y = newY;
            map[x][y] = 'P';
            System.out.println("Success");   
        } else {
            System.out.println("Fail");
        }
    }


    /*移動先が壁でないかどうかを判定*/
    private boolean isValidArea(int x, int y, char[][] map) {
        return map[x][y] != '#';
    }   

    /* [x][y]がGoldの位置と合致するか判定 */ 
    private boolean is_GPosi(int x, int y, List<int[]> G_Posi) {
        for (int i = 0; i < G_Posi.size(); i++) {
            if (G_Posi.get(i)[0] == x && G_Posi.get(i)[1] == y) {
                return true;
            }
        }
        return false;
    }

    /* [x][y]がExitの位置と合致するか判定 */
    public boolean is_EPosi(int x, int y, List<int[]> E_Posi) {
        for (int i = 0; i < E_Posi.size(); i++) {
            if (E_Posi.get(i)[0] == x && E_Posi.get(i)[1] == y) {
                return true;
            }
        }
        return false;
    }


    /* Pickupメソッド。PがGと同じ位置にいる場合にGcounterをカウントアップし、G_Posiリストから削除する*/
    public boolean pickup(List<int[]> G_Posi, char[][] map) {
        for (int i = 0; i < G_Posi.size(); i++) {
            int[] list = G_Posi.get(i);
            if (list[0] == x && list[1] == y ) {
                G_Posi.remove(i); // ゴールド位置をリストから削除
                Gcounter++; //Gold所有数をカウントアップ
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

