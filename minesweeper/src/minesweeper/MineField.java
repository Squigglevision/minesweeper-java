package minesweeper;

import java.util.Random;
import java.util.Scanner;

public class MineField {

	private int size = 10;
	private int rowLength =size;
	private int columnLength = size;
	private int bombCount = size;
	
	private int bombNum = 100;
	private int spaceNum = 50;
	private int defaultNum = 0;
	
	private int[][] visibleBoard = new int[rowLength][columnLength];
	private int[][] hiddenBoard = new int[rowLength][columnLength];
	

	
	// Using colours in console:
	//	public static final String BRIGHT_BLUE_TEXT = "\u001b-etc";
	
	// Have user able to play another game
	// Fix cascade
	
	
	public void startGame()
	{
		System.out.println("---- NEW MINESWEEPER GAME STARTED ----");
		generateBombs();
		setProximityNums();
//		displayGameBoard(visibleBoard);
		// NOTE: make sure to delete line below:
		displayGameBoard(hiddenBoard);
		
		boolean playing = true;
		while (playing)
		{
			displayGameBoard(visibleBoard);
			playing = playMove();
			if(checkWin())
			{
				displayGameBoard(hiddenBoard);
				System.out.println("---- YOU WON THE GAME!! ----");
				break;
			}
			
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
				System.out.println(" column: " +column + " row: " + row );
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
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Column Number: ");
        int column= scanner.nextInt();
        System.out.print("\nEnter Row Number: ");
        int row = scanner.nextInt();
//        scanner.close();

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
            System.out.print("---- BOOM! You LOST the game! ----");
            return false;
        }
        else if(hiddenBoard[row][column] == spaceNum || (hiddenBoard[row][column] > 0 &&  hiddenBoard[row][column] < hiddenBoard.length - 1) )
        {
//            updateVisibleGameBoard(row, column);
        	showAdjacentSpacesAndNums(row, column);
        }
        else
        {
        	visibleBoard[row][column] = hiddenBoard[row][column];
        }

        return true;
    }
	
