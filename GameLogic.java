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
    private int botX, botY; //botの位置を決める変数
    private Player player; // プレイヤーオブジェクトの宣言？
    private int G_toWin = 0; //勝利条件はデフォルト０

    private List<int[]> G_Posi = new ArrayList<>(); //Goldのポジションを保存するリスト
    private List<int[]> E_Posi = new ArrayList<>(); //Exitのポジションを保存するリスト

    public void loadMap(String fileName) { //fileNameと言うファイルを読み込むメソッド
        try {
            List<char[]> lines = new ArrayList<>(); //char[]型のデータを保存するリストを作って、linesと言う名前にする（設計図を作る）
                                                    //new ArrayList<>();で　その設計図に沿ったオブジェクトを作る
            BufferedReader br = new BufferedReader(new FileReader("DungeonOfDoom/dungeon.txt")); //fileを読むFileReaderを効率よく読み取るBufferReaderのオブジェクトを作成
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
        //try-catch構文　IOException(入出力系のエラー)が出たら 変数eに格納
        } catch (IOException e) {
            System.out.println("Error: Could not load the map file.");
            //eの中にあるエラー情報の詳細を出力
            e.printStackTrace();
        }
    }


    public void initialisePlayers() {
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

    }

    public void movePlayer(String direction) {
        player.move(direction, map, G_Posi); // Player クラスの move メソッドを呼び出す
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

}




