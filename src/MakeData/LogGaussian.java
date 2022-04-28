package MakeData;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.math3.distribution.LogNormalDistribution;

public class LogGaussian {

	
	public static void main(String[] args) {
		double myu = 1.0;
		double sigma= 1.5;
		try {
			FileWriter fw = new FileWriter("/Users/nagatadaiki/ExpData/LogGaussian(μ,σ)=("+myu+","+sigma+").csv", false);
			PrintWriter pw = new PrintWriter(new BufferedWriter(fw));
			//ヘッダーの指定
			pw.print("count");
			pw.print(",");
			pw.print("value");
			pw.println();
			//対数正規分布に従う乱数を発生
			for(int count=1;count<=100000;count++) {
				double value = logNormalDistribution(myu,sigma);
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
	
	public static double logNormalDistribution(double myu, double sigma) {
		LogNormalDistribution distribution = new LogNormalDistribution(myu, sigma);
		double rand_value = distribution.sample();
		return rand_value;
	}
	
	 public void logNormalDistributionTest() {
	        double MU = 3.0; // ln(x)の平均μ 大きいほどグラフの右側が伸びる
	        double SIGMA = 1.0; // ln(x)の標準偏差σ 大きいほどグラフが横に広がる
	        LogNormalDistribution distribution = new LogNormalDistribution(MU, SIGMA);

	        List<Double> results = IntStream.rangeClosed(1, 100000).boxed()
	                .map(i -> distribution.sample())
	                .collect(Collectors.toList());
	        try {
				FileWriter fw = new FileWriter("/Users/nagatadaiki/ExpData/LogGaussian(μ,σ)=("+MU+","+SIGMA+").csv", false);
				PrintWriter pw = new PrintWriter(new BufferedWriter(fw));
				//ヘッダーの指定
				pw.print("count");
				pw.print(",");
				pw.print("value");
				pw.println();
				//対数正規分布に従う乱数を発生
				int count = 1;
				for(double val : results) {
					//double value = logRandom(lamda);
					pw.print(count);
					pw.print(",");
					pw.print(val);
					pw.println();
					count++;
				}
				pw.close();
				System.out.println("csvファイルを出力しました．");
			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
	        
	        
	 }
		
}
