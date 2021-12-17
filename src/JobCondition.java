
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class JobCondition {
	
	private int JobNum; //ジョブ番号
	private int Kj; //工程数
	private int[] PT; //各工程の標準作業時間
	private int[] PM; //各工程の作業機械番号
	
	
	//コンストラクタ(ジョブのインスタンス作成)
	public JobCondition(int j, int M, String DataSetPath) {
		//処理工程数は各ジョブで異なるがとりあえず機械台数Mに合わせてPTとPMの配列のサイズを確保
		this.PT = new int[M+1];
		this.PM = new int[M+1];
		this.JobNum = j;
		this.setJobCondition(DataSetPath+"job"+String.valueOf(j)+".csv");
	}
	
	//ジョブ条件をcsvファイルから読み込む
	public void setJobCondition(String fileName) {
		BufferedReader br = null;
		try {
			//指定のファイルを読み込む
			FileInputStream inputFile  = new FileInputStream(fileName);
			InputStreamReader isp = new InputStreamReader(inputFile);
			br = new BufferedReader(isp);
			String line;//csvファイルから読み込んだ一行分の文字列
			int row=0;//何行目か
			while((line = br.readLine())!=null){
				if(row>0) {
					String data[] = line.split(",");
					this.PM[row] = Integer.parseInt(data[1]);
					this.PT[row] = Integer.parseInt(data[2]);
				}
				row++;
			}
			this.Kj=row; //読み込んだ行数はそのジョブの工程数と等しい.
			System.out.println("job.csvファイルの読み込みを完了しました.");
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
	

	
	public int getJobNum() {
		return this.JobNum;
	}
	
	public int getKj() {
		return this.Kj;
	}
	
	public int[] getPT() {
		return this.PT;
	}
	
	public int[] getPM() {
		return this.PM;
	}
	


}
