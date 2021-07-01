/**
 * @author <a href="mailto:naabuallaban@edu.hse.ru"> Nadia Abu Al Laban</a>
 */
package nlaban.homework1.cells;

import nlaban.homework1.Game;
import nlaban.homework1.roles.Role;

public abstract class Cell {
	// Позиция на игровом поле.
	public int position;

	// Тип (для отображения на поле) и название (для отображения в логе)
	public final char type;
	public final String title;

	/**
	 * Проверка позиции Бота.
	 * @return True, если бот стоит на клетке.
	 */
	public boolean isBotOnCell() {
		return Game.bot.getPosition() == position;
	}

	/**
	 * Проверка позиции Игрока.
	 * @return True, если игрок стоит на клетке.
	 */
	public boolean isPlayerOnCell() {
		return Game.player.getPosition() == position;
	}

	/**
	 * Конструктор клетки.
	 * @param position Позиция.
	 * @param type Тип клетки.
	 * @param title Название клетки.
	 */
	public Cell(int position, char type, String title) {
		this.position = position;
		this.type = type;
		this.title = title;
	}

	/**
	 * Обработка попадания на клетку.
	 * @param player Игрок.
	 * @return Строку для выводв в консоль при попадании на клетку.
	 */
	public abstract String getMessageOnStep(Role player);

	/**
	 * Обработка ответа на да/нет вопрос.
	 * @param player Игрок.
	 * @param ans Ответ игрока.
	 * @return Строку для вывода в консоль после ответа на вопрос.
	 */
	public String getMessageOnYN(Role player, boolean ans) {
		return "";
	}

	/**
	 * Отображение клетки на поле.
	 * @return Строковое представлеие клетки.
	 */
	public String display() {
		char pl = isPlayerOnCell() ? 'P' : ' ';
		char bt = isBotOnCell() ? 'B' : ' ';
		return String.format("| %c[%c]%c |", pl, type, bt);
	}

	/**
	 * Отображение информации о клетке.
	 * @return Строковое представление информации.
	 */
	@Override
	public String toString() {
		return String.format("[Cell %d] - %s. \n", position, title);
	}
}
