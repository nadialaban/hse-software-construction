/**
 * @author <a href="mailto:naabuallaban@edu.hse.ru"> Nadia Abu Al Laban</a>
 */
package nlaban.homework1.cells;

import nlaban.homework1.Game;
import nlaban.homework1.roles.Role;

public class TaxiCell extends Cell {

	// Константы, описывающие граничрые значения для генерации дистанции.
	public static final int MIN_TAXI_DIST = 3;
	public static final int MAX_TAXI_DIST = 5;

	/**
	 * Конструктор клетки.
	 *
	 * @param position Позиция.
	 */
	public TaxiCell(int position) {
		super(position, 'T', "The Taxi parking");
	}

	/**
	 * Обработка попадания на клетку.
	 *
	 * @param player Игрок.
	 * @return Строку для выводв в консоль при попадании на клетку,
	 */
	@Override
	public String getMessageOnStep(Role player) {
		int taxiDistance = Game.getRandomInt(MIN_TAXI_DIST, MAX_TAXI_DIST);
		String pl = player.getClass().getSimpleName();
		String moved = player.move(taxiDistance);

		return String.format("%s> %s got a taxi and shifted forward by [%d] cells.\n%s\n",
				this, pl, taxiDistance, moved);
	}
}
