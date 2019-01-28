
import java.util.*;
import java.util.concurrent.*;
import java.io.*;
public class test1 {
static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
static StringTokenizer st;
static PrintWriter pr = new PrintWriter(new OutputStreamWriter(System.out));
	/* TASKS:
	 	* If user doesn't enter proper string for rock paper scissors
	 	* If user's ship list is too big for grid
	 	* "YOU SUNK THE SHIP" 
	 */
	static String[][] grid = new String[9][9];
	static String[][] comp = new String[9][9];
	static ArrayList<Integer>a = new ArrayList<Integer>();
	static ArrayList<Integer>a2 = new ArrayList<Integer>();
	static ArrayList<Pair>p = new ArrayList<Pair>();
	static ArrayList<Sunken> sunken = new ArrayList<Sunken>();
	static ArrayList<Sunken> csunken = new ArrayList<Sunken>();
	
	public static void main(String[] args)throws IOException, InterruptedException{
		HashMap<String, Integer>coor = new HashMap<String, Integer>();
		for(int i = 0; i < 9; i++) {
			coor.put(Character.toString((char)(i+65)), i+1);
		}
		fillgrid(grid);
		for(int i = 0; i < 9; i++) {
			for(int j = 0; j < 9; j++) {
				p.add(new Pair(i, j));
			}
		}
		
		
		System.out.println("Welcome to BATTLESHIP");
		System.out.println("What is your name?");
		String name = readLine();
		System.out.println("Hello "+name+"!"+"\n");
		
		shiptype();
		compset(a,coor);
		setup(a2,coor);
		
		System.out.println("The computer is choosing!");
		TimeUnit.SECONDS.sleep(a.size()+1);
		System.out.println("The computer has finished choosing!");
		
		System.out.println("\n"+"WHO WILL GO FIRST?");
		/*int c = RPS();
		System.out.println(c==1?"The Computer goes first!":"You go first!"+"\n");
		*/
		int c = 0;
		TimeUnit.SECONDS.sleep(2);
		System.out.println("Let the battle begin!");
		String[][] enemy = new String[9][9];
		fillgrid(enemy);
		
		for(int i = 0; i < 81*2; i++) {
			if(win(comp)) {
				System.out.println("You win!");
				return;
			}
			else if(win(grid)) {
				System.out.println("The computer win!");
				return;
			}
			if(i%2==c) { //your turn
				System.out.println("Your turn:");
				print(comp); ///ooooi
				System.out.println("Please enter coordinates");
				while(true) {
					int y = coor.get(next().toUpperCase())-1;
					int x = readInt()-1;
					if(enemy[x][y]== "0"||comp[x][y]=="H") {
						System.out.println("You already entered this; TRY AGAIN");
						continue;
					}
					else if(comp[x][y].equals(".")) {
						System.out.println("YOU MISSED");
						enemy[x][y] = "0";
						break;
					}
					else {
						System.out.println("YOU HIT");
						enemy[x][y] = comp[x][y];
						comp[x][y]="H";
						sinkship2(x, y);
						break;
					}
				}
				System.out.println();
			}
			else {
				System.out.println("The Computer's Turn");
				TimeUnit.SECONDS.sleep(2);
				compturn();
				System.out.println();
				print(grid);
				TimeUnit.SECONDS.sleep(1);
				System.out.println();
			}
		}
		
		
		
		br.close();
	}
	
	
	
	
	static void compturn() {
		int r = (int)(Math.random()*p.size());
		int x = p.get(r).x;
		int y = p.get(r).y;
		while(true) {
			if(grid[x][y]== "0"||grid[x][y]=="H") {
				continue;
			}
			else if(grid[x][y].equals(".")) {
				System.out.println("THE COMPUTER MISSED");
				grid[x][y] = "0";
				break;
			}
			else {
				System.out.println("THE COMPUTER HIT");
				grid[x][y] = "H";
				sinkship();
				break;
			}
		}
		p.remove(r);
	}
	static void compset(ArrayList<Integer>arsenal, HashMap<String, Integer>coor){
		comp = fillgrid(comp);
		while(!arsenal.isEmpty()){
			int r1 = (int)(Math.random()*8+0);
			int r2 = (int)(Math.random()*8+0);
			String direction = (int)(Math.random()*2+1)==1?"down":"right";
			int s = (int)(Math.random()*arsenal.size());
			if(fill2(r1, r2, direction, arsenal.get(s))) {
				arsenal.remove(arsenal.get(s));
			}
		}
	}
	
	
	static void setup(ArrayList<Integer>arsenal, HashMap<String,Integer>coor) throws IOException{
		while(!arsenal.isEmpty()) {
			System.out.println("THIS IS YOUR GRID");
			print(grid);
			System.out.println("SHIPS:");
			for(int i : arsenal)
				System.out.println(i);
			System.out.println("Which ship do you choose?");
			int ship = readInt();
			System.out.println("Start coordinate");
			int y = coor.get(next().toUpperCase())-1; int x = readInt()-1;
			System.out.println("Direction"+"\n"+"DOWN:RIGHT");
			String direction = readLine();
			if(fill(x, y, direction, ship)==true)
				arsenal.remove(arsenal.indexOf(ship));
			else {
				System.out.println("Invalid Input");
				continue;
			}
		}
	}
	static boolean fill(int x, int y, String direction, int ship){
		String backup[][] = copygrid(grid);
		if(direction.equalsIgnoreCase("down")) {
			if(x>9||y>9||x+ship>9)return false;
			for(int i = x; i < x+ship; i++) {
				if(grid[i][y].equals(".")) {
					grid[i][y] = Integer.toString(ship);
				}
				else {
					for(int j = x; j < x+ ship; j++)
						grid[j][y] = backup[j][y];
					return false;
				}
			}
		}
		else if(direction.equalsIgnoreCase("right")) {
			if(x>9||y>9||y+ship>9)return false;
			for(int j = y; j < y + ship; j++) {
				if(grid[x][j].equals(".")) {
					grid[x][j]=Integer.toString(ship);
				}
				else {
					for(j = y; j < y + ship; j++)
						grid[x][j] = backup[x][j];
					return false;
				}
			}
		}
		sunken.add(new Sunken(ship, x, y, direction.toLowerCase()));
		return true;
	}	
	static boolean fill2(int x, int y, String direction, int ship){
		String backup[][] = copygrid(comp);
		if(direction.equals("down")) {
			if(x>9||y>9||x+ship>9)return false;
			for(int i = x; i < x+ship; i++) {
				if(comp[i][y].equals(".")) {
					comp[i][y] = Integer.toString(ship);
				}
				else {
					for(int j = x; j < x+ship; j++)
					comp[j][y] = backup[j][y];
					return false;
				}
			}
		}
		else if(direction.equals("right")) {
			if(x>9||y>9||y+ship>9)return false;
			for(int j = y; j < y + ship; j++) {
				if(comp[x][j].equals(".")) {
					comp[x][j]=Integer.toString(ship);
				}
				else {
					for(j = y; j < y + ship; j++)
						comp[x][j] = backup[x][j];
					return false;
				}
			}
		}
		csunken.add(new Sunken(ship, x, y, direction));
		return true;
	}
	
