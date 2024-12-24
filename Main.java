import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        GameLogic game = new GameLogic();
        
        game.loadMap("DungeonOfDoom/dungeon.txt");
        game.initialisePlayers();

        Scanner scanner = new Scanner(System.in);

        // プレイヤーの移動を受け付けるゲームループ
        while (true) {
            game.displayMap();
            System.out.println("Enter a command (HELLO / GOLD / PICKUP / MOVE <direction> / LOOK / QUIT:");
            String command = scanner.nextLine().toUpperCase(); // 入力を大文字に変換


            switch (command) {
                case"HELLO":
                    game.hello();
                    break;
                case"GOLD":
                    game.displayG();
                    break;
                case "MOVE N":
                    game.movePlayer("N"); // プレイヤーの移動
                    // game.displayMap(); // 移動後のマップを表示
                    break;
                case "MOVE E":
                    game.movePlayer("E"); // プレイヤーの移動
                    // game.displayMap(); // 移動後のマップを表示
                    break;        
                case "MOVE W":
                    game.movePlayer("W"); // プレイヤーの移動
                    // game.displayMap(); // 移動後のマップを表示
                    break;
                case "MOVE S":
                    game.movePlayer("S"); // プレイヤーの移動
                    // game.displayMap(); // 移動後のマップを表示
                    break;        
                case "PICKUP":
                    game.pickupG();
                    break;
                case "QUIT":
                    game.quit();
                    break;
                default:
                    System.out.println("Invalid command.");
            }



            // // ゲーム終了条件のチェック（仮に追加）
            // // 例: 必要なゴールドを集めて出口に到達したら終了
            // if (game.isGameOver()) {
            //     System.out.println("Game Over!");
            //     break;
            // }
        }
    }
}