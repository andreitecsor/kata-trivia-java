package trivia.game.mechanics;

import trivia.game.model.Category;
import trivia.game.model.Player;

import java.text.MessageFormat;
import java.util.*;

import static trivia.game.model.Category.getCategory;
import static trivia.game.util.MathUtil.isEven;

public class GameBetter implements IGame {
    private static final int BOARD_LENGTH = 12;
    private static final int WINNING_SCORE = 6;
    public static final int NUMBER_OF_CARDS = 50;

    private List<Player> players = new ArrayList<>();
    private Map<Category, Deque<String>> questionDecks = new EnumMap<>(Category.class);
    private int currentPlayerIndex = 0;

    public GameBetter() {
        prepareQuestionDecks();
    }

    private void prepareQuestionDecks() {
        for (Category category : Category.values()) {
            Deque<String> questions = new ArrayDeque<>();

            String pattern = category + " Question {0}";
            for (int i = 0; i < NUMBER_OF_CARDS; i++) {
                questions.add(MessageFormat.format(pattern, i));
            }

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
        int newLocation = player.getLocation() + roll;
        if (newLocation == BOARD_LENGTH) {
            player.setLocation(BOARD_LENGTH);
        } else {
            player.setLocation(newLocation % BOARD_LENGTH);
        }
        System.out.println(player.getName() + "'s new location is " + player.getLocation());
    }

    private boolean remainsInPenaltyBox(Player player, int roll) {
        if (player.isInPenaltyBox()) {
            if (!isEven(roll)) {
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
    public boolean isRightAnswer() {
        Player player = players.get(currentPlayerIndex);
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();

        if (player.isInPenaltyBox()) {
            return true;
        }

        System.out.println("Answer was correct!!!!");
        player.addCoin();
        System.out.println(player.getName() + " now has " + player.getPurse() + " Gold Coins.");

        return player.getPurse() < WINNING_SCORE;

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
