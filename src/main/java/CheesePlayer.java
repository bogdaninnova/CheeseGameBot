import java.util.ArrayList;

public class CheesePlayer {

    private String name;
    private ArrayList<CheeseCard> cards;

    public CheesePlayer(String name) {
        setName(name);
        setCards(new ArrayList<>());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<CheeseCard> getCards() {
        return cards;
    }

    public void setCards(ArrayList<CheeseCard> cards) {
        this.cards = cards;
    }

    public void addCard(CheeseCard card) {
        cards.add(card);
    }
}
