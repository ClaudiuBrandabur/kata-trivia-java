package trivia;

import java.util.Random;
import java.util.Scanner;

// DON'T TOUCH THIS CLASS. DON'T REFACTOR THIS CLASS.
// ONLY RUN IT TO MANUALLY PLAY THE GAME YOURSELF TO UNDERSTAND THE PROBLEM
public class PlayGame {

	private static final Scanner scanner = new Scanner(System.in);

	public static void main(String[] args) {
		System.out.println("\n\n*** Welcome to Trivia Game ***\n\n");

		IGame game = initialiseGame();

		System.out.println("--Starting game--\n\n");
		playTheGame(game);
		System.out.println("\n\n>> Game won!!!\n\n");
	}

	private static IGame initialiseGame() {
		System.out.println("Initialise Game\n");
		int numberOfPlayers = setNumberOfPlayers();

		IGame game = new GameBetter();

		addPlayersToTheGame(numberOfPlayers, game);

		if (!game.isPlayable()) {
			System.err.println("The Game is not playable with only " + numberOfPlayers + " player(s).");
			System.err.println("There must be a minimum of 2 players to start the game.\n");
			try {
				Thread.sleep(250);
			} catch (InterruptedException interruptedException) {
				System.err.println(interruptedException);
			}
			return initialiseGame();
		}

		return game;
	}

	private static int setNumberOfPlayers() {
		System.out.println("Enter number of players: 1 - 6");

		String input = scanner.nextLine().trim().toLowerCase();
		if (!input.matches("\\d+")) {
			System.err.println("Insert only numbers please!");
			return setNumberOfPlayers();
		}

		int numberOfPlayers = Integer.parseInt(input);
		if (numberOfPlayers < 1 || numberOfPlayers > 6) {
			System.err.println("Number of players must be between 1 - 6");
			return setNumberOfPlayers();
		}
		return numberOfPlayers;
	}

	private static void addPlayersToTheGame(int numberOfPlayers, IGame aGame) {
		System.out.println("Reading names for " + numberOfPlayers + " players:");
		for (int i = 1; i <= numberOfPlayers; i++) {
			System.out.println("Player " + i + " name: ");
			String playerName = scanner.nextLine();
			boolean playerAdded = aGame.add(playerName);
			if (!playerAdded) {
				System.err.println("Player " + i + " name " + playerName + " was not added, the name already exists.");
				System.err.println("Please insert new name for the player " + i + ".");
				--i;
			}
			System.out.println("\n");
		}
	}

	private static void playTheGame(IGame game) {
		boolean notAWinner;
		do {
			int roll = readRoll();
			game.roll(roll);

			System.out.println(">> Was the answer correct? [y/n] ");
			boolean correct = readYesNo();
			if (correct) {
				notAWinner = game.wasCorrectlyAnswered();
			} else {
				notAWinner = game.wrongAnswer();
			}
		} while (notAWinner);
	}

	private static int readRoll() {
		System.out.println(">> Throw a die and input roll, or [ENTER] to generate a random roll: ");
		String rollStr = scanner.nextLine().trim();
		if (rollStr.isEmpty()) {
			int roll = new Random().nextInt(6) + 1;
			System.out.println(">> Random roll: " + roll);
			return roll;
		}
		if (!rollStr.matches("\\d+")) {
			System.err.println("Not a number: '" + rollStr + "'");
			return readRoll();
		}
		int roll = Integer.parseInt(rollStr);
		if (roll < 1 || roll > 6) {
			System.err.println("Invalid roll. Must be between 1 - 6.");
			return readRoll();
		}
		return roll;
	}

	private static boolean readYesNo() {
		String yn = scanner.nextLine().trim().toUpperCase();
		if (!yn.matches("[YN]")) {
			System.err.println("y or n please");
			return readYesNo();
		}
		return yn.equalsIgnoreCase("Y");
	}

}
