import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;
    private List<Card> hand;
    private boolean isCardless;
    
    public Player(String name) {
        this.name = name;
        this.hand = new ArrayList<>();
        this.isCardless = false;
    }
    
    public String getName() {
        return name;
    }
    
    public List<Card> getHand() {
        return hand;
    }
    
    public void addCard(Card card) {
        hand.add(card);
    }
    
    public void removeCard(Card card) {
        hand.remove(card);
    }
    
    public Card playCard(int index) {
        if (index >= 0 && index < hand.size()) {
            return hand.remove(index);
        }
        return null;
    }
    
    public int getHandSize() {
        return hand.size();
    }
    
    public boolean isCardless() {
        return isCardless;
    }
    
    public void setCardless(boolean cardless) {
        isCardless = cardless;
    }
    
    public boolean hasWon() {
        return hand.isEmpty() && !isCardless;
    }
    
    public void pickCards(List<Card> cards) {
        hand.addAll(cards);
    }
    
    public Card getCard(int index) {
        if (index >= 0 && index < hand.size()) {
            return hand.get(index);
        }
        return null;
    }
}
