package MakeData;
import java.util.ArrayList;
import java.util.Collections;

public class MakeJobCondition {
	
	private int JobNum; //ジョブ番号
	private int Kj; //工程数
	private ArrayList<Integer> PT = new ArrayList<Integer>(); //各工程の標準作業時間
	private ArrayList<Integer> PM = new ArrayList<Integer>(); //各工程の作業機械番号
	private Double due; //納期
	
	
	//コンストラクタ(ジョブのインスタンス作成)
	public MakeJobCondition(int j, int M) {
		this.JobNum = j;
		setKj(M);
		setPT();
		setPM();
		setDue();
	}
	
	//ジョブの処理工程数を乱数で決定(M-4~Mの一様乱数)
	public void setKj(int M) {
		int Kj = RandomNumber.createUniformRand(1, M-1);
		this.Kj = Kj;
	}
	
	//ジョブの各工程の標準処理時間PTを決定(10~20の一様乱数)
	public void setPT() {
		for(int i=1;i<=this.Kj;i++) {
			int rand = RandomNumber.createUniformRand(10,10);
			this.PT.add(rand);
		}
	}
	
	//各工程を処理する機械番号を決定(1~Mの番号をランダムに並び替えている)
	public void setPM() {
		ArrayList<Integer> PM = new ArrayList<Integer>();
		for(int k=1;k<=this.Kj;k++) {
			PM.add(k);
			Collections.shuffle(PM);
			this.PM=PM;
		}
	}
	
	//納期決定(納期=作業時間合計＊0.8とする)
	public void setDue() {
		int SumPT=0;
		for(int k=1;k<=Kj;k++) {
			SumPT = SumPT+this.PT.get(k-1);
		}
		Double due = SumPT*0.8;
		this.due = due;
	}

	
	public int getJobNum() {
		return this.JobNum;
	}
	
	public int getKj() {
		return this.Kj;
	}
	
	public ArrayList<Integer> getPT() {
		return this.PT;
	}
	
	public ArrayList<Integer> getPM() {
		return this.PM;
	}
	
	public Double getDue() {
		return this.due;
	}

}
