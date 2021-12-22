import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class Worker {

    private int workerNum;
    private double[] alpha;
    private double[] beta;
    private int M;

    //コンストラクタ(作業者のインスタンス作成)
    public Worker(int i,int M,String fileName){
        this.workerNum = i;
        this.M = M;
        this.alpha = new double[M+1];
        this.beta = new double[M+1];
		this.setWorkerCondition(fileName);
    }

    //ジョブ条件をcsvファイルから読み込む
	public void setWorkerCondition(String fileName) {
		BufferedReader br = null;
		try {
			//指定のファイルを読み込む
			FileInputStream inputFile  = new FileInputStream(fileName+"/Worker"+String.valueOf(this.workerNum)+".csv");
			InputStreamReader isp = new InputStreamReader(inputFile);
			br = new BufferedReader(isp);
			String line;//csvファイルから読み込んだ一行分の文字列
			int row=0;//何行目か
			while((line = br.readLine())!=null){
				if(row>0) {
					String data[] = line.split(",");
					this.alpha[row] = Double.parseDouble(data[1]);
					this.beta[row] = Double.parseDouble(data[2]);
				}
				row++;
			}
			System.out.println("worker.csvファイルの読み込みを完了しました.");
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

    //作業者番号を取得
    public int getWokerNum(){
        return this.workerNum;
    }
    

    //各機械に対するαを取得
    public double[] getAlpha(){
        return this.alpha;
    }

    //各機械に対するβを取得
    public double[] getBeta(){
        return this.beta;
    }
    
}
