import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class MakeLP_WA {

	public MakeLP_WA(WorkerCondition workerCon,Condition jobCon){

		 //動作確認用出力
		 for(int i=1;i<=3;i++){
            for(int m=1;m<=3;m++){
                double alpha=workerCon.getAlpha(i, m);
                double beta=workerCon.getBeta(i, m);
                System.out.println("作業者"+i+"の機械"+m+"に対するα="+alpha+",β="+beta+"です\n");
            }
        }
		
		try {
			//書き出すlpファイルの定義
			PrintWriter lp=new PrintWriter(new FileWriter(new File("/Users/nagatadaiki/ExpData/Data2/worker.lp")));
			//変数の定義
			int i,j,k,m,PM1,PM2;
			//ジョブ数を条件クラス「Condition」から取得
			int J = jobCon.getJobNum();
			//機械台数を条件クラス「Condition」から取得
			int M = jobCon.getMachineNum();
			//作業者人数は機械台数と同じ
			int I = jobCon.getMachineNum();
			//ジョブ毎の工程数を条件クラス「Condition」から取得
			int[] Kj= jobCon.getProcessNum();

			//制約式を数える
			int Count = 1; 

			/**
			 * 目的関数
			 */
			lp.println("minimize"); //目的関数最小化
			lp.println("\\目的関数(1)");//目的関数は総納期遅れ最小化
			lp.print("OBJ:");
			for(i=1;i<=I;i++){
				for(j=1;j<=J;j++){
					for(k=1;k<=Kj[j];k++){
						for(m=1;m<=M;m++){
							lp.print("CV_"+i+"_"+j+"_"+k+"_"+m+"x_"+i+"_"+m);
							if(i!= I||j!=J||k!=Kj[J]||m!=M){
								lp.print(" + ");
							}
						}
					}
				}
			}
			lp.println();

			/**
			 * 制約式
			 */
			lp.println("subject to");

			//決定変数x>=0の制約
			for(i=1;i<=I;i++){
				for(m=1;m<=M;m++){
					lp.println("_C"+Count+": x_"+i+"_"+m+">= 0");
					Count++;
				}
			}

			//作業時間平均μ>=0の制約
			for(i=1;i<=I;i++){
				for(j=1;j<=J;j++){
					for(k=1;k<=Kj[j];k++){
						for(m=1;m<=M;m++){
							lp.println("_C"+Count+": μ_"+i+"_"+j+"_"+k+"_"+m+">= 0");
							Count++;
						}
					}
				}
			}

			//作業時間標準偏差σ>=0の制約
			for(i=1;i<=I;i++){
				for(j=1;j<=J;j++){
					for(k=1;k<=Kj[j];k++){
						for(m=1;m<=M;m++){
							lp.println("_C"+Count+": sig_"+i+"_"+j+"_"+k+"_"+m+">= 0");
							Count++;
						}
					}
				}
			}

			//変動係数CV>=0の制約
			for(i=1;i<=I;i++){
				for(j=1;j<=J;j++){
					for(k=1;k<=Kj[j];k++){
						for(m=1;m<=M;m++){
							lp.println("_C"+Count+": CV_"+i+"_"+j+"_"+k+"_"+m+">= 0");
							Count++;
						}
					}
				}
			}
			

			//作業時間平均の定義(μ=PT*α)
			lp.println("\\作業時間平均定義");
			for(j=1;j<=J;j++){
				for(k=1;k<=Kj[j];k++){
					for(i=1;i<=I;i++){
						for(m=1;m<=M;m++){
							lp.println("_C"+Count+": μ_"+i+"_"+j+"_"+k+"_"+m+" = "+jobCon.getPT(j,k)*workerCon.getAlpha(i,m));
							Count++;
						}
					}
				}
			}

			//作業時間標準偏差の定義(σ=PT*0.2*β)
			lp.println("\\作業時間標準偏差定義");
			for(j=1;j<=J;j++){
				for(k=1;k<=Kj[j];k++){
					for(i=1;i<=I;i++){
						for(m=1;m<=M;m++){
							lp.println("_C"+Count+": sig_"+i+"_"+j+"_"+k+"_"+m+" = "+jobCon.getPT(j,k)*0.2*workerCon.getBeta(i,m));
							Count++;
						}
					}
				}
			}

			//作業時間変動係数CVの定義(CV=σ/μ)
			lp.println("\\作業時間変動係数定義");
			for(j=1;j<=J;j++){
				for(k=1;k<=Kj[j];k++){
					for(i=1;i<=I;i++){
						for(m=1;m<=M;m++){
							lp.println("_C"+Count+": CV_"+i+"_"+j+"_"+k+"_"+m+" = "+0.2*workerCon.getBeta(i,m)/workerCon.getBeta(i,m));
							Count++;
						}
					}
				}
			}

			//各機械に配置する作業者は1人の制約
			lp.println("\\作業者配置人数制約");
			for(m=1;m<=M;m++){
				lp.print("_C"+Count+":");
				for(i=1;i<=I;i++){
					lp.print("x_"+i+"_"+m);
					if(i!=I){
						lp.print(" + ");
					}
				}
				Count++;
				lp.println();
			}
			lp.println();
			
			//各作業者が使用する機械は1台
			lp.println("\\機械使用台数制約");
			for(i=1;i<=I;i++){
				lp.print("_C"+Count+":");
				for(m=1;m<=M;m++){
					lp.print("x_"+i+"_"+m);
					if(m!=M){
						lp.print(" + ");
					}
				}
				Count++;
				lp.println();
			}
			lp.println();
			
			/**
			* 変数範囲設定
			*/
			lp.println("Bounds");
			//作業時間平均範囲
			lp.println("\\作業時間平均範囲");
			for(i=1;i<=I;i++){
				for(j=1;j<=J;j++){
					for(k=1;k<=Kj[j];k++){
						for(m=1;m<=M;m++){
							lp.println("0<=μ_"+i+"_"+j+"_"+k+"_"+m+"<=100");
						}
					}
				}
			}
			lp.println();

			//作業時間標準偏差範囲
			lp.println("\\作業時間標準偏差範囲");
			for(i=1;i<=I;i++){
				for(j=1;j<=J;j++){
					for(k=1;k<=Kj[j];k++){
						for(m=1;m<=M;m++){
							lp.println("0<=sig_"+i+"_"+j+"_"+k+"_"+m+"<=100");
						}
					}
				}
			}
			lp.println();

			//作業時間変動係数範囲
			lp.println("\\作業時間変動係数範囲");
			for(i=1;i<=I;i++){
				for(j=1;j<=J;j++){
					for(k=1;k<=Kj[j];k++){
						for(m=1;m<=M;m++){
							lp.println("0<=CV_"+i+"_"+j+"_"+k+"_"+m+"<=100");
						}
					}
				}
			}
			lp.println();

			/**
			* 変数型
			*/
			lp.println("binary");

			lp.println("\\作業者配置決定変数");
			for(i=1;i<=I;i++){
				for(m=1;m<=M;m++){
					lp.println("x_"+i+"_"+m);
				}
			}
			lp.println();
			lp.println("end");
			lp.close();
			System.out.println("作業者配置LPファイル出力完了");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
