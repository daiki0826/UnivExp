package MakeData;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class ErlangDistribution {

	public static void main(String[] args) {
		
		try {
			FileWriter fw = new FileWriter("/Users/nagatadaiki/ExpData/アーラン分布/Erlang.csv", false);
			PrintWriter pw = new PrintWriter(new BufferedWriter(fw));
			//ヘッダーの指定
			pw.print("count");
			pw.print(",");
			pw.print("value");
			pw.println();
			//アーラン乱数の発生
			for(int count=1;count<=1000;count++) {
				double value = erlang(2,2);
				pw.print(count);
				pw.print(value);
				pw.println();
			}
			pw.close();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
        

	}
	
	static double erlang(double lambda, int k){
		double tp = 1.0;
		double tau;
		int n;
		for (n = 1; n <= k; n ++) {
			tp = tp * (1 - Math.random());
		}
		tau = -1.0 / lambda / (double)k * Math.log(tp);
		return (tau);
	}

}
