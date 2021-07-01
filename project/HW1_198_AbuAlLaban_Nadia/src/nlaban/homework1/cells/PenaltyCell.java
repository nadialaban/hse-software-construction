/**
 * @author <a href="mailto:naabuallaban@edu.hse.ru"> Nadia Abu Al Laban</a>
 */
package nlaban.homework1.cells;

import nlaban.homework1.roles.Role;

public class PenaltyCell extends Cell {

	// Константы, описывающие граничрые значения для генерации коэффициента.
	public static final double MIN_PENALTY_COEF = 0.01;
	public static final double MAX_PENALTY_COEF = 0.1;

	// Коэффициент для расчета штрафа.
	public static double penaltyCoef;

	/**
	 * Конструктор клетки.
	 *
	 * @param position Позиция.
	 */
	public PenaltyCell(int position) {
		super(position, '%', "The penalty cell"
		);
	}

	/**
	 * Обработка попадания на клетку.
	 *
	 * @param player Игрок.
	 * @return Строку для выводв в консоль при попадании на клетку,
	 */
	@Override
	public String getMessageOnStep(Role player) {
		int penalty = player.getPenalty();
		String pl = player.getClass().getSimpleName();
		if (!player.pay(penalty)) {
			return String
					.format("%s> %s have not enough money to pay the penalty [%d $].\n", this, pl, penalty);
		} else {
			return String.format("%s> %s has paid penalty.\n- %d $\n", this, pl, penalty);
		}
	}
}
