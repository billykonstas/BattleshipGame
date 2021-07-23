//package battleship;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors

class Ship
{
    String name;
    int cells;
    int [] coords = new int[4];
    String start;
    String end;
    boolean sunk=false;
    int hits=0;

    public Ship (String name,int cells)
    {
        this.name=name;
        this.cells=cells;
        this.start="";
        this.end="";
    }

    public Ship (String name,int cells, String start, String end)
    {
        this.name=name;
        this.cells=cells;
        this.start=start;
        this.end=end;
    }

    public void setCoords(int [] coords)
    {
        this.coords=coords;
    }

    public String getName()
    {
        return this.name;
    }

    public int getCells()
    {
        return this.cells;
    }

    public String getStart()
    {
        return  this.start;
    }

    public String getEnd()
    {
        return this.end;
    }

    public boolean isSunk()
    {
        return sunk;
    }

    public void sink()
    {
        this.sunk=true;
    }

    public void isHit(int x, int y)
    {
        if ((x>=this.coords[0]&&x<=this.coords[2])&&(y>=this.coords[1]&&y<=this.coords[3]))
        {
            this.hits++;
            if (this.hits==this.cells)
            {
                System.out.println("You sank a ship!");
                sink();
            }
            else
                System.out.println("You hit a ship!");
        }


    }
}

class Player
{
    int id;
    Ship[] fleet;
    String [][] gameField; //the game board with the positions of the ships
    String [][] hiddenGameField; //the game board with the fog

    public Player(int id)
    {
        /*
        Ship aircraftCarrier = new Ship("Aircraft Carrier",5,"F3","F7");
        Ship battleship = new Ship("Battleship",4,"A1","D1");
        Ship submarine = new Ship("Submarine",3,"J10","J8");
        Ship cruiser = new Ship("Cruiser",3,"B9","D9");
        Ship destroyer = new Ship("Destroyer",2,"I2","J2");
         */

        Ship aircraftCarrier = new Ship("Aircraft Carrier",5);
        Ship battleship = new Ship("Battleship",4);
        Ship submarine = new Ship("Submarine",3);
        Ship cruiser = new Ship("Cruiser",3);
        Ship destroyer = new Ship("Destroyer",2);

        this.id=id;
        this.fleet = new Ship[]{aircraftCarrier,battleship,submarine, cruiser, destroyer}; //the fleet of ships
        this.gameField = new String[11][11];
        this.hiddenGameField = new String[11][11];
        createGameField(this.gameField);
        createGameField(this.hiddenGameField);
    }

    public int getId()
    {
        return this.id;
    }

    public Ship[] getFleet()
    {
        return this.fleet;
    }

    public String[][] getGameField()
    {
        return this.gameField;
    }

    public String[][] getHiddenGameField()
    {
        return this.hiddenGameField;
    }

    public void createGameField(String[][] gameField)
    {
        char letters=64;

        for (int i=0; i < gameField.length; i++)
        {
            for (int j=0; j < gameField.length; j++)
            {
                if (i==0)
                {
                    if(j==0)
                        gameField[i][j]=" ";
                    else
                    {
                        gameField[i][j]= String.valueOf(j);
                    }
                }
                else
                if (j==0)
                {
                    gameField[i][j]= String.valueOf((char) (letters+i));
                }
                else
                    gameField[i][j]="~";
            }
        }
    }

