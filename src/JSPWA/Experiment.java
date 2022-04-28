package JSPWA;
import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

/*
 * 実験を行うクラス
 */
public class Experiment {
	
	private double[] objval = new double[20]; //目的関数保存用配列(余力の大きさを変化させるためとりあえずサイズ10にしといた)
	private Condition detailCon; //作業者配置決定後の全ジョブの条件をまとめたインスタンス

	public Experiment() {
		System.out.println(Constant.expPath);
		//実験ディレクトリを作成
		File jobSetFile = new File(Constant.expPath);
		if(jobSetFile.mkdir()) {
			System.out.println("実験ディレクトリの作成に成功しました");
		}
		Constant.workCon = new WorkerCondition();
		Constant.jobCon = new Condition();
	}
	
	//ジョブショップ問題LPファイル作成
	public void makeLP() {
		MakeLP_3 makeLP = new MakeLP_3();
		System.out.println("ジョブショップ問題のLPファイルを作成しました");
	}
	
	public void solveLP() {
		String LPFilePath = Constant.expPath+"/jobshop.lp";
		SolveLP solveLP = new SolveLP(LPFilePath);
		LogFile.writeLog(Constant.expPath, "Total_Tardiness = "+solveLP.getObjval());
		double[][]ST = solveLP.getGUNTST();
		double[][]CT = solveLP.getGUNTCT();
		double[][][] x = solveLP.getX();
		Output output = new Output(ST,CT,x,Constant.jobCon);
		output.MakeGunt(Constant.expPath+"/Schedule.xml");
		System.out.println("シミュレーションを開始します");
		//this.loopSimulation(ST, CT, margin2,objval[count]);
		//LoopSimulator LS = new LoopSimulator(500);
		//LS.loopSimulation(ST,CT,margin2,objval[count]);
	}

	 public void copy() {
		File jobSetSrc = new File(Constant.jobSetPath);
		File jobSetDest = new File(Constant.expPath+"/Jobset");
		File workerSetSrc = new File(Constant.workerSetPath);
		File workerSetDest = new File(Constant.expPath+"/WorkerSet");
		try {
			FileUtils.copyDirectory(jobSetSrc, jobSetDest);
			FileUtils.copyDirectory(workerSetSrc, workerSetDest);
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	 }
	 
	 
	 
	 /*
	  *　シミュレーション実験
	  *  ・提案手法のスケジュールを基に実際の生産活動をシミュレーションする．
	  *  ・複数回シミュレーションを行うが，TrialNum で試行回数を変更できる．
	 public void loopSimulation(double[][] ST,double[][] CT,double margin,double objval) {
		 int TrialNum = 500;
		 double[] Tardiness = new double[TrialNum+1];
		 double[] Tard_plus = new double[TrialNum+1];
		 double[] Tard_minus = new double[TrialNum+1];
		 double[] over_CT = new double[TrialNum+1];
		 double[] shortage_CT = new double[TrialNum+1];
		 double[] div_CT = new double[TrialNum+1];
		 int count_div_tardiness = 0;
		//実験ディレクトリを作成
		System.out.println("シミュレーション実験を開始");
		String expPath = Constant.expPath+"/Simulation"+String.valueOf(margin)+"σ"; //ディレクトリへのパス
		File jobSetFile = new File(expPath);
		if(jobSetFile.mkdir()) {
			System.out.println("シミュレーション実験ディレクトリの作成に成功しました");
		}
		for(int count=1;count<=TrialNum;count++) {
			System.out.println("シミュレーション"+String.valueOf(count)+"回目");
			Simulation simulator = new Simulation(ST, CT,null);
			simulator.runSimulation();
			Output output = new Output(simulator.getRealST(),simulator.getRealCT(),null);
			output.MakeGunt(Constant.expPath+"/Simulation"+String.valueOf(margin)+"σ/"+String.valueOf(count)+"試行目.xml");
			Tardiness[count] = simulator.Sim_tard();
			Tard_plus[count] = simulator.div_tard(objval)[0];
			Tard_minus[count] = simulator.div_tard(objval)[1];
			over_CT[count] = simulator.over_CT();
			shortage_CT[count] = simulator.shortage_CT();
			div_CT[count] = simulator.div_CT();
		}
		try {
			FileWriter fw = new FileWriter(expPath+"/Simulation"+String.valueOf(margin)+"σ_"+Constant.J+"J"+Constant.M+"M.csv", false);
			PrintWriter pw = new PrintWriter(new BufferedWriter(fw));
			//ヘッダーの指定
            pw.print("Trial");
            pw.print(",");
            pw.print("Tardiness_Real");
            pw.print(",");
            pw.print("Tard_plus");
            pw.print(",");
            pw.print("Tard_minus");
            pw.print(",");
            pw.print("over_CT");
            pw.print(",");
            pw.print("shortage_CT");
            pw.print(",");
            pw.print("div_CT");
            pw.println();
	   		for(int count=1;count<=TrialNum;count++) {
	   			pw.print(count);
	            pw.print(",");
	            pw.print(Tardiness[count]);
	            pw.print(",");
	            pw.print(Tard_plus[count]);
	            if(Tard_plus[count]>0) count_div_tardiness++;
	            pw.print(",");
	            pw.print(Tard_minus[count]);
	            pw.print(",");
	            pw.print(over_CT[count]);
	            pw.print(",");
	            pw.print(shortage_CT[count]);
	            pw.print(",");
	            pw.print(div_CT[count]);
	            pw.println();
	 		}
	   		pw.print(""+","+""+","+""+","+""+","+""+","+""+","+""+","+count_div_tardiness);
	   		pw.println();
	   		pw.close();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	 }*/

}
