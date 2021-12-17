import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class MakeLP {

	/**
	 * 
	 * LPファイル作成クラス
	 * 
	 */
	public  MakeLP(Condition con) {
		try {
			//書き出すlpファイルの定義
			PrintWriter lp=new PrintWriter(new FileWriter(new File("/Users/nagatadaiki/ExpData/Data1/jobshop.lp")));
			//変数の定義
			int j,k,j1,j2,k1,k2,PM1,PM2;
			//ジョブ数を条件クラス「Condition」から取得
			int J = con.getJobNum();
			//機械台数を条件クラス「Condition」から取得
			int M = con.getMachineNum();
			//ジョブ毎の工程数を条件クラス「Condition」から取得
			int []Kj= con.getProcessNum();
			//ジョブの納期を条件クラス「Condition」から取得
			Double[] D = con.getDue();
			
		    int Count = 1; //制約式を数える
		    M = 10000; //BigM法
		    
		    
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
			lp.print("_C"+Count);
			for(j=1;j<=J;j++) {
				for(k=1;k<=Kj[j];k++) {
					lp.println("_C"+Count+": CT_"+j+"_"+k+">="+0);
					Count++;
				}
			}
			
			//決定変数y>=0の制約
			for(j=1;j<=J;j++) {
				for(k=1;k<=Kj[j];k++) {
					for(j2=1;j2<=J;j++) {
						for(k2=1;k2<=Kj[j2];k2++) {
							lp.println("_C"+Count+": y_"+j+"_"+k+"_"+j2+"_"+k2+">="+0);
							Count++;
						}
					}
					
				}
			}
			
			//納期遅れ定義(最終工程の終了時刻CT-納期遅れT =　納期D
			lp.println("\\各ジョブの納期遅れ定義");
			for(j=1;j<=J;j++) {
				lp.println("_C"+Count+": CT_"+j+Kj[j]+" - T_"+j+"> -"+D[j]);
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
			
			//機械能力制約(1)アロー演算子で表現
			lp.println("\\機械能力制約(1)");
			for(j1=1;j1<=J;j++) {
				for(j2=(j1+1);j2<=J;j++) {
					for(k1=1;k1<=Kj[j1];j++) {
						PM1=con.getPM(j1,k1);
						for(k2=1;k2<=Kj[j2];k2++) {
							PM2 = con.getPM(j2, k2);
							if(PM1==PM2) {
								lp.println("_C"+Count+": y_"+j1+"_"+j2+"_"+k1+"_"+k2+"=0 -> ST_"+j1+"_"+k1+" - CT_"+j2+"_"+k2+">=0");
								Count++;
							}
						}
					}
				}
			}
			
			//機械能力制約(2)アロー演算子で表現
			lp.println("\\機械能力制約(1)");
			for(j1=1;j1<=J;j++) {
				for(j2=(j1+1);j2<=J;j++) {
					for(k1=1;k1<=Kj[j1];j++) {
						PM1=con.getPM(j1,k1);
						for(k2=1;k2<=Kj[j2];k2++) {
							PM2 = con.getPM(j2, k2);
							if(PM1==PM2) {
								lp.println("_C"+Count+": y_"+j1+"_"+j2+"_"+k1+"_"+k2+"=1 -> ST_"+j2+"_"+k2+" - CT_"+j1+"_"+k1+">=0");
								Count++;
							}
						}
					}
				}
			}
			
			//機械能力制約(1)bigMで表現
			lp.println("\\機械能力制約(1)");
			for(j1=1;j1<=J;j++) {
				for(j2=(j1+1);j2<=J;j++) {
					for(k1=1;k1<=Kj[j1];j++) {
						PM1=con.getPM(j1,k1);
						for(k2=1;k2<=Kj[j2];k2++) {
							PM2 = con.getPM(j2, k2);
							if(PM1==PM2) {
								lp.println("_C"+Count+": ST_"+j1+"_"+k1+"- CT_"+j2+"_"+k2+"+"+M+"Y_"+j1+"_"+k1+"_"+j2+"_"+k2+">="+0);
								Count++;
							}
						}
					}
				}
			}
			
			//機械能力制約(2)bigM法で表現
			lp.println("\\機械能力制約(2)");
			for(j1=1;j1<=J;j++) {
				for(j2=(j1+1);j2<=J;j++) {
					for(k1=1;k1<=Kj[j1];j++) {
						PM1=con.getPM(j1,k1);
						for(k2=1;k2<=Kj[j2];k2++) {
							PM2 = con.getPM(j2, k2);
							if(PM1==PM2) {
								lp.println("_C"+Count+": ST_"+j2+"_"+k2+"- CT_"+j1+"_"+k1+"-"+M+"Y_"+j1+"_"+k1+"_"+j2+"_"+k2+">="+(-M));
								Count++;
							}
						}
					}
				}
			}
			

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
			 * バイナリー変数
			 */
			lp.println("binary");

			//決定変数y
			lp.println("\\処理開始時刻設定");
			for(j1=1;j1<=J;j1++) {
				for(j2=1;j2<=J;j2++) {
					for(k1=1;k1<=Kj[j1];k1++) {
						for(k2=1;k2<=Kj[j2];j2++) {
							lp.println("y_"+j1+"_"+j2+"_"+k1+"_"+k2);
						}
					}
				}
			}
			lp.println();
			lp.println("end");

			lp.close();


		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

	}
	
}
