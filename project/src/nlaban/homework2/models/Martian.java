/**
 * @author <a href="mailto:naabuallaban@edu.hse.ru"> Nadia Abu Al Laban</a>
 */
package nlaban.homework2.models;

import java.util.ArrayList;
import java.util.Collection;

public interface Martian<T> {

	/**
	 * Проверяет, есть ли потомок с заданным генкодом.
	 *
	 * @param val - генкод.
	 * @return true, если есть, иначе false.
	 */
	boolean hasDescendantWithValue(T val);

	/**
	 * Проверяет, есть ли ребенок с заданным генкодом.
	 *
	 * @param val - генкод.
	 * @return true, если есть, иначе false.
	 */
	boolean hasChildWithValue(T val);

	/**
	 * Геттер для детей.
	 *
	 * @return Коллекцию детей.
	 */
	Collection<Martian<T>> getChildren();

	/**
	 * Геттер для потомков.
	 *
	 * @return Коллекцию потомков.
	 */
	Collection<Martian<T>> getDescendants();

	/**
	 * Геттер для генкода.
	 *
	 * @return Генетический код.
	 */
	T getValue();

	/**
	 * Геттер для родителя.
	 *
	 * @return Объект-родителя.
	 */
	Martian<T> getParent();

}
