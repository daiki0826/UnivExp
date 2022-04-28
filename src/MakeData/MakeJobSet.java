package MakeData;
import java.io.BufferedWriter;
import java.io.File;
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
public class MakeJobSet {
	
	public static void main(String[] args) {
		
		int J=9;
		int M=9;
		
		for(int count=1;count<=20;count++) {
			String filepath = "/Users/nagatadaiki/ExpData/JobData/"+String.valueOf(M)+"Machine/"+String.valueOf(J)+"J"+String.valueOf(M)+"M/set"+String.valueOf(count);
			File jobSetFile = new File(filepath);
			if(jobSetFile.mkdir()) {
				System.out.println("作業者ランダム配置実験ディレクトリの作成に成功しました");
			}
			MakeCondition exp = new MakeCondition(J, M);
			try {
				exportJobCSV(exp,J,M,count);
			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
			
		}
	}
	
	/*
	 * csvファイルを生成するクラス
	 */
	public static void exportJobCSV(MakeCondition con, int J,int M,int setnum) throws IOException {
		
		for(int count=1;count<=3;count++) { //納期の長さのみを変更したデータセットを作るために納期長，中，短で3回ループ
			String path ="";
			switch(count) {
			case 1: //納期長
				//ジョブのデータセットの保存先絶対パス
				path = "/Users/nagatadaiki/ExpData/JobData/"+String.valueOf(M)+"Machine/"+String.valueOf(J)+"J"+String.valueOf(M)+"M/set"+String.valueOf(setnum)+"/納期長";
				break;
			case 2: //納期中
				path = "/Users/nagatadaiki/ExpData/JobData/"+String.valueOf(M)+"Machine/"+String.valueOf(J)+"J"+String.valueOf(M)+"M/set"+String.valueOf(setnum)+"/納期中";
				break;
			case 3: //納期短
				path = "/Users/nagatadaiki/ExpData/JobData/"+String.valueOf(M)+"Machine/"+String.valueOf(J)+"J"+String.valueOf(M)+"M/set"+String.valueOf(setnum)+"/納期短";
				break;
			}
			
			Path p = Paths.get(path);
			try {
				Files.createDirectory(p);
			} catch (IOException e1) {
				// TODO 自動生成された catch ブロック
				e1.printStackTrace();
			}
			/*
			 * ジョブJ個それぞれの条件を記載したCSVファイル作成
			 */
			for(int j=1;j<=J;j++) {
				try {
					String jobNum = String.valueOf(j);
					FileWriter fw = new FileWriter(path+"/Job"+jobNum+".csv", false);
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
			/*
			 * 全ジョブの納期を記載したcsvファイル作成
			 */
			FileWriter fw = new FileWriter(path+"/Due.csv", false);
	        PrintWriter pw = new PrintWriter(new BufferedWriter(fw));
	        //ヘッダーの指定
	        pw.print("ジョブ番号");
	        pw.print(",");
	        pw.print("納期");
	        pw.println();
	        //各ジョブの納期をcsvファイルに書き出す
	        double[] SumPT = con.get_SumPT();
	        double Dj=0;
	        for(int j=1;j<=J;j++) {
	        	switch(count) {
	        	case 1://納期長
	        		Dj= SumPT[j]*2.0;
	        		pw.print(j);
		        	pw.print(",");
		        	pw.print(Dj);
		        	pw.println();
		        	break;
	        	case 2://納期中
	        		Dj = SumPT[j]*1.5;
	        		pw.print(j);
		        	pw.print(",");
		        	pw.print(Dj);
		        	pw.println();
		        	break;
	        	case 3://納期短
	        		Dj = SumPT[j]*1.0;
	        		pw.print(j);
		        	pw.print(",");
		        	pw.print(Dj);
		        	pw.println();
		        	break;
	        	}	
	        }
	        pw.close();
	        
	        
		    /*
		     * 各機械の負荷を記載したcsvファイル作成
		     */
		    FileWriter fw_1 = new FileWriter(path+"/MachineLoad.csv", false);
		    PrintWriter pw_1 = new PrintWriter(new BufferedWriter(fw_1));
		    //ヘッダーの指定
	        pw_1.print("機械番号");
	        pw_1.print(",");
	        pw_1.print("負荷(SumPT)");
	        pw_1.println();
	        //各機械の負荷を計算してcsvファイルに書き出す
	        int[] machineLoad = new int[M+1];
	        for(int m=1;m<=M;m++) {
	        	for(int j=1;j<=J;j++) {
	        		for(int k=1;k<=con.getProcessNum()[j];k++) {
	        			if(con.getPM(j, k)==m) {
	        				machineLoad[m] = machineLoad[m]+con.getPT(j, k);
	        			}
	        		}
	        	}
	        	pw_1.print(m);
	        	pw_1.print(",");
	        	pw_1.print(machineLoad[m]);
	        	pw_1.println();
	        }
	        pw_1.close();
	        // 出力確認用のメッセージ
	        System.out.println("csvファイルを出力しました");
		}
	}


}
