import static java.nio.file.StandardCopyOption.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/*
 * 一回の実験を行うクラス
 */
public class Experiment {
	
	private String jobSetPath;	//入力として与えるジョブセットのフォルダパス
	private String workerSetPath;	//入力として与える作業者セットのフォルダパス
	private String expDirectoryPath;	//一回の実験データを全て包括するフォルダパス
	private Condition inputJobCon;	//ジョブ条件
	private WorkerCondition inputWorkerCon; //作業者条件
	
	
	public Experiment(String jobSetPath,String workerSetPath, String expDirectoryPath,int J,int M) {
		this.jobSetPath = jobSetPath;
		this.workerSetPath = workerSetPath;
		this.expDirectoryPath = expDirectoryPath;
		//実験ディレクトリを作成
		 File jobSetFile = new File(expDirectoryPath);
		 if(jobSetFile.mkdir()) {
			 System.out.println("実験ディレクトリの作成に成功しました");
		 }
		
		//入力データをexpディクトリにコピーしておく
		Path jobSet = Paths.get(this.jobSetPath);
		Path workerSet = Paths.get(this.workerSetPath);
		Path expDirectory = Paths.get(this.expDirectoryPath);
		try {
			//ジョブセットと作業者セットの入力ファイルを実験ファイルにコピー(すでにある場合は置き換える)
			Files.copy(jobSet, expDirectory,REPLACE_EXISTING);
			Files.copy(workerSet, expDirectory,REPLACE_EXISTING);
			System.out.println("入力データコピー完了");
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		
		//ジョブセットのCSVファイルからジョブ条件クラス"Condition.java"のインスタンス作成
		this.inputJobCon = new Condition(J, M, this.jobSetPath);
		//作業者セットのCSVファイルから作業者条件クラス"WorkerCondition.java"のインスタンス作成
		this.inputWorkerCon = new WorkerCondition(this.workerSetPath, M);
	}
	
	//作業者配置LPファイル作成
	public void makeLP_WA() {
		MakeLP_WA makeLP_WA = new MakeLP_WA(this.inputWorkerCon, this.inputJobCon,expDirectoryPath);
	}
	
	//作業者配置問題求解
	public void solveLP_WA() {
		String lpFilePath = expDirectoryPath+"/worker.lp";
		SolveLP_WA solveLP_WA = new SolveLP_WA(lpFilePath, this.inputJobCon);
		solveLP_WA.exportCSV(jobSetPath, expDirectoryPath);
	}
	
	//ジョブショップ問題LPファイル作成
	public void makeLP() {
		//一旦試しにただのジョブショップ問題解いてみる
		MakeLP makeLP = new MakeLP(this.inputJobCon, expDirectoryPath);
	}
	
	//ジョブショップ問題求解してxmlファイル作成
	public void solveLP() {
		String lpFilePath = expDirectoryPath+"/jobshop.lp";
		SolveLP solveLP = new SolveLP(lpFilePath,this.inputJobCon);
		double[][]ST = solveLP.getGUNTST();
		double[][]CT = solveLP.getGUNTCT();
		Output output = new Output(this.inputJobCon,ST,CT);
		output.MakeGunt(expDirectoryPath);
	}
	

}
