package trivia.game.mechanics;

public interface IGame {
    void add(String playerName);

    void roll(int roll);

    boolean increaseScore();

    boolean isInPenaltyBox();
}