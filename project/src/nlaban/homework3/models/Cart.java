package nlaban.homework3.models;

public class Cart {
	/**
	 * X position.
	 */
	private double x = 0;

	/**
	 * Y position.
	 */
	private double y = 0;

	/**
	 * Method to set coordinates from arguments.
	 *
	 * @param args - arguments.
	 */
	public void setCoordinates(String[] args) {
		if (args == null || args.length > 2) {
			System.out.println("Please enter correct arguments next time!");
			System.out.println("Arguments: <x> <y>");
		} else {
			if (args.length > 0) {
				try {
					x = Double.parseDouble(args[0]);
					if (args.length == 2) {
						y = Double.parseDouble(args[1]);
					}
				} catch (NumberFormatException e) {
					System.out.println("Please, enter correct arguments next time!");
					System.out.println("All arguments should be doubles.");
					System.out.println("Default values will be used now.");
				}
			}
		}
	}

	/**
	 * Setter for X.
	 *
	 * @param x - new x position.
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * getter for X.
	 *
	 * @return x.
	 */
	public double getX() {
		return x;
	}

	/**
	 * Setter for Y.
	 *
	 * @param y - new Y position.
	 */
	public void setY(double y) {
		this.y = y;
	}

	/**
	 * Getter for Y.
	 *
	 * @return Y.
	 */
	public double getY() {
		return y;
	}

	/**
	 * @return - Description of cart position.
	 */
	@Override
	public String toString() {
		return String.format("\tThe cart is on the position (%.2f; %.2f) now.", x, y);
	}
}
