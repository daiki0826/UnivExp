import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;

/*
 * 機械に対する作業者配置をランダムに行った上で(前提で)スケジューリング問題のLPファイルを作成
 */
public class MakeLP_RandomWA {
	
	private int[][] x;
	private double[][] Myu;
	private double[][] Sig;
	

	 public MakeLP_RandomWA(Condition jobCon,WorkerCondition workCon,int trial){
	  
			//変数の定義
			int j,k,p,q,PM1,PM2;
			int Count = 1; //制約式を数える
			int L = 10000; //BigM法
			//ジョブ数を条件クラス「Condition」から取得
			int J = jobCon.getJobNum();
			//機械台数を条件クラス「Condition」から取得
			int M = jobCon.getMachineNum();
			//ジョブ毎の工程数を条件クラス「Condition」から取得
			int[] Kj= jobCon.getProcessNum();
			//ジョブの納期を条件クラス「Condition」から取得
			Double[] D = jobCon.getDue();
			//ランダムに作業者配置を決定
			this.randomWA();
			
			//ランダムに決定した配置でμ及びσを決定
			this.setMyu_Sig(x, jobCon, workCon);
			String expPath = Constant.expPath+"/RandomWA/"+String.valueOf(trial)+"試行"; //ディレクトリへのパス
			File jobSetFile = new File(expPath);
			if(jobSetFile.mkdir()) {
				System.out.println("作業者ランダム配置実験ディレクトリの作成に成功しました");
			}
			
			/*
			 * LPファイル作成(許容乖離の大きさ変更)
			 */
			for(int margin=0;margin<=30;margin+=10) {//許容乖離の大きさを0σ~3σ(0.5刻み)で変更した
				double margin2 = (double)margin/10;
				try {
					//LPファイルパス指定
					PrintWriter lp = new PrintWriter(new FileWriter(new File(Constant.expPath+"/RandomWA/"+String.valueOf(trial)+"試行"+"/jobshop_"+String.valueOf(margin2)+"σ_"+String.valueOf(trial)+".lp")));
					
					/**
					 * 目的関数
					 */
					lp.println("minimize"); //目的関数最小化

					lp.println("\\目的関数(1)");//目的関数は総納期遅れ最小化
					lp.print("OBJ:");
					for(j=1;j<=J;j++) {
						lp.print("T_"+j);
						if(j != J)
							lp.print(" + ");
					}
					lp.println();

					/**
					 * 制約式
					 */
					lp.println("subject to");
					
					//処理開始時刻ST>=0の制約
					for(j=1;j<=J;j++) {
						for(k=1;k<=Kj[j];k++) {
							lp.println("_C"+Count+": ST_"+j+"_"+k+">="+0);
							Count++;
						}
					}

					//処理完了時刻CT>=0の制約
					for(j=1;j<=J;j++) {
						for(k=1;k<=Kj[j];k++) {
							lp.println("_C"+Count+": CT_"+j+"_"+k+">="+0);
							Count++;
						}
					}

					//決定変数y>=0の制約
					for(j=1;j<=J;j++) {
						for(p=1;p<=J;p++) {
							for(k=1;k<=Kj[j];k++) {
								for(q=1;q<=Kj[p];q++) {
									lp.println("_C"+Count+": y_"+j+"_"+p+"_"+k+"_"+q+">="+0);
									Count++;
								}
							}
						}
					}

					//納期遅れ定義(最終工程の終了時刻CT-納期遅れT =　納期D
					lp.println("\\各ジョブの納期遅れ定義");
					for(j=1;j<=J;j++) {
						lp.println("_C"+Count+": T_"+j+"-CT_"+j+"_"+Kj[j]+"> -"+D[j]);
						//lp.println("_C"+Count+": CT_"+j+Kj[j]+" - T_"+j+"> -"+D[j]);
						Count++;
					}

					//処理完了時刻定義
					lp.println("\\ジョブの各工程の終了時刻定義");
					for(j=1;j<=J;j++) {
						for(k=1;k<=Kj[j];k++) {
							double PT = this.Myu[j][k]+margin2*this.Sig[j][k]; //marginの値で余力の大きさ決定
							lp.println("_C"+Count+": CT_"+j+"_"+k+"- ST_"+j+"_"+k+"="+PT);
							//lp.println("_C"+Count+": CT_"+j+"_"+k+"- ST_"+j+"_"+k+"="+con.getPT(j,k));
							Count++;
						}
					}

					//先行関係制約
					lp.println("\\工程間の先行関係制約");
					for(j=1;j<=J;j++) {
						for(k=1;k<=Kj[j]-1;k++) {
							lp.println("_C"+Count+": ST_"+j+"_"+(k+1)+"- CT_"+j+"_"+k+">="+0);
							Count++;
						}
					}

//					//機械能力制約(1)アロー演算子で表現
//					lp.println("\\機械能力制約(1)");
//					for(j=1;j<=J;j++) {
//						for(p=(j+1);p<=J;p++) {
//							for(k=1;k<=Kj[j];k++) {
//								PM1=con.getPM(j,k);
//								for(q=1;q<=Kj[p];q++) {
//									PM2 = con.getPM(p, q);
//									if(PM1==PM2) {
//										lp.println("_C"+Count+": y_"+j+"_"+p+"_"+k+"_"+q+"=0 -> ST_"+j+"_"+k+" - CT_"+p+"_"+q+">=0");
//										Count++;
//									}
//								}
//							}
//						}
//					}
					
					//機械能力制約(1)アロー演算子で表現
					lp.println("\\機械能力制約(1)");
					for(j=1;j<=J;j++) {
						for(k=1;k<=Kj[j];k++) {
							PM1=jobCon.getPM(j,k);
							for(p=j+1;p<=J;p++) {
								for(q=1;q<=Kj[p];q++) {
									PM2 = jobCon.getPM(p, q);
									if(PM1==PM2) {
										lp.println("_C"+Count+": y_"+j+"_"+p+"_"+k+"_"+q+"=0 -> ST_"+j+"_"+k+" - CT_"+p+"_"+q+">=0");
										Count++;
									}
								}
							}
						}
					}
					
//					//機械能力制約(2)アロー演算子で表現
//					lp.println("\\機械能力制約(2)");
//					for(j=1;j<=J;j++) {
//						for(p=(j+1);p<=J;p++) {
//							for(k=1;k<=Kj[j];k++) {
//								PM1=con.getPM(j,k);
//								for(q=1;q<=Kj[p];q++) {
//									PM2 = con.getPM(p, q);
//									if(PM1==PM2) {
//										lp.println("_C"+Count+": y_"+j+"_"+p+"_"+k+"_"+q+"=1 -> ST_"+p+"_"+q+" - CT_"+j+"_"+k+">=0");
//										Count++;
//									}
//								}
//							}
//						}
//					}
					
					//機械能力制約(2)アロー演算子で表現
					lp.println("\\機械能力制約(2)");
					for(j=1;j<=J;j++) {
						for(k=1;k<=Kj[j];k++) {
							PM1=jobCon.getPM(j,k);
							for(p=j+1;p<=J;p++) {
								for(q=1;q<=Kj[p];q++) {
									PM2 = jobCon.getPM(p, q);
									if(PM1==PM2) {
										lp.println("_C"+Count+": y_"+j+"_"+p+"_"+k+"_"+q+"=1 -> ST_"+p+"_"+q+" - CT_"+j+"_"+k+">=0");
										Count++;
									}
								}
							}
						}
					}

					// //機械能力制約(1)bigMで表現
					// lp.println("\\機械能力制約(1)");
					// for(j=1;j<=J;j++) {
					// 	for(p=(j+1);p<=J;p++) {
					// 		for(k=1;k<=Kj[j];k++) {
					// 			PM1=con.getPM(j,k);
					// 			for(q=1;q<=Kj[p];q++) {
					// 				PM2 = con.getPM(p, q);
					// 				if(PM1==PM2) {
					// 					lp.println("_C"+Count+": ST_"+j+"_"+k+"- CT_"+p+"_"+q+"+"+L+"Y_"+j+"_"+k+"_"+p+"_"+q+">="+0);
					// 					Count++;
					// 				}
					// 			}
					// 		}
					// 	}
					// }
				
					// //機械能力制約(2)bigM法で表現
					// lp.println("\\機械能力制約(2)");
					// for(j=1;j<=J;j++) {
					// 	for(p=(j+1);p<=J;p++) {
					// 		for(k=1;k<=Kj[j];k++) {
					// 			PM1=con.getPM(j,k);
					// 			for(q=1;q<=Kj[p];q++) {
					// 				PM2 = con.getPM(p,q);
					// 				if(PM1==PM2) {
					// 					lp.println("_C"+Count+": ST_"+p+"_"+q+"- CT_"+j+"_"+k+"-"+L+"Y_"+j+"_"+k+"_"+p+"_"+q+">="+(-L));
					// 					Count++;
					// 				}
					// 			}
					// 		}
					// 	}
					// }
					
					/**
					 * 変数範囲設定
					 */
					lp.println("Bounds");

					//処理開始時刻範囲
					lp.println("\\処理開始時刻範囲");
					for(j=1;j<=J;j++) {
						for(k=1;k<=Kj[j];k++) {
							lp.println(0+"<=ST_"+j+"_"+k+"<="+1000);
						}
					}
					lp.println();

					//処理終了時刻範囲
					lp.println("\\処理完了時刻範囲");
					for(j=1;j<=J;j++) {
						for(k=1;k<=Kj[j];k++) {
							lp.println(0+"<=CT_"+j+"_"+k+"<="+1000);
							Count++;
						}
					}
					lp.println();
					
					/**
					 * 変数型
					 */
					lp.println("binary");
				
					//決定変数y
					lp.println("\\先行関係決定変数");
					for(j=1;j<=J;j++) {
						for(p=1;p<=J;p++) {
							for(k=1;k<=Kj[j];k++) {
								for(q=1;q<=Kj[p];q++) {
									lp.println("y_"+j+"_"+p+"_"+k+"_"+q);
								}
							}
						}
					}
					lp.println();
					lp.println("end");
					lp.close();
					System.out.println("LPファイル出力完了");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
	 }
	 
	 
	 //作業者配置をランダムに決定
	 public void randomWA() {
		 int M = Constant.M;
		 this.x = new int[M+1][M+1];
		 ArrayList<Integer> temp = new ArrayList<Integer>();
		 for(int i=1;i<=M;i++) {
			 temp.add(i);
		 }
		 //作業者順番をシャッフルしてランダム配置にする
		 for(int count = 1;count<=M;count++){
			 Collections.shuffle(temp);
		 }
		 for(int i=1;i<=M;i++) {
			 for(int m=1;m<=M;m++) {
				 if(temp.get(m-1)==i) {
					 x[i][m] = 1;
				 }else {
					 x[i][m] = 0;
				 }
			 }
		 }
	 }
	 
	 //作業平均時間と作業時間標準偏差を決定
	 public void setMyu_Sig(int[][] x,Condition jobCon,WorkerCondition workCon) {
		 int M = Constant.M;
		 int J = Constant.J;
		 int[] Kj = jobCon.getProcessNum();
		 this.Myu = new double[J+1][M+1];
		 this.Sig = new double[J+1][M+1];
		 for(int i=1;i<=M;i++) {
			 for(int m=1;m<=M;m++) {
				 for(int j=1;j<=J;j++) {
					 for(int k=1;k<=Kj[j];k++) {
						 if(jobCon.getPM(j, k) == m&&x[i][m]==1) {
							 this.Myu[j][k] = jobCon.getPT(j, k)*workCon.getAlpha(i, m);
							 this.Sig[j][k] = jobCon.getPT(j, k)*0.2*workCon.getBeta(i, m);
						 }
					 }
				 }
			 }
		 }
	 }
	 
	 //ランダムに決定した作業者配置を取得
	 public int[][] getX() {
		 return this.x;
	 }
	 
	 //ランダムに配置した場合の作業時間標準偏差の総和を取得
	 public double get_Sum_sigma() {
		 double sum_sigma = 0;
		 for(int j=1;j<=Constant.J;j++) {
			 for(int k=1;k<=Constant.M;k++) {
				 sum_sigma = sum_sigma + this.Sig[j][k];
			 }
		 }
		 return sum_sigma;
	 }
	
}
