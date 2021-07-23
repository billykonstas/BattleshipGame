# Battleship Game
 
![alt_text](https://github.com/billykonstas/BattleshipGame/blob/main/images/menu.png)

# Description
Battleship is a game made with Java. It is te typical battleship game, where two players place a fleet of ships on a 10x10 matrix, and the take guesses each turn to find and sink the opponent's ships. 



# Boards

![alt_text](https://github.com/billykonstas/BattleshipGame/blob/main/images/game_display.png)

There are two boards visible at each turn, the opponent's and the player's.
When the player make a correct guess, it show the X mark on the board. When it's a miss it shows M. With 0 are the locations of the ships. 

# Fleet

There are five ships available to be placed on the board:

* Aircraft Carrier (5 cells)
* Battleship (4 cells)
* Submarine (3 cells)
* Cruiser (3 cells)
* Destroyer (2 cells)

# Fleet setup

There are three ways to place the ships on the board:

* Manually
* Automatically
* From a file

## Manually
The player enters the coordinates of the board, where wants to place the ship.

## Automatically
The program places the ships automatically. **This feature is not currently working**

## From a file
The user can create a txt file with the coordinates and place it on the **SRC** folder. Then they can choose the spacific file to import the coordinates into the game. The structure of the .txt file must be as following.
``` TXT
F3 F7
A1 D1
J10 J8
B9 D9
I2 J2
```
# Turns
Each turn the player specifies a coordinate to hit. The game informs them if they hit or missed and their turn is ended, then it's the other player's turn. If the player hits all cells of the ship, the ship is sinked.

# Win
The first player to sink all the ships of the oppenent, wins the game.
