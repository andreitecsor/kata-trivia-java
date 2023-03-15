package trivia.game.model;

import trivia.game.mechanics.GameBetter;

// TODO victorrentea 2023-03-14: chiar ai nevoie de toti seterii ?
public class Player {
    private final String name; // TODO victorrentea 2023-03-14: final?
    private int coins; // TODO victorrentea 2023-03-14: purse = 'geanta' / poseta
    private int location = -1; // = 1 todo [ 0 .. 11 ]
    private boolean inPenaltyBox;

    public Player(String name) {
        this.name = name;
    }

    public void advance(int roll) {
        int newLocation = location + roll;
        location = (newLocation) % GameBetter.BOARD_LENGTH;
    }

    public void addCoin() {
        coins += 1;
    }

    public String getName() {
        return this.name;
    }

    public int getCoins() {
        return this.coins;
    }

    public int getLocation() { // [ 1..12]
        return this.location + 1;
    }

    public boolean isInPenaltyBox() {
        return this.inPenaltyBox;
    }

    public void setInPenaltyBox(boolean inPenaltyBox) {
        this.inPenaltyBox = inPenaltyBox;
    }

    @Override
    public String toString() {
        return "Player(name=" + this.getName() + ", purse=" + this.getCoins() + ", location=" + this.getLocation() + ", inPenaltyBox=" + this.isInPenaltyBox() + ")";
    }
}