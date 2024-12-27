import java.util.Scanner; //Scannerをインポート

public class Main {
    public static void main(String[] args) { //このプログラムのエントリーポイント。 String[]argsは”任意の数の文字列を入力として受け取る”
        GameLogic game = new GameLogic(); //GameLogicクラスのオブジェクトを作成（そのクラスのメソッドを使用するため）
        Scanner s = new Scanner(System.in); //コマンドラインから入力を受け取るためのScannerオブジェクトの生成

        game.inquireMap(s); //マップの読み込みと初期化
        game.positioningPandB(); //プレイヤーとボットをランダムに配置する

        
        /*コマンドの読み取り*/
        while (true) {

            /*正しいコマンドを入力するまで繰り返し*/
            while (true) {
                System.out.println("Enter a command(HELLO / GOLD / PICKUP / MOVE <direction> / LOOK / QUIT): ");
                String command = s.nextLine().toUpperCase(); // 入力を大文字に変換しながら読み取り
                System.out.println(); //改行

                if (command.equals("HELLO")) { 
                    game.hello(); //勝利条件を確認する
                    break;
                } else if (command.equals("GOLD")) {
                    game.displayG(); //保有しているGoldの数を表示する
                    break;
                } else if (command.equals("MOVE N")) {
                    game.movePlayer("N"); //上に１マス移動
                    break;
                } else if (command.equals("MOVE E")) {
                    game.movePlayer("E"); //右に１マス移動
                    break;
                } else if (command.equals("MOVE W")) {
                    game.movePlayer("W"); //左に１マス移動
                    break;
                } else if (command.equals("MOVE S")) {
                    game.movePlayer("S"); //下に１マス移動
                    break;
                } else if (command.equals("PICKUP")) {
                    game.pickupG(); //Goldを拾う
                    break;
                } else if (command.equals("LOOK")) {
                    game.look(); //周囲5*5マスを表示する
                    break;
                } else if (command.equals("QUIT")) {
                    game.quit(); //Gameを終了する
                    break;
                } else {
                    System.out.println("Error: invalid input."); //無効な入力の場合にはターンを消費しない
                }
            }
            game.moveBot(); // ターン終了時にBotが１マス動く

        }
    }
}