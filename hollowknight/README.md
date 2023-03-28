HollowKnight Class Reference
========================

--------------
class Engine
--------------
	Overview: 
                Main engine of our game. In charge of things such as random map generation, keyboard input, title screen viewing, GUI, etc. 

    	
	Calling: 
                Engine engine = new Engine();


	Arguments: 
                None.


        Functions:
                interactWithKeyboard() - Creates an instance of our game, with keyboard input being the method of moving around. This is contrasted with the next function, which takes in an array of inputs to play the game. Initializes the game, creates our random world, places special gameplay tiles, and runs a loop which lets our character move.

                interactWithInputString(String input) - Like interactWithKeyboard(), except takes in a string of inputs that plays the game for us. Does not allow for keyboard input. Mainly used for debugging.

                initializeWorld() - Initializes world by filling in Height X Width with blank tiles.

                initializeVoidWorld() - Initializes void world.

                drawRoom(int width, int height, int x, int y, TETile[][] worlds) - Generates room of size N with a width of W and length of L. Bottom left tile is (X, Y). Includes size of walls (+2 in each direction). Returns true if room is drawn, otherwise returns false.

                drawHallwayWidthOne(int length, int x, int y, String s, TETile[][] worlds) - Draws hallway of length L. Returns true if room is drawn, otherwise returns false.

                drawHallwayWidthTwo(int length, int x, int y, String s, TETile[][] worlds) - Draws hallway of length L with width 2. Returns true if room is drawn, otherwise returns false.

                drawHallwayVariableWidth(int length, int x, int y, String s, int variable, TETile[][] worlds) - Draws hallway based on input value.

                drawRandomTileMap(Random random) - Draws a random tile map based on specified behavior for the game. Uses a randomized algorithm and previous methods to create various hallways and rooms to create fun gameplay.

                projectGame(int width, int height) - Main game function.

                solicitNCharsInput() - New screen loop.

                drawFrameSeed(String s) - Draws frame with text in middle of screen and a seed.

                drawFrame(String s) - Draws frame with text in middle of screen.

                drawFrameNoClear(String s, int x, int y, Color color) - Draws frame with no clear.

                drawTitleScreen() - Draws the title screen.

                showHelp() - Shows help menu.

                showGUI(String tileName) - Shows GUI.

                mainGameLoop() - Main game loop.

                placeCharacter(TETile[][] worlds) - Puts moving tile in top right corner.

                placeSpecialTile(TETile[][] worlds, Random r) - Places coin tile.

                placeManySpecialTiles(TETile[][] worlds, Random r) - Places many coin tiles.

                darkMap(int range) - Makes entire map dark except for x tiles around player.

                join(String first, String... others) - Joins two file paths together.

                writeObjectToFile(File save, Object o) - Writes object to file.

                readObject(File save) - Reads object.

                saveGame() - Helper function to save the game.

                loadGame() - Helper function to load the game.

                getTileName() - Helper function to return tile name under mouse cursor. Automatically gets mouseX and mouseY. Then finds tile.

                moveFunction(char c, TETile[][] worlds) - Helper function for moving.


--------------
class Main
--------------
	Overview: 
                This is the main entry point for the program. This class simply parses the command line inputs, and lets the byow.Core.Engine class take over in either keyboard or input string mode.


--------------
class RandomUtils
--------------
	Overview: 
                A library of static methods to generate pseudo-random numbers from different distributions (bernoulli, uniform, gaussian, discrete, and exponential). Also includes methods for shuffling an array and other randomness related stuff you might want to do. Feel free to modify this file.


        Functions: 
                Too many to list. Some examples are:
                        gaussian(Random random) - Returns a random real number from a gaussian distribution given a random object.

                        poisson(Random random, double lambda) - Returns a random integer from a Poisson distribution with mean "lambda".


--------------
class Room
--------------
	Overview: 
                Represents a room with width, height, x, and y coordinates. Uses random object to determine size.

    	
	Calling: 
                Room r = new Room(random, x, y);


	Arguments: 
                random - Random object used for random number generation.
                x - X Coordinate of room.
                y - Y Coordinate of room.


        Functions:
                getWidth() - Gets width of room.
                getHeight() - Gets height of room.
                getX() - Gets X value of room.
                getY() - Gets Y value of room.

