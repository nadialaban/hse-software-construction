import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import nlaban.hw4.lib.interfaces.ValidationError;
import nlaban.hw4.lib.ValidatorImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tools.AllAnnotationsClass;
import tools.ConstrainedAnnotationClass;
import tools.ListAnnotationsClass;
import tools.TestExceptionsClass;

@DisplayName("Тестирование библиотеки аннотаций")
class ValidatorTest {

	AllAnnotationsClass obj;
	List<ValidationError> errors;
	ValidatorImpl validator = new ValidatorImpl();

	@BeforeEach
	void prepareObject() {
		obj = new AllAnnotationsClass();
	}

	List<ValidationError> getListOfErrors(Object obj) {
		return new ArrayList<>(validator.validate(obj));
	}

	@Test
	@DisplayName("Все аннотации с допустимыми значениями")
	void allValid() {
		var fields = obj.getClass().getDeclaredFields();

		// проверяем, что все поля кроме notNull имеют значение null
		// то есть они валидны
		for (var field : fields) {
			if (!field.getName().equals("notNull")) {
				try {
					var value = field.get(obj);
					assertNull(value);
				} catch (IllegalAccessException e) {
					// сюда не должно заходить, тк все поля паблик
				}
			}
		}

		assertNotNull(obj.notNull);

		// Поскольку все поля имеют допустимые значения - 0 ошибок
		errors = getListOfErrors(obj);
		assertEquals(0, errors.size());
	}

	@Test
	@DisplayName("Исключения")
	void exceptionsTest() {
		var errorObj = new TestExceptionsClass();
		errors = getListOfErrors(errorObj);
		// Проверяем, что поля были пропущены валидатором,
		// тк если бы можно было кидать исключение, оно было бы проброшено и проверка бы прервалась
		assertEquals(0, errors.size());

		// Проверяем все ли описанные в классе исключения добавились
		var exceptions = validator.getExceptions();

		// Неправильный тип объекта
		assertTrue(exceptions.contains("error: invalid type (AnyOf works with String)"));
		assertTrue(exceptions.contains("error: invalid type (InRage works with decimal numbers)"));
		assertTrue(exceptions.contains("error: invalid type (Positive works with decimal numbers)"));
		assertTrue(exceptions.contains("error: invalid type (Negative works with decimal numbers)"));
		assertTrue(exceptions.contains("error: invalid type (NotBlank works with String)"));
		assertTrue(exceptions.contains("error: invalid type (Size works with String, Map, List and Set)"));

		// Неправильные границы
		assertTrue(exceptions.contains("error: conflicting params (InRange)"));
		assertTrue(exceptions.contains("error: conflicting params (Size)"));

		// Конфликтующие аннотации
		assertTrue(exceptions.contains("error: conflicting annotations (value couldn't be both Negative and Positive)"));

	}

	@Test
	@DisplayName("Аннотация @NotBull - допустимое значение")
	void notNullValid() {
		// Значение допустимо
		obj.notNull = false;

		// Ошибки нет и не должно быть
		errors = getListOfErrors(obj);
		assertEquals(0, errors.size());
	}

	@Test
	@DisplayName("Аннотация @NotNull - недопустимое значение")
	void notNullInvalid() {
		// Значение недопустимо
		obj.notNull = null;
		errors = getListOfErrors(obj);

		// Ошибка есть и это та ошибка, которую мы ждем
		assertEquals(1, errors.size());
		assertNull(errors.get(0).getFailedValue());
		assertEquals("must not be null", errors.get(0).getMessage());
		assertEquals("notNull", errors.get(0).getPath());
	}

	@Test
	@DisplayName("Аннотация @AnyOf - допустимое значение")
	void anyOfValid() {
		// Значение среди допустимых
		obj.anyOf = "a";

		// Ошибки нет и не должно быть
		errors = getListOfErrors(obj);
		assertEquals(0, errors.size());
	}

	@Test
	@DisplayName("Аннотация @AnyOf - недопустимое значение")
	void anyOfInvalid() {
		// Значение вне допустимых
		obj.anyOf = "d";
		errors = getListOfErrors(obj);

		// Ошибка есть
		assertEquals(1, errors.size());
		assertEquals(obj.anyOf, errors.get(0).getFailedValue());
		assertEquals("value '" + obj.anyOf + "' is not valid", errors.get(0).getMessage());
		assertEquals("anyOf", errors.get(0).getPath());
	}

	@Test
	@DisplayName("Аннотация @InRange - допустимое значение")
	void inRangeValid() {
		// Значение внутри допустимого интервала
		obj.inRange = 4;

		// Ошибки нет и не должно быть
		errors = getListOfErrors(obj);
		assertEquals(0, errors.size());
	}

