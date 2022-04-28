package JSPWA;
import ilog.concert.IloException;
import ilog.concert.IloLPMatrix;
import ilog.cplex.IloCplex;

public class SolveLP {
	private double objval; //計算結果の目的関数
	private double[] Tj;	//各ジョブの納期遅れ
    private double[][] GUNTST;	//ジョブの工程ごとの処理開始時刻
	private double[][] GUNTCT;	//ジョブの工程ごとの処理完了時刻
	private double[][] PT; //ジョブj工程kの処理時間
	private double[][][] x; //決定変数xの値
	private double[][][][] y; //決定変数yの値
	
	private int J; //ジョブ数
	private int M; //機械台数
	private int I; //作業者数
	private int[] Kj; //各ジョブの工程数
	private Condition condition; //作業者配置を決定するための入力データとなる各ジョブの条件

	public SolveLP(String LPfile) {

		//出力される変数の数を数える
    	int Count = 0;
    	this.condition = Constant.jobCon;
		this.J = Constant.J;
		this.M = Constant.M;
		this.I = Constant.M;
    	this.Kj = this.condition.getProcessNum();
    	Tj = new double[J+1];
		GUNTST = new double[J+1][M+1];
		GUNTCT = new double[J+1][M+1];
		PT = new double[J+1][M+1];
		x = new double[I+1][J+1][M+1];
		y = new double[J+1][J+1][M+1][M+1];


		try {
			IloCplex cplex=new IloCplex();

			cplex.setParam(IloCplex.IntParam.MIPDisplay, 0);

			//解くlpファイル指定
			cplex.importModel(LPfile); 
			//lpファイルを解く
			long start = System.currentTimeMillis();
			cplex.solve();	
			long end = System.currentTimeMillis();
			System.out.println("計算時間 = "+(end-start));
			//目的関数の値を取得
			double objval = cplex.getObjValue();
			this.objval = objval;
			System.out.println("目的関数 :"+objval);
			//LogFile.writeLog(Constant.expPath, "Scheduling_OBJValue = "+objval);
			LogFile.writeLog(Constant.expPath, "計算時間 : "+(end-start));
			//出た解をxに前から順番に出力
			IloLPMatrix lp = (IloLPMatrix) cplex.LPMatrixIterator().next();
			double[] x=(cplex.getValues(lp));
			int[] value=new int[50000];   //cplexで解いた値
			String[] name=new String[50000];  //cplexで解いた解の変数名
			

			for (int i = 0; i < x.length; i++) {
				name[i]=lp.getNumVar(i).getName();
				value[i]=(int) Math.round(x[i]);
//
				System.out.print(" "+name[i]+" = ");
				System.out.println(" "+value[i]);
				//LogFile.writeLog(expDirectoryPath," "+name[i]+" = "+value[i]);
			}
			
			//各ジョブの納期遅れをソルバーから取得
			for(int j=1;j<=J;j++) {
				this.Tj[j] = value[Count];
				Count++;
			}
			//ジョブj工程kの処理開始時刻をソルバーから取得
			for(int j=1;j<=J;j++) {
				for(int k=1;k<=Kj[j];k++) {
					this.GUNTST[j][k] = value[Count];
					Count++;
					//System.out.println("ジョブ"+j+"工程"+k+"の処理開始時刻="+GUNTST[j][k]);
				}
			}
			System.out.println();

			//ジョブj工程kの処理完了時刻をソルバーから取得
			for(int j=1;j<=J;j++) {
				for(int k=1;k<=Kj[j];k++) {
					this.GUNTCT[j][k] = value[Count];
					Count++;
					//System.out.println("ジョブ"+j+"工程"+k+"の処理完了時刻="+GUNTCT[j][k]);
				}
			}
			System.out.println();
			
			
			//ジョブj工程kの処理時間PTをソルバーより取得
			for(int j=1;j<=J;j++) {
				for(int k=1;k<=Kj[j];k++) {
					this.PT[j][k] = value[Count];
					Count++;
					//System.out.println("ジョブ"+j+"工程"+k+"の処理完了時刻="+GUNTCT[j][k]);
				}
			}
			System.out.println();
			
			//決定変数xの値を取得
			for(int i=1;i<=I;i++) {
				for(int j=1;j<=J;j++) {
					for(int k=1;k<=Constant.M;k++) {
						this.x[i][j][k] = value[Count];
						Count++;
					}
				}
			}
			
			
			
			//決定変数yの値を取得
			for(int j=1;j<=J;j++) {
				for(int p=1;p<=J;p++) {
					for(int k=1;k<=Kj[j];k++) {
						for(int q=1;q<=Kj[j];q++) {
							//System.out.println("y_"+j+"_"+p+"_"+k+"_"+q+" = "+value[Count]);
							this.y[j][p][k][q] = value[Count];
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
	

	
	public double getObjval() {
		return this.objval;
	}

	public double[] getTj() {
		return this.Tj;
	}
	
	public double[][] getGUNTST() {
		return this.GUNTST;
	}


	public double[][] getGUNTCT() {
		return this.GUNTCT;
	}
	
	public double[][] getPT(){
		return this.PT;
	}
	
	public double[][][] getX(){
		return this.x;
	}
	
	public double[][][][] getY(){
		return this.y;
	}

}
