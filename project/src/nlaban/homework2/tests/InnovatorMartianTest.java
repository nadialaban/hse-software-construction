package nlaban.homework2.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import nlaban.homework2.models.InnovatorMartian;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Тестирование Марсианина-Новатора")
class InnovatorMartianTest {

	InnovatorMartian<Integer> m1;
	InnovatorMartian<Integer> m2;
	InnovatorMartian<Integer> m3;
	InnovatorMartian<Integer> m4;
	InnovatorMartian<Integer> m5;

	@BeforeEach
	void Refresh() {
		m1 = new InnovatorMartian<>(1);
		m2 = new InnovatorMartian<>(2);
		m3 = new InnovatorMartian<>(3);
		m4 = new InnovatorMartian<>(4);
		m5 = new InnovatorMartian<>(5);
	}

	/**
	 * Тестирование метода для установки генкода.
	 */
	@DisplayName("Установка генкода")
	@Test
	void setValue() {
		assertEquals(1, m1.getValue());
		m1.setValue(2);
		assertEquals(2, m1.getValue());
	}

	/**
	 * Тестирование метода для установки родителя.
	 */
	@DisplayName("Установка родителя")
	@Test
	void setParent() {
		// Проверяем, лежит ли  при создании null.
		var child = new InnovatorMartian<>(1);
		assertNull(child.getParent());

		// Проверяем, добавился ли родитель после setParent.
		var parent = new InnovatorMartian<>(2);
		assertTrue(child.setParent(parent));
		assertEquals(parent, child.getParent());

		// Проверяем, что нельзя сделать родителем себя.
		assertFalse(parent.setParent(parent));

		// Проверяем, что можнно поменять родителя,
		// при этом у старого родителя обновляется список детей
		var grandChild = new InnovatorMartian<>(3);
		assertTrue(grandChild.setParent(child));
		assertTrue(grandChild.setParent(parent));
		assertTrue(child.getChildren().isEmpty());

		// Проверяем, что нельзя сделать своим родителем своего потомка
		assertFalse(parent.setParent(child));
	}

	/**
	 * Тестирование метода для установки детей.
	 */
	@DisplayName("Установка детей")
	@Test
	void setChildren() {
		// Проверяем, что будет, если передать налл.
		assertTrue(m1.setChildren(null));

		// Проверяем, что метод работает правильно.
		Collection<InnovatorMartian<Integer>> arr = new ArrayList<>(Arrays.asList(m2, m3));
		assertTrue(m1.setChildren(arr));
		assertEquals(arr, m1.getChildren());

		// Проверяем, что старые дети узнают, что мы не их родители.
		Collection<InnovatorMartian<Integer>> arr1 = new ArrayList<>(Arrays.asList(m4, m5));
		assertTrue(m1.setChildren(arr1));
		assertNull(m2.getParent());
		assertNull(m3.getParent());

		// Проверяем, что нельзя сделать себя ребенком.
		arr.add(m1);
		assertFalse(m1.setChildren(arr));

		// Проверяем, что нельзя сделать ребенком предка.
		var arr2 = new ArrayList<>(Collections.singletonList(m1));
		assertFalse(m4.setChildren(arr2));
	}

	/**
	 * Тестирование метода для добавления детей.
	 */
	@Test
	@DisplayName("Добавление ребенка")
	void addChild() {
		// Проверяем, что нельзя добавить налл.
		assertFalse(m1.addChild(null));

		// Проверяем, что метод работает.
		assertTrue(m1.addChild(m2));
		assertTrue(m1.getChildren().contains(m2));
		assertEquals(m1, m2.getParent());

		// Проверяем, что нельзя добавить себя.
		assertFalse(m1.addChild(m1));

		// Проверяем, что нельзя добавить своего предка.
		assertFalse(m2.addChild(m1));

		// Проверяем, что нельзя добавить своего ребенка.
		assertFalse(m1.addChild(m2));
	}

	/**
	 * Тестирование метода для удаления детей.
	 */
	@DisplayName("Удаление ребенка")
	@Test
	void removeChild() {
		// Проверяем, что нельзя убрать налл.
		assertFalse(m1.removeChild(null));

		// Проверяем, что метод работает.
		assertTrue(m1.addChild(m2));
		assertTrue(m1.getChildren().contains(m2));
		assertTrue(m1.removeChild(m2));

		// Проверяем, что сообщили родителю.
		assertFalse(m1.getChildren().contains(m2));

		// Проверяем, что сообщили ребенку.
		assertNull(m2.getParent());
	}

	/**
	 * Тестирование метода для поиска генкода.
	 */
	@DisplayName("Поиск генкода")
	@Test
	void hasSomebodyWithValue() {
		m1.addChild(m2);
		m2.addChild(m3);
		// Проверяем знначния, которые есть.
		assertTrue(m1.hasChildWithValue(2));
		assertTrue(m1.hasDescendantWithValue(3));
		// Проверяем значения, которых нет.
		assertFalse(m1.hasDescendantWithValue(4));
		assertFalse(m1.hasChildWithValue(3));
	}

	/**
	 * Тестирование метода для получения строкового представления объекта.
	 */
	@DisplayName("Строковое представление")
	@Test
	void testToString() {
		var reference = "InnovatorMartian (Integer:1)";
		assertEquals(reference, m1.toString());
	}

}