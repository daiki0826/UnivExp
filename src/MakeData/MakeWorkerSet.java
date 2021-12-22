package MakeData;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MakeWorkerSet {
    
    public static void main(String[] args) {
		
        //機械台数(ここを変更すれば任意の人数の作業者セットを作成できる)
		int M=3;
       
        //作業者を機械台数分だけ生成
		MakeWorker[] workers = new MakeWorker[M+1];
        for(int i=1;i<=M;i++){
            workers[i] = new MakeWorker(i, M);
        }

        exportWorkerCSV(M,workers);	
	}

    public static void exportWorkerCSV(int M, MakeWorker[] workers){
        //保存先の絶対パス
		String path = "/Users/nagatadaiki/ExpData/WorkerSet/";

        //新しいデータセットを作るときは"Set"の後の番号変える
        //M(作業者の人数)の値に応じたディレクトリに保存
        String dataSetNum = String.valueOf(M)+"Worker/Set1";

        //指定のパスにディレクトリを作成(指定のパスは"/Users/nagatadaiki/ExpData/%dWorker/Set1")
		Path p = Paths.get(path,dataSetNum);
		try {
			Files.createDirectory(p);
		} catch (IOException e1) {
			// TODO 自動生成された catch ブロック
			e1.printStackTrace();
		}

        //作業者熟練度のcsvファイル出力
        try {
            FileWriter fw = new FileWriter(path+dataSetNum+"/熟練度.csv", false);
            PrintWriter pw = new PrintWriter(new BufferedWriter(fw));
            //ヘッダーの指定
            pw.print("作業者番号");
            pw.print(",");
            pw.print("熟練度");
            pw.println();
            for(int i=1;i<=M;i++){
                pw.print(i);
                pw.print(",");
                pw.print(workers[i].getSkill_level());
                pw.println();
            }
            pw.close();
        } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
        }

        for(int i=1;i<=M;i++){
            //作業者毎のα,βのcsvファイル出力
            try {
                FileWriter fw = new FileWriter(path+dataSetNum+"/worker"+i+".csv", false);
                PrintWriter pw = new PrintWriter(new BufferedWriter(fw));
                //ヘッダーの指定
                pw.print("機械番号");
                pw.print(",");
                pw.print("alpha");
                pw.print(",");
                pw.print("beta");
                pw.println();
                for(int k=1;k<=M;k++){
                    pw.print(k);
                    pw.print(",");
                    pw.print(workers[i].getAlpha()[k]);
                    pw.print(",");
                    pw.print(workers[i].getBeta()[k]);
                    pw.println();
                }
                pw.close();
            } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
            }
        }
        // 出力確認用のメッセージ
        System.out.println("csvファイルを出力しました");
    }
}
