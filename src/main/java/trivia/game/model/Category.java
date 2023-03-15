package trivia.game.model;

public enum Category {
    POP ,
    SCIENCE, // nu bazam pe NUMELE enumurilor nici pe ordinea lor
    SPORTS,
    //    DARK_AGES,
    ROCK;

    public static Category getCategory(int location) {
        Category[] categories = Category.values();
        // TODO victorrentea 2023-03-14: sa te iei dupa indecsii unui enum e riscant - daca cineva modifica ordinea.
        //   mai bine pune in enum un int camp luat prin constructor
        return categories[(location + (categories.length - 1)) % categories.length];
        //The category index starts at 0, but our location starts at 1 => added the (categories.length - 1) to location
        //e.g. Location 1 should have category 0 (POP)
        //     1 % 4 = 1 => category with index 1 (SCIENCE)
        //     (1 + 3) % 4 = 0 => category with index 0 (POP)
    }

    // TODO victorrentea 2023-03-14: accidental coupling: daca apare DARK_AGES ?
    @Override
    public String toString() {
        return name().substring(0, 1).toUpperCase() + name().substring(1).toLowerCase();
    }
}