	public void showAdjacentSpacesAndNums(int row, int column) {
		int selectedSquare = hiddenBoard[row][column];
		System.out.println(selectedSquare);
		int selectedRowLength = 3;
		int selectedColumnLength = 3;
		int rowStartIndex = row;
		int columnStartIndex = column;
		
		
		if(hiddenBoard[row][column] == spaceNum) {
			for(int rowIndex = rowStartIndex; rowIndex < rowLength; rowIndex++) {
				
					if(hiddenBoard[rowIndex][column] != bombNum && rowIndex < rowLength-1 && rowIndex >= 0 && column < columnLength-1 && column >= 0) {
						if(hiddenBoard[rowIndex][column] == spaceNum) {
									visibleBoard[rowIndex][column] = hiddenBoard[rowIndex][column];
									
									for(int smallerColumn = column; smallerColumn > 0; smallerColumn--) {
										if(smallerColumn > 0 && smallerColumn < columnLength && hiddenBoard[rowIndex][smallerColumn+1] == spaceNum) {
											visibleBoard[rowIndex][smallerColumn] = hiddenBoard[rowIndex][smallerColumn];		
										}

									}
									
									for(int higherColumn = column; higherColumn < columnLength; higherColumn++) {
										if(higherColumn > 0 && higherColumn < columnLength && hiddenBoard[rowIndex][higherColumn-1] == spaceNum) {
											visibleBoard[rowIndex][higherColumn] = hiddenBoard[rowIndex][higherColumn];
										}
									}
									

							}
						else {visibleBoard[rowIndex][column] = hiddenBoard[rowIndex][column];
						break;};
						
					}	

			}	
		}
		
		if(hiddenBoard[row][column] == spaceNum) {

			for(int rowIndex = rowStartIndex; rowIndex >= 0; rowIndex--) {


				
					if(hiddenBoard[rowIndex][column] != bombNum && rowIndex >= 0 && column >= 0) {
						if(hiddenBoard[rowIndex][column] == spaceNum) {
									visibleBoard[rowIndex][column] = hiddenBoard[rowIndex][column];
									
									for(int smallerColumn = column; smallerColumn > 0; smallerColumn--) {
										if(smallerColumn > 0 && smallerColumn < columnLength-1 && hiddenBoard[rowIndex][smallerColumn+1] == spaceNum) {
											visibleBoard[rowIndex][smallerColumn] = hiddenBoard[rowIndex][smallerColumn];		
										}

									}
									
									for(int higherColumn = column; higherColumn < columnLength; higherColumn++) {
										if(higherColumn > 0 && higherColumn < columnLength && hiddenBoard[rowIndex][higherColumn-1] == spaceNum) {
											visibleBoard[rowIndex][higherColumn] = hiddenBoard[rowIndex][higherColumn];
										}
									}
									

							}
						else {visibleBoard[rowIndex][column] = hiddenBoard[rowIndex][column];
						break;};
						
					}	

			}	
		}
		
		
		if(hiddenBoard[row][column] == spaceNum) {
			for(int columnIndex = columnStartIndex; columnIndex < columnLength; columnIndex++) {
				
					if(hiddenBoard[row][columnIndex] != bombNum && row < rowLength-1 && row >= 0 && columnIndex < columnLength-1 && columnIndex >= 0) {
						if(hiddenBoard[row][columnIndex] == spaceNum) {
									visibleBoard[row][columnIndex] = hiddenBoard[row][columnIndex];
									
						
									
									for(int smallerRow = row; smallerRow > 0; smallerRow--) {
										if(smallerRow > 0 && smallerRow <rowLength-1 && hiddenBoard[smallerRow+1][columnIndex] == spaceNum) {
											visibleBoard[smallerRow][columnIndex] = hiddenBoard[smallerRow][columnIndex];		
										}

									}
									
									for(int higherRow = row; higherRow < rowLength; higherRow++) {
										if(higherRow < rowLength && higherRow > 0 && hiddenBoard[higherRow-1][columnIndex] == spaceNum) {
											visibleBoard[higherRow][columnIndex] = hiddenBoard[higherRow][columnIndex];
										}
									}
									

							}
						else {visibleBoard[row][columnIndex] = hiddenBoard[row][columnIndex];
						break;
						};
						
					}	

			}	
		}
		
		
		if(hiddenBoard[row][column] == spaceNum) {

			for(int columnIndex = columnStartIndex; columnIndex >= 0; columnIndex--) {

				
					if(hiddenBoard[row][columnIndex] != bombNum && row >= 0 && columnIndex >= 0) {
						if(hiddenBoard[row][columnIndex] == spaceNum) {
									visibleBoard[row][columnIndex] = hiddenBoard[row][columnIndex];

//									
									for(int smallerRow = row; smallerRow > 0; smallerRow--) {
										if(smallerRow > 0 && smallerRow < rowLength-1 && hiddenBoard[smallerRow+1][columnIndex] == spaceNum) {
											visibleBoard[smallerRow][columnIndex] = hiddenBoard[smallerRow][columnIndex];		
										}

									}
									
									for(int higherRow = row; higherRow < rowLength; higherRow++) {
										if(higherRow < rowLength && higherRow > 0 && hiddenBoard[higherRow-1][columnIndex] == spaceNum) {
											visibleBoard[higherRow][columnIndex] = hiddenBoard[higherRow][columnIndex];
										}
									}

							}
						else {visibleBoard[row][columnIndex] = hiddenBoard[row][columnIndex];
						break;};
						
					}	

			}	
		}
		
		
		
		
		
//		if(hiddenBoard[row][column] == spaceNum) {
//			for(int rowIndex = rowStartIndex; rowIndex < rowLength; rowIndex++) {
//				for(int columnIndex = columnStartIndex; columnIndex < columnLength; columnIndex++) {
//					
//					if(hiddenBoard[rowIndex][columnIndex] != bombNum && rowIndex < rowLength-1 && rowIndex >= 0 && columnIndex < columnLength-1 && columnIndex >= 0) {
//						if(hiddenBoard[rowIndex][columnIndex] == spaceNum) {
//									visibleBoard[rowIndex][columnIndex] = hiddenBoard[rowIndex][columnIndex];
//									
//
//							}
//						else visibleBoard[rowIndex][columnIndex] = hiddenBoard[rowIndex][columnIndex];
//						
//					}	
//				}
//			}	
//		}
//		
//		
//		if(hiddenBoard[row][column] == spaceNum) {
//			for(int rowIndex = rowStartIndex; rowIndex > rowLength; rowIndex--) {
//				for(int columnIndex = columnStartIndex; columnIndex > columnLength; columnIndex--) {
//					
//					if(hiddenBoard[rowIndex][columnIndex] != bombNum && rowIndex < rowLength-1 && rowIndex >= 0 && columnIndex < columnLength-1 && columnIndex >= 0) {
//						if(hiddenBoard[rowIndex][columnIndex] == spaceNum) {
//									visibleBoard[rowIndex][columnIndex] = hiddenBoard[rowIndex][columnIndex];
//									
//
//							}
//						else visibleBoard[rowIndex][columnIndex] = hiddenBoard[rowIndex][columnIndex];
//						
//					}	
//				}
//			}	
//		}
		
		
		else visibleBoard[row][column] = hiddenBoard[row][column];

	}
	
	
//	public void showAdjacentSpacesAndNums(int row, int column) {
//		int selectedSquare = hiddenBoard[row][column];
//		System.out.println(selectedSquare);
//		int selectedRowLength = 3;
//		int selectedColumnLength = 3;
//		int rowStartIndex = row-1;
//		int columnStartIndex = column-1;
//		
//		if(row == 0 || row == rowLength-1) {
//			selectedRowLength = 2;
//			if (row==0) {
//				rowStartIndex = 0;
//			}
//		}
//		
//		if(column == 0 || column == columnLength-1) {
//			selectedColumnLength = 2;
//			if(column == 0) {
//				columnStartIndex = 0;
//			}
//		}
//		
//		if(hiddenBoard[row][column] == spaceNum) {
//			for(int rowIndex = rowStartIndex; rowIndex < selectedRowLength + rowStartIndex; rowIndex++) {
//				for(int columnIndex = columnStartIndex; columnIndex < selectedColumnLength + columnStartIndex; columnIndex++) {
//					
//					if(hiddenBoard[rowIndex][columnIndex] != bombNum) {
//						if(hiddenBoard[rowIndex][columnIndex] == spaceNum) {
//									visibleBoard[rowIndex][columnIndex] = hiddenBoard[rowIndex][columnIndex];
//									
//
//							}
//						else visibleBoard[rowIndex][columnIndex] = hiddenBoard[rowIndex][columnIndex];
//						
//					}	
//				}
//			}	
//		}
//		else visibleBoard[row][column] = hiddenBoard[row][column];
//
//	}
    
    public boolean checkWin()
    {
        for(int row = 0; row < 10; row++)
        {
            for(int column = 0; column < 10; column++)
            {
                if(visibleBoard[row][column]==0)
                {
                    if(hiddenBoard[row][column] != bombNum)
                    {
                        return false;
                    }
                }
            }
        }
        return true;
    }
	
	

    
    
    
    
}
