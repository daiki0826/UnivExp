package MakeData;
import java.util.ArrayList;

public class MakeCondition {
	
	private int J; //ジョブ数
	private int M; //機械台数
	private Double[] Due; //各ジョブの納期
	private double[] SumPT; //各ジョブの総作業時間
	private MakeJobCondition[] JobCons; //各ジョブの条件
	
	/*
	 * コンストラクタ(実験条件のインスタンス作成)
	 */
	public MakeCondition(int J, int M) {
		this.J = J;
		this.M = M;
		this.setJobCondition();
		this.set_SumPT();
		this.setDue();
	}
	
	//ジョブをJ個生成して配列に格納
	public void setJobCondition() {
		JobCons = new MakeJobCondition[J+1];//配列番号とジョブ番号を揃えるためにJ+1個の配列を用意
		for(int j=1;j<=this.J;j++) {
			this.JobCons[j] = new MakeJobCondition(j,M);
		}
	}
	
	//各ジョブの納期を取り出して納期配列に格納
	public void setDue() {
		this.Due = new Double[J+1];
		for(int j=1;j<=J;j++) {
			this.Due[j]=this.JobCons[j].getDue();
		}
	}
	
	//各ジョブの納期を取り出して納期配列に格納
	public void set_SumPT() {
		this.SumPT = new double[J+1];
		for(int j=1;j<=J;j++) {
			this.SumPT[j]=this.JobCons[j].get_SumPT();
		}
	}
	
	//各ジョブの工程数を取得
	public int[] getProcessNum() {
		int[] Kj= new int[J+1];
		for(int j=1;j<=J;j++) {
			Kj[j]=this.JobCons[j].getKj();
		}
		return Kj;
	}
	
	//ジョブ条件の配列を取得
	public MakeJobCondition[] getJobCondition() {
		return this.JobCons;
	}
	
	//ジョブj工程kの標準作業時間を取得
	public int getPT(int j, int k) {
		MakeJobCondition job = this.JobCons[j];
		ArrayList<Integer> PT=job.getPT();
		return PT.get(k-1);
	}
	
	//ジョブj工程kを処理する機械番号を取得
	public int getPM(int j, int k) {
		MakeJobCondition job = this.JobCons[j];
		ArrayList<Integer> PM=job.getPM();
		return PM.get(k-1);
	}
	
	public MakeJobCondition[] getJobConditions() {
		return this.JobCons;
	}
	
	public Double[] getDue() {
		return this.Due;
	}
	
	public double[] get_SumPT() {
		return this.SumPT;
	}
	
}
