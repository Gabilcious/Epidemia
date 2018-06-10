import java.util.Random;

public class Generator {
	private static Generator instance;
	private Random rnd;

	private Generator(int seed) {
		rnd = new Random(seed);
	}

	public static void init(int seed) {
		instance = new Generator(seed);
	}

	public static Generator getInstance() {
		return instance;
	}

	public double nextDouble() {
		return rnd.nextDouble();
	}

	public int nextInt() {
		return rnd.nextInt();
	}

	public int nextInt(int bound) {
		return rnd.nextInt(bound);
	}
}
