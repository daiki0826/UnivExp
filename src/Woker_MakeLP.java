import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Woker_MakeLP {

	public Woker_MakeLP(Condition con){
		
		try {
			//書き出すlpファイルの定義
			PrintWriter lp=new PrintWriter(new FileWriter(new File("/Users/nagatadaiki/ExpData/Data2/worker.lp")));
			//変数の定義
			int i,j,k,m,PM1,PM2;
			//ジョブ数を条件クラス「Condition」から取得
			int J = con.getJobNum();
			//機械台数を条件クラス「Condition」から取得
			int M = con.getMachineNum();
			int I = con.getMachineNum();
			//ジョブ毎の工程数を条件クラス「Condition」から取得
			int[] Kj= con.getProcessNum();

			int Count = 1; //制約式を数える

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
			for(i=1;i<=I;i++){
				for(j=1;j<=J;j++){
					for(k=1;k<=Kj[j];k++){
						for(m=1;m<=M;m++){
							lp.println("_C"+Count+": CV_"+i+"_"+j+"_"+k+"_"+m+"="+"");
						}
					}
				}
			}
			lp.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
