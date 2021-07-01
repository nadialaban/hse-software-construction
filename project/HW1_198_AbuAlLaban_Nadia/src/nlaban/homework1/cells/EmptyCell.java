/**
 * @author <a href="mailto:naabuallaban@edu.hse.ru"> Nadia Abu Al Laban</a>
 */
package nlaban.homework1.cells;

import nlaban.homework1.roles.Role;

public class EmptyCell extends Cell {

	/**
	 * Конструктор клетки.
	 *
	 * @param position Позиция.
	 */
	public EmptyCell(int position) {
		super(position, 'E', "The Relaxation area");
	}

	/**
	 * Обработка попадания на клетку.
	 *
	 * @param player Игрок.
	 * @return Строку для выводв в консоль при попадании на клетку,
	 */
	@Override
	public String getMessageOnStep(Role player) {
		return this + "> Just relax there\n";
	}
}
