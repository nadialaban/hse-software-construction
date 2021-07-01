package nlaban.homework2.models;

import java.util.ArrayList;
import java.util.Collection;

public class InnovatorMartian<T> implements Martian<T> {

	// Генетический код.
	protected T value;
	// Родитель марсианина.
	protected InnovatorMartian<T> parent;
	// Коллекция детей.
	protected Collection<InnovatorMartian<T>> children = new ArrayList<>();

	/**
	 * Конструктор, принимающий генкод и родителя.
	 *
	 * @param _value - значение генкода.
	 */
	public InnovatorMartian(T _value) {
		value = _value;
		parent = null;
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
			if (((InnovatorMartian<T>) descendant).value.equals(val)) {
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
	 * Метод для изменения генкода.
	 *
	 * @param val - генкод.
	 */
	public void setValue(T val) {
		value = val;
	}

	/**
	 * Метод для изменения родителя.
	 *
	 * @param _parent - новый родитель.
	 * @return true, если получилось, иначе false
	 */
	public boolean setParent(InnovatorMartian<T> _parent) {
		// Нельзя сделать родителем себя или своего потомка.
		if (_parent == null || this.getDescendants().contains(_parent) || this.equals(_parent)) {
			return false;
		}

		if (parent != null) {
			parent.children.remove(this);
		}

		_parent.children.add(this);
		parent = _parent;

		return true;
	}

	/**
	 * Метод для изменения детей.
	 *
	 * @param _children - коллекция детей.
	 * @return true, если получилось, иначе false
	 */
	public boolean setChildren(Collection<InnovatorMartian<T>> _children) {
		// Нельзя сделать своим ребенком себя или своего предка.
		if (_children == null) {
			children = new ArrayList<>();
			return true;
		}

		if (_children.contains(this)) {
			return false;
		}

		for (var child : _children) {
			if (child.getDescendants().contains(this)) {
				return false;
			}
		}

		// Говорим старым детям, что мы больше не их родитель. (прискорбно получается...)
		for (var child : children) {
			child.parent = null;
		}

		children = new ArrayList<>();

		// Говорим новым детям, что мы их новый родитель и сами запоминаем.
		for (var child : _children) {
			child.setParent(this);
		}

		return true;
	}

	/**
	 * Метод для добавления ребенка.
	 *
	 * @param child - новый ребенок.
	 * @return true, если получилось, иначе false.
	 */
	public boolean addChild(InnovatorMartian<T> child) {
		// Нельзя сделать своим ребенком себя или своего предка.
		if (child == null || child.equals(this) ||
				child.getDescendants().contains(this) || children.contains(child)) {
			return false;
		}

		child.setParent(this);
		return true;
	}

	/**
	 * Метод для удаления ребенка.
	 *
	 * @param child - ребенок.
	 * @return true, если получилось, иначе false.
	 */
	public boolean removeChild(InnovatorMartian<T> child) {
		// Нельзя удалить ребенка, которого у нас нет.
		if (child == null || !children.contains(child)) {
			return false;
		}

		child.parent = null;
		children.remove(child);

		return true;
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
