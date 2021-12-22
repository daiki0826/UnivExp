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
	public static final double createNormalRand(int x, int y) {
		Random rand = new Random();
		double value = rand.nextGaussian(x, y);
		return value;
	}
}
