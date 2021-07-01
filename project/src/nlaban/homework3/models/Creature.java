package nlaban.homework3.models;

public class Creature implements Runnable {

	/**
	 * Radian.
	 */
	private static final double RAD = Math.PI/180;

	/**
	 * Minimal value of delay.
	 */
	private static final int MIN_DELAY = 1000;
	/**
	 * Maximal value of delay.
	 */
	private static final int MAX_DELAY = 5000;

	/**
	 * Name of creature.
	 */
	private final String name;
	/**
	 * Coefficient.
	 */
	private final double s;
	/**
	 * Angle to pull the cart.
	 */
	private final double angle;

	/**
	 * The cart.
	 */
	private static final Cart cart = new Cart();

	/**
	 * Constructor of creature.
	 *
	 * @param name  - creature's name.
	 * @param s     - moving coefficient.
	 * @param angle - moving angle.
	 */
	public Creature(String name, double s, double angle) {
		this.name = name;
		this.s = s;
		this.angle = angle;
	}

	/**
	 * Getter for cart.
	 * @return cart.
	 */
	public static Cart getCart() {
		return cart;
	}

	/**
	 * Moving the cart.
	 */
	public void move() {
		cart.setX(cart.getX() + s * Math.cos(angle * RAD));
		cart.setY(cart.getY() + s * Math.sin(angle * RAD));
	}

	/**
	 * When an object implementing interface <code>Runnable</code> is used to create a thread,
	 * starting the thread causes the object's
	 * <code>run</code> method to be called in that separately executing
	 * thread.
	 * <p>
	 * The general contract of the method <code>run</code> is that it may take any action whatsoever.
	 *
	 * @see Thread#run()
	 */
	@Override
	public void run() {
		while (!Thread.currentThread().isInterrupted()) {
			synchronized (cart) {
				move();
				// System.out.printf("> %s pulls the cart!%n", name);
			}

			try {
				//noinspection BusyWait
				Thread.sleep(Utils.getRandomInt(MIN_DELAY, MAX_DELAY));
			} catch (InterruptedException e) {
				return;
			}
		}
	}

	/**
	 * @return - Description of creature.
	 */
	@Override
	public String toString() {
		return String.format("%s [s: %.2f\tangle: %.2f]", name, s, angle);
	}
}
