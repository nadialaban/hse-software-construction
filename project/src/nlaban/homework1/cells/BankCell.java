/**
 * @author <a href="mailto:naabuallaban@edu.hse.ru"> Nadia Abu Al Laban</a>
 */
package nlaban.homework1.cells;

import nlaban.homework1.roles.Player;
import nlaban.homework1.roles.Role;

public class BankCell extends Cell{
	// Константы, описывающие граничрые значения для генерации коэффициента на взятие займа.
	public static final double MIN_CREDIT_COEF = 0.002;
	public static final double MAX_CREDIT_COEF = 0.2;
	// Константы, описывающие граничрые значения для генерации коэффициента на возврат займа.
	public static final double MIN_DEBT_COEF = 1.0;
	public static final double MAX_DEBT_COEF = 3.0;

	// Коэффиценты для расчета займа и долга.
	public static double creditCoef;
	public static double debtCoef;

	/**
	 * Конструктор клетки.
	 * @param position Позиция.
	 */
	public BankCell(int position) {
		super(position, '$', "The Bank office");
	}

	/**
	 * Обработка попадания на клетку.
	 * @param player Игрок.
	 * @return Строку для выводв в консоль при попадании на клетку,
	 */
	@Override
	public String getMessageOnStep(Role player) {
		if (player instanceof Player) {
			Player pl = (Player) player;
			if (pl.hasDebt()) {
				int d = pl.getDebt();
				if(!pl.payDebt())
					return String.format("%s> You have not enough money to pay your debt [%d $].\n", this, d);
				else
					return  String.format("%s> You've paid your debt.\n - %d $\n", this, d);
			} else {
				if (((Player)player).getCreditLimit() < 1)
					return  String.format("%s> You can't take credit... You haven't spent money yet.\n", this);
				pl.yn = true;
				return  String.format("%s> Would you like to get a credit? (y/n)\n", this);
			}
		}

		return  String.format("%s> Bot has no options here.\n", this);
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
			player.gettingCredit = true;
			return String.format("> How much money you want to get? Your limit is [%d $].\n", ((Player)player).getCreditLimit());
		}
		return "Ok.\n";
	}

	/**
	 * Обработка выдачи кредита.
	 * @param player Игрок.
	 * @param amount Размер кредита.
	 * @return Сообщение о выдачи кредита.
	 */
	public String getMessageOnGettingCredit(Role player, int amount) {
		((Player)player).getCredit(amount);
		return  String.format("> The bank gave you money. Now you have debt %d $\n+ %d $\n",
				((Player)player).getDebt(), amount);
	}
}
