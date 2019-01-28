package Battleship;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ArrayList;
import java.io.IOException;


public class Board {
	public int r, c; 
	public String[][] board;
	public Reader br;
	
	public Board(int r, int c) {
		this.r = r;
		this.c = c;
		this.board = new String[r][c];
	}
	/**
	 * @param intializes the game board
	 */
	public void initBoard() {
		for(int i = 0; i < 9; i++) Arrays.fill(board, ".");
	}
	/**
	 * @param makes a copy of the board
	 * @return a copy of the board
	 */
	public String[][] copyOfBoard() {
		return board;
	}
	/**
	 * @param displays the game board
	 */
	public void displayBoard() {
		System.out.println("  A B C D E F G H I");
		for(int i = 0; i < 9; i++) {
			System.out.print(( i + 1 ) + " ");
			
			for(int j = 0; j < 9; j++) 
				System.out.print(board[i][j] + " ");
			
			System.out.println();
		}
		System.out.println();
	}
	/**
	 * @param check if its game over
	 * @return false if the game can continue, true otherwise
	 */
	public boolean gameOver() {
		for(int i = 0; i < 9; i++) {
			for(int j = 0; j < 9; j++) 
				if(board[i][j].equals("*")) return false;
		}
		return true;
	}
	/**
	 * @param places down ships
	 */
	public void placeShips() {
		
	}
	/**
	 * @param helper method for placeShips
	 */
	public void fill(Point point, String direction, int shipLength) {
		if(!check(point, direction, shipLength)) return;
		
		if(direction.equalsIgnoreCase("down")) {
			for(int i = point.x; i < point.x + shipLength; i++) board[i][point.y] = Integer.toString(shipLength);
		}
		else if(direction.equalsIgnoreCase("right")) {
			for(int j = point.y; j < point.y + shipLength; j++) board[point.x][j] = Integer.toString(shipLength);
		}
		
	}
	/**
	 * @param helper method for fill
	 * @return
	 */
	public boolean check(Point point, String direction, int shipLength) {
		if(direction.equalsIgnoreCase("down")) {
			if(point.x > 9 || point.y > 9 || point.x + shipLength > 9) return false;
			for(int i = point.x; i  < point.x + shipLength; i++) {
				if(board[i][point.y].equals(".")) continue;
				else  return false;
			}
		}
		
		else if(direction.equalsIgnoreCase("right")) {
			if(point.x > 9 || point.y > 9 || point.y + shipLength > 9) return false;
			for(int j = point.y; j < point.y + shipLength; j++) {
				if(board[point.x][j].equals(".")) continue;
				else return false;
			}
		}
		return true;
	}
	/**
	 * @param helper method for check, undo board changes
	 */
	public void undoBoardChanges(int start, int end, String direction, int pos, String[][] backup) {
		if(direction.equalsIgnoreCase("down"))
			for(int i = start; i < end; i++) board[i][pos] = backup[i][pos];
		else
			for(int j = start; j < end; j++) board[pos][j] = backup[pos][j];
	}
	/**
	 * @param Setting up the board
	 */
	public void setUpBoard(ArrayList<Integer> arsenal, HashMap<String, Integer> coor) throws IOException{
		while(!arsenal.isEmpty()) {
			System.out.println("THIS IS YOUR GRID");
			displayBoard();
			System.out.println("SHIPS:");
			
			for(int i: arsenal) System.out.println(i);
			
			System.out.println("Which ship do you choose?");
			int ship = br.readInt();
			
			System.out.println("Start coordinate");
			int x =  br.readInt() - 1, y = coor.get(br.next().toUpperCase()) - 1;
			
			System.out.println("Direction" + "\n" + "DOWN:RIGHT");
			String direction = br.readLine();
			
			if(check(new Point(x, y), direction, ship)) 
				arsenal.remove(arsenal.indexOf(ship));
			else {
				System.out.println("Invalid Input");
				continue;
			}
			
		}
	}
	
}