    //Method responsible for filling the gameField, the parameters are:
    public void fillGameField()
    {
        System.out.println("\nPlayer "+getId()+", place your ships on the game field:");
        printGameField(gameField);
        Scanner scanner = new Scanner(System.in);
        int[] coords = new int[4]; //0:beginRow,1:beginColumn,2:endRow,3:endColumn
        String beginCoord = "";
        String endCoord = "";
        boolean correctCoords=false;

        for (Ship ship : getFleet()) {
            System.out.println("\nEnter the coordinates of the " + ship.getName() + " (" + ship.getCells() + " cells):");

            while (!correctCoords) //Accepts to be initialized by user or in code
            {

                if (ship.getStart().equals("") && ship.getEnd().equals("")) {
                    beginCoord = scanner.next();
                    endCoord = scanner.next();
                } else {
                    beginCoord = ship.getStart();
                    endCoord = ship.getEnd();
                }

                //System.out.println("Begin: " + beginCoord + " end: " + endCoord); //For checking reasons

                coords = coordsCreator(beginCoord, endCoord);

                if (coords[0] == coords[2]) //horizontal
                {
                    if (Math.abs(coords[3] - coords[1]) + 1 == ship.getCells()) {

                        if (checkNeighbours(getGameField(), coords, "h")) {
                            for (int i = coords[1]; i <= coords[3]; i++) {
                                this.gameField[coords[0]][i] = "O";
                            }
                            correctCoords = true;
                        } else {
                            System.out.println("\nError! You placed it too close to another one. Try again:");
                        }
                    } else {
                        System.out.println("\nError! The given coordinates don't correspond to the size of the " + ship.getName() + "!");
                        System.out.println("Try again:");
                    }

                } else if (coords[1] == coords[3]) //vertical
                {
                    if (Math.abs(coords[2] - coords[0]) + 1 == ship.getCells()) {
                        if (checkNeighbours(getGameField(), coords, "v")) {
                            for (int i = coords[0]; i <= coords[2]; i++) {
                                this.gameField[i][coords[1]] = "O";
                            }
                            correctCoords = true;
                        } else {
                            System.out.println("\nError! You placed it too close to another one. Try again:");
                        }

                    } else {
                        System.out.println("\nError! The given coordinates don't correspond to the size of the " + ship.getName() + "!");
                        System.out.println("Try again:");
                    }
                } else {
                    System.out.println("\nError! Wrong " + ship.getName() + " location!");
                    System.out.println("Try again:");
                }
            }
            ship.setCoords(coords);
            printGameField(getGameField());
            correctCoords = false;
        }
    }

    public void fillGameFieldFromFile()
    {
        int[] coords = new int[4]; //0:beginRow,1:beginColumn,2:endRow,3:endColumn
        String beginCoord = "";
        String endCoord = "";
        try {
            File folder = new File("D:/Documents/Github Projects/BattleshipGame/src/");
            FilenameFilter filter = new FilenameFilter() {
                @Override
                public boolean accept(File f, String name) {
                    return name.endsWith(".txt");
                }
            };
            System.out.println("Available files:");
            String[] pathnames = folder.list(filter);
            for (String pathname : pathnames) {
                System.out.println(pathname);
            }
            System.out.println("Write the name of the file you want to import the coordinates:");
            Scanner scanner = new Scanner(System.in);
            String filename = scanner.next();
            System.out.println("\nReading coordinates from file "+filename+" ...\n");
            File myCoords = new File("D:/Documents/Github Projects/BattleshipGame/src/"+filename);
            Scanner reader = new Scanner(myCoords);
            for (Ship ship : getFleet())
            {
                beginCoord=reader.next();
                endCoord= reader.next();

                coords = coordsCreator(beginCoord, endCoord);


                if (coords[0] == coords[2]) //horizontal
                {
                    if (Math.abs(coords[3] - coords[1]) + 1 == ship.getCells()) {

                        if (checkNeighbours(getGameField(), coords, "h")) {
                            for (int i = coords[1]; i <= coords[3]; i++) {
                                this.gameField[coords[0]][i] = "O";
                            }
                        } else {
                            System.out.println("\nError! You placed it too close to another one.");
                        }
                    } else {
                        System.out.println("\nError! The given coordinates don't correspond to the size of the " + ship.getName() + "!");
                    }

                } else if (coords[1] == coords[3]) //vertical
                {
                    if (Math.abs(coords[2] - coords[0]) + 1 == ship.getCells()) {
                        if (checkNeighbours(getGameField(), coords, "v")) {
                            for (int i = coords[0]; i <= coords[2]; i++) {
                                this.gameField[i][coords[1]] = "O";
                            }
                        } else {
                            System.out.println("\nError! You placed it too close to another one.");
                        }

                    } else {
                        System.out.println("\nError! The given coordinates don't correspond to the size of the " + ship.getName() + "!");
                    }
                } else {
                    System.out.println("\nError! Wrong " + ship.getName() + " location!");
                }
                ship.setCoords(coords);
            }
            printGameField(getGameField());
        } catch (FileNotFoundException e) {
            System.out.println("Error! Couldn't find the file. Try again.");
            fillGameFieldFromFile();
        }
        catch (NoSuchElementException e){
            System.out.println("Error! File is unreadable! Please enter the coordinates manually.");
            fillGameField();
        }
    }

    public void printGameField(String[][] gameField)
    {
        System.out.println("");
        for (String[] strings : gameField) {
            for (int j = 0; j < gameField.length; j++) {
                System.out.print(strings[j] + " ");
            }
            System.out.println();
        }
        //System.out.println();
    }

