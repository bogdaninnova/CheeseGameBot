import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

public class CheeseGame {

    private ArrayList<CheesePlayer> players = new ArrayList<>();
    private int turn;
    private ArrayList<CheeseCard> cards;
    private ArrayList<CheeseCard> tableCards = new ArrayList<>();
    private int die = 0;

    private Random rand = new Random();

    public CheeseGame(Set<String> usersLogin) {
        addCards();
        for (int i = 0; i <= 5; i++)
            tableCards.add(getRandomCard());
        for (String userName : usersLogin)
            players.add(new CheesePlayer(userName));
        turn = rand.nextInt(players.size()-1);
    }

    public ArrayList<CheeseCard> getTable() {
        return tableCards;
    }

    public CheeseCard turn(String userName, int position, boolean isGet) {
        if (players.get(turn).getName().equals(userName)) {
            CheeseCard cheeseCard = null;
            if (isGet) {
                cheeseCard = getCardFromTable(position);
                players.get(turn).addCard(cheeseCard);
            } else {
                getCardFromTable(position);
            }
            skipTurn();

            return cheeseCard;
        }
        return null;
    }

    public void skipTurn() {
        if (++turn == players.size())
            turn = 0;
    }

    public String getCurrentPlayer() {
        return players.get(turn).getName();
    }

    private CheeseCard getCardFromTable(int position) {
        CheeseCard chosen = tableCards.get(position);
        tableCards.remove(position);
        tableCards.add(getRandomCard());
        return chosen;
    }

    public CheeseCard getRandomCard() {
        int num = rand.nextInt(cards.size() - 1);
        CheeseCard result = cards.get(num);
        cards.remove(num);
        return result;
    }

    private void addCards() {
        cards = new ArrayList<>();
        //---------------------- 1 ----------------------
        cards.add(new CheeseCard(1, false));
        cards.add(new CheeseCard(1, false));
        cards.add(new CheeseCard(1, false));
        cards.add(new CheeseCard(1, false));
        cards.add(new CheeseCard(1, true));
        cards.add(new CheeseCard(1, true));

        //---------------------- 2 ----------------------
        cards.add(new CheeseCard(2, false));
        cards.add(new CheeseCard(2, false));
        cards.add(new CheeseCard(2, false));
        cards.add(new CheeseCard(2, false));
        cards.add(new CheeseCard(2, true));
        cards.add(new CheeseCard(2, true));

        //---------------------- 3 ----------------------
        cards.add(new CheeseCard(3, false));
        cards.add(new CheeseCard(3, false));
        cards.add(new CheeseCard(3, false));
        cards.add(new CheeseCard(3, true));
        cards.add(new CheeseCard(3, true));
        cards.add(new CheeseCard(3, true));

        //---------------------- 3 ----------------------
        cards.add(new CheeseCard(4, false));
        cards.add(new CheeseCard(4, false));
        cards.add(new CheeseCard(4, false));
        cards.add(new CheeseCard(4, true));
        cards.add(new CheeseCard(4, true));
        cards.add(new CheeseCard(4, true));

        //---------------------- 5 ----------------------
        cards.add(new CheeseCard(5, false));
        cards.add(new CheeseCard(5, false));
        cards.add(new CheeseCard(5, true));
        cards.add(new CheeseCard(5, true));
        cards.add(new CheeseCard(5, true));
        cards.add(new CheeseCard(5, true));

        //---------------------- 6 ----------------------
        cards.add(new CheeseCard(6, false));
        cards.add(new CheeseCard(6, false));
        cards.add(new CheeseCard(6, true));
        cards.add(new CheeseCard(6, true));
        cards.add(new CheeseCard(6, true));
        cards.add(new CheeseCard(6, true));
    }


    public int getDie() {
        return die;
    }

    public void setDie() {
        this.die = rand.nextInt(5) + 1;
    }
}
