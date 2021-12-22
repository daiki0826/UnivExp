import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


public class MakeLP {

    public MakeLP(Condition con){
        //書き出すlpファイルの定義
			
			try {
				PrintWriter lp = new PrintWriter(new FileWriter(new File("/Users/nagatadaiki/ExpData/Data2/jobshop.lp")));
				//変数の定義
				int j,k,p,q,PM1,PM2;
				//ジョブ数を条件クラス「Condition」から取得
				int J = con.getJobNum();
				//機械台数を条件クラス「Condition」から取得
				int M = con.getMachineNum();
				//ジョブ毎の工程数を条件クラス「Condition」から取得
				int[] Kj= con.getProcessNum();
				//ジョブの納期を条件クラス「Condition」から取得
				Double[] D = con.getDue();
				
				for(j=1;j<=J;j++){
					System.out.println("ジョブ"+j+"は"+Kj[j]+"工程です\n");
					for(k=1;k<=Kj[j];k++){
						System.out.println("工程"+k+"の作業時間は"+con.getPT(j, k)+"です\n");
					}
				}
				int Count = 1; //制約式を数える
				int L = 10000; //BigM法

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
						lp.println("_C"+Count+": CT_"+j+"_"+k+"- ST_"+j+"_"+k+"="+con.getPT(j,k));
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

//				//機械能力制約(1)アロー演算子で表現
//				lp.println("\\機械能力制約(1)");
//				for(j=1;j<=J;j++) {
//					for(p=(j+1);p<=J;p++) {
//						for(k=1;k<=Kj[j];k++) {
//							PM1=con.getPM(j,k);
//							for(q=1;q<=Kj[p];q++) {
//								PM2 = con.getPM(p, q);
//								if(PM1==PM2) {
//									lp.println("_C"+Count+": y_"+j+"_"+p+"_"+k+"_"+q+"=0 -> ST_"+j+"_"+k+" - CT_"+p+"_"+q+">=0");
//									Count++;
//								}
//							}
//						}
//					}
//				}
				
				//機械能力制約(1)アロー演算子で表現
				lp.println("\\機械能力制約(1)");
				for(j=1;j<=J;j++) {
					for(k=1;k<=Kj[j];k++) {
						PM1=con.getPM(j,k);
						for(p=j+1;p<=J;p++) {
							for(q=1;q<=Kj[p];q++) {
								PM2 = con.getPM(p, q);
								if(PM1==PM2) {
									lp.println("_C"+Count+": y_"+j+"_"+p+"_"+k+"_"+q+"=0 -> ST_"+j+"_"+k+" - CT_"+p+"_"+q+">=0");
									Count++;
								}
							}
						}
					}
				}
				
//				//機械能力制約(2)アロー演算子で表現
//				lp.println("\\機械能力制約(2)");
//				for(j=1;j<=J;j++) {
//					for(p=(j+1);p<=J;p++) {
//						for(k=1;k<=Kj[j];k++) {
//							PM1=con.getPM(j,k);
//							for(q=1;q<=Kj[p];q++) {
//								PM2 = con.getPM(p, q);
//								if(PM1==PM2) {
//									lp.println("_C"+Count+": y_"+j+"_"+p+"_"+k+"_"+q+"=1 -> ST_"+p+"_"+q+" - CT_"+j+"_"+k+">=0");
//									Count++;
//								}
//							}
//						}
//					}
//				}
				
				//機械能力制約(2)アロー演算子で表現
				lp.println("\\機械能力制約(2)");
				for(j=1;j<=J;j++) {
					for(k=1;k<=Kj[j];k++) {
						PM1=con.getPM(j,k);
						for(p=j+1;p<=J;p++) {
							for(q=1;q<=Kj[p];q++) {
								PM2 = con.getPM(p, q);
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
