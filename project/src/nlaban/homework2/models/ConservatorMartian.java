package nlaban.homework2.models;

import java.util.ArrayList;
import java.util.Collection;

public class ConservatorMartian<T> implements Martian<T> {

	// Генетический код.
	protected final T value;
	// Родитель марсианина.
	protected final ConservatorMartian<T> parent;
	// Коллекция детей.
	protected final Collection<ConservatorMartian<T>> children;

	/**
	 * Конструктор, принимающий иноватора.
	 *
	 * @param _martian - новатор.
	 */
	public ConservatorMartian(InnovatorMartian<T> _martian) {
		value = _martian.value;
		parent = null;
		children = new ArrayList<>();
		for (var child : _martian.children) {
			children.add(new ConservatorMartian<>((InnovatorMartian<T>) child, this));
		}
	}

	/**
	 * Геттер для генкода.
	 *
	 * @return Генетический код.
	 */
	public T getValue() {
		return value;
	}

	/**
	 * Геттер для родителя.
	 *
	 * @return Объект-родителя.
	 */
	public Martian<T> getParent() {
		return parent;
	}

	/**
	 * Конструктор, принимающий иноватора.
	 *
	 * @param _martian - новатор.
	 * @param _parent  - родитель-консерватор.
	 */
	private ConservatorMartian(InnovatorMartian<T> _martian, ConservatorMartian<T> _parent) {
		value = _martian.value;
		parent = _parent;
		children = new ArrayList<>();
		for (var child : _martian.children) {
			children.add(new ConservatorMartian<>(child, this));
		}
	}

	/**
	 * Геттер для детей.
	 *
	 * @return Коллекцию детей.
	 */
	public Collection<Martian<T>> getChildren() {
		//FIXME меняется
		return new ArrayList<>(children);
	}

	/**
	 * Геттер для потомков.
	 *
	 * @return Коллекцию потомков.
	 */
	public Collection<Martian<T>> getDescendants() {
		//FIXME меняется
		Collection<Martian<T>> descendants = new ArrayList<>();

		for (var child : children) {
			descendants.add(child);
			descendants.addAll(child.getDescendants());
		}

		return descendants;
	}

	/**
	 * Проверяет, есть ли потомок с заданным генкодом.
	 *
	 * @param val - генкод.
	 * @return true, если есть, иначе false.
	 */
	public boolean hasDescendantWithValue(T val) {
		var descendants = this.getDescendants();
		for (var descendant : descendants) {
			if (((ConservatorMartian<T>) descendant).value.equals(val)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Проверяет, есть ли ребенок с заданным генкодом.
	 *
	 * @param val - генкод.
	 * @return true, если есть, иначе false.
	 */
	public boolean hasChildWithValue(T val) {
		for (var child : children) {
			if (child.value.equals(val)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @return Строковое представление новатора.
	 */
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + " (" + value.getClass().getSimpleName() + ":" + value
				.toString() + ")";
	}

}