	static boolean win(String[][] grid) {
		for(int i = 0; i < 9; i++) {
			for(int j = 0; j < 9; j++) {
				if(!grid[i][j].equals(".")&&!grid[i][j].equals("H")&&!grid[i][j].equals("0")) {
					return false;
				}
			}
		}
		return true;
	}
	
	static void sinkship() {//when comp takes ship
		for(Sunken s : sunken) {
			boolean fall = false;
			int x = s.x;
			int y = s.y;
			String type = s.type;
			int ship = s.s;
			if(type.equals("down")) {
				for(int i = x; i <= x+ship; i++) {
					for(int j = y; j <= y+ship; j++) {
						if(grid[i][j].equals("H"))fall = true;
						else {
							fall = false;
							break;
						}//end of else
					}//end of j
				} //end of i
					if(fall == true) {
						System.out.println("The Computer has sunken your ship with size " + ship);
						sunken.remove(sunken.indexOf(ship));
					}
				}//end of if
				if(type.equals("right")) {
					for(int i = y; i <= y+ship; i++) {
						for(int j = x; j <= x+ship; j++) {
							if(grid[i][j].equals("H"))fall = true;
							else {
								fall = false;
								break;
							}
						}//end of j
					}//end of i
						if(fall == true) {
							System.out.println("The Computer has sunken your ship with size " + ship);
							sunken.remove(sunken.indexOf(ship));
						}
				}//end of if
		}

	}
	static void sinkship2(int x, int y) {//player hit comp
		System.out.println("XY" + x + " "+y );
		String d = find(x, y, csunken);
		int ship = findship(x, y, csunken);
		boolean f = false;
		System.out.println("direction:"+d);
		if(d.equalsIgnoreCase("down")) {
			for(int j = x; j < x +ship; j++) {
				if(comp[j][y].equals("H"))
					f = true;
				else {
					f = false;
					break;
				}
			}
		}
		else if(d.equalsIgnoreCase("right")) {
			for(int i = y; i < y+ship; i++) {
				if(comp[x][i].equals("H"))
					f = true;
				else {
					f = false;
					break;
				}
			}
		}
		if(f=true) {
			System.out.println("You have sunken ship model with size " + ship);
		}
		else 
			return;
	}
	static String find(int d, int v, ArrayList<Sunken>derp) {
		for(Sunken s : derp) {
			if(s.x == d&&s.y == v)
				return s.type;
		}
		return null;
	}
	static int findship(int x, int y, ArrayList<Sunken>derp) {
		for(Sunken s : derp) {
			if(s.x == x && s.y == y)return s.s;
		}
		return 0; 
	}
	

