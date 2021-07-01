/**
 * @author <a href="mailto:naabuallaban@edu.hse.ru"> Nadia Abu Al Laban</a>
 */
package nlaban.homework1.roles;

import nlaban.homework1.cells.BankCell;

public class Player extends Role {

	// Информация о финансах игрока.
	private int debt;

	public int getDebt() {
		return debt;
	}

	/**
	 * Рассчет лимита на взятие кредита.
	 *
	 * @return Лимит.
	 */
	public int getCreditLimit() {
		return (int) Math.round(BankCell.creditCoef * spentMoney);
	}

	/**
	 * Проеверка на наличие долга.
	 *
	 * @return True, если есть долг.
	 */
	public boolean hasDebt() {
		return getDebt() > 0;
	}

	/**
	 * Конструктор игрока.
	 *
	 * @param money Стартовый капитал.
	 */
	public Player(int money) {
		super(money, "Player");
	}

	/**
	 * Выплата долга.
	 *
	 * @return False, если обанкротился.
	 */
	public boolean payDebt() {
		boolean notBankrupt = pay(getDebt());
		debt = 0;
		return notBankrupt;
	}

	/**
	 * Взятие кредита в банке.
	 *
	 * @param amount Размер кредита.
	 */
	public void getCredit(int amount) {
		debt = (int) Math.round(amount * BankCell.debtCoef);
		money += amount;
		gettingCredit = false;
	}

	/**
	 * Описание игрока.
	 *
	 * @return Строковое представление игрока.
	 */
	@Override
	public String toString() {
		return String.format("PLAYER\t[%s\tdebt: %d $]\n", super.toString(), getDebt());
	}
}