	@Test
	@DisplayName("Аннотация @InRange - недопустимое значение")
	void inRangeInvalid() {
		// Значение вне допустимого интервала
		obj.inRange = 6;
		errors = getListOfErrors(obj);

		// Ошибка есть
		assertEquals(1, errors.size());
		assertEquals(obj.inRange, errors.get(0).getFailedValue());
		assertEquals("value should be in range [2;5]", errors.get(0).getMessage());
		assertEquals("inRange", errors.get(0).getPath());
	}

	@Test
	@DisplayName("Аннотация @Negative - допустимое значение")
	void negativeValid() {
		// Значение внутри допустимого интервала
		obj.neg = -1;

		// Ошибки нет и не должно быть
		errors = getListOfErrors(obj);
		assertEquals(0, errors.size());
	}

	@Test
	@DisplayName("Аннотация @Negative - недопустимое значение")
	void negativeInvalid() {
		// Значение вне допустимого интервала
		obj.neg = 1;
		errors = getListOfErrors(obj);

		// Ошибка есть
		assertEquals(1, errors.size());
		assertEquals(obj.neg, errors.get(0).getFailedValue());
		assertEquals("value should be negative", errors.get(0).getMessage());
		assertEquals("neg", errors.get(0).getPath());
	}

	@Test
	@DisplayName("Аннотация @Positive - допустимое значение")
	void positiveValid() {
		// Значение внутри допустимого интервала
		obj.pos = 1;

		// Ошибки нет и не должно быть
		errors = getListOfErrors(obj);
		assertEquals(0, errors.size());
	}

	@Test
	@DisplayName("Аннотация @Positive - недопустимое значение")
	void positiveInvalid() {
		// Значение вне допустимого интервала
		obj.pos = -1;
		errors = getListOfErrors(obj);

		// Ошибка есть
		assertEquals(1, errors.size());
		assertEquals(obj.pos, errors.get(0).getFailedValue());
		assertEquals("value should be positive", errors.get(0).getMessage());
		assertEquals("pos", errors.get(0).getPath());
	}

	@Test
	@DisplayName("Аннотация @NotBlank - допустимое значение")
	void notBlankValid() {
		// Значение допустимо
		obj.notBlank = "not blank";

		// Ошибки нет и не должно быть
		errors = getListOfErrors(obj);
		assertEquals(0, errors.size());
	}

	@Test
	@DisplayName("Аннотация @NotBlank - недопустимое значение")
	void notBlankInvalid() {
		// Значение недопустимо
		obj.notBlank = "    ";
		errors = getListOfErrors(obj);

		// Ошибка есть
		assertEquals(1, errors.size());
		assertEquals(obj.notBlank, errors.get(0).getFailedValue());
		assertEquals("value should not be blank", errors.get(0).getMessage());
		assertEquals("notBlank", errors.get(0).getPath());
	}

	@Test
	@DisplayName("Аннотация @NotEmpty - допустимое значение")
	void notEmptyValid() {
		// Значения допустимы
		obj.notEmptyString = "not empty";
		obj.notEmptyList = List.of(1);
		obj.notEmptyMap = Map.of("ONE", 1);
		obj.notEmptySet = Set.of(1);

		// Ошибки нет и не должно быть
		errors = getListOfErrors(obj);
		assertEquals(0, errors.size());
	}

	@Test
	@DisplayName("Аннотация @NotEmpty - недопустимое значение")
	void notEmptyInvalid() {
		// Значение недопустимы
		obj.notEmptyString = "";
		obj.notEmptyMap = Map.of();
		obj.notEmptyList = List.of();
		obj.notEmptySet = Set.of();

		errors = getListOfErrors(obj);
		errors.sort(Comparator.comparing(ValidationError::getPath));
		// Ошибки есть
		assertEquals(4, errors.size());

		assertEquals(obj.notEmptyList, errors.get(0).getFailedValue());
		assertEquals("collection should not be empty", errors.get(0).getMessage());
		assertEquals("notEmptyList", errors.get(0).getPath());

		assertEquals(obj.notEmptyMap, errors.get(1).getFailedValue());
		assertEquals("map should not be empty", errors.get(1).getMessage());
		assertEquals("notEmptyMap", errors.get(1).getPath());

		assertEquals(obj.notEmptySet, errors.get(2).getFailedValue());
		assertEquals("collection should not be empty", errors.get(2).getMessage());
		assertEquals("notEmptySet", errors.get(2).getPath());

		assertEquals(obj.notEmptyString, errors.get(3).getFailedValue());
		assertEquals("string should not be empty", errors.get(3).getMessage());
		assertEquals("notEmptyString", errors.get(3).getPath());

	}

	@Test
	@DisplayName("Аннотация @Size - допустимое значение")
	void sizeValid() {
		// Значения допустимы
		obj.sizeString = "12";
		obj.sizeList = List.of(1, 2);
		obj.sizeMap = Map.of("ONE", 1,"TWO",2);
		obj.sizeSet = Set.of(1, 2);

		// Ошибки нет и не должно быть
		errors = getListOfErrors(obj);
		assertEquals(0, errors.size());
	}

