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
	
	
	
}

