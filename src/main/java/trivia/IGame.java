package trivia;

public interface IGame {

	boolean isPlayable();

	boolean add(String playerName);

	void roll(int roll);

	boolean wasCorrectlyAnswered();

	boolean wrongAnswer();

}
