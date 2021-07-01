package nlaban.homework2;

import java.text.ParseException;
import java.util.regex.Pattern;
import nlaban.homework2.models.ConservatorMartian;
import nlaban.homework2.models.InnovatorMartian;
import nlaban.homework2.models.Martian;

public class GenealogicalTree {

	/**
	 * Возращает отчет для марсианина во главе дерева.
	 *
	 * @param head - марсианин^ стоящий во главе.
	 * @return Строковое представление дерева, во главе которого стоит марсианин.
	 */
	public static <T> String getReport(Martian<T> head) {
		var str = getReport(head, 0);
		return str.substring(0, str.length() - 1);
	}

	/**
	 * Возращает отчет для марсианина во главе дерева.
	 *
	 * @param current - текущий марсианин.
	 * @param lvl     - уровень вложенноти дерева.
	 * @return Строковое представление дерева, во главе которого стоит текущий марсианин.
	 */
	private static <T> String getReport(Martian<T> current, int lvl) {
		StringBuilder str = new StringBuilder("    ".repeat(lvl));
		str.append(current.toString()).append("\n");

		var children = current.getChildren();

		for (var child : children) {
			str.append(getReport(child, lvl + 1));
		}

		return str.toString();
	}

	/**
	 * Метод, который парсит дерево.
	 *
	 * @param tree - дерево в строковом формате.
	 * @param <T>  - Тип значения марсианина.
	 * @return - "корневого" марсианина.
	 * @throws ParseException - если строка неверная.
	 */
	public static <T> Martian<T> ParseTree(String tree) throws ParseException {
		var lines = tree.split("\n");
		var regex = "^(Innovator|Conservator)Martian \\((String|Integer|Double):(.*)\\)$";
		var pattern = Pattern.compile(regex);
		var matcher = pattern.matcher(lines[0]);

		if (!matcher.matches()) {
			throw new ParseException(String.format("Line doesn't match to regex!\n"
					+ "Line: \"%s\"\nRegex: \"%s\"\n", lines[0], regex), 0);
		}

		var martianType = matcher.group(1);
		var valType = matcher.group(2);
		var lvl = 0;
		InnovatorMartian<T> martian = ParseLine(lines[0], lvl++, martianType, valType);

		// считывать детей
		var parent = martian;
		var previous = martian;

		for (int i = 1; i < lines.length; i++) {
			var line = lines[i];
			InnovatorMartian<T> current;
			try {
				// Проверяем, читаем ли мы ребенка.
				current = ParseLine(line, lvl, martianType, valType);
			} catch (ParseException ex) {
				try {
					// Проверяем, читаем ли мы внука.
					current = ParseLine(line, lvl + 1, martianType, valType);
					lvl++;
					parent = previous;
				} catch (ParseException ex1) {
					// Проверяем, читаем ли мы брата.
					current = ParseLine(line, lvl - 1, martianType, valType);
					lvl--;
					parent = (InnovatorMartian<T>) parent.getParent();
				}

			}

			current.setParent(parent);
			previous = current;

		}

		if (martianType.equals("Conservator")) {
			return new ConservatorMartian<>(martian);
		}

		return martian;
	}

	/**
	 * Метод, который парсит строку.
	 *
	 * @param line        - строка.
	 * @param lvl         - уровень вложенности.
	 * @param martianType - тип марсианина.
	 * @param valType     - тип значения.
	 * @param <T>         - тип значения марсианина.
	 * @return марсианина.
	 * @throws ParseException - строка не соответствует типу.
	 */
	protected static <T> InnovatorMartian<T> ParseLine(String line, int lvl, String martianType,
			String valType)
			throws ParseException {
		var regex = String.format("^(    ){%d}%sMartian \\(%s:(.*)\\)$", lvl, martianType, valType);
		var pattern = Pattern.compile(regex);
		var matcher = pattern.matcher(line);

		if (!matcher.matches()) {
			throw new ParseException(String.format("Line doesn't match to regex!\n"
					+ "Line: \"%s\"\nRegex: \"%s\"\n", line, regex), 0);
		}

		InnovatorMartian<T> martian;
		if (valType.equals("Integer")) {
			// @SuppressWarnings("unchecked") // не работает...
			//noinspection unchecked
			martian = (InnovatorMartian<T>) new InnovatorMartian<>(Integer.parseInt(matcher.group(2)));
		} else if (valType.equals("Double")) {
			// @SuppressWarnings("unchecked") // не работает...
			//noinspection unchecked
			martian = (InnovatorMartian<T>) new InnovatorMartian<>(Double.parseDouble(matcher.group(2)));
		} else {
			// @SuppressWarnings("unchecked") // не работает...
			//noinspection unchecked
			martian = (InnovatorMartian<T>) new InnovatorMartian<>(matcher.group(2));
		}

		return martian;
	}
}
