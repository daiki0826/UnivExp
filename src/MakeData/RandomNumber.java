package MakeData;
import java.util.Random;

public class RandomNumber {
	
	//y以上x+y未満の範囲で一様乱数を生成する(0以外で始める時は+yする)
	public static final int createUniformRand(int x,int y) {
		Random rand = new Random();
		int value = rand.nextInt(x)+y;
		return value;
	}
	
	//平均x,標準偏差yの正規乱数を生成
	public static final double createNormalRand(double myu, double sig) {
		Random rand = new Random();
		double value = rand.nextGaussian(myu, sig);
		return value;
	}

	//y以上x+y未満の範囲で一様乱数を生成する(0以外で始める時は+yする)
	public static final double createUniformRand_Double(double x,double y) {
		Random rand = new Random();
		int temp_x = (int)(x*10);
		int temp_y = (int)(y*10);
		int temp_value = rand.nextInt(temp_x)+temp_y;
		double value = (double)temp_value/10;
		System.out.println("temp_x="+temp_x);
		System.out.println("temp_y="+temp_y);
		System.out.println("temp_value="+temp_value);
		System.out.println("value="+value);
		System.out.println();
		return value;
	}
	
	//指数分布に従う乱数を発生
	static double logRandom(double lamda) {
		return - Math.log(1 - Math.random()) / lamda;
	}
	
	//f(x;k,λ)のアーラン分布に従う乱数を発生させる．(所与の標準作業時間PTを基に作業者別のアーラン分布を作成)
	public static final double createErlangRand(int k,double lambda,double PT) {
		Random rand = new Random();
		double[] Erlang_X = new double[100000];
		double[] val_final = new double[100000];
		for(int count=0;count<100000;count++) {
			double value = 0;
			for(int count_2=1;count_2<=k;count_2++) {
				value = value+logRandom(lambda);
			}
			Erlang_X[count] = value;
			val_final[count] = PT+(Erlang_X[count]-k/lambda);
		}
//		try {
//			FileWriter fw = new FileWriter("/Users/nagatadaiki/ExpData/アーラン分布/Erlang(λ,k)=("+lambda+","+k+").csv", false);
//			PrintWriter pw = new PrintWriter(new BufferedWriter(fw));
//			//ヘッダーの指定
//			pw.print("count");
//			pw.print(",");
//			pw.print("value");
//			pw.println();
//			for(int count=0;count<100000;count++) {
//				pw.print(count+1);
//				pw.print(",");
//				pw.print(Erlang_X[count]);
//				pw.print(",");
//				pw.print(val_final[count]);
//				pw.println();
//			}
//			pw.close();
//			System.out.println("csvファイルを出力しました．");
//		} catch (IOException e) {
//			// TODO 自動生成された catch ブロック
//			e.printStackTrace();
//		}
		return val_final[rand.nextInt(100000)];
	}
	


	
	
	
}

