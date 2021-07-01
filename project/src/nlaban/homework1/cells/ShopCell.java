/**
 * @author <a href="mailto:naabuallaban@edu.hse.ru"> Nadia Abu Al Laban</a>
 */
package nlaban.homework1.cells;

import nlaban.homework1.Game;
import nlaban.homework1.roles.Bot;
import nlaban.homework1.roles.Role;

public class ShopCell extends Cell{
	// Константы, описывающие ограничение значения для генерации стоимости.
	public static final int MIN_N = 50;
	public static final int MAX_N = 500;
	// Константы, описывающие ограничение значения для генерации коэффициента на границы компенсации.
	public static final double MIN_K_COEF = 0.5;
	public static final double MAX_K_COEF = 0.9;

	// Константы, описывающие ограничение значения для коэффициента компенсации.
	public static final double MIN_COMPENSATION_COEF = 0.1;
	public static final double MAX_COMPENSATION_COEF = 1;
	// Константы, описывающие ограничение значения для коэффициента цены.
	public static final double MIN_IMPROVEMENT_COEF = 0.1;
	public static final double MAX_IMPROVEMENT_COEF = 2;

	// Стоимость и коэффициент для улучшения.
	private int N;
	private final double improvementCoef;

	public int getN() {
		return N;
	}

	// Компенсация и коэффициент для увеличения.
	private int K;
	private final double compensationCoef;

	public int getK() {
		return K;
	}

	// Владелец.
	public Role owner;

	/**
	 * Конструктор клетки.
	 * @param position Позиция.
	 */
	public ShopCell(int position) {
		super(position, 'S', "The Shop");
		N = Game.getRandomInt(MIN_N, MAX_N);
		K = Game.getRandomInt((int) Math.round(MIN_K_COEF * N), (int) Math.round(MAX_K_COEF * N));
		compensationCoef = Game.getRandomDouble(MIN_COMPENSATION_COEF, MAX_COMPENSATION_COEF);
		improvementCoef = Game.getRandomDouble(MIN_IMPROVEMENT_COEF, MAX_IMPROVEMENT_COEF);
	}

	/**
	 * Улучшение магазина.
	 */
	public void improve() {
		K += (int) Math.round(K * compensationCoef);
		N += (int) Math.round(N * improvementCoef);
	}

	/**
	 * Обработка попадания на клетку.
	 * @param player Игрок.
	 * @return Строку для выводв в консоль при попадании на клетку,
	 */
	@Override
	public String getMessageOnStep(Role player) {
		if (owner != null && !owner.equals(player)) {
			player.payCompensation(this);
			return String.format("%s> %s paid compensation to %s\n - %d $",
					this, player.getName(), owner.getName(), K);
		}
		return player instanceof Bot ? botAction(player) : playerAction(player);
	}

	/**
	 * Действия Бота при попадании на клетку.
	 * @param bot Бот.
	 * @return Строку для вывода в консоль.
	 */
	public String botAction(Role bot) {
		if (owner == null) {
			if (bot.getMoney() < N)
				return String.format("%s> Not enough money to buy this shop.", this);
			if (Game.getRandomDouble(0,1) >= 0.5) {
				return String.format("%s%s", this, bot.buy(this));
			}
			return String.format("%s> Bot isn't going to buy this shop.\n", this);
		}
		if (bot.getMoney() < N)
			return String.format("%s> Not enough money to improve this shop.\n", this);

		if (Game.getRandomDouble(0,1) >= 0.5) {
			return String.format("%s%s", this, bot.improve(this));
		}
		return String.format("%s> Bot isn't going to improve this shop.\n", this);
	}

	/**
	 * Действия Игрока при попадании на клетку.
	 * @param player  Игрок.
	 * @return Строку для вывода в консоль.
	 */
	public String playerAction(Role player) {
		if (owner == null) {
			if (player.getMoney() < N)
				return String.format("%s> Not enough money to buy this shop.\n", this);
			player.yn = true;
			return String.format("%s> This shop has no owner.\n> Would you like to buy it? [- %d $] (y/n)\n", this, N);
		}
		if (player.getMoney() < N)
			return String.format("%s> Not enough money to improve this shop.\n", this);
		player.yn = true;
		return String.format("%s> This is your shop.\n> Would you like to improve it? [- %d $] (y/n)\n", this, N);
	}

	/**
	 * Обработка ответа на да/нет вопрос.
	 * @param player Игрок.
	 * @param ans Ответ игрока.
	 * @return Строку для вывода в консоль после ответа на вопрос.
	 */
	@Override
	public String getMessageOnYN(Role player, boolean ans) {
		player.yn = false;
		if (ans) {
			if (player.equals(owner))
				return player.improve(this);
			return player.buy(this);
		}
		return "Ok.";
	}

	/**
	 * Отображение клетки на поле.
	 * @return Строковое представлеие клетки.
	 */
	@Override
	public String display() {
		char pl = isPlayerOnCell() ? 'P' : ' ';
		char bt = isBotOnCell() ? 'B' : ' ';
		char t = owner == null ? 'S' : owner.equals(Game.currentPlayer) ? 'M' : 'O';
		return String.format("| %c[%c]%c |", pl, t, bt);
	}

	/**
	 * Отображение информации о клетке.
	 * @return Строковое представление информации.
	 */
	@Override
	public String toString() {
		String o = owner == null ? "No owner" : String.format("Owner: %s", owner.getName());
		return String.format("%s(%s\tN: %d\tK: %d\tImp.coef.: %.2f\tComp.coef.: %.2f)\n",
				super.toString(), o, N, K, improvementCoef, compensationCoef);
	}
}
