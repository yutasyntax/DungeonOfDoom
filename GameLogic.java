import java.io.*; //ファイル出入力に関する必要なクラス
//ランダムな値を生成するクラス
import java.util.*; //リスト系に必要なもの
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GameLogic {
    private char [][] map; //２次元のマップデータ（変数）を格納する
    private int playerX, playerY; //playerの位置を決める変数を作成
    // private Bot bot; // Botクラス型の変数の「宣言」？この段階ではデータもオブジェクトも持たない
    private int botX, botY; //botの位置を決める変数
    private Player player; // プレイヤーオブジェクトの宣言？
    private int G_toWin = 0; //勝利条件はデフォルト０

    private List<int[]> G_Posi = new ArrayList<>(); //Goldのポジションを保存するリスト
    private List<int[]> E_Posi = new ArrayList<>(); //Exitのポジションを保存するリスト


    /*マップの形式が正しいかどうかをチェック */
    public void inquireMap(Scanner s){
        // Scanner s = new Scanner(System.in); 
        while (true) {
            System.out.println("Input the pass (e.g., DungeonOfDoom/dungeon.txt):");
            String fileName = s.nextLine(); //ユーザーの入力をfileName に代入する

            /*fileNameを使ってマップを読み込む。正常にロードされるまで繰り返す*/
            if (loadMap(fileName)) {
                break;
            } else {
                System.out.println("Please try again.");
            }
        }    
    }


    /*マップの読み込み*/
    public boolean loadMap(String fileName) { //mainで入力されたmapfileを読み込むメソッド

        /*ファイルが渡されなかった場合のエラー */
        if (fileName == null || fileName.isEmpty()) {
            System.out.println("Error: file is not provided.");
            return false;
        }

        try {
            List<char[]> lines = new ArrayList<>(); //char[]型のデータを保存するリストを作って、linesと言う名前にする（設計図を作る）
                                                    //new ArrayList<>();で　その設計図に沿ったオブジェクトを作る
            BufferedReader br = new BufferedReader(new FileReader(fileName)); //fileを読むFileReaderを効率よく読み取るBufferReaderのオブジェクトを作成
            String line;

            int lineNum = 0; //現在の行No.を保存
            Pattern pattern = Pattern.compile("win (\\d+)"); //win　数字の正規表現を探す
        

            //brに対して実際にreadlineして１行のデータをlineに代入。ファイルの終わりに達するまで繰り返す
            while ((line = br.readLine()) != null) {
                lineNum++;

                if (lineNum == 2) {
                    Matcher matcher = pattern.matcher(line);
                    if (matcher.find()) {
                        G_toWin = Integer.parseInt(matcher.group(1));
                    } else {
                        G_toWin = 0;
                    }
                } else if (lineNum > 2) { //3行め以降に対して・・・
                    //lineというストリング型に対してchar型に変換する。それをさっきのlinesと言うリストに入れる
                    //linesは複数の行を同時に抱えることができる
                    lines.add(line.toCharArray());
                }
            }
            //接続を閉じてリソースを解放
            br.close();
            
            //char[行数][列数]は2次元の配列
            //toArrayによりリスト型のlinesを2次元の配列に変換。
            //配列にすることで2行目の３列目とかにアクセスするのが楽になる
            map = lines.toArray(new char[lines.size()][]);


            // GoldとExitの位置を把握
            for (int i = 0; i < map.length; i++) { //全ての列で繰り返し
                for (int j = 0; j < map[i].length; j++) { //全ての行で繰り返し
                    if (map[i][j] == 'G') {
                        G_Posi.add(new int[]{i, j});
                    } else if (map[i][j] == 'E') {
                        E_Posi.add(new int[]{i, j});
                    }
                }
            }

            return true;
        //try-catch構文　IOException(入出力系のエラー)が出たら 変数eに格納
        } catch (IOException e) {
            System.out.println("Error: Could not load the map file.");
            //eの中にあるエラー情報の詳細を出力
            e.printStackTrace();
            return false;
        }
    }


    public void positioningPandB() {
        if (map == null) {
            System.out.println("Error: Map is not loaded.");
            return;
        }

        Random rand = new Random(); //標準ライブラリのランダムクラスを使ったオブジェクト作成
        // int playerX, playerY;
        
        // プレイヤーのランダム配置
        // do-while構文で最低一度はループを実行する
        do {
            // nextIntはrandomクラスのメソッド。０〜（）の範囲内でランダムな数を返す。
            playerX = rand.nextInt(map.length -2) +2; //map.length＝行の範囲内
            playerY = rand.nextInt(map[playerX].length); //map[playerX].length=０行目だとしたときの列数
        } while (map[playerX][playerY] == '#');
        
        //　マップにプレイヤーを配置する
        map[playerX][playerY] = 'P';

        // Player オブジェクトの位置情報も更新
        player = new Player(playerX, playerY);

        // botをランダム配置
        do {
            botX = rand.nextInt(map.length -2) +2;
            botY = rand.nextInt(map[botX].length);
        } while (map[botX][botY] == '#' || (botX == playerX && botY == playerY));
        
        //　マップにボットを配置する
        map[botX][botY] = 'B';

        // Botのオブジェクトを生成
        // bot = new Bot(botX, botY); //新しいBotオブジェクトを生成して変数に割り当てる

    }

    public void movePlayer(String direction) {
        player.move(direction, map, G_Posi); // Player クラスの move メソッドを呼び出す
        playerX = player.getX(); // プレイヤーのX座標を更新
        playerY = player.getY(); // プレイヤーのY座標を更新
        captured(player.getX(), player.getY()); //Botに捕まったかどうか確認
    }

    public void moveBot() {
        Random rand = new Random();
        int newX = botX;
        int newY = botY;
        
        while (true) {
            newX = botX;
            newY = botY;

            //ランダムに進行方向を選択
            int botDirection = rand.nextInt(4);

            //方向ごとの条件
            if (botDirection == 0) {
                newX = botX - 1; // 北
            } else if (botDirection == 1) {
                newX = botX + 1; // 南
            } else if (botDirection == 2) {
                newY = botY + 1; // 東
            } else if (botDirection == 3) {
                newY = botY - 1; // 西
            }

            if (newX >= 0 && newX < map.length && newY >= 0 && newY < map[newX].length && map[newX][newY] != '#') {
                break;
            }
        }
        // 現在の位置を元の状態に戻す
        if (isGoldPosition(botX, botY, G_Posi)) {
            map[botX][botY] = 'G'; // 元の位置がゴールドの場合はGに戻す
        } else if (is_EPosi(botX, botY, E_Posi)) {
            map[botX][botY] = 'E'; // 元の位置がExitの場合はEに戻す
        } else {
            map[botX][botY] = '.'; // それ以外の場合は通常の床に戻す
        }

        // 新しい位置に移動
        botX = newX;
        botY = newY;
        map[botX][botY] = 'B'; // Botを新しい位置に配置

        captured(botX, botY); //Botに捕まったかどうか確認
    }

// Botがゴールド位置にいるか確認
private boolean isGoldPosition(int x, int y, List<int[]> goldPositions) {
    for (int[] gold : goldPositions) {
        if (gold[0] == x && gold[1] == y) {
            return true;
        }
    }
    return false;
}

// BotがExit位置にいるか確認
private boolean is_EPosi(int x, int y, List<int[]> exitPositions) {
    for (int[] exit : exitPositions) {
        if (exit[0] == x && exit[1] == y) {
            return true;
        }
    }
    return false;
}







    //マップを表示するメソッド
    public void displayMap() {
        // 2次元配列の変数mapが初期化（宣言されて、値が代入されて、配列であればそのサイズn✖️ｎが設定されること）されてない場合
        if (map == null) {
            System.out.println("Map is not loaded.");
            return;
        }

        //enhanced for loop(mapの各行を取り出してはrowと言う変数に格納しながらループする)
        for (int i = 0; i < map.length; i++) {
            //1次元の行rowから1文字ごとの文字を抜き取ってtileに代入。
            for (int j = 0; j < map[i].length; j++){
                System.out.print(map[i][j]);
            }
            System.out.println(); //各行の終わりに改行を入れる。
        }
    }

    public void pickupG() {
        if (player.pickup(G_Posi, map)) {
            System.out.println("Success. Gold owned: " + player.getGcount());
        } else {
            System.out.println("Fail. Gold owned: " + player.getGcount());
        }
    }

    public void displayG() {
        System.out.println("Gold Owned: " + player.getGcount());
    }

    //ただGtoWinを表示するだけ
    public void hello() {
        System.out.println("Gold to win: " + G_toWin);
    }

    public void quit() {
        if (player.is_EPosi(player.getX(), player.getY(), E_Posi) && player.getGcount() >= G_toWin) {
            System.out.println("WIN. You became a millionaire!");
        } else {
            System.out.println("LOSE.");
        }
        System.exit(0);
    }


    public void captured(int currentX, int currentY) {
        System.out.println("Player position: (" + player.getX() + ", " + player.getY() + ")");
        System.out.println("Bot position: (" + botX + ", " + botY + ")");
        System.out.println("Checking collision at: (" + currentX + ", " + currentY + ")");

        if (playerX == botX && playerY == botY) {
            System.out.println("LOSE. You are captured.");
            map[botX][botY] = '*';
            displayMap();
            System.exit(0); // ゲームを終了
        }
    }

    public void look() {
        int startX = playerX - 2; // プレイヤーを中心にする
        int startY = playerY - 2;
    
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                int currentX = startX + i;
                int currentY = startY + j;
    
                if (currentX < 0 || currentX >= map.length || currentY < 0 || currentY >= map[currentX].length) {
                    // 範囲外は壁として表示
                    System.out.print("#");
                } else if (currentX == playerX && currentY == playerY) {
                    // プレイヤーの位置を中心にする
                    System.out.print("P");
                } else {
                    // それ以外はマップの内容をそのまま表示
                    System.out.print(map[currentX][currentY]);
                }
            }
            System.out.println(); // 行ごとに改行
        }
    }


}




