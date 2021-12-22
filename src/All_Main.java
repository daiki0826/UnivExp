public class All_Main {

	public static void main(String[] args) {
		
		int J=3;
		int M=3;
		
		//データセットを作成
		
		//ジョブデータディレクトリパス
		String DataSetpath = "/Users/nagatadaiki/ExpData/Data2/Condition/";
		Condition condition = new Condition(J, M, DataSetpath);
		for(int j=1;j<=J;j++) {
			System.out.println(String.format("ジョブ%dの納期は%fです",j,condition.getDue()[j]));
		}
		
		// MakeLP makeLP = new MakeLP(condition);
		// SolveLP solveLP = new SolveLP("/Users/nagatadaiki/ExpData/Data2/jobshop.lp",condition);

		//作業者セットデータディレクトリパス
		String WorkerDataPath = "/Users/nagatadaiki/ExpData/WorkerSet/3Worker/Set1";
		WorkerCondition workerCon = new WorkerCondition(WorkerDataPath, M);
		MakeLP_WA makeLP_WA = new MakeLP_WA(workerCon,condition);
		SolveLP_WA solveLP_WA = new SolveLP_WA("/Users/nagatadaiki/ExpData/Data2/worker.lp");
		
		
		
	}

}
