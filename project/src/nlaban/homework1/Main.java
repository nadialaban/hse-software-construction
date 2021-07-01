package nlaban.homework1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
import nlaban.homework1.cells.BankCell;
import nlaban.homework1.roles.Player;

public class Main {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int money, height, width;

		if (args.length != 3) {
			System.out.println("Please enter correct arguments!");
			System.out.println("Arguments: <height> <width> <starting capital>");
			return;
		}

		try {
			height = Integer.parseInt(args[0]);
			width = Integer.parseInt(args[1]);
			money = Integer.parseInt(args[2]);
		} catch (NumberFormatException e) {
			System.out.println("Please enter correct arguments!");
			System.out.println("All arguments should be integers.");
			return;
		}

		Game.setUpGame(money, height, width);

		// Выыодим информацию
		System.out.print(Game.getFullInformation());

		do {
			System.out.println("Press ENTER to continue");

			try {
				br.readLine();
			} catch (IOException ignored) {
			}

			makeStep(scanner);
		} while (!Game.bot.isBankrupt() && !Game.player.isBankrupt());
	}

	/**
	 * Ход игрока.
	 *
	 * @param scanner сканнер.
	 */
	public static void makeStep(Scanner scanner) {
		System.out.print(Game.currentPlayer.step());

		if (Game.currentPlayer instanceof Player) {
			if (Game.currentPlayer.yn) {
				System.out.print(Game.currentPlayer.getCell()
						.getMessageOnYN(Game.currentPlayer, getYNAnswer(scanner)));
			}
			if (Game.currentPlayer.gettingCredit) {
				System.out.print(((BankCell) Game.currentPlayer.getCell())
						.getMessageOnGettingCredit(Game.currentPlayer,
								getInt(scanner, ((Player) Game.currentPlayer).getCreditLimit())));
			}
		}

		System.out.print(Game.getShortInformation());

		if (Game.currentPlayer.isBankrupt()) {
			System.out.printf("%s is a bankrupt. Game over!\n", Game.currentPlayer.getName());
		} else {
			Game.changePlayer();
		}

	}

	/**
	 * Получение ответа да/нет.
	 *
	 * @param scanner сканнер.
	 * @return True, если да.
	 */
	private static boolean getYNAnswer(Scanner scanner) {
		String val;
		do {
			val = scanner.nextLine();
			if (!val.equals("y") && !val.equals("n")) {
				System.out.println("Wrong format! (y/n) ");
			}
		} while (!val.equals("y") && !val.equals("n"));
		return val.equals("y");
	}


	/**
	 * Получение числа.
	 *
	 * @param scanner сканнер.
	 * @param lim     Предел.
	 * @return Число.
	 */
	private static int getInt(Scanner scanner, int lim) {
		int val;
		do {
			while (!scanner.hasNextInt()) {
				scanner.next();
				System.out.println("Please, enter correct value.");
			}
			val = scanner.nextInt();
			if (val <= 0 || val > lim) {
				System.out.println("Please enter correct value.");
			}
		} while (val <= 0 || val > lim);
		return val;
	}

}