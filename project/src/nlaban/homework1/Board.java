/**
 * @author <a href="mailto:naabuallaban@edu.hse.ru"> Nadia Abu Al Laban</a>
 */
package nlaban.homework1;

import nlaban.homework1.cells.BankCell;
import nlaban.homework1.cells.Cell;
import nlaban.homework1.cells.EmptyCell;
import nlaban.homework1.cells.PenaltyCell;
import nlaban.homework1.cells.ShopCell;
import nlaban.homework1.cells.TaxiCell;

public class Board {

	// Константы, описывающие граничные значения для размерностей поля.
	final int MIN_SIZE = 6;
	final int MAX_SIZE = 30;
	// Константы, описывающие граничные значения для количества клеток определенного типа.
	final int MAX_TAXI_AMOUNT = 2;
	final int MAX_PENALTY_AMOUNT = 2;

	// Размерности поля.
	public int height;
	public int width;
	public int[] corners;

	// Клетки поля, индекс по часовой стрелке 0 - стартовая клетка.
	private final Cell[] cells;

	/**
	 * Конструктор игрового поля.
	 *
	 * @param height Высота.
	 * @param width  Ширина.
	 */
	public Board(int height, int width) {
		if (width < MIN_SIZE || width > MAX_SIZE || height < MIN_SIZE || height > MAX_SIZE) {
			throw new IllegalArgumentException(
					String.format("Dimensions should be in diapason [%d, %d]", MIN_SIZE, MAX_SIZE));
		}

		this.height = height;
		this.width = width;
		corners = new int[]{0, width - 1, width + height - 2, 2 * width + height - 3};
		cells = new Cell[2 * (height + width) - 4];
		createBoard();
	}

	/**
	 * Генерация поля.
	 */
	private void createBoard() {
		cells[0] = new EmptyCell(0);

		// Заполняем линии
		for (int i = 1; i < 5; i++) {
			if (i != 4) {
				cells[corners[i]] = new EmptyCell(corners[i]);
			}
			int start = corners[i - 1] + 1;
			int end = i != 4 ? corners[i] - 1 : cells.length - 1;
			int emptyCells = end - start + 1;

			int tmp = Game.getRandomInt(start, end);
			cells[tmp] = new BankCell(tmp);
			emptyCells--;

			emptyCells = createCellsOnSide('t', MAX_TAXI_AMOUNT, start, end, emptyCells);
			createCellsOnSide('p', MAX_PENALTY_AMOUNT, start, end, emptyCells);
		}

		// Заполняем оставщиеся клетки магазинами
		for (int i = 0; i < cells.length; i++) {
			if (cells[i] == null) {
				cells[i] = new ShopCell(i);
			}
		}
	}

	/**
	 * Создает случайное количество (из диапозона [0, max] клеток заданного типа.
	 *
	 * @param type       Тип клетки.
	 * @param max        Максимальное количество.
	 * @param start      Начало отрезка на поле.
	 * @param end        Конец отрезка на поле.
	 * @param emptyCells Количество пустых клеток на отрезке.
	 * @return Количество пустых клеток.
	 */
	private int createCellsOnSide(char type, int max, int start, int end, int emptyCells) {
		int tmpAmount = Game.getRandomInt(0, max);

		while (tmpAmount > 0 && emptyCells > 0) {
			int tmp = Game.getRandomInt(start, end);
			if (cells[tmp] != null) {
				continue;
			}

			switch (type) {
				case 't':
					cells[tmp] = new TaxiCell(tmp);
					break;
				case 'p':
					cells[tmp] = new PenaltyCell(tmp);
					break;
				default:
					break;
			}

			emptyCells--;
			tmpAmount--;
		}

		return emptyCells;
	}

	/**
	 * Отображение доски.
	 *
	 * @return Доска в строковом представлении.
	 */
	public String display() {
		StringBuilder res = new StringBuilder();

		for (int i = 0; i < height; i++) {
			// Первая линия.
			if (i == 0) {
				for (int j = 0; j < width; j++) {
					res.append(cells[j].display());
				}
			}
			// Последняя линия.
			else if (i == height - 1) {
				for (int j = corners[3]; j >= corners[2]; j--) {
					res.append(cells[j].display());
				}
			} else {
				res.append(cells[cells.length - i].display());
				res.append("         ".repeat(width - 2));
				res.append(cells[corners[1] + i].display());
			}
			res.append("\n");
		}

		return res.toString();
	}

	/**
	 * @return Количество клеток поля.
	 */
	public int getLength() {
		return cells.length;
	}

	/**
	 * @param pos Индекс.
	 * @return Клетка по индексу.
	 */
	public Cell getCellByPos(int pos) {
		return cells[pos];
	}
}
