/**
 * @author <a href="mailto:naabuallaban@edu.hse.ru"> Nadia Abu Al Laban</a>
 */
package nlaban.homework1.roles;

public class Bot extends Role {

	/**
	 * Конструктор бота.
	 *
	 * @param money Стартовый капитал.
	 */
	public Bot(int money) {
		super(money, "Bot");
	}

	/**
	 * Описание Бота.
	 *
	 * @return Строковое представление бота.
	 */
	@Override
	public String toString() {
		return String.format("BOT\t[%s]\n", super.toString());
	}

}
