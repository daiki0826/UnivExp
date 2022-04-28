package JSPWA;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/*
 * 各種データを出力するクラス
 */
public class Output {
	
	private int J;
	private int M;
	private double[][] ST;
	private double[][] CT;
	private double[][] myu;
	private double[][] sig;
	private double[][][] x;
	
	private Condition condition;
	
	public Output(double[][] ST,double[][] CT,double[][][] x,Condition con) {
		if(con!=null) this.condition = con;
		this.J = Constant.J;
		this.M = Constant.M;
		this.ST = ST;
		this.CT = CT;
		this.myu = new double[J+1][M+1];
		this.sig = new double[J+1][M+1];
		this.x = x;
	}
	

	
	public void MakeGunt(String filePath) {
		
		//xmlデータファイル
		try {
			File xml;
			xml = new File(filePath);
			BufferedWriter bw = new BufferedWriter(new FileWriter(xml));
			bw.write("<ChartData>");
			bw.newLine();
			/*
			 * ジョブの機械割り当てをガントチャートにするxml記述
			 */
			for(int m=1;m<=Constant.M;m++) {
				for(int j=1;j<=J;j++) {
					for(int k=1;k<=Constant.M;k++) {
						if(Constant.jobCon.getPM(j, k)==m) {
							//開始時刻と終了時刻を(hour:minute:second)のフォーマットにする
								int beginHour = (int)ST[j][k]/60;
								int beginMinute = (int)ST[j][k]%60;
								int endHour = (int)CT[j][k]/60;
								int endMinute = (int)CT[j][k]%60;
								bw.write("<Data>");
								bw.newLine();
								bw.write("<品番>"+j+"</品番>");
								bw.newLine();
								bw.write("<品名>"+k+"</品名>");
								bw.newLine();
								bw.write("<開始時刻>"+String.valueOf(beginHour)+" : "+String.valueOf(beginMinute)+" : 00"+"</開始時刻>");
								bw.newLine();
								bw.write("<終了時刻>"+String.valueOf(endHour)+" : "+String.valueOf(endMinute)+" : 00"+"</終了時刻>");
								bw.newLine();
								bw.write("<資源番号>"+"Machine"+Constant.jobCon.getPM(j, k)+"</資源番号>");
								bw.newLine();
								bw.write("</Data>");
								bw.newLine();
						}
					}
				}
			}
			/*
			 * 作業者の機械割り当てをガントチャートにするxml記述．
			 */
			for(int m=1;m<=Constant.M;m++) {
				for(int j=1;j<=Constant.J;j++) {
					for(int k=1;k<=Constant.M;k++) {
						if(Constant.jobCon.getPM(j, k)==m) {
							for(int i=1;i<=Constant.M;i++) {
								if(x[i][j][k]==1);
								//開始時刻と終了時刻を(hour:minute:second)のフォーマットにする
								int beginHour = (int)ST[j][k]/60;
								int beginMinute = (int)ST[j][k]%60;
								int endHour = (int)CT[j][k]/60;
								int endMinute = (int)CT[j][k]%60;
								bw.write("<Data>");
								bw.newLine();
								bw.write("<品番>"+i+"</品番>");
								bw.newLine();
								bw.write("<品名>"+i+"</品名>");
								bw.newLine();
								bw.write("<開始時刻>"+String.valueOf(beginHour)+" : "+String.valueOf(beginMinute)+" : 00"+"</開始時刻>");
								bw.newLine();
								bw.write("<終了時刻>"+String.valueOf(endHour)+" : "+String.valueOf(endMinute)+" : 00"+"</終了時刻>");
								bw.newLine();
								bw.write("<資源番号>"+"M"+Constant.jobCon.getPM(j, k)+"</資源番号>");
								bw.newLine();
								bw.write("</Data>");
								bw.newLine();
							}
						}
					}
				}
			}
			bw.write("</ChartData>");
			bw.close();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}
	
	/*
	public void MakeGunt(String filePath) {
		if(condition!=null) {
			for(int j=1;j<=Constant.J;j++) {
				for(int k=1;k<=Constant.M;k++) {
					System.out.println("myu("+j+","+k+")="+myu[j][k]);
					System.out.println("sig("+j+","+k+")="+sig[j][k]);
				}
			}
		}
		//xmlデータファイル
		try {
			File xml;
			xml = new File(filePath);
			BufferedWriter bw = new BufferedWriter(new FileWriter(xml));
			bw.write("<ChartData>");
			bw.newLine();

			for(int m=1;m<=Constant.M;m++) {
				for(int j=1;j<=J;j++) {
					for(int k=1;k<=Constant.M;k++) {
						if(Constant.jobCon.getPM(j, k)==m) {
							//開始時刻と終了時刻を(hour:minute:second)のフォーマットにする
							if(this.condition!=null){
								int beginHour = (int)ST[j][k]/60;
								int beginMinute = (int)ST[j][k]%60;
								int endHour = (int)(ST[j][k]+myu[j][k])/60;
								int endMinute = (int)(ST[j][k]+myu[j][k])%60;
								int margin_beginHour = endHour;
								int margin_beginMinute = endMinute;
								int margin_endHour = (int)CT[j][k]/60;
								int margin_endMinute = (int)CT[j][k]%60;
								bw.write("<Data>");
								bw.newLine();
								bw.write("<品番>"+j+"</品番>");
								bw.newLine();
								bw.write("<品名>"+k+"</品名>");
								bw.newLine();
								bw.write("<開始時刻>"+String.valueOf(beginHour)+" : "+String.valueOf(beginMinute)+" : 00"+"</開始時刻>");
								bw.newLine();
								bw.write("<終了時刻>"+String.valueOf(endHour)+" : "+String.valueOf(endMinute)+" : 00"+"</終了時刻>");
								bw.newLine();
								bw.write("<資源番号>"+"Machine"+condition.getPM(j, k)+"</資源番号>");
								bw.newLine();
								bw.write("</Data>");
								bw.write("<Data>");
								bw.newLine();
								bw.write("<品番>"+j+"</品番>");
								bw.newLine();
								bw.write("<品名>"+k+"</品名>");
								bw.newLine();
								bw.write("<開始時刻>"+String.valueOf(margin_beginHour)+" : "+String.valueOf(margin_beginMinute)+" : 00"+"</開始時刻>");
								bw.newLine();
								bw.write("<終了時刻>"+String.valueOf(margin_endHour)+" : "+String.valueOf(margin_endMinute)+" : 00"+"</終了時刻>");
								bw.newLine();
								bw.write("<資源番号>"+"Machine"+condition.getPM(j, k)+"</資源番号>");
								bw.newLine();
								bw.write("</Data>");
								bw.newLine();
							}else {
								int beginHour = (int)ST[j][k]/60;
								int beginMinute = (int)ST[j][k]%60;
								int endHour = (int)CT[j][k]/60;
								int endMinute = (int)CT[j][k]%60;
								bw.write("<Data>");
								bw.newLine();
								bw.write("<品番>"+j+"</品番>");
								bw.newLine();
								bw.write("<品名>"+k+"</品名>");
								bw.newLine();
								bw.write("<開始時刻>"+String.valueOf(beginHour)+" : "+String.valueOf(beginMinute)+" : 00"+"</開始時刻>");
								bw.newLine();
								bw.write("<終了時刻>"+String.valueOf(endHour)+" : "+String.valueOf(endMinute)+" : 00"+"</終了時刻>");
								bw.newLine();
								bw.write("<資源番号>"+"Machine"+Constant.jobCon.getPM(j, k)+"</資源番号>");
								bw.newLine();
								bw.write("</Data>");
								bw.newLine();
							}
						}
					}
				}
			}
			
			bw.write("</ChartData>");
			bw.close();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}*/
}
