import java.io.File;

public class Output_WA_Result {
	
	private double[][] myu;
	private double[][] sig;
	private int[] worker;
	private SolveLP_WA solveLP_WA;
	
	 public Output_WA_Result(SolveLP_WA solveLP_WA, String filePath) {
		 
		 
		 
		//絶対パスでディレクトリ作成
		 File jobSetFile = new File(filePath);
		 if(jobSetFile.mkdir()) {
			 //各ジョブJ個の条件を記述したcsvファイルをそれぞれ出力する
			 
		 }
	 }
	 
	 public void setMyu() {
		 
	 }

}
