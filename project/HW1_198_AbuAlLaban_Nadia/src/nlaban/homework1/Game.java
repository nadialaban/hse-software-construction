/**
 * @author <a href="mailto:naabuallaban@edu.hse.ru"> Nadia Abu Al Laban</a>
 */
package nlaban.homework1;

import java.util.Random;
import nlaban.homework1.cells.BankCell;
import nlaban.homework1.cells.PenaltyCell;
import nlaban.homework1.roles.Bot;
import nlaban.homework1.roles.Player;
import nlaban.homework1.roles.Role;

public class Game {

	public static Board board;
	public static Role currentPlayer;

	public static Bot bot;
	public static Player player;

	/**
	 * Установка игры.
	 *
	 * @param money  Стартовый капитал.
	 * @param height Высота поля.
	 * @param width  Ширина поля.
	 */
	public static void setUpGame(int money, int height, int width) {
		BankCell.creditCoef = getRandomDouble(BankCell.MIN_CREDIT_COEF, BankCell.MAX_CREDIT_COEF);
		BankCell.debtCoef = getRandomDouble(BankCell.MIN_DEBT_COEF, BankCell.MAX_DEBT_COEF);
		PenaltyCell.penaltyCoef = getRandomDouble(PenaltyCell.MIN_PENALTY_COEF,
				PenaltyCell.MAX_PENALTY_COEF);

		bot = new Bot(money);
		player = new Player(money);

		currentPlayer = getRandomInt(0, 1) == 1 ? bot : player;

		board = new Board(height, width);
	}

	/**
	 * Смена игрока.
	 */
	public static void changePlayer() {
		currentPlayer = currentPlayer instanceof Bot ? player : bot;
	}

	/**
	 * Генератор случайных чисел.
	 *
	 * @param min Минимум.
	 * @param max Максимум.
	 * @return Случайное вещественное число в заданных границах.
	 */
	public static double getRandomDouble(double min, double max) {
		Random rnd = new Random();
		return min + rnd.nextDouble() * (max - min);
	}

	/**
	 * Генератор случайных чисел.
	 *
	 * @param min Минимум.
	 * @param max Максимум.
	 * @return Случайное целочисленное число в заданных границах.
	 */
	public static int getRandomInt(int min, int max) {
		return (int) Math.round(getRandomDouble(min, max));
	}

	/**
	 * Короткое описание состояния игры.
	 *
	 * @return Строковое представление.
	 */
	public static String getShortInformation() {
		return String.format("\n%s%s%s", bot, player, board.display());
	}

	/**
	 * Подробное описание состояния игры.
	 *
	 * @return Строковое представление.
	 */
	public static String getFullInformation() {
		return "\nCOEFFICIENTS: \n" + String.format("- Credit:\t%.2f\n", BankCell.creditCoef)
				+ String.format("- Penalty:\t%.2f\n", PenaltyCell.penaltyCoef)
				+ String.format("- Debt:\t%.2f\n", BankCell.debtCoef)
				+ getShortInformation();
	}
}
