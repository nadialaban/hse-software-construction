package nlaban.homework2.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.text.ParseException;
import java.util.Arrays;
import nlaban.homework2.GenealogicalTree;
import nlaban.homework2.models.ConservatorMartian;
import nlaban.homework2.models.InnovatorMartian;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Тестирование дерева")
class GenealogicalTreeTest {

	@DisplayName("Парсинг дерева")
	@Test
	void ParseTree() throws ParseException {
		// TODO написать нормальный тест если успею
		// Проверяем, если формат стринг.
		var martian1 = GenealogicalTree.ParseTree("InnovatorMartian (String:James)");
		var expected1 = new InnovatorMartian<>("James");
		assertEquals(GenealogicalTree.getReport(expected1), GenealogicalTree.getReport(martian1));

		// Проверяем, если формат инт.
		var martian2 = GenealogicalTree.ParseTree("InnovatorMartian (Integer:1)");
		var expected2 = new InnovatorMartian<>(1);
		assertEquals(GenealogicalTree.getReport(expected2), GenealogicalTree.getReport(martian2));

		// Проверяем, если формат дабл.
		var martian3 = GenealogicalTree.ParseTree("InnovatorMartian (Double:1.1)");
		var expected3 = new InnovatorMartian<>(1.1);
		assertEquals(GenealogicalTree.getReport(expected3), GenealogicalTree.getReport(martian3));

		// Проверяем
		var martian4 = GenealogicalTree.ParseTree("ConservatorMartian (String:James)");
		var expected4 = new ConservatorMartian<>((InnovatorMartian<Object>)martian1);
		assertEquals(GenealogicalTree.getReport(expected4), GenealogicalTree.getReport(martian4));

		// Проверяем дерево с вложенностью,
		var expected5  = "InnovatorMartian (Integer:1)\n"
									+ "    InnovatorMartian (Integer:2)\n"
									+ "        InnovatorMartian (Integer:3)\n"
									+ "    InnovatorMartian (Integer:4)";
		var martian5 = GenealogicalTree.ParseTree(expected5);
		assertEquals(expected5, GenealogicalTree.getReport(martian5));

		// Проверяем, если неверный формат.
		assertThrows(ParseException.class, () -> GenealogicalTree.ParseTree("aaa"));
	}
}