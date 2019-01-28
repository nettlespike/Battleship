package Battleship;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.util.stream.Stream;

public class Reader {
	public BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	public StringTokenizer st;
	public Stream<?> stream;
	
	public Reader(Stream<?> stream) {
		this.stream = stream;
	}
	
	public String next() throws IOException{
		while(st == null || !st.hasMoreTokens())
			st = new StringTokenizer(br.readLine().trim());
		return st.nextToken();
	}
	public int readInt() throws IOException{
		return Integer.parseInt(next());
	}
	public String readLine() throws IOException{
		return br.readLine().trim();
	}
}
