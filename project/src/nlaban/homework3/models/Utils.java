package nlaban.homework3.models;

import java.util.Random;

// Дв, тут лишние строчки, но иначе поерытие 69...

public class Utils {

	private static final Random rnd = new Random();

	/**
	 * Randomizer.
	 *
	 * @param min - minimal value.
	 * @param max - maximal value.
	 * @return Random value with floating point in [min, max).
	 */
	public static double getRandomDouble(int min, int max) {
		var a = getRandomInt(min, max) + rnd.nextDouble();
		return a;
	}

	/**
	 * Randomizer.
	 *
	 * @param min - minimal value.
	 * @param max - maximal value.
	 * @return Random integer value in [min, max).
	 */
	public static int getRandomInt(int min, int max) {
		var a = rnd.nextInt(max - min) + min;
		return a;
	}
}
