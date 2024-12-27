import java.util.List;
import java.util.Random;

public class Bot {
    private int x, y; // Botの位置


    /* コンストラクタ */
    public Bot(int botX, int botY) {
        this.x = botX;
        this.y = botY;
    }

    /* Botの移動 */
    public void move(char[][] map, List<int[]> G_Posi, List<int[]> E_Posi) {
        Random r = new Random();
        int newX = x;
        int newY = y;

        /*０から３までのランダムに生成した数字に応じてnewXとnewYを更新 */
        while (true) {
            int rnd = r.nextInt(4);
            if (rnd == 0) {
                newX = x - 1; // 北
            } else if (rnd == 1) {
                newX = x + 1; // 南
            } else if (rnd == 2) {
                newY = y + 1; // 東
            } else if (rnd == 3) {
                newY = y - 1; // 西
            }

            /*移動先が壁でないかどうかを判定 */
            if (map[newX][newY] != '#') {
                break;
            }
        }


        /* Bが居た場所を元のタイル(G,E,.)に戻す */
        if (is_GPosi(x, y, G_Posi)) {
            map[x][y] = 'G';
        } else if (is_EPosi(x, y, E_Posi)) {
            map[x][y] = 'E';
        } else {
            map[x][y] = '.';
        }

        /* Botを配置 */
        x = newX;
        y = newY;
        map[x][y] = 'B';
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

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }


}
