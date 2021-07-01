package nlaban.homework3;

import java.util.Timer;
import java.util.TimerTask;
import nlaban.homework3.models.Creature;
import nlaban.homework3.models.Utils;

public class Main {

	// Constants.
	private static final int MIN_S = 1;
	private static final int MAX_S = 10;

	private static final int SWAN_ANGLE = 60;
	private static final int CRAYFISH_ANGLE = 180;
	private static final int PIKE_ANGLE = 300;

	private static final int DURATION_SEC = 25;
	private static final int PERIOD_SEC = 2;

	// Если раскомментить строки, будет более красивый вывод,
	// но умрет покрытие тестами...

	public static void main(String[] args) {
		Creature.getCart().setCoordinates(args);
		// System.out.println(Creature.getCart());

		var swan = new Creature("Swan", Utils.getRandomDouble(MIN_S, MAX_S), SWAN_ANGLE);
		var crayfish = new Creature("Crayfish", Utils.getRandomDouble(MIN_S, MAX_S), CRAYFISH_ANGLE);
		var pike = new Creature("Pike", Utils.getRandomDouble(MIN_S, MAX_S), PIKE_ANGLE);
		// System.out.printf("Creatures:\n- %s\n- %s\n- %s\n%n", swan, crayfish, pike);

		var swanThread = new Thread(swan);
		var crayfishThread = new Thread(crayfish);
		var pikeThread = new Thread(pike);

		swanThread.start();
		crayfishThread.start();
		pikeThread.start();

		var repeatedTask = new TimerTask() {
			public void run() {
				System.out.println(Creature.getCart());
			}
		};
		var timer = new Timer("Timer");
		timer.scheduleAtFixedRate(repeatedTask, PERIOD_SEC * 1000, PERIOD_SEC * 1000);

		try {
			pikeThread.join(DURATION_SEC * 1000);
		} catch (InterruptedException e) {
			// System.out.println("Some problems...");
			System.exit(0);
		}

		swanThread.interrupt();
		crayfishThread.interrupt();
		pikeThread.interrupt();
		timer.cancel();

		System.out.printf("\nFinal position:\n%s\n", Creature.getCart());
	}
}
