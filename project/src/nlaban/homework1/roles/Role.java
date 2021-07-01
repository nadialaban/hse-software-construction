/**
 * @author <a href="mailto:naabuallaban@edu.hse.ru"> Nadia Abu Al Laban</a>
 */
package nlaban.homework1.roles;

import nlaban.homework1.Game;
import nlaban.homework1.cells.Cell;
import nlaban.homework1.cells.PenaltyCell;
import nlaban.homework1.cells.ShopCell;

public abstract class Role {

	private final String name;

	public String getName() {
		return name;
	}

	// Индекс клетки на игровом поле, на которой стоит персонаж.
	private int position;

	public int getPosition() {
		return position;
	}

	/**
	 * @return Клетка, на которой находится персонаж.
	 */
	public Cell getCell() {
		return Game.board.getCellByPos(position);
	}

	// Бюджет персонажа.
	protected int money;
	protected int spentMoney;

	public int getMoney() {
		return money;
	}

	// Флаги для состояний.
	public boolean gettingCredit = false;
	public boolean yn = false;

	/**
	 * Коструктор персонажа.
	 *
	 * @param money Стартовый капитал.
	 * @param name  Имя персонажа.
	 */
	public Role(int money, String name) {
		this.money = money;
		this.name = name;
	}

	/**
	 * Проверка на банкротство.
	 *
	 * @return True, если обанкротился.
	 */
	public boolean isBankrupt() {
		return money < 0;
	}

	/**
	 * Перемещение персонажа по полю.
	 *
	 * @param distance дистанция.
	 * @return Сообщение о перемещении.
	 */
	public String move(int distance) {
		position += distance;
		position %= Game.board.getLength();
		return getCell().getMessageOnStep(this);
	}

	/**
	 * @return Размер штрафа.
	 */
	public int getPenalty() {
		return (int) Math.round(PenaltyCell.penaltyCoef * money);
	}

	/**
	 * Трата денег.
	 *
	 * @param amount количество
	 * @return False, если персонаж обанкротился.
	 */
	public boolean pay(int amount) {
		money -= amount;
		return money >= 0;
	}

	/**
	 * Ход в игре.
	 *
	 * @return Строка для вывода в строку.
	 */
	public String step() {
		int dice1 = Game.getRandomInt(1, 6);
		int dice2 = Game.getRandomInt(1, 6);

		String res = String.format("%s rolled [%d] and [%d] and moved to..\n", getName(), dice1, dice2);
		res += move(dice1 + dice2);

		return res;
	}

	/**
	 * Покупка магазина.
	 *
	 * @param shop Магазин.
	 * @return Сообщение о покупке.
	 */
	public String buy(ShopCell shop) {
		shop.owner = this;
		pay(shop.getN());
		spentMoney += shop.getN();
		return String.format("> %s is new owner of this shop.\n- %d $\n", name, shop.getN());
	}

	/**
	 * Улучшение магазина.
	 *
	 * @param shop Магазин.
	 * @return Сообщение об улучшении.
	 */
	public String improve(ShopCell shop) {
		int cost = shop.getN();
		pay(cost);
		spentMoney += cost;
		shop.improve();
		return String.format("> %s improved this shop. New compensation is [%d $]\n- %d $\n",
				name, shop.getK(), cost);
	}

	/**
	 * Выплата компенсации владельцу.
	 *
	 * @param shop Магазин.
	 */
	public void payCompensation(ShopCell shop) {
		money -= shop.getK();
		shop.owner.money += shop.getK();
	}

	/**
	 * Описание персонажа.
	 *
	 * @return Строковое представление персонажа.
	 */
	@Override
	public String toString() {
		return String.format("pos: %d\tmoney: %d $\tspent: %d $", getPosition(), money, spentMoney);
	}
}