    //Method for taking the input string and make them int coords
    public static int[] coordsCreator(String beginCoord,String endCoord)
    {
        int[] coords = new int[4]; //0:beginRow,1:beginColumn,2:endRow,3:endColumn
        String[] temp;
        int coordTemp;

        //2 things happen hear: first we make the input uppercase, then we take the input e.g F3 and
        //insert to temp = [F,3]
        temp = beginCoord.toUpperCase().split("(?<=\\D)(?=\\d)");
        coords[0] = temp[0].charAt(0)-64; //we transform the letter to an int coord in our gameField
        coords[1]= Integer.parseInt(temp[1]);

        temp = endCoord.toUpperCase().split("(?<=\\D)(?=\\d)");
        coords[2] = temp[0].charAt(0)-64;
        coords[3]= Integer.parseInt(temp[1]);

        if (coords[0]>coords[2]) //only if the user gives coords from the end of the row to the front
        {
            coordTemp=coords[0];
            coords[0]=coords[2];
            coords[2]=coordTemp;
        }
        if(coords[1]>coords[3]) //only if the user gives coords from the bottom of the column to the top
        {
            coordTemp=coords[1];
            coords[1]=coords[3];
            coords[3]=coordTemp;
        }
        return coords;
    }

    public static boolean checkNeighbours(String[][] gameField,int[] coords,String orientation)
    {

        switch (orientation){
            case "h"://coords[0]==coords[2], coords[1], coords[3] -> change
                if(coords[1]!=1)
                {
                    if(gameField[coords[0]][coords[1] - 1].equals("O")) //left neighbour
                        return false;
                }
                if(coords[3]!=1)
                {
                    if(gameField[coords[0]][coords[1] + 1].equals("O"))//right neighbour
                        return false;
                }
                if(coords[0]==1) //if we are at the top row
                {
                    for(int i=coords[1]; i<=coords[3]; i++)
                    {
                        if(gameField[coords[0]][i + 1].equals("O"))
                        {
                            return false;
                        }
                    }
                }
                else if(coords[0]==10) //if we are at the last row
                {
                    for(int i=coords[1]; i<=coords[3]; i++)
                    {
                        if(gameField[coords[0]][i - 1].equals("O"))
                        {
                            return false;
                        }
                    }
                }

                if (coords[0]!=1&&coords[0]!=10) //if we are anywhere in the gameField
                {
                    for(int i=coords[1]; i<=coords[3]; i++)
                    {
                        if(gameField[coords[0]+1][i].equals("O") || gameField[coords[0]-1][i].equals("O"))
                        {
                            return false;
                        }
                    }
                }
                break;

            case "v": //coords[1]==coords[3], coords[0], coords[2] -> change
                if(coords[0]!=1)
                {
                    if(gameField[coords[0]-1][coords[1]].equals("O")) //top neighbour
                        return false;
                }
                if(coords[2]!=10)
                {
                    if(gameField[coords[2]+1][coords[3]].equals("O"))//bottom neighbour
                        return false;
                }
                if(coords[1]==1) //if we are at the first column
                {
                    for(int i=coords[0]; i<=coords[2]; i++)
                    {
                        if(gameField[i+1][coords[1]].equals("O"))
                        {
                            return false;
                        }
                    }
                }
                else if(coords[0]==10) //if we are at the last column
                {
                    for(int i=coords[0]; i<=coords[2]; i++)
                    {
                        if(gameField[i-1][coords[0]].equals("O"))
                        {
                            return false;
                        }
                    }
                }

                if (coords[1]!=1&&coords[1]!=10) //if we are anywhere in the gameField
                {
                    for(int i=coords[0]; i<=coords[2]; i++)
                    {
                        if(gameField[i][coords[1]+1].equals("O") || gameField[i][coords[1]-1].equals("O"))
                        {
                            return false;
                        }
                    }
                }
                break;

        }
        return true;
    }

