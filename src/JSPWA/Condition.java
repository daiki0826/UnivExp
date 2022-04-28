package JSPWA;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class Condition {
	
	private int J; //ジョブ数
	private int M; //機械台数
	private Double[] Due; //各ジョブの納期
	private JobCondition[] JobCons; //各ジョブの条件
	private WorkerCondition WC; //作業者の条件クラス
	private double[][][]Myu; //作業者iがジョブjのk工程を処理する平均時間
	private double[][][]Sig; //作業者iがジョブjのk工程を処理する標準偏差
	
	/*
	 * コンストラクタ(実験条件のインスタンス作成)
	 */
	public Condition() {
		this.J = Constant.J;
		this.M = Constant.M;
		this.Due = new Double[J+1];//配列番号とジョブ番号を揃えるためにJ+1個の配列を用意
		this.JobCons = new JobCondition[J+1];//配列番号とジョブ番号を揃えるためにJ+1個の配列を用意
		this.Myu = new double[M+1][J+1][M+1];
		this.Sig = new double[M+1][J+1][M+1];
		this.WC = Constant.workCon;
		this.setDue();
		this.setJobCondition();
		this.calcurate_Myu_Sig();
	}
	
	//csvファイルから納期設定を取り出す
	public void setDue() {
		BufferedReader br = null;
		try {
			//指定のファイルを読み込む
			String path = Constant.jobSetPath+"/Due.csv";
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
	
	//作業時間平均Myuと作業時間標準偏差Sigを算出
	public void calcurate_Myu_Sig() {
		for(int i=1;i<=M;i++) {
			for(int j=1;j<=J;j++) {
				for(int k=1;k<=M;k++) {
					for(int m=1;m<=M;m++) {
						if(m==JobCons[j].getPM()[k]) {
							Myu[i][j][k] = JobCons[j].getPT()[k]*WC.getAlpha(i, m);
							Sig[i][j][k] = JobCons[j].getPT()[k]*WC.getAlpha(i, m)*0.2;
						}
					}
				}
			}
		}
	}
	
	//ジョブをJ個生成して配列に格納
	public void setJobCondition() {
		for(int j=1;j<=this.J;j++) {
			this.JobCons[j] = new JobCondition(j,this.M,Constant.jobSetPath);
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
		int[] PM=job.getPM();
		return PM[k];
	}
	
	//ジョブj工程kの作業時間平均を取得
	public double getMyu(int i,int j,int k) {
		return this.Myu[i][j][k];
	}
	
	//ジョブj工程kの作業時間標準偏差を取得
	public double getSig(int i,int j,int k) {
		return this.Sig[i][j][k];
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
