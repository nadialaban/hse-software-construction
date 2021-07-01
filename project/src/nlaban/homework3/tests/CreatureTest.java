package nlaban.homework3.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import nlaban.homework3.models.Creature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Тестирование существа")
class CreatureTest {

	@DisplayName("Перемещение")
	@Test
	void move() {
		var swan = new Creature("Swan", 5, 60);
		swan.move();
		assertEquals("2,5", String.format("%.1f", Creature.getCart().getX()));
		assertEquals("4,33", String.format("%.2f", Creature.getCart().getY()));
	}

	@DisplayName("Строковое представление")
	@Test
	void testToString() {
		var swan = new Creature("Swan", 5, 60);
		var exp = "Swan [s: 5,00\tangle: 60,00]";
		assertEquals(exp, swan.toString());
	}
}