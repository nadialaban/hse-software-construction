package nlaban.homework3.tests;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

import nlaban.homework3.models.Utils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Тестирование утилит")
class UtilsTest {

	@DisplayName("Рандом")
	@Test
	void getRandom() {
		var a = Utils.getRandomInt(-10, 10);
		var b = Utils.getRandomInt(-10, 10);
		assertNotEquals(a, b);
		var c = Utils.getRandomDouble(-10, 10);
		var d = Utils.getRandomDouble(-10, 10);
		assertNotEquals(c, d);
	}
}