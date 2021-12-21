import ilog.concert.IloException;
import ilog.concert.IloLPMatrix;
import ilog.cplex.IloCplex;
import java.io.IOException;


public class SolveLp {
    int[][] GUNTST;				//ジョブの工程ごとの処理開始時刻
	int[][] GUNTCT;				//ジョブの工程ごとの処理完了時刻

	private Result result;

	public SolveLP(String file, Condition condition) {

		int i,count;
		int j,k,m,j1,j2,k1,k2;
		int J = condition.getJobNum();
		int K = condition.getMachineNum();

		GUNTST = new int[J+1][K+1];
		GUNTCT = new int[J+1][K+1];



		try {
			IloCplex cplex=new IloCplex();

			cplex.setParam(IloCplex.IntParam.MIPDisplay, 0);
			//cplex.setParam(IloCplex.IntParam.IntSolLim ,0);

			//cplex.setParam(IloCplex.);




			cplex.importModel(file); //解くファイル指定
			cplex.solve();	//lpファイルを解く

			//実行可能解が存在
			//出た解をxに前から順番に出力
			IloLPMatrix lp = (IloLPMatrix) cplex.LPMatrixIterator().next();
			double[] x=(cplex.getValues(lp));
			int[] value=new int[50000];   //cplexで解いた値
			String[] name=new String[50000];  //cplexで解いた解の変数名
			double v= cplex.getObjValue();


			for (i = 0; i < x.length; i++) {
				name[i]=lp.getNumVar(i).getName();
				value[i]=(int) Math.round(x[i]);

				System.out.print(" "+name[i]+" = ");
				System.out.println(" "+value[i]);
			}

			//result.setName(name);
			//result.setValue(value);
			System.out.println();
			System.out.println("x.length = "+x.length);
			System.out.println("SUM_L = "+v);

			count=0;
			int sumL=0;
			for(j=1;j<=J;j++) {
				System.out.println("L_"+j+" = "+value[count]);
				sumL += x[count];
				count++;
			}
			System.out.println();

			for(j=1;j<=J;j++) {
				for(k=1;k<=K;k++) {
					GUNTST[j][k] = value[count];
					GUNTCT[j][k] = value[count]+condition.getPT(j, k);

					count++;
				}
			}
			System.out.println();

			
			for(j1=1;j1<=J;j1++) {
				for(j2=(j1+1);j2<=J;j2++) {
					for(k1=1;k1<=K;k1++) {
						m = condition.get_m_n(j1,k1);
						for(k2=1;k2<=K;k2++) {
							if(m==condition.get_m_n(j2,k2)) {
								//System.out.println("y_"+j1+"_"+k1+"_"+j2+"_"+k2+" = "+value[count]);
								count++;
							}
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



	public int[][] getGUNTBT() {
		return this.GUNTBT;
	}


	public int[][] getGUNTCT() {
		return this.GUNTCT;
	}
}