	@Test
	@DisplayName("Аннотация @Size - недопустимое значение")
	void sizeInvalid() {
		// Значения недопустимы
		obj.sizeString = "1";
		obj.sizeMap = Map.of("ONE", 1);
		obj.sizeList = List.of(1);
		obj.sizeSet = Set.of(1);

		errors = getListOfErrors(obj);
		errors.sort(Comparator.comparing(ValidationError::getPath));

		// Ошибки есть
		assertEquals(4, errors.size());

		assertEquals(obj.sizeList, errors.get(0).getFailedValue());
		assertEquals("collection size should be in range [2;4]", errors.get(0).getMessage());
		assertEquals("sizeList", errors.get(0).getPath());

		assertEquals(obj.sizeMap, errors.get(1).getFailedValue());
		assertEquals("map size should be in range [2;4]", errors.get(1).getMessage());
		assertEquals("sizeMap", errors.get(1).getPath());

		assertEquals(obj.sizeSet, errors.get(2).getFailedValue());
		assertEquals("collection size should be in range [2;4]", errors.get(2).getMessage());
		assertEquals("sizeSet", errors.get(2).getPath());

		assertEquals(obj.sizeString, errors.get(3).getFailedValue());
		assertEquals("string length should be in range [2;4]", errors.get(3).getMessage());
		assertEquals("sizeString", errors.get(3).getPath());

	}

	@Test
	@DisplayName("Аннотация @Constrained")
	void constrained() {
		// Значения заранее прописаны в классе
		var obj = new ConstrainedAnnotationClass();
		errors = getListOfErrors(obj);

		// Ошибка поля класса, помеченного аннотацией @Constrained есть
		// Ошибки поля класса, не помеченного аннотацией @Constrained – нет
		assertEquals(1, errors.size());
		assertEquals(obj.constrained.n, errors.get(0).getFailedValue());
		assertEquals("value should be positive", errors.get(0).getMessage());
		assertEquals("constrained.n", errors.get(0).getPath());
	}

	@Test
	@DisplayName("Аннотация в списках")
	void listAnnottaions() {
		// Значения заранее прописаны
		var obj = new ListAnnotationsClass();

		errors = getListOfErrors(obj);
		errors.sort(Comparator.comparing(ValidationError::getPath));
		assertEquals(7, errors.size());

		// Элемены списка – объекты класса, помеченного аннотацией @Constrained
		// Аннотация внутри списка сработала
		assertEquals(obj.listConstrained.get(0), errors.get(2).getFailedValue());
		assertEquals("must not be null", errors.get(2).getMessage());
		assertEquals("listConstrained[0]", errors.get(2).getPath());
		// Аннотация внутри элемента списка сработала
		assertEquals(obj.listConstrained.get(2).n, errors.get(3).getFailedValue());
		assertEquals("value should be positive", errors.get(3).getMessage());
		assertEquals("listConstrained[2].n", errors.get(3).getPath());

		// Вложенный список с аннотациями на каждом уровне
		// Аннотация на первом уровне списка сработала
		assertEquals(obj.innerListAnnotation.get(0), errors.get(0).getFailedValue());
		assertEquals("collection should not be empty", errors.get(0).getMessage());
		assertEquals("innerListAnnotation[0]", errors.get(0).getPath());
		// Аннотация на втором уровне списка сработала
		assertEquals(obj.innerListAnnotation.get(1).get(1), errors.get(1).getFailedValue());
		assertEquals("value should not be blank", errors.get(1).getMessage());
		assertEquals("innerListAnnotation[1][1]", errors.get(1).getPath());

		// Дважды вложенный список с аннотациями на каждом уровне
		// Аннотация на первом уровне списка сработала
		assertEquals(obj.twiceInnerListAnnotation.get(0), errors.get(4).getFailedValue());
		assertEquals("must not be null", errors.get(4).getMessage());
		assertEquals("twiceInnerListAnnotation[0]", errors.get(4).getPath());
		// Аннотация на втором уровне списка сработала
		assertEquals(obj.twiceInnerListAnnotation.get(1).get(0), errors.get(5).getFailedValue());
		assertEquals("collection should not be empty", errors.get(5).getMessage());
		assertEquals("twiceInnerListAnnotation[1][0]", errors.get(5).getPath());
		// Аннотация на тетьем уровне списка сработала
		assertEquals(obj.twiceInnerListAnnotation.get(2).get(1).get(2), errors.get(6).getFailedValue());
		assertEquals("value should be negative", errors.get(6).getMessage());
		assertEquals("twiceInnerListAnnotation[2][1][2]", errors.get(6).getPath());
	}

}