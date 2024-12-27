import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;



public class GameLogic {
    private char [][] map; //２次元のマップデータ（変数）を格納する
    private Player player; // プレイヤーオブジェクトの変数
    private int playerX, playerY; //playerの位置の変数
    
    private Bot bot; // Botオブジェクトの変数
    private int botX, botY; //botの位置の変数

    private int G_toWin = 0; //勝利条件

    private List<int[]> G_Posi = new ArrayList<>(); //Goldのポジションを保存するリスト
    private List<int[]> E_Posi = new ArrayList<>(); //Exitのポジションを保存するリスト


    /*マップの形式が正しいかどうかをチェック */
    public void inquireMap(Scanner s){
        while (true) {
            System.out.println("Input the pass (e.g., DungeonOfDoom/dungeon.txt):");
            String fileName = s.nextLine(); //ユーザーの入力をfileName に代入する

            /*マップを読み込む。正常にロードされるまで繰り返す*/
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
            List<char[]> list = new ArrayList<>(); //マップ（charの配列）を保存するためのオブジェクト
            FileReader fr = new FileReader(fileName); //ファイルから文字を読み取るためのオブジェクト
            BufferedReader br = new BufferedReader(fr); //行単位で文字を読み取るためのオブジェクト
        
            
            /*mapデータ内の文字列"win (number)"から数字を読み取り、３行目以降からマップの構成を読み取る。*/
            String line;
            int lineNum = 0;

            while ((line = br.readLine()) != null) { 
                lineNum++;

                if (line.startsWith("win ")) {
                    String num = line.substring(4).trim(); // "win "をtrimして数字だけを取得
                    int G_toWin = Integer.parseInt(num);
                    System.out.println("\nGold to win: " + G_toWin);
                } else {
                        G_toWin = 0; 
                }
                if (lineNum > 2) {
                    list.add(line.toCharArray()); //読み込んだ文字列をchar[]に変換してリストに追加
                }
            }
            br.close(); 
            
            map = list.toArray(new char[list.size()][]); //listの集合を2次元の配列に変換。（char[行数][列数]は2次元の配列）

            /*map（2次元配列）からGoldとExitの位置を取得*/
            for (int i = 0; i < map.length; i++) { //全ての行に対して繰り返す
                for (int j = 0; j < map[i].length; j++) { //全ての列に対して繰り返す
                    if (map[i][j] == 'G') {
                        G_Posi.add(new int[]{i, j});
                    } else if (map[i][j] == 'E') {
                        E_Posi.add(new int[]{i, j});
                    }
                }
            }
            return true;

        /*try-catch構文　IOException(入出力系のエラー)が発生した場合の対応 */
        } catch (IOException e) {
            System.out.println("Error: ");
            e.printStackTrace(); //エラーの詳細を出力
            return false;
        }
    }


    /*Player(P)とBot(B)をランダムに配置する */
    public void positioningPandB() {

        Random r = new Random(); //ランダムクラスのオブジェクト生成
        
        /* プレイヤーの配置。マップファイルの最初の2行はメタデータなので、3行目から最終行までの範囲でランダムな数を生成する。* 壁(#)がない場所を指定するまで繰り返す*/
        do {
            playerX = r.nextInt(map.length -2) +2; //ランダムに行を選択
            playerY = r.nextInt(map[playerX].length); //指定された行内でランダムに列を選択
        } while (map[playerX][playerY] == '#');
        
        map[playerX][playerY] = 'P'; //指定された地点にプレイヤーを表示する
        player = new Player(playerX, playerY);  //Playerオブジェクトを生成して位置情報を更新


        /* botの配置。#とPがない場所を指定するまで繰り返す*/
        do {
            botX = r.nextInt(map.length -2) +2;
            botY = r.nextInt(map[botX].length);
        } while (map[botX][botY] == '#' || (botX == playerX && botY == playerY));
        
        map[botX][botY] = 'B'; //指定された地点にボットを配置する
        bot = new Bot(botX, botY); //Botオブジェクトを生成して位置情報を更新

    }


    public void movePlayer(String direction) {
        player.move(direction, map, G_Posi); // Player クラスの move メソッドを呼び出す
        playerX = player.getX(); // プレイヤーのX座標を更新
        playerY = player.getY(); // プレイヤーのY座標を更新
        captured(player.getX(), player.getY()); //Botに捕まったかどうか確認
    }




    public void moveBot() {
        bot.move(map, G_Posi, E_Posi); // Bot の移動処理を委譲
        captured(bot.getX(), bot.getY()); // 捕獲判定
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


//     public void moveBot() {
//         Random rand = new Random();
//         int newX = botX;
//         int newY = botY;
        
//         while (true) {
//             newX = botX;
//             newY = botY;

//             //ランダムに進行方向を選択
//             int botDirection = rand.nextInt(4);

//             //方向ごとの条件
//             if (botDirection == 0) {
//                 newX = botX - 1; // 北
//             } else if (botDirection == 1) {
//                 newX = botX + 1; // 南
//             } else if (botDirection == 2) {
//                 newY = botY + 1; // 東
//             } else if (botDirection == 3) {
//                 newY = botY - 1; // 西
//             }

//             if (newX >= 0 && newX < map.length && newY >= 0 && newY < map[newX].length && map[newX][newY] != '#') {
//                 break;
//             }
//         }
//         // 現在の位置を元の状態に戻す
//         if (isGoldPosition(botX, botY, G_Posi)) {
//             map[botX][botY] = 'G'; // 元の位置がゴールドの場合はGに戻す
//         } else if (is_EPosi(botX, botY, E_Posi)) {
//             map[botX][botY] = 'E'; // 元の位置がExitの場合はEに戻す
//         } else {
//             map[botX][botY] = '.'; // それ以外の場合は通常の床に戻す
//         }

//         // 新しい位置に移動
//         botX = newX;
//         botY = newY;
//         map[botX][botY] = 'B'; // Botを新しい位置に配置

//         captured(botX, botY); //Botに捕まったかどうか確認
//     }

// // Botがゴールド位置にいるか確認
// private boolean isGoldPosition(int x, int y, List<int[]> goldPositions) {
//     for (int[] gold : goldPositions) {
//         if (gold[0] == x && gold[1] == y) {
//             return true;
//         }
//     }
//     return false;
// }

// // BotがExit位置にいるか確認
// private boolean is_EPosi(int x, int y, List<int[]> exitPositions) {
//     for (int[] exit : exitPositions) {
//         if (exit[0] == x && exit[1] == y) {
//             return true;
//         }
//     }
//     return false;
// }

