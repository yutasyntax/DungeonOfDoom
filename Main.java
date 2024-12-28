import java.util.Scanner;

public class Main {
    public static void main(String[] args) { //Entry point for this program
        GameLogic game = new GameLogic(); //Create object of GameLogic class
        Scanner s = new Scanner(System.in); //Create Scanner object to receive input

        game.inquireMap(s); //Loading and initializing the map
        game.positioningPandB(); //Randomly place the player(P) and the bot(B)

        /*Reading commands*/
        while (true) {

            /*repeat until entering the correct command　*/
            while (true) {
                System.out.println("Enter a command(HELLO / GOLD / PICKUP / MOVE <direction> / LOOK / QUIT): ");
                String command = s.nextLine().toUpperCase(); // Read input while converting it to uppercase
                System.out.println(); //Add a new line
                
                if (command.equals("HELLO")) { 
                    game.hello(); //Check winning conditions
                    break;
                } else if (command.equals("GOLD")) {
                    game.displayG(); //Display the number of gold the player has
                    break;
                } else if (command.equals("MOVE N")) {
                    game.movePlayer("N"); //Move 1space upward
                    break;
                } else if (command.equals("MOVE E")) {
                    game.movePlayer("E"); //Move 1space to the right
                    break;
                } else if (command.equals("MOVE W")) {
                    game.movePlayer("W"); //Move 1space to the left
                    break;
                } else if (command.equals("MOVE S")) {
                    game.movePlayer("S"); //Move 1space downward
                    break;
                } else if (command.equals("PICKUP")) {
                    game.pickupG(); //Pick up Gold
                    break;
                } else if (command.equals("LOOK")) {
                    game.look(); //Display 5*5 squares around P
                    break;
                } else if (command.equals("QUIT")) {
                    game.quit(); //Quit the game
                    break;
                } else {
                    System.out.println("Error: invalid input."); //In case of invalid input, no turn is consumed
                }
            }
            game.moveBot(); // B moves 1space at the end of the turn
        }
    }
}