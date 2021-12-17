package MakeData;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
 

/*
 * データセットを生成するクラス
 * 実行するとデータセットのディレクトリを作成してその中にジョブ毎の条件を記載したcsvファイルを生成.
 */
public class MakeData {
	
	public static void main(String[] args) {
		
		int J=3;
		int M=3;
		
		MakeCondition exp = new MakeCondition(J, M);
		try {
			exportCSV(exp, J);
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		
		
		
	}
	
	//csvファイルを生成するクラス
	public static void exportCSV(MakeCondition con, int J) throws IOException {
		//保存先の絶対パス
		String path = "/Users/nagatadaiki/ExpData/";
		
		//新しいデータセットを生成するときは「Data」の後の数字を変更すること!
		String dataSetNum = "Data2/Condition";
		
		//指定のパスにディレクトリを作成(指定のパスは"/Users/nagatadaiki/ExpData/Data2/Condition")
		Path p = Paths.get(path,dataSetNum);
		try {
			Files.createDirectory(p);
		} catch (IOException e1) {
			// TODO 自動生成された catch ブロック
			e1.printStackTrace();
		}
		//ジョブJ個それぞれの条件を記載したCSVファイル作成
		for(int j=1;j<=J;j++) {
			try {
				String jobNum = String.valueOf(j);
				FileWriter fw = new FileWriter(path+dataSetNum+"/Job"+jobNum+".csv", false);
	            PrintWriter pw = new PrintWriter(new BufferedWriter(fw));
	            //ヘッダーの指定
	            pw.print("工程番号");
	            pw.print(",");
	            pw.print("処理機械番号");
	            pw.print(",");
	            pw.print("標準作業時間");
	            pw.println();
	            
	            for(int k=1;k<=con.getProcessNum()[j];k++) {
	            	String processNum = String.valueOf(k);
	            	String machineNum = String.valueOf(con.getPM(j, k));
	            	String processTime = String.valueOf(con.getPT(j, k));
	            	pw.print(processNum);
	            	pw.print(",");
	            	pw.print(machineNum);
	            	pw.print(",");
	            	pw.print(processTime);
	            	pw.println();
	            }
	            pw.close();
	        } catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
		}
		//全ジョブの納期を記載したcsvファイル作成
		FileWriter fw = new FileWriter(path+dataSetNum+"/Due.csv", false);
        PrintWriter pw = new PrintWriter(new BufferedWriter(fw));
        //ヘッダーの指定
        pw.print("ジョブ番号");
        pw.print(",");
        pw.print("納期");
        pw.println();
        //各ジョブの納期をcsvファイルに書き出す
        Double[] due = con.getDue();
        for(int j=1;j<=J;j++) {
        	pw.print(j);
        	pw.print(",");
        	pw.print(due[j]);
        	pw.println();
        }
        pw.close();
        // 出力確認用のメッセージ
        System.out.println("csvファイルを出力しました");
	}


}