	static void print(String[][] grid) {
		System.out.println("  A B C D E F G H I");
		for(int i = 0; i < 9; i++) {
			System.out.print((i+1)+" ");
			
			for(int j = 0; j < 9; j++) {
				System.out.print(grid[i][j]+" ");
		
			}
			System.out.println();
		}
		System.out.println();
	}
	static String[][]fillgrid(String[][] grid) {
		for(int i = 0; i < 9; i++) {
			for(int j = 0; j < 9 ; j++) {
				grid[i][j] = ".";
			}
		}
		return grid;
	}
	static String[][]copygrid(String[][] grid) {
		String copy[][] = new String[9][9];
		for(int i = 0; i < 9; i++) {
			for(int j = 0; j < 9; j++) {
				copy[i][j] = grid[i][j];
			}
		}
		return copy;
	}
	static void shiptype() throws IOException{
		System.out.println("Please enter size of ships"+"\n"+"When done, enter \"END\".");
		String str = "";
		while(!str.equals("end")) {
			str = readLine().toLowerCase();
			if(str.equals("end"))break;
			a.add(Integer.parseInt(str));
			a2.add(Integer.parseInt(str));
		}
	}
	
	static int RPS() throws IOException, InterruptedException{ //false comp go first
		System.out.println("Let's do rock, paper, scissors!");
		ArrayList<String>why = new ArrayList<String>();
		
		why.add("rock");
		why.add("scissor");
		why.add("paper");
		
		int c = (int)(Math.random()*2);
		System.out.println("Ready?");
		TimeUnit.SECONDS.sleep(1);
		System.out.print("Rock, ");
		TimeUnit.SECONDS.sleep(1);
		System.out.print("paper, ");
		TimeUnit.SECONDS.sleep(1);
		System.out.print("scissors, ");
		TimeUnit.SECONDS.sleep(1);
		System.out.println("SHOOT!");
		
		String ans = next().toLowerCase();
		int yans = why.indexOf(ans);
		System.out.println("The computer plays "+ why.get(c)+".");
		if(yans == 2 && c == 0)return 0;
		else if(yans<c) return 0;
		else if(yans==c) {
			System.out.println("It's a tie!"+"\n"+"Let's do it again!");
			RPS();
		}
		return 1;
	}

static String next() throws IOException{
	while(st==null||!st.hasMoreTokens()) {
		st = new StringTokenizer(br.readLine().trim());
	}
	return st.nextToken();
}
static int readInt() throws IOException{
	return Integer.parseInt(next());
}
static String readLine() throws IOException{
	return br.readLine().trim();
}
}
class Pair{
	int y;
	int x;
	
	public Pair(int x, int y) {
		this.x = x;
		this.y = y;
	}
}
class Sunken{
	int s; //ship type
	int x, y; //coordinates 
	String type;
	public Sunken(int s, int x, int y, String type) {
		this.s = s;
		this.x = x;
		this.y = y;
		this.type = type;
	}
}
