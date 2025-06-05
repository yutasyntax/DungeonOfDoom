# Gold Escape
Gold Escape is a simple text-based dungeon escape game written in Java 21.
The player (P) must collect a required amount of gold (G) and reach an exit (E) tile to win, while avoiding a bot (B) that moves randomly around the map.


## File Structure
- Main.java: Program entry point
- GameManager.java: Controls the game flow and input
- GameLogic.java: Core game logic (map, movement, win/loss)
- Player.java: Player movement and gold pickup
- Bot.java: Random bot movement
- Renderer.java: Handles map display
- CommandProcessor.java: Parses and dispatches commands
- dungeon.txt: Sample map file
- README.md: Project overview and instructions


## How to Run
- Compile the program: javac *.java
- Run the game: java Main


## Commands(case-insensitive)
- HELLO: Displays the amount of gold needed to win
- GOLD: Displays the number of gold the player currently owns
- PICKUP: If there is gold at the player's position, pick it up
- MOVE N/S/E/W: Move one tile in specified direction
- LOOK: Displays a 5x5 map centered on the player
- QUIT: Exits the game. If the victory conditions are met, the player wins; if not, the player loses


## Object-Oriented Design
- Encapsulation: Game data is accessed only through public methods
- List-based position tracking: Gold and exit positions are stored in lists
- Modular movement: Player and bot use similar movement logic


## Notes
1. The game ends if:
 - The player is caught by the bot (LOSE).
 - The player reaches the exit with enough gold (WIN).

2. Input is read from the terminal during gameplay.



