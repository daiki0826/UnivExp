import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import ilog.concert.IloException;
import ilog.concert.IloLPMatrix;
import ilog.cplex.IloCplex;

/*
 * cplexで解くクラス
 * 以下の3つの変数を取得する
 * ・作業者の機械割当を決定するx(i,m)
 * ・作業者が確定した状態でのジョブj工程kの作業時間平均
 * ・作業者が確定した状態でのジョブj工程kの作業時間標準偏差
 */
public class SolveLP_WA {
	
	private int[][] sol_x; //作業者iを機械mに割り当てるかどうか(バイナリー変数)
	private double[][][][] myu; //作業者iが機械mでジョブj工程kを処理する作業時間平均μ(i,m,j,k)
	private double[][][][] sig;	//作業者iが機械mでジョブj工程kを処理する作業時間標準偏差σ(i,m,j,k)
	private double[][] CV;  //ジョブj工程kの作業時間変動係数
	private double objval;
	private int J; //ジョブ数
	private int M; //機械台数
	private int I; //作業者数
	private int[] Kj; //各ジョブの工程数
	private Condition condition; //作業者配置を決定するための入力データとなる各ジョブの条件
	
    public SolveLP_WA(String file,Condition condition){
    	
    	//出力される変数の数を数える
    	int Count = 0;
    	//各種条件を取得
    	this.J = condition.getJobNum();
    	this.M = condition.getMachineNum();
    	this.I = condition.getMachineNum();
    	this.Kj = condition.getProcessNum();
    	this.condition = condition;
    	
    	//出力される解を保存
    	this.sol_x = new int[I+1][M+1];
    	this.myu = new double[M+1][M+1][J+1][M+1]; //[機械番号][ジョブ番号][工程番号]
    	this.sig = new double[M+1][M+1][M+1][M+1]; //[機械番号][ジョブ番号][工程番号]
    	this.CV = new double[J+1][M+1];
    	
    	
        try {
            IloCplex cplex=new IloCplex();
		    cplex.setParam(IloCplex.IntParam.MIPDisplay, 0);

            //解くlpファイル指定
			cplex.importModel(file); 
			//lpファイルを解く
			cplex.solve();
			
			IloLPMatrix lp = (IloLPMatrix) cplex.LPMatrixIterator().next();
			double[] x=(cplex.getValues(lp));
			double[] value=new double[50000];   //cplexで解いた値
			String[] name=new String[50000];  //cplexで解いた解の変数名
			
            //目的関数の値を取得
			this.objval = cplex.getObjValue();
			System.out.println("目的関数 :"+objval);
			
			//解をコンソールに出力して確認する
			System.out.println("x.length = "+x.length);
			for (int i = 0; i < x.length; i++) {
				name[i]=lp.getNumVar(i).getName();
				value[i]=x[i];

				System.out.print(" "+name[i]+" = ");
				System.out.println(" "+value[i]);
			}
			
			//決定変数xを取得
			for(int i=1;i<=I;i++) {
				for(int m=1;m<=M;m++) {
					this.sol_x[i][m] = (int)value[Count];
					System.out.println("決定変数x_"+i+"_"+m+" = "+this.sol_x[i][m]);
					Count++;
				}
			}
			System.out.println("決定変数xの解は"+Count+"まで");
			System.out.println();
		
			//作業時間平均μを取得
			for(int i=1;i<=I;i++) {
				for(int m=1;m<=M;m++) {
					for(int j=1;j<=J;j++) {
						for(int k=1;k<=Kj[j];k++) {
							this.myu[i][m][j][k] = value[Count];
							System.out.println("μ_"+i+"_"+m+"_"+j+"_"+k+" = "+this.myu[i][m][j][k]);
							Count++;
						}
					}
				}
			}
			System.out.println("作業時間平均の解は"+Count+"まで");
			System.out.println();
			
			//作業時間標準偏差σを取得
			for(int i=1;i<=I;i++) {
				for(int m=1;m<=M;m++) {
					for(int j=1;j<=J;j++) {
						for(int k=1;k<=Kj[j];k++) {
							this.sig[i][m][j][k] = value[Count];
							System.out.println("μ_"+i+"_"+m+"_"+j+"_"+k+" = "+this.sig[i][m][j][k]);
							Count++;
						}
					}
				}
			}
			System.out.println("作業時間標準偏差の解は"+Count+"まで");
			System.out.println();
			
			//作業時間変動係数CVを取得
			for(int i=1;i<=I;i++) {
				for(int m=1;m<=M;m++) {
					if(this.sol_x[i][m]==1) {//x_i_m=1の場合は二次元配列CVに作業時間変動係数として保存
						for(int j=1;j<=J;j++) {
							for(int k=1;k<=Kj[j];k++) {
								CV[j][k] = value[Count];
								System.out.println("CV_"+i+"_"+m+"_"+j+"_"+k+" = "+this.CV[j][k]);
								Count++;
							}
						}
					}else {//x_i_m=0の場合は保存せずにカウントだけ進める
						for(int j=1;j<=J;j++) {
							for(int k=1;k<=Kj[j];k++) {
								Count++;
							}
						}
					}
				}
			}
			System.out.println("作業時間変動係数の解は"+Count+"まで");
			System.out.println();
			
			//計算終了
			cplex.end();
            
        } catch (IloException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
    }
    
    //保存している決定変数xの解を取得
    public int[][] getSol_x() {
    	return this.sol_x;
    }
    
    //保存している作業時間平均μの値を取得
    public double[][][][] getMyu(){
    	return this.myu;
    }
    
    //保存している作業時間標準偏差σの値を取得
    public double[][][][] getSig(){
    	return this.sig;
    }
    
    
    /*
     * 作業者割当決定後の作業時間平均μと作業時間標準偏差σをcsvファイルに出力するメソッド
     */
    public void exportCSV(String JobSetPath, String destFilePath) {
    	
    	for(int j=1;j<=this.J;j++) {
    		try {
    			FileWriter fw = new FileWriter(destFilePath+"/Job"+j+".csv", false);
    			PrintWriter pw = new PrintWriter(new BufferedWriter(fw));
    			//ヘッダーの指定
	            pw.print("工程番号");
	            pw.print(",");
	            pw.print("処理機械番号");
	            pw.print(",");
	            pw.print("標準作業時間");
	            pw.print(",");
	            pw.print("作業者番号");
	            pw.print(",");
	            pw.print("平均作業時間");
	            pw.print(",");
	            pw.print("作業時間標準偏差");
	            pw.println();
	            
	            for(int k=1;k<=Kj[j];k++) {
	            	String processNum = String.valueOf(k);
	            	String machineNum = String.valueOf(this.condition.getPM(j, k));
	            	String processTime = String.valueOf(this.condition.getPT(j, k));
	            	String workerNum = "";
	            	String Myu = "";
	            	String Sig = "";
	            	int PM = condition.getPM(j, k);
	            	for(int i=1;i<=I;i++) {
	            		if(sol_x[i][PM]==1) {
	            			double myu = this.myu[i][PM][j][k];
	            			double sig = this.sig[i][PM][j][k];
	            			int worker = i;
	            			Myu = String.valueOf(myu);
	            			Sig = String.valueOf(sig);
	            			workerNum = String.valueOf(worker);
	            			System.out.println("μ("+PM+","+j+","+k+")="+Myu);
	            			System.out.println("sig("+PM+","+j+","+k+")="+Sig);
	            		}
	            	}
	            	pw.print(processNum);
	            	pw.print(",");
	            	pw.print(machineNum);
	            	pw.print(",");
	            	pw.print(processTime);
	            	pw.print(",");
	            	pw.print(workerNum);
	            	pw.print(",");
	            	pw.print(Myu);
	            	pw.print(",");
	            	pw.print(Sig);
	            	pw.println();
	            }
	            pw.close();
			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
    	}
    }
    
}
