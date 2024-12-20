import java.io.*; //ファイル出入力に関する必要なクラス
import java.util.*; //リスト系に必要なもの

package DungeonOfDoom;
public class GameLogic {
    private char [][] map; //２次元のマップデータ（変数）を格納する

    public void loadMap(String fileName) { //fileNameと言うファイルを読み込むメソッド
        try {
            List<char[]> lines = new ArrayList<>(); //char[]型のデータを保存するリストを作って、linesと言う名前にする（設計図を作る）
                                                    //new ArrayList<>();で　その設計図に沿ったオブジェクトを作る
            BufferedReader br = new BufferedReader(new FileReader(fileName)); //fileを読むFileReaderを効率よく読み取るBufferReaderのオブジェクトを作成
            String line;

            //brに対して実際にreadlineして１行のデータをlineに代入。ファイルの終わりに達するまで繰り返す
            while ((line = br.readLine()) != null) {
                //lineというストリング型に対してchar型に変換する。それをさっきのlinesと言うリストに入れる
                //linesは複数の行を同時に抱えることができる
                lines.add(line.toCharArray());
            }
            //接続を閉じてリソースを解放
            br.close();
            
            //char[行数][列数]は2次元の配列
            //toArrayによりリスト型のlinesを2次元の配列に変換。
            //配列にすることで2行目の３列目とかにアクセスするのが楽になる
            map = lines.toArray(new char[lines.size()][]);

        //try-catch構文　IOException(入出力系のエラー)が出たら 変数eに格納
        } catch (IOException e) {
            System.out.println("Error: Could not load the map file.");
            //eの中にあるエラー情報の詳細を出力
            e.printStackTrace();
        }
    }

    public void displayMap() {
        // 2次元配列の変数mapが初期化（宣言されて、値が代入されて、配列であればそのサイズn✖️ｎが設定されること）されてない場合
        if (map == null) {
            System.out.println("Map is not loaded.");
            return;
        }

        //enhanced for loop(mapの各行を取り出してはrowと言う変数に格納しながらループする)
        for (char[] row : map){
            //1次元の行rowから1文字ごとの文字を抜き取ってtileに代入。
            for (char tile : row){
                System.out.print(tile);
            }
            System.out.println(); //各行の終わりに改行を入れる。
        }
    }
}


public class Main {
    public static void main(String[] args) {
        GameLogic game = new GameLogic();
        game.loadMap("dungeon.txt");
        game.displayMap();
    }    
}
