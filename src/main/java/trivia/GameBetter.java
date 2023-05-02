package trivia;

import java.util.ArrayList;
import java.util.LinkedList;

// REFACTOR ME
public class GameBetter implements IGame {

	private ArrayList players = new ArrayList();
	private int[] places = new int[6];
	private int[] purses = new int[6];
	private boolean[] inPenaltyBox = new boolean[6];
	private boolean[] isOnStreak = new boolean[6];
	private int[] consecutiveCorrectAnswers = new int[6];

	private LinkedList popQuestions = new LinkedList();
	private LinkedList scienceQuestions = new LinkedList();
	private LinkedList sportsQuestions = new LinkedList();
	private LinkedList rockQuestions = new LinkedList();
	private LinkedList geographyQuestions = new LinkedList();

	private int currentPlayer = 0;
	private boolean isGettingOutOfPenaltyBox;

	public GameBetter() {
		for (int i = 0; i < 50; i++) {
			popQuestions.addLast(createQuestion("Pop", i));
			scienceQuestions.addLast(createQuestion("Science", i));
			sportsQuestions.addLast(createQuestion("Sports", i));
			rockQuestions.addLast(createQuestion("Rock", i));
			geographyQuestions.addLast(createQuestion("Geography", i));
		}
	}

	private String createQuestion(String type, int index) {
		return type + " Question " + index;
	}

	private int howManyPlayers() {
		return players.size();
	}

	public boolean isPlayable() {
		return (howManyPlayers() >= 2);
	}

	private void resetPlayersTour() {
		if (currentPlayer == players.size()) {
			currentPlayer = 0;
		}
	}

	private void resetPlacesOfCurrentPlayer() {
		if (places[currentPlayer] > 14) {
			places[currentPlayer] = places[currentPlayer] - 15;
		}
	}

	private boolean didPlayerWin() {
		return !(purses[currentPlayer] == 12);
	}

	public boolean add(String playerName) {
		if (players.contains(playerName)) {
			return false;
		}

		players.add(playerName);
		places[howManyPlayers()] = 0;
		purses[howManyPlayers()] = 0;
		consecutiveCorrectAnswers[howManyPlayers()] = 0;
		inPenaltyBox[howManyPlayers()] = false;
		isOnStreak[howManyPlayers()] = false;

		System.out.println(playerName + " was added");
		System.out.println("They are player number " + players.size());
		return true;
	}

	public void roll(int roll) {
		System.out.println(players.get(currentPlayer) + " is the current player");
		System.out.println("They have rolled a " + roll);

		if (inPenaltyBox[currentPlayer]) {
			rollWhileInPenaltyBox(roll);
		} else {
			rollWhileNotInPenaltyBox(roll);
		}
	}

	private void rollWhileInPenaltyBox(int roll) {
		System.out.println(players.get(currentPlayer) + " is in the penalty box.");
		System.out.println(players.get(currentPlayer) + " have to roll an odd dice value to get out of the penalty box.");

		if (roll % 2 != 0) {
			System.out.println(players.get(currentPlayer) + " is getting out of the penalty box");
			isGettingOutOfPenaltyBox = true;

			rollWhileNotInPenaltyBox(roll);
		} else {
			System.out.println(players.get(currentPlayer) + " is not getting out of the penalty box");
			isGettingOutOfPenaltyBox = false;
		}
	}

	private void rollWhileNotInPenaltyBox(int roll) {
		places[currentPlayer] = places[currentPlayer] + roll;

		resetPlacesOfCurrentPlayer();

		System.out.println(players.get(currentPlayer) + "'s new location is " + places[currentPlayer]);
		System.out.println("The category is " + currentCategory());
		askQuestion();
	}

	private void askQuestion() {
		if ("Pop".equals(currentCategory())) {
			System.out.println(popQuestions.removeFirst());
		}
		if ("Science".equals(currentCategory())) {
			System.out.println(scienceQuestions.removeFirst());
		}
		if ("Sports".equals(currentCategory())) {
			System.out.println(sportsQuestions.removeFirst());
		}
		if ("Rock".equals(currentCategory())) {
			System.out.println(rockQuestions.removeFirst());
		}
		if ("Geography".equals(currentCategory())) {
			System.out.println(geographyQuestions.removeFirst());
		}
	}

	private String currentCategory() {
		switch (places[currentPlayer]) {
			case 0:
			case 5:
			case 10:
				return "Pop";
			case 1:
			case 6:
			case 11:
				return "Science";
			case 2:
			case 7:
			case 12:
				return "Sports";
			case 3:
			case 8:
			case 13:
				return "Rock";
			default:
				return "Geography";
		}
	}

	public boolean wasCorrectlyAnswered() {
		if (!inPenaltyBox[currentPlayer]) {
			return fluxCorrectlyAnswered();
		}

		if (isGettingOutOfPenaltyBox) {
			return fluxCorrectlyAnswered();
		}

		currentPlayer++;
		resetPlayersTour();
		return true;
	}

	private boolean fluxCorrectlyAnswered() {
		System.out.println("Answer was correct!!!!");
		consecutiveCorrectAnswers[currentPlayer]++;
		if (isOnStreak[currentPlayer]) {
			System.out.println(players.get(currentPlayer) + " is On Streak! Has a total of " + consecutiveCorrectAnswers[currentPlayer] + " consecutive correct answers!");
		}

		if (consecutiveCorrectAnswers[currentPlayer] == 2) {
			System.out.println(players.get(currentPlayer) + " got 2 consecutive correct answers! Is getting on a streak!");
			isOnStreak[currentPlayer] = true;
		}

		if (consecutiveCorrectAnswers[currentPlayer] % 3 == 0) {
			System.out.println(players.get(currentPlayer) + " got 3 consecutive correct answers, so receives 2 Gold Coins.");
			purses[currentPlayer] += 2;
		} else {
			System.out.println(players.get(currentPlayer) + " receives 1 Gold Coin.");
			purses[currentPlayer]++;
		}

		System.out.println(players.get(currentPlayer) + " now has " + purses[currentPlayer] + " Gold Coins.");

		boolean winner = didPlayerWin();
		currentPlayer++;
		resetPlayersTour();

		return winner;
	}

	public boolean wrongAnswer() {
		System.out.println("Question was incorrectly answered");

		if (isOnStreak[currentPlayer]) {
			System.out.println(players.get(currentPlayer) + " is losing the Streak :(");
			consecutiveCorrectAnswers[currentPlayer] = 0;
			isOnStreak[currentPlayer] = false;
			inPenaltyBox[currentPlayer] = false;
		} else {
			System.out.println(players.get(currentPlayer) + " was sent to the penalty box");
			inPenaltyBox[currentPlayer] = true;
		}

		currentPlayer++;
		resetPlayersTour();
		return true;
	}

}
