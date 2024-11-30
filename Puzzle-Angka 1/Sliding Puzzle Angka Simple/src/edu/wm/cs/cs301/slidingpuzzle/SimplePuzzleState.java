package edu.wm.cs.cs301.slidingpuzzle;

import java.util.Arrays;
import java.util.Random;

public class SimplePuzzleState implements PuzzleState {

	private int[][] board;
	private int pathLength;
	private SimplePuzzleState parent;
	private Operation operation;
	
	
	public SimplePuzzleState(){
		super();
		this.board = null;
		this.pathLength = 0 ;
		this.parent = null;
		this.operation = null;
	}
	
	
	
	
	
	@Override
	public void setToInitialState(int dimension, int numberOfEmptySlots) {
		board = new int[dimension][dimension]; 
		int n = 1;
		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++){
				board[i][j] = n;
				n++;
				if (i == dimension - 1 && j > dimension - numberOfEmptySlots - 1){ 
					board[i][j]= 0;
				}
			}
		}
	}
	

	@Override
	public int getValue(int row, int column) {
		if (row >= 0 && column >= 0 && row < board.length && column < board.length){
			return board[row][column];
		}
		return -1;
	}

	@Override
	public PuzzleState getParent() {
		return parent;
	}

	@Override
	public Operation getOperation() {
		return operation;
	}

	@Override
	public int getPathLength() {
		return pathLength;
	}

	@Override
	public PuzzleState move(int row, int column, Operation op) {
		
		if (getValue(row, column) == 0){
			return null;
		}
		
		SimplePuzzleState newState = new SimplePuzzleState();
		newState.board = new int[board.length][board.length];
		for (int i = 0; i < board.length; i++){
			for (int j = 0; j < board.length; j++){
				newState.board[i][j] = this.getValue(i, j);
			}
		}
		
		switch (op) {
		
			case MOVERIGHT:
				if (column + 1 < board.length){
					if (isEmpty(row, column + 1)){
						int temp= newState.board[row][column];
						newState.parent = this;
						newState.board[row][column] = 0;
						newState.board[row][column + 1] = temp;
						newState.pathLength = this.pathLength + 1;
						newState.operation = op;
					}
					else {
						return null;
					}
					
				}
				else {
					return null;
				}
				break;
			case MOVELEFT:
				if (column - 1 >= 0){
					if (isEmpty(row, column - 1)){
						int temp = newState.board[row][column];
						newState.parent = this;
						newState.board[row][column] = 0;
						newState.board[row][column - 1] = temp;
						newState.pathLength = this.pathLength + 1;
						newState.operation = op;
					}
					else {
						return null;
					}
				}
				else {
					return null;
				}
				break;
			case MOVEUP:
				if (row - 1 >= 0) {
					if (isEmpty(row - 1, column)) {
						int temp = newState.board[row][column];
						newState.parent = this;
						newState.board[row][column] = 0;
						newState.board[row - 1][column] = temp;
						newState.pathLength = this.pathLength + 1;
						newState.operation = op;
					}
					else {
						return null;
					}
				}
				else {
					return null;
				}
				break;
			case MOVEDOWN:
				if (row + 1 < board.length){
					if (isEmpty(row + 1, column)){
						int temp = newState.board[row][column];
						newState.parent = this;
						newState.board[row][column] = 0;
						newState.board[row+1][column] = temp;
						newState.pathLength = this.pathLength + 1;
						newState.operation = op;
					}
					else {
						return null;
					}
				}
				else {
					return null;
				}
				break;
		
			default:
				break;
		}
	
		return newState;
	}

	@Override
	public PuzzleState flip(int startRow, int startColumn, int endRow, int endColumn) {

		
		if (isEmpty(startRow, startColumn)){
			return null;
		}
		if (!isEmpty(endRow, endColumn)){
			return null;
		}
		
		int rowDiff = endRow - startRow;
		int columnDiff = endColumn - startColumn;
		
		PuzzleState newState = new SimplePuzzleState();
		newState = this;
		
		while (rowDiff != 0 || columnDiff != 0){
			if (isEmpty(startRow, startColumn + 1) && columnDiff > 0) {
				newState = newState.move(startRow, startColumn, Operation.MOVERIGHT);
				startColumn++;
				columnDiff--;
				
			}
			if (isEmpty(startRow, startColumn - 1) && columnDiff < 0) {
				newState =  newState.move(startRow, startColumn, Operation.MOVELEFT);
				startColumn--;
				columnDiff++;
				
			}
			if (isEmpty(startRow + 1, startColumn) && rowDiff > 0) {
				newState = newState.move(startRow, startColumn, Operation.MOVEDOWN);
				startRow++;
				rowDiff--;
				
			}
			if (isEmpty(startRow - 1, startColumn) && rowDiff < 0) {
				newState =  newState.move(startRow, startColumn, Operation.MOVEUP);
				startRow--;
				rowDiff++;
				
			}
			return newState;
			
		}
		return newState;
	}
	
	@Override
	public PuzzleState shuffleBoard(int pathLength) {
		
		int counter = 0;
		SimplePuzzleState newState = new SimplePuzzleState();
		
		newState.board = new int[board.length][board.length];
		for (int i = 0; i < board.length; i++){
			for (int j = 0; j < board.length; j++){
				newState.board[i][j] = this.getValue(i, j);
			}
		}

		while(counter < pathLength){
			Operation move = newState.randomMove();
			int[] emptyTile = newState.randomEmpty();
			int row = emptyTile[0];
			int column = emptyTile[1];
			switch (move) {
				case MOVERIGHT:
					column--;
					break;
				case MOVELEFT:
					column++;
					break;
				case MOVEUP:
					row++;
					break;
				case MOVEDOWN:
					row--;
					break;
				default:
					break;
			}
			if (newState.getValue(row, column) > 0){
				newState = (SimplePuzzleState) newState.move(row, column, move);
				counter ++;
			}
		}
	return newState;
	}
	
	private Operation randomMove(){
		
		Operation move = null;
		
		Random randomGenerator = new Random();
		int randomNumber = randomGenerator.nextInt(4);
		
		switch(randomNumber){
			case 0:
				move = Operation.MOVERIGHT;
				break;
			case 1:
				move = Operation.MOVELEFT;
				break;
			case 2:
				move = Operation.MOVEUP;
				break;
			case 3:
				move = Operation.MOVEDOWN;
				break;
			default:
				break;
		}
		
		return move;
		
	}
	
	private int[] randomEmpty(){

		int[] empty1 = null;
		int[] empty2 = null;
		int[] empty3 = null;
		
		
		int counter = 0;
		Random randomGenerator = new Random();
		int randomNumber = 0;
		
		for (int i = 0; i < board.length; i++){
			for (int j = 0; j < board.length; j++){
				if (isEmpty(i, j)){
					if (counter == 0){
						empty1 = new int[2];
						empty1[0] = i;
						empty1[1] = j;
					}
					else if (counter == 1){
						empty2 = new int[2];
						empty2[0] = i;
						empty2[1] = j;
					}
					else if (counter == 2){
						empty3 = new int[2];
						empty3[0] = i;
						empty3[1] = j;
					}
					counter++;
				}
			}
		}
	if (counter == 1){
		return empty1;
	}
	else if (counter == 2){
		randomNumber = randomGenerator.nextInt(2);
	}
	else {
		randomNumber = randomGenerator.nextInt(3);	
	}
	switch (randomNumber) {
	case 0:
		return empty1;
	case 1:
		return empty2;
	case 2:
		return empty3;

	default:
		break;
	}
	return null;
	}

	@Override
	public boolean isEmpty(int row, int column) {
		if (row >= 0 && column >= 0 && row < board.length && column < board.length){	
			if (this.board[row][column] == 0) {
				return true;
		}
	}
		return false;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.deepHashCode(board);
	
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SimplePuzzleState other = (SimplePuzzleState) obj;
		if (!Arrays.deepEquals(board, other.board))
			return false;
			
		return true;
	}
		



}

