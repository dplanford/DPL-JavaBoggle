package opusgames.com.boggle.delegates;


/**
 * delegate for printing boggle information
 */
public interface BogglePrintDelegate {
	
	abstract void print(String str);
		
	abstract void println();

	abstract void println(String str);
		
	abstract void printf(String format, Object... args);
	
	abstract void clear();
}