    public void takeShot(Player enemy)
    {

        int[] coords = new int[2]; //0:beginRow,1:beginColumn,2:endRow,3:endColumn
        String[] temp;
        boolean shotCoordsCorrect;
        String[][] gameField=getGameField();
        String[][] hiddenGameField=getHiddenGameField();
        Ship[] fleet=getFleet();

        System.out.println("Player "+getId()+", fire!");
        do
        {
            shotCoordsCorrect=true;
            Scanner scanner = new Scanner(System.in);
            String shotCoords=scanner.next();

            //2 things happen hear: first we make the input uppercase, then we take the input e.g F3 and
            //insert to temp = [F,3]
            temp = shotCoords.toUpperCase().split("(?<=\\D)(?=\\d)");
            coords[0] = temp[0].charAt(0)-64; //we transform the letter to an int coord in our gameField
            coords[1]= Integer.parseInt(temp[1]);

            if (coords[0]>10||coords[0]<1||coords[1]<1||coords[1]>10)
            {
                System.out.println("Captain! That position doesn't exist! Try again:");
                shotCoordsCorrect=false;
            }


        }while (!shotCoordsCorrect);

        if(enemy.gameField[coords[0]][coords[1]].equals("O"))
        {
            enemy.gameField[coords[0]][coords[1]]="X";
            this.hiddenGameField[coords[0]][coords[1]]="X";
            //printGameField(hiddenGameField);
            for (Ship ship : enemy.fleet) {
                ship.isHit(coords[0],coords[1]);
            }

        }
        else if(enemy.gameField[coords[0]][coords[1]].equals("X"))
        {
            System.out.println("You already hit that!");
            //printGameField(hiddenGameField);
        }
        else
        {
            enemy.gameField[coords[0]][coords[1]]="M";
            this.hiddenGameField[coords[0]][coords[1]]="M";
            //printGameField(hiddenGameField);
            System.out.println("You missed!");
        }

    }
}

public class Main {


    public static boolean isFinished(Ship[] fleet)
    {
        int sunkShips=0;
        for (Ship ship : fleet)
        {
            if(ship.isSunk())
                sunkShips++;
        }

        return sunkShips == fleet.length;
    }

    public static void changePlayer()
    {
        System.out.println("\nPress Enter and pass the move to another player");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void chooseInput(Player player)
    {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nPlayer "+player.getId()+" welcome to Battleship!\n\nChoose how you want place you ships:");
        System.out.println("1 - Manually\n2 - Automatically\n3 - From .txt file");
        System.out.print(">");
        int input = scanner.nextInt();
        while (input!=1&&input!=2&&input!=3)
        {
            System.out.println("\nPlease select one from the available options.");
            System.out.print(">");
            input= scanner.nextInt();
        }
        switch (input)
        {
            case 1:
                player.fillGameField();
                break;
            case 2:
                System.out.println("\nUnder construction. Please do it manually.\n");
                player.fillGameField();
                break;
            case 3:
                player.fillGameFieldFromFile();
                break;
        }

    }

    public static void logo()
    {
        System.out.println("------------------------------------------------------");
        System.out.println("|----------------------------------------------------|");
        System.out.println("||     ____        __  __  __          __    _      ||");
        System.out.println("||    / __ )____ _/ /_/ /_/ /__  _____/ /_  (_)___  ||");
        System.out.println("||   / __  / __ `/ __/ __/ / _ \\/ ___/ __ \\/ / __ \\ ||");
        System.out.println("||  / /_/ / /_/ / /_/ /_/ /  __(__  ) / / / / /_/ / ||");
        System.out.println("|| /_____/\\__,_/\\__/\\__/_/\\___/____/_/ /_/_/ .___/  ||");
        System.out.println("||                                        /_/       ||");
        System.out.println("|----------------------------------------------------|");
        System.out.println("------------------------------------------------------");
        System.out.println("=========== Created by Billy Konstantaras ============\n");
    }

    public static void playerScreen(Player player)
    {
        System.out.println("Enemy's fleet:");
        player.printGameField(player.hiddenGameField);
        System.out.println("\n---------------------");
        System.out.println("\nYour fleet:");
        player.printGameField(player.gameField);
    }

    public static void game()
    {
        boolean gameFinished=false;
        int winner=0;
        Player player1 = new Player(1);
        chooseInput(player1);
        changePlayer();

        Player player2 = new Player(2);
        chooseInput(player2);


        while(!gameFinished)
        {
            if (!isFinished(player1.fleet))
            {
                changePlayer();
                playerScreen(player1);
                player1.takeShot(player2);

                if (!isFinished(player2.fleet))
                {
                    changePlayer();
                    playerScreen(player2);
                    player2.takeShot(player1);
                }
                else
                {
                    gameFinished=true;
                    winner=2;
                }
            }
            else
            {
                gameFinished=true;
                winner=1;
            }
        }

        System.out.println("\nCongratulations Player "+winner+" !You sank the last ship. Victory ! ");
    }

    public static void main(String[] args) {

        logo();
        game();

    }
}
