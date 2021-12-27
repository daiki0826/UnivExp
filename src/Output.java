import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/*
 * 各種データを出力するクラス
 */
public class Output {
	
	private int J;
	private int[] Kj;
	private double[][] ST;
	private double[][] CT;
	private Condition condition;
	
	public Output(Condition condition,double[][] ST,double[][] CT) {
		this.condition = condition;
		this.J = condition.getJobNum();
		this.Kj = condition.getProcessNum();
		this.ST = ST;
		this.CT = CT;
	}
	
	public void MakeGunt(String expDirectoryPath) {
		
		//xmlデータファイル
		try {
			File xml = new File(expDirectoryPath+"/Schedule.xml"); 
			BufferedWriter bw = new BufferedWriter(new FileWriter(xml));
			bw.write("<ChartData>");
			bw.newLine();
			for(int j=1;j<=J;j++) {
				for(int k=1;k<=Kj[j];k++) {
					//開始時刻と終了時刻を(hour:minute:second)のフォーマットにする
					int beginHour = (int)ST[j][k]/60;
					int beginMinute = (int)ST[j][k]%60;
					int endHour = (int)CT[j][k]/60;
					int endMinute = (int)CT[j][k]%60;
					bw.write("<Data>");
					bw.newLine();
					bw.write("<品番>"+j+"-"+k+"</品番>");
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
					bw.newLine();
				}
			}
			bw.write("</ChartData>");
			bw.close();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}
}
