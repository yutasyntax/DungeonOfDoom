import java.util.Scanner;

/**
 * GameManager handles the main game flow:
 * - Initializes the game
 * - Receives player input
 * - Delegates command processing and game state updates
 */
public class GameManager {

    private GameLogic logic;
    private CommandProcessor commandProcessor;

    public GameManager() {
        this.logic = new GameLogic();
        this.commandProcessor = new CommandProcessor(logic);
    }

    /*
     * Runs the main game loop.
     * Waits for player input and processes each turn.
     */
    public void startGame() {
        Scanner scanner = new Scanner(System.in);
        logic.initializeGame(scanner);
    
        System.out.println("Welcome to Gold Escape!");
    
        while (true) {
            System.out.println("Enter a command => HELLO / GOLD / PICKUP / MOVE (N,S,W,E) / LOOK / QUIT:");
            System.out.print("> ");
            String input = scanner.nextLine();
            commandProcessor.processCommand(input);
            logic.updateGameState();

            try {
                logic.updateGameState();
            } catch (Exception e) {
                System.out.println("Error during bot movement: " + e.getMessage());
                e.printStackTrace();
            }

        }
    }
}
