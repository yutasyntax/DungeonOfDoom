/**
 * Processes player input and routes commands to the appropriate GameLogic methods.
 */
public class CommandProcessor {
    private GameLogic logic;

    public CommandProcessor(GameLogic logic) {
        this.logic = logic;
    }

    /**
     * Parses and executes a command string entered by the player.
     */
    public void processCommand(String input) {
        String cmd = input.trim().toUpperCase();

        switch (cmd) {
            case "HELLO":
                logic.hello(); // Show win condition
                break;
            case "GOLD":
                logic.displayG(); // Show player's gold count
                break;
            case "PICKUP":
                logic.pickupG(); // Try to collect gold on current tile
                break;
            case "LOOK":
                logic.look(); // Show 5x5 map around the player
                break;
            case "QUIT":
                logic.quit(); // End the game
                break;
            default:
                if (cmd.startsWith("MOVE")) {
                    String[] parts = cmd.split(" ");
                    if (parts.length == 2) {
                        logic.movePlayer(parts[1]);
                    } else {
                        System.out.println("Invalid MOVE command.");
                    }
                } else {
                    System.out.println("Invalid command.");
                }
                break;
        }
    }
}
