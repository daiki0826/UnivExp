public class All_Main {

	public static void main(String[] args) {
		
		//実験条件
		int J=3;
		int M=5;
		
		/*
		 * 入力ファイルパス定義
		 *	・ジョブ条件フォルダの絶対パス
		 *	・作業者条件フォルダの絶対パス
		 */
		String jobSetPath = "/Users/nagatadaiki/ExpData/JobSet/Data3";
		String workerSetPath = "/Users/nagatadaiki/ExpData/WorkerSet/5Worker/Set1";
		
		/*
		 * 実験データパス定義
		 * ・一回の実験データ出力などを包括するディレクトリ
		 */
		String  expDirectoryPath = "/Users/nagatadaiki/ExpData/exp2";
		
		/*
		 * 実験実行
		 */
		Experiment exp = new Experiment(jobSetPath, workerSetPath, expDirectoryPath, J, M);
//		exp.makeLP_WA();
//		exp.solveLP_WA();
		exp.makeLP();
		exp.solveLP();
		

		
		
//		//ジョブデータディレクトリパス
//		String DataSetpath = "/Users/nagatadaiki/ExpData/JobSet/Data3/";
//		Condition condition = new Condition(J, M, DataSetpath);
//		for(int j=1;j<=J;j++) {
//			System.out.println(String.format("ジョブ%dの納期は%fです",j,condition.getDue()[j]));
//		}
		
		// MakeLP makeLP = new MakeLP(condition);
		// SolveLP solveLP = new SolveLP("/Users/nagatadaiki/ExpData/Data2/jobshop.lp",condition);

		//作業者セットデータディレクトリパス
//		String WorkerDataPath = "/Users/nagatadaiki/ExpData/WorkerSet/5Worker/Set1";
//		WorkerCondition workerCon = new WorkerCondition(WorkerDataPath, M);
//		MakeLP_WA makeLP_WA = new MakeLP_WA(workerCon,condition);
//		SolveLP_WA solveLP_WA = new SolveLP_WA("/Users/nagatadaiki/ExpData/exp1/worker.lp",condition);
		
		
	}

}
