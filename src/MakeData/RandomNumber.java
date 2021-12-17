package MakeData;
import java.util.Random;

public class RandomNumber {
	
	public static final int createUniformRand(int x,int y) {
		Random rand = new Random();
		int value = rand.nextInt(x)+y;
		return value;
	}
	
	public static final double createNormalRand(int x, int y) {
		Random rand = new Random();
		double value = rand.nextGaussian(x, y);
		return value;
	}
}
