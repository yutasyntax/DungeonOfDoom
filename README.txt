# Dungeon of Doom
Dungeon of Doom is a game running on Java 21. The player (P) aims to collect a specified number of gold (G) or more and reach the exit (E) tile to escape.

## File Structure
- Main.java: Entry point of the program
- GameLogic.java: Manages the overall game logic
- Player.java: Handles player actions and movements.

## Installation
- Compile the program:
   ```bash
   javac DungeonOfDoom/*.java
   ```

- Run the game:
   ```bash
   java -cp DungeonOfDoom Main
   ```

## Commands
Players can enter the following commands (case doesn't matter):
- HELLO: Displays the amount of gold needed to win.
- GOLD: Displays the number of gold the player currently owns.
- PICKUP: If there is gold at the player's position, pick it up.
- MOVE <direction>: Moves one space in the entered direction (N, S, E, W).
- LOOK: Displays a 5x5 map centered on the player.
- QUIT: Exits the game. If the victory conditions are met, the player wins; if not, the player loses.



## Object oriented design
The program is based on object-oriented design practices, including:

- Encapsulation: The player, the location of gold on the map, the exit location, the number of gold held by the player, etc. can only be accessed through getters. This prevents the data from being manipulated illegally from outside.
- Position management by list: Gold and exit positions are also managed by list. This makes it easier to detect collisions between players and them. It also makes future expansion (increasing the number of exit, bot stealing G as a looter, etc.) easier.
- Unified movement logic: The player and the bot share the same logic for position management and movement. This design allows for better code reuse.
