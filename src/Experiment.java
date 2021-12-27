import java.io.File;

/*
 * 一回の実験を行うクラス
 */
public class Experiment {
	
	private String jobSetPath;	//入力として与えるジョブセットのフォルダパス
	private String dueFilePath;	//入力として与える各ジョブの納期設定の記述したcsvファイルのパス
	private String workerSetPath;	//入力として与える作業者セットのフォルダパス
	private String expDirectoryPath;	//一回の実験データを全て包括するフォルダパス
	private Condition inputJobCon;	//ジョブ条件
	private WorkerCondition inputWorkerCon; //作業者条件
	private int J;
	private int M;
	
	
	
	public Experiment(String jobSetPath,String workerSetPath, String expDirectoryPath,int J,int M) {
		this.jobSetPath = jobSetPath;
		this.dueFilePath = jobSetPath+"/Due.csv";
		this.workerSetPath = workerSetPath;
		this.expDirectoryPath = expDirectoryPath;
		this.J = J;
		this.M = M;
		//実験ディレクトリを作成
		 File jobSetFile = new File(expDirectoryPath);
		 if(jobSetFile.mkdir()) {
			 System.out.println("実験ディレクトリの作成に成功しました");
		 }
		
		//ジョブセットのCSVファイルからジョブ条件クラス"Condition.java"のインスタンス作成
		this.inputJobCon = new Condition(J, M, this.jobSetPath);
		//作業者セットのCSVファイルから作業者条件クラス"WorkerCondition.java"のインスタンス作成
		this.inputWorkerCon = new WorkerCondition(this.workerSetPath, M);
	}
	
	//作業者配置LPファイル作成
	public void makeLP_WA() {
		MakeLP_WA makeLP_WA = new MakeLP_WA(this.inputWorkerCon, this.inputJobCon,expDirectoryPath);
		System.out.println("作業者配置問題のLPファイルを作成しました");
	}
	
	//作業者配置問題求解
	public void solveLP_WA() {
		String lpFilePath = expDirectoryPath+"/worker.lp";
		SolveLP_WA solveLP_WA = new SolveLP_WA(lpFilePath, this.inputJobCon);
		solveLP_WA.exportCSV(jobSetPath, expDirectoryPath);
		System.out.println("作業者配置問題を求解できました");
	}
	
	//ジョブショップ問題LPファイル作成
	public void makeLP() {
		//作業者配置決定後のCSVファイルからジョブ毎の条件をインポートして条件クラスを生成．
//		Condition detailCondition = new Condition(J,M,expDirectoryPath+"/JobSet");
//		MakeLP makeLP = new MakeLP(detailCondition, expDirectoryPath);
//		System.out.println("ジョブショップ問題のLPファイルを作成しました");
		
		MakeLP makeLP = new MakeLP(inputJobCon, expDirectoryPath);
	}
	
	//ジョブショップ問題求解してxmlファイル作成
	public void solveLP() {
		String lpFilePath = expDirectoryPath+"/jobshop.lp";
		SolveLP solveLP = new SolveLP(lpFilePath,this.inputJobCon);
		double[][]ST = solveLP.getGUNTST();
		double[][]CT = solveLP.getGUNTCT();
		Output output = new Output(this.inputJobCon,ST,CT);
		output.MakeGunt(expDirectoryPath);
		System.out.println("ジョブショップ問題を求解できました");
	}
	
	// public void copy() {
	// 	//入力データをexpディクトリにコピーしておく
	// 	Path jobSet = Paths.get(this.jobSetPath);
	// 	Path dueFile = Paths.get(this.dueFilePath);
	// 	Path workerSet = Paths.get(this.workerSetPath);
	// 	Path expDirectory = Paths.get(this.expDirectoryPath+"/JobSet/Due.csv");
	// 	try {
	// 		//ジョブセットと作業者セットの入力ファイルを実験ファイルにコピー(すでにある場合は置き換える)
	// 		//Files.copy(jobSet, expDirectory,REPLACE_EXISTING);
	// 		Files.copy(dueFile, expDirectory,REPLACE_EXISTING);
	// 		//Files.copy(workerSet, expDirectory,REPLACE_EXISTING);
	// 		System.out.println("入力データコピー完了");
	// 	} catch (IOException e) {
	// 		// TODO 自動生成された catch ブロック
	// 		e.printStackTrace();
	// 	}
	// }
	

}
