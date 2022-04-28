package MakeData;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class ErlangDistribution {

	public static void main(String[] args) {
		
		double lamda=1.67;
		int k=2;
		
		try {
			FileWriter fw = new FileWriter("/Users/nagatadaiki/ExpData/アーラン分布/Erlang(λ,k)=("+lamda+","+k+").csv", false);
			PrintWriter pw = new PrintWriter(new BufferedWriter(fw));
			//ヘッダーの指定
			pw.print("count");
			pw.print(",");
			pw.print("value");
			pw.println();
			//アーラン乱数の発生
			for(int count=1;count<=100000;count++) {
				double value = erlang(lamda,k)*10;
				//double value = logRandom(lamda);
				pw.print(count);
				pw.print(",");
				pw.print(value);
				pw.println();
			}
			pw.close();
			System.out.println("csvファイルを出力しました．");
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}
	
//	static double erlang_2(double lambda, int k){
//		double tp = 1.0;
//		double tau;
//		int n;
//		for (n = 1; n <= k; n ++) {
//			tp = tp * (1 - Math.random());
//		}
//		tau = -1.0 / lambda / (double)k * Math.log(tp);
//		return (tau);
//	}
	
	static double erlang(double lamda, int k){
		double value = 0;
		for(int count=1;count<=k;count++) {
			value = value+logRandom(lamda);
		}
		return value;
	}
	
	static double logRandom(double lamda) {
		return - Math.log(1 - Math.random()) / lamda;
	}

}
