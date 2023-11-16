package minesweeper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class MineField {

	private int rowLength = 10;
	private int columnLength = 10;
	private int bombCount = 10;
	
	private int bombNum = 100;
	private int spaceNum = 50;
	private int defaultNum = 0;
	
	private int[][] visibleBoard;
	private int[][] hiddenBoard;
	Scanner scanner = new Scanner(System.in);

	

	
	public void startGame()
	{
		System.out.println("---- NEW MINESWEEPER GAME STARTED ----  \n");
		visibleBoard = new int[rowLength][columnLength];
		hiddenBoard = new int[rowLength][columnLength];
		generateBombs();
		setProximityNums();

		
		boolean playing = true;
		while (playing)
		{
			displayGameBoard(visibleBoard);
			playing = playMove();	
		}
		
        System.out.print("\n Would you like to play again? \n (y/n) \n ");
        String playAgain= scanner.next();
        if(playAgain.equals("y")) {
        	System.out.println("... Starting new game ... \n");
        	startGame();
        } else {
        	scanner.close();
        	System.out.println("*** Thanks for playing! ***");
        }      	
	}
	
	public void generateBombs()
	{
		int fields = 0;
		while(fields != bombCount)
		{
			Random randomBomb = new Random();
			// Generate bomb coordinates
			int row = randomBomb.nextInt(rowLength);
			int column = randomBomb.nextInt(columnLength);
			// Add bomb if not already at coordinates
			if(!(hiddenBoard[row][column] == bombNum)) {
				hiddenBoard[row][column] = bombNum;
				fields++;
			}	
		}
	}
	
	public void setProximityNums()
    {
        for(int row=0; row<rowLength; row++)
        {
            for(int column=0; column<columnLength; column++)
            {
                int count=0;
                
                if(hiddenBoard[row][column] != bombNum)
                {
                    if(row != 0)
                    {
                        if(hiddenBoard[row-1][column] == bombNum) count++;
                        if(column != 0)
                        {
                            if(hiddenBoard[row-1][column-1] == bombNum) count++;
                        }
                    }
                    if(row != rowLength - 1)
                    {
                        if(hiddenBoard[row+1][column] == bombNum) count++;
                        if(column != columnLength - 1)
                        {
                            if(hiddenBoard[row+1][column+1] == bombNum) count++;
                        }
                    }
                    if(column!= 0)
                    {
                        if(hiddenBoard[row][column-1] == bombNum) count++;
                        if(row != rowLength - 1)
                        {
                            if(hiddenBoard[row+1][column-1] == bombNum) count++;
                        }
                    }
                    if(column != columnLength - 1)
                    {
                        if(hiddenBoard[row][column+1] == bombNum) count++;
                        if(row != 0)
                        {
                            if(hiddenBoard[row-1][column+1] == bombNum) count++;
                        }
                    }           
                    if(count == 0 ) {
                    	count = spaceNum;
                    }                  
                    hiddenBoard[row][column] = count;
                }
            }
        }
    }
	
	public void displayGameBoard (int[][] gameBoard)
    {
		// Print out column indices
        System.out.print("\t ");
        for(int column=0; column < columnLength; column++)
        {
            System.out.print(" " + column + "  ");
        }
        // Print out game board:
        System.out.print("\n");
        for(int row=0; row<rowLength; row++)
        {
            System.out.print(row + "\t| ");
            for(int column = 0; column < columnLength; column++)
            {
                if(gameBoard[row][column] == defaultNum)
                {
                    System.out.print("-");
                }
                else if(gameBoard[row][column] == bombNum)
                {
                    System.out.print("X");
                }      
                else if(gameBoard[row][column]==spaceNum)
                {
                    System.out.print(" ");
                }
                else
                {
                    System.out.print(gameBoard[row][column]);
                }
                System.out.print(" | ");
            }
            System.out.print("\n");
        }
    }
	
	
	
	public boolean playMove()
    {
        System.out.print("Enter Column Number: ");
        int column= scanner.nextInt();
        System.out.print("\nEnter Row Number: ");
        int row = scanner.nextInt();

        // Validate input:
        if(row < 0 || row > rowLength-1 || column < 0 || column > columnLength-1 || visibleBoard[row][column] != defaultNum)
        {
            System.out.print("\nPlease type a correct input\n");
            return true;
        }

        // Bomb clicked, game over:
        if(hiddenBoard[row][column] == bombNum)
        {
        	displayGameBoard(hiddenBoard);
            System.out.print("\n---- BOOM! You LOST the game! ---- \n");
            return false;
        }
        else if(hiddenBoard[row][column] == spaceNum)

        {

        	showAdjacentSpacesAndNums(row, column);
        }
        else
        {
        	visibleBoard[row][column] = hiddenBoard[row][column];
        }

        return checkWin();
    }
	
	
	public void showAdjacentSpacesAndNums(int row, int column) {
		int selectedRowLength = 3;
		int selectedColumnLength = 3;
		int rowStartIndex = row-1;
		int columnStartIndex = column-1;
		
		ArrayList<Coordinate> coordinates = new ArrayList<>(Arrays.asList());
		
		if(row == 0 || row == rowLength-1) {
			selectedRowLength = 2;
			if (row==0) {
				rowStartIndex = 0;
			}
		}
		
		if(column == 0 || column == columnLength-1) {
			selectedColumnLength = 2;
			if(column == 0) {
				columnStartIndex = 0;
			}
		}
		
		for(int rowIndex = rowStartIndex; rowIndex < selectedRowLength + rowStartIndex; rowIndex++) {
			for(int columnIndex = columnStartIndex; columnIndex < selectedColumnLength + columnStartIndex; columnIndex++) {
				if(hiddenBoard[rowIndex][columnIndex] == spaceNum && visibleBoard[rowIndex][columnIndex] == defaultNum) {
					coordinates.add(new Coordinate(rowIndex, columnIndex));
				}
				visibleBoard[rowIndex][columnIndex] = hiddenBoard[rowIndex][columnIndex];
				
			}
		}
		for(int index = 0; index < coordinates.size(); index++) {
			Coordinate coordinate = coordinates.get(index);
			showAdjacentSpacesAndNums(coordinate.row, coordinate.column);
		}
		
	}
    
    public boolean checkWin()
    {
        for(int row = 0; row < rowLength; row++)
        {
        	// Return (playing) true if there are any uncovered tiles:
        	
            for(int column = 0; column < columnLength; column++)
            {
                if(visibleBoard[row][column] == defaultNum)
                {
                    if(hiddenBoard[row][column] != bombNum)
                    {
                        return true;
                    }
                }
            }
        }
        
        displayGameBoard(hiddenBoard);
		System.out.println("\n ---- YOU WON THE GAME!! ---- \n");
        return false;
    }
	
	

    
    
    
    
}
