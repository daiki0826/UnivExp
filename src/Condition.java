
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class Condition {
	
	private String DataSetPath;
	private int J; //ジョブ数
	private int M; //機械台数
	private Double[] Due; //各ジョブの納期
	private JobCondition[] JobCons; //各ジョブの条件
	
	/*
	 * コンストラクタ(実験条件のインスタンス作成)
	 */
	public Condition(int J, int M, String DataSetPath) {
		this.DataSetPath = DataSetPath;
		this.J = J;
		this.M = M;
		this.Due = new Double[J+1];//配列番号とジョブ番号を揃えるためにJ+1個の配列を用意
		this.JobCons = new JobCondition[J+1];//配列番号とジョブ番号を揃えるためにJ+1個の配列を用意
		this.setDue();
		this.setJobCondition();
	}
	
	//csvファイルから納期設定を取り出す
	public void setDue() {
		BufferedReader br = null;
		try {
			//指定のファイルを読み込む
			String path = this.DataSetPath+"Due.csv";
			FileInputStream inputFile  = new FileInputStream(path);
			InputStreamReader isp = new InputStreamReader(inputFile);
			br = new BufferedReader(isp);
			String line;//csvファイルから読み込んだ一行分の文字列
			int row=0;//何行目か
			while((line = br.readLine())!=null){
				if(row>0) {
					String data[] = line.split(",");
					this.Due[row]= Double.parseDouble(data[1]);
				}
				row++;
			}
			System.out.println("Due.csvファイルの読み込みを完了しました.");
		} catch (FileNotFoundException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			System.out.println("ファイルが見つかりませんでした");
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			System.out.println("ファイルの読み込みに失敗しました");
		}
		try {
			br.close();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}
	
	//ジョブをJ個生成して配列に格納
	public void setJobCondition() {
		for(int j=1;j<=this.J;j++) {
			this.JobCons[j] = new JobCondition(j,this.M,this.DataSetPath);
		}
	}
	
	//ジョブ数Jを取得
	public int getJobNum() {
		return this.J;
	}
	
	//機械台数Mを取得
	public int getMachineNum() {
		return this.M;
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
	public JobCondition[] getJobCondition() {
		return this.JobCons;
	}
	
	//ジョブj工程kの標準作業時間を取得
	public int getPT(int j, int k) {
		JobCondition job = this.JobCons[j];
		int[] PT=job.getPT();
		return PT[k];
	}
	
	//ジョブj工程kを処理する機械番号を取得
	public int getPM(int j, int k) {
		JobCondition job = this.JobCons[j];
		int[] PM=job.getPT();
		return PM[k];
	}
	
	//ジョブ条件配列を取得
	public JobCondition[] getJobConditions() {
		return this.JobCons;
	}
	
	//納期配列を取得
	public Double[] getDue() {
		return this.Due;
	}
	
}
