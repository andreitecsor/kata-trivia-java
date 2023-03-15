package trivia.game.model;

import java.util.Arrays;

public enum Category {
    POP("Pop", 0),
    SCIENCE("Science", 1),
    SPORTS("Sports", 2),
    ROCK("Rock", 3);
    private final String name;
    private final int index;

    Category(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public static Category getCategory(int location) {
        Category[] categories = Category.values();
        int calculatedIndex = (location + (categories.length - 1)) % categories.length;
        return Arrays.stream(categories)
                .filter(category -> category.index == calculatedIndex)
                .findFirst().orElseThrow(IndexOutOfBoundsException::new);
    }

    @Override
    public String toString() {
        return this.name;
    }
}
