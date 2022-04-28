import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

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
		Constant.jobCon = new Condition(Constant.jobSetPath);
		
	}
	
	//作業者配置LPファイル作成
	public void makeLP_WA() {
		MakeLP_WA makeLP_WA = new MakeLP_WA();
		System.out.println("作業者配置問題のLPファイルを作成しました");
	}
	
	//作業者配置問題求解
	public void solveLP_WA() {
		String lpFilePath = Constant.expPath+"/worker.lp";
		SolveLP_WA solveLP_WA = new SolveLP_WA(lpFilePath);
		solveLP_WA.exportCSV(Constant.expPath);
		System.out.println("作業者配置問題を求解できました");
	}
	
	
	//ジョブショップ問題LPファイル作成
	public void makeLP() {
		//作業者配置決定後のCSVファイルからジョブ毎の条件をインポートして条件クラスを生成．
		Condition detailCondition = new Condition(Constant.expPath);
		this.detailCon = detailCondition;
		for(int margin=0;margin<=20;margin+=2) { //余力0σ~2σまで0.2刻みで
			double margin2 = (double)margin/10;
			MakeLP makeLP = new MakeLP(detailCondition,margin2);
		}
		System.out.println("ジョブショップ問題のLPファイルを作成しました");
	}
	
	//ジョブショップ問題LPファイル作成
	public void makeLP_2() {
		//矩形集合を用いた定式化によるLPファイル
		Condition detailCondition = new Condition(Constant.expPath);
		this.detailCon = detailCondition;
		for(int margin=0;margin<=20;margin+=2) { //余力0σ~2σまで0.2刻みで
			double margin2 = (double)margin/10;
			MakeLP_2 makeLP_2 = new MakeLP_2(detailCondition,margin2);
		}
		System.out.println("ジョブショップ問題のLPファイルを作成しました");
	}
	
	//ジョブショップ問題求解してxmlファイル作成
	public void solveLP() {
//		int count = 0; //ループ回数カウント
//		for(int margin=0;margin<=30;margin+=5) { //余力+0σ~+3σまで0.5刻みで順に求解していく
//			double margin2 = (double)margin/10;
//			System.out.println("margin = "+margin2);
//			String LPFilePath = Constant.expPath+"/jobshop_"+String.valueOf(margin2)+"σ.lp";
//			SolveLP solveLP = new SolveLP(LPFilePath);
//			LogFile.writeLog(Constant.expPath, "Tardiness_"+String.valueOf(margin2)+"σ = "+solveLP.getObjval());
//			this.objval[count] = solveLP.getObjval();
//			double[][]ST = solveLP.getGUNTST();
//			double[][]CT = solveLP.getGUNTCT();
//			Output output = new Output(ST,CT);
//			output.MakeGunt(Constant.expPath,margin2,false);
//			System.out.println("シミュレーションを開始します");
//			this.loopSimulation(ST, CT, margin2,objval[count]);
//			count++;
//		}
		
		int count = 0; //ループ回数カウント
		LoopSimulator LS = new LoopSimulator(500);
		for(int margin=0;margin<=30;margin+=2) { //余力+0σ~+2σまで0.2刻みで順に求解していく
			double margin2 = (double)margin/10;
			System.out.println("margin = "+margin2);
			String LPFilePath = Constant.expPath+"/jobshop_"+String.valueOf(margin2)+"σ.lp";
			SolveLP solveLP = new SolveLP(LPFilePath);
			LogFile.writeLog(Constant.expPath, "Total_Tardiness_"+String.valueOf(margin2)+" = "+solveLP.getObjval());
			this.objval[count] = solveLP.getObjval();
			double[][]ST = solveLP.getGUNTST();
			double[][]CT = solveLP.getGUNTCT();
			Output output = new Output(ST,CT,this.detailCon);
			output.MakeGunt(Constant.expPath+"/Schedule_"+String.valueOf(margin2)+"σ.xml");
			System.out.println("シミュレーションを開始します");
			//this.loopSimulation(ST, CT, margin2,objval[count]);
			LS.loopSimulation(ST,CT,margin2,objval[count]);
			count++;
		}
		System.out.println("ジョブショップ問題を求解できました");
	}
	
	public void solveLP_gurobi() {
		int count = 0; //ループ回数カウント
		LoopSimulator LS = new LoopSimulator(500);
		for(int margin=0;margin<=30;margin+=10) { //余力+0σ~+2σまで0.2刻みで順に求解していく
			double margin2 = (double)margin/10;
			System.out.println("margin = "+margin2);
			String LPFilePath = Constant.expPath+"/jobshop_"+String.valueOf(margin2)+"σ.lp";
			SolveLP_gurobi solveLP_g = new SolveLP_gurobi(LPFilePath);
//			LogFile.writeLog(Constant.expPath, "Total_Tardiness_"+String.valueOf(margin2)+" = "+solveLP.getObjval());
//			this.objval[count] = solveLP.getObjval();
//			double[][]ST = solveLP.getGUNTST();
//			double[][]CT = solveLP.getGUNTCT();
//			Output output = new Output(ST,CT,this.detailCon);
//			output.MakeGunt(Constant.expPath+"/Schedule_"+String.valueOf(margin2)+"σ.xml");
//			System.out.println("シミュレーションを開始します");
//			//this.loopSimulation(ST, CT, margin2,objval[count]);
//			LS.loopSimulation(ST,CT,margin2,objval[count]);
//			count++;
		}
		System.out.println("ジョブショップ問題を求解できました");
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
	 
	 /*
	  * ランダムに作業者を配置した実験を複数回試行する
	  */
	 public void RandomWA() {
		//実験ディレクトリを作成
		System.out.println("作業者ランダム配置実験を開始");
		String expPath = Constant.expPath+"/RandomWA"; //ディレクトリへのパス
		File jobSetFile = new File(expPath);
		if(jobSetFile.mkdir()) {
			System.out.println("作業者ランダム配置実験ディレクトリの作成に成功しました");
		}
		
		
		//作業者ランダム配置試行回数を設定
		int TrialNum=50; 
		
		//LPファイル作成(試行回数分の配列に保存)
        MakeLP_RandomWA[] LPFiles = new MakeLP_RandomWA[TrialNum+1];
        for(int count=1;count<=TrialNum;count++) {
			LPFiles[count] = new MakeLP_RandomWA(Constant.jobCon,Constant.workCon,count);
		}
        //ランダムに作業者を配置した場合の標準偏差の総和をcsvファイルに出力
		try {
			FileWriter fw_1 = new FileWriter(expPath+"/Sum_σ.csv", false);
			PrintWriter pw_1 = new PrintWriter(new BufferedWriter(fw_1));
			//ヘッダーの指定
			pw_1.print("TrialNum");
			pw_1.print(",");
			pw_1.print("Sum_sigma");
			pw_1.println();
			for(int count=1;count<=TrialNum;count++) {
				pw_1.print(count);
				pw_1.print(",");
				pw_1.print(LPFiles[count].get_Sum_sigma());
				pw_1.println();
			}
			pw_1.close();
		} catch (IOException e1) {
			// TODO 自動生成された catch ブロック
			e1.printStackTrace();
		}
		try {
			FileWriter fw = new FileWriter(expPath+"/RandomWA"+Constant.J+"J"+Constant.M+"M.csv", false);
			PrintWriter pw = new PrintWriter(new BufferedWriter(fw));
			//ヘッダーの指定
            pw.print("TrialNum");
            pw.print(",");
            pw.print("+0σ");
            pw.print(",");
            pw.print("+0.5σ");
            pw.print(",");
            pw.print("+1.0σ");
            pw.print(",");
            pw.print("+1.5σ");
            pw.print(",");
            pw.print("+2.0σ");
            pw.print(",");
            pw.print("+2.5σ");
            pw.print(",");
            pw.print("+3.0σ");
            pw.println();
            //ヘッダーの指定ここまで
            
            
//            //LPファイル作成(試行回数分の配列に保存)
//            MakeLP_RandomWA[] LPFiles = new MakeLP_RandomWA[TrialNum+1];
//        	for(int trial=1;trial<=TrialNum;trial++) {
//    			LPFiles[trial] = new MakeLP_RandomWA(Constant.jobCon,Constant.workCon,trial);
//    		}
        	
        	//CPLEXにて求解
    		for(int trial=1;trial<=TrialNum;trial++) {
    			pw.print(trial);
    			pw.print(",");
    			boolean objval_Flag=false;
    			for(int margin=0;margin<=30;margin+=10) {
    				double margin2 = (double)margin/10;
    				String LPFilePath = Constant.expPath+"/RandomWA/"+String.valueOf(trial)+"試行"+"/jobshop_"+String.valueOf(margin2)+"σ_"+String.valueOf(trial)+".lp";
    				SolveLP solveLP = new SolveLP(LPFilePath);
    				double[][]ST = solveLP.getGUNTST();
    				double[][]CT = solveLP.getGUNTCT();
    				Output output = new Output(ST,CT,null);
    				output.MakeGunt(Constant.expPath+"/RandomWA/"+String.valueOf(trial)+"試行/Schedule_"+String.valueOf(margin2)+"σ.xml");
    				if(margin2==0) {
    					for(int i=1;i<=Constant.M;i++) {
        					for(int m=1;m<=Constant.M;m++) {
        						//作業者配置をログファイルに記録
        						LogFile.writeLog(Constant.expPath+"/RandomWA/"+String.valueOf(trial)+"試行","x("+String.valueOf(i)+","+String.valueOf(m)+") = "+String.valueOf(LPFiles[trial].getX()[i][m]));
        					}
        				}
    				}
    				pw.print(solveLP.getObjval());
    				if(margin2 != 3) pw.print(",");
    			}
    			pw.println();
    		}
    		//ファイルの最終行に提案手法の目的関数値(総納期遅れ)を記録
    		pw.print("Proposed Method");
    		pw.print(",");
    		int count = 0;
            for(int margin=0;margin<=30;margin+=5) {
            	if(margin!=30) {
            		pw.print(this.objval[count]);
            		pw.print(",");
            	}else {
            		pw.print(this.objval[count]);
            	}
            	count++;
            }
    		pw.close();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		System.out.println(TrialNum+"回試行の作業者ランダム配置実験を終了");
	 }

}
