import gurobi.GRB;
import gurobi.GRBEnv;
import gurobi.GRBException;
import gurobi.GRBModel;


public class SolveLP_gurobi {
	private double objval; //計算結果の目的関数
    private double[][] GUNTST;	//ジョブの工程ごとの処理開始時刻
	private double[][] GUNTCT;	//ジョブの工程ごとの処理完了時刻
	private double[] Tj;	//各ジョブの納期遅れ
	private int J; //ジョブ数
	private int M; //機械台数
	private int I; //作業者数
	private int[] Kj; //各ジョブの工程数
	private Condition condition; //作業者配置を決定するための入力データとなる各ジョブの条件

	public SolveLP_gurobi(String LPfile) {

		//出力される変数の数を数える
//    	int Count = 0;
//    	this.condition = Constant.jobCon;
//		this.J = Constant.J;
//		this.M = Constant.M;
//		this.I = Constant.M;
//    	this.Kj = this.condition.getProcessNum();
//		GUNTST = new double[J+1][M+1];
//		GUNTCT = new double[J+1][M+1];
//		Tj = new double[J+1];


		try {
			GRBEnv env = new GRBEnv();
			GRBModel model = new GRBModel(env,LPfile);
			model.optimize();
			double ovjval = model.get(GRB.DoubleAttr.ObjVal);
			System.out.println("目的関数="+ovjval);
			env.dispose();
		}catch (GRBException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}
	
//	public void simulation() {
//		Simulation sim = new Simulation(GUNTST, GUNTCT) ;
//		sim.runSimulation();
//	}


	public double getObjval() {
		return this.objval;
	}
	
	public double[][] getGUNTST() {
		return this.GUNTST;
	}


	public double[][] getGUNTCT() {
		return this.GUNTCT;
	}
	
	public double[] getTj() {
		return this.Tj;
	}
}