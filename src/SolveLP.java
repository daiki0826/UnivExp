import ilog.concert.IloException;
import ilog.concert.IloLPMatrix;
import ilog.cplex.IloCplex;


public class SolveLP {
    private double[][] GUNTST;	//ジョブの工程ごとの処理開始時刻
	private double[][] GUNTCT;	//ジョブの工程ごとの処理完了時刻
	private int J; //ジョブ数
	private int M; //機械台数
	private int I; //作業者数
	private int[] Kj; //各ジョブの工程数
	private Condition condition; //作業者配置を決定するための入力データとなる各ジョブの条件

	public SolveLP(String file, Condition condition) {

		//出力される変数の数を数える
    	int Count = 0;
		this.J = condition.getJobNum();
		this.M = condition.getMachineNum();
		this.I = condition.getMachineNum();
    	this.Kj = condition.getProcessNum();
    	this.condition = condition;

		GUNTST = new double[J+1][M+1];
		GUNTCT = new double[J+1][M+1];



		try {
			IloCplex cplex=new IloCplex();

			cplex.setParam(IloCplex.IntParam.MIPDisplay, 0);

			//解くlpファイル指定
			cplex.importModel(file); 
			//lpファイルを解く
			cplex.solve();	
			
			//目的関数の値を取得
			double objval = cplex.getObjValue();
			System.out.println("目的関数 :"+objval);
			//出た解をxに前から順番に出力
			IloLPMatrix lp = (IloLPMatrix) cplex.LPMatrixIterator().next();
			double[] x=(cplex.getValues(lp));
			int[] value=new int[50000];   //cplexで解いた値
			String[] name=new String[50000];  //cplexで解いた解の変数名
			double v= cplex.getObjValue();


			for (int i = 0; i < x.length; i++) {
				name[i]=lp.getNumVar(i).getName();
				value[i]=(int) Math.round(x[i]);

				System.out.print(" "+name[i]+" = ");
				System.out.println(" "+value[i]);
			}

			//ジョブj工程kの処理開始時刻及び処理完了時刻をソルバーから取得
			for(int j=1;j<=J;j++) {
				for(int k=1;k<=Kj[j];k++) {
					GUNTST[j][k] = value[Count];
					GUNTCT[j][k] = value[Count]+condition.getPT(j,k);
					Count++;
				}
			}
			System.out.println();

			
			for(int j=1;j<=J;j++) {
				for(int p=1;p<=J;p++) {
					for(int k=1;k<=Kj[j];k++) {
						for(int q=1;q<=Kj[j];q++) {
							System.out.println("y_"+j+"_"+p+"_"+k+"_"+q+" = "+value[Count]);
								Count++;
						}
					}
				}
			}
			System.out.println();

			cplex.end();



		} catch (IloException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}	//変数宣言
	}



	public double[][] getGUNTST() {
		return this.GUNTST;
	}


	public double[][] getGUNTCT() {
		return this.GUNTCT;
	}
}
