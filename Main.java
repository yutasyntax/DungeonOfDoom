public class Main {
    public static void main(String[] args) {
        GameLogic game = new GameLogic();
        
        game.loadMap("DungeonOfDoom/dungeon.txt");

        game.initialisePlayers();

        game.displayMap();

        // プレイヤーの移動を受け付けるゲームループ
        while (true) {
            game.movePlayer(); // プレイヤーの移動
            game.displayMap(); // 移動後のマップを表示

            // // ゲーム終了条件のチェック（仮に追加）
            // // 例: 必要なゴールドを集めて出口に到達したら終了
            // if (game.isGameOver()) {
            //     System.out.println("Game Over!");
            //     break;
            // }
        }
    }
}