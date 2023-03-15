package trivia.game.mechanics;

import trivia.game.model.Category;
import trivia.game.model.Player;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static trivia.game.model.Category.getCategory;

public class GameBetter implements IGame {
    public static final int BOARD_LENGTH = 12;
    public static final int NUMBER_OF_CARDS = 50;
    private static final int WINNING_SCORE = 6;
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
                    .mapToObj(i -> String.format("%s Question %d", category, i))
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
            if (isOdd(roll)) {
                System.out.println(player.getName() + " is getting out of the penalty box");
                player.setInPenaltyBox(false);
            } else {
                System.out.println(player.getName() + " is not getting out of the penalty box");
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean increaseScore() {
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
    public boolean isInPenaltyBox() {
        Player player = players.get(currentPlayerIndex);
        System.out.println("Question was incorrectly answered");
        System.out.println(player.getName() + " was sent to the penalty box");
        player.setInPenaltyBox(true);
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        return true;
    }
}
