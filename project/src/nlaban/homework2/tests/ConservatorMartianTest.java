package nlaban.homework2.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import nlaban.homework2.models.ConservatorMartian;
import nlaban.homework2.models.InnovatorMartian;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Тестирование Марсианина-Консерватора")
class ConservatorMartianTest {

	InnovatorMartian<Integer> im1;
	InnovatorMartian<Integer> im2;
	InnovatorMartian<Integer> im3;
	InnovatorMartian<Integer> im4;
	InnovatorMartian<Integer> im5;

	@BeforeEach
	void Refresh() {
		im1 = new InnovatorMartian<>(1);
		im2 = new InnovatorMartian<>(2);
		im3 = new InnovatorMartian<>(3);
		im4 = new InnovatorMartian<>(4);
		im5 = new InnovatorMartian<>(5);
	}


	/**
	 * Тестирование конструктора консерватора.
	 */
	@DisplayName("Создание консерваторов")
	@Test
	void createConservator() {
		// Создаем дерево новаторов.
		im1.addChild(im2);
		im2.setChildren(new ArrayList<>(Arrays.asList(im3, im4)));
		im3.addChild(im5);

		// Создание консерватора.
		var cm = new ConservatorMartian<>(im2);

		// Проверяем, что если менять новатора, консерватор остаются на месте.
		im2.setValue(22);
		assertNotEquals(im2.getValue(), cm.getValue());
		// Проверяем, что это же касается его предков.
		im3.removeChild(im5);
		assertNotEquals(im2.getDescendants().size(), cm.getDescendants().size());
		// Проверяем, что это же касается его предков.
		im3.addChild(im5);
		im3.setValue(6);
		@SuppressWarnings("unchecked")
		var cm1 = (ConservatorMartian<Integer>) cm.getChildren().toArray()[0];
		assertNotEquals(im3.getValue(), cm1.getValue());

	}

	/**
	 * Тестирование метода для получения родителя.
	 */
	@DisplayName("Получение родителя")
	@Test
	void getParent() {
		im2.setParent(im1);
		var cm1 = new ConservatorMartian<>(im1);
		@SuppressWarnings("unchecked")
		var cm2 = (ConservatorMartian<Integer>) cm1.getChildren().toArray()[0];
		assertNull(cm1.getParent());
		assertNotNull(cm2.getParent());
		assertEquals(cm1, cm2.getParent());
	}

	/**
	 * Тестирование метода для получения предков.
	 */
	@DisplayName("Получение предков")
	@Test
	void getDescendants() {
		// Создаем дерево новаторов.
		im1.addChild(im2);
		im2.addChild(im3);

		// Создаем консерватора
		var cm1 = new ConservatorMartian<>(im1);
		@SuppressWarnings("unchecked")
		var cm2 = (ConservatorMartian<Integer>) cm1.getChildren().toArray()[0];
		@SuppressWarnings("unchecked")
		var cm3 = (ConservatorMartian<Integer>) cm2.getChildren().toArray()[0];

		var col = new ArrayList<>(Arrays.asList(cm2, cm3));
		// Проверяем, совпадают ли дети.
		assertEquals(col, cm1.getDescendants());
	}

	/**
	 * Тестирование метода для поиска генкода.
	 */
	@DisplayName("Поиск генкода")
	@Test
	void hasSomebodyWithValue() {
		im1.addChild(im2);
		im2.addChild(im3);
		var cm1 = new ConservatorMartian<>(im1);
		// Проверяем знначния, которые есть.
		assertTrue(cm1.hasChildWithValue(2));
		assertTrue(cm1.hasDescendantWithValue(3));
		// Проверяем значения, которых нет.
		assertFalse(cm1.hasDescendantWithValue(4));
		assertFalse(cm1.hasChildWithValue(3));
	}


	/**
	 * Тестирование метода для получения строкового представления объекта.
	 */
	@DisplayName("Строковое представление")
	@Test
	void testToString() {
		var cm = new ConservatorMartian<>(im1);
		var reference = "ConservatorMartian (Integer:1)";
		assertEquals(reference, cm.toString());
	}
}
