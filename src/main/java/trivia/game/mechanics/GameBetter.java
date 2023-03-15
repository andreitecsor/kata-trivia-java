package trivia.game.mechanics;

import trivia.game.model.Category;
import trivia.game.model.Player;

import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static trivia.game.model.Category.getCategory;

public class GameBetter implements IGame {
    public static final int BOARD_LENGTH = 12;
    private static final int WINNING_SCORE = 6;
    public static final int NUMBER_OF_CARDS = 50;

    private final List<Player> players = new ArrayList<>();
    private final Map<Category, Deque<String>> questionDecks = new EnumMap<>(Category.class);
    private int currentPlayerIndex;

    public GameBetter() {
        prepareQuestionDecks();
    }

    private static boolean isOdd(int number) {
        return number % 2 != 0;
    }

    private void prepareQuestionDecks() {
        for (Category category : Category.values()) {
            Deque<String> questions = IntStream.range(0, NUMBER_OF_CARDS)
                    .mapToObj(i -> MessageFormat.format(category + " Question {0}", i))
                    .collect(Collectors.toCollection(ArrayDeque::new));

            questionDecks.put(category, questions);
        }
    }

    @Override
    public void add(String playerName) {
        players.add(new Player(playerName));

        System.out.println(playerName + " was added");
        System.out.println("They are player number " + players.size());
    }

    @Override
    public void roll(int roll) {
        Player player = players.get(currentPlayerIndex);

        System.out.println(player.getName() + " is the current player");
        System.out.println("They have rolled a " + roll);

        if (remainsInPenaltyBox(player, roll)) {
            return;
        }

        updateLocation(player, roll);

        readQuestion(player.getLocation());
    }

    private void readQuestion(int location) {
        Category category = getCategory(location);
        System.out.println("The category is " + category);
        System.out.println(questionDecks.get(category).pop());
    }

    private void updateLocation(Player player, int roll) {

        player.advance(roll);
        System.out.println(player.getName() + "'s new location is " + player.getLocation());
    }

    private boolean remainsInPenaltyBox(Player player, int roll) {
        if (player.isInPenaltyBox()) {
            if (isOdd(roll)) { // TODO victorrentea 2023-03-14: bun, explicativ, dar as fi tinut metoda aia local
                System.out.println(player.getName() + " is getting out of the penalty box");
                player.setInPenaltyBox(false);
            } else {
                System.out.println(player.getName() + " is not getting out of the penalty box");
                return true;
            }
        }
        return false;
    }


    //    public void handleCorrectAnswer() { ... player.addCoin();} // COMMAND
//    public boolean canGameContinue() {return player.getCoins() < WINNING_SCORE; } // COMMAND
    @Override
    public boolean isRightAnswer() { // TODO victorrentea 2023-03-14: numele nu reflecta ce intoarce de fapt
        Player player = players.get(currentPlayerIndex);
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();

        if (player.isInPenaltyBox()) {
            return true;
        }

        System.out.println("Answer was correct!!!!");
        player.addCoin();
        System.out.println(player.getName() + " now has " + player.getCoins() + " Gold Coins.");

        return player.getCoins() < WINNING_SCORE;

    }

    @Override
    public boolean isWrongAnswer() {
        Player player = players.get(currentPlayerIndex);

        System.out.println("Question was incorrectly answered");
        System.out.println(player.getName() + " was sent to the penalty box");
        player.setInPenaltyBox(true);

        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        return true;
    }
}
