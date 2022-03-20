import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class LogFile {

	public static final void writeLog(String fileName,Object log) {
		try {
			FileWriter fw = new FileWriter(fileName+"/log.txt",true);
			PrintWriter pw = new PrintWriter(new BufferedWriter(fw));
			pw.println(log);
			pw.close();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}
	
}
