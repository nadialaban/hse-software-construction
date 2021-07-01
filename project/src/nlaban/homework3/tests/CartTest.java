package nlaban.homework3.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import nlaban.homework3.models.Cart;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Тестирование класса телеги")
class CartTest {

	@DisplayName("Установка координат")
	@Test
	void setCoordinates() {
		var cart = new Cart();
		assertEquals(0, cart.getX());
		assertEquals(0, cart.getY());

		cart.setX(1);
		cart.setY(2);
		assertEquals(1, cart.getX());
		assertEquals(2, cart.getY());

		var args = new String[]{"0", "0", "0"};
		cart.setCoordinates(args);
		assertEquals(1, cart.getX());
		assertEquals(2, cart.getY());

		args = new String[]{"0", "0"};
		cart.setCoordinates(args);
		assertEquals(0, cart.getX());
		assertEquals(0, cart.getY());

		args = new String[]{"aa"};
		cart.setCoordinates(args);
		assertEquals(0, cart.getX());
	}

	@DisplayName("Строковое представление")
	@Test
	void testToString() {
		var cart = new Cart();
		var exp = "\tThe cart is on the position (0,00; 0,00) now.";
		assertEquals(exp, cart.toString());
	}
}
