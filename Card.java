public class Card {
    private String rank;
    private String suit;
    private String color;
    
    public Card(String rank, String suit) {
        this.rank = rank;
        this.suit = suit;
        this.color = (suit.equals("Hearts") || suit.equals("Diamonds")) ? "Red" : "Black";
    }
    
    public String getRank() {
        return rank;
    }
    
    public String getSuit() {
        return suit;
    }
    
    public String getColor() {
        return color;
    }
    
    @Override
    public String toString() {
        return rank + " of " + suit;
    }
    
    public boolean isSpecialCard() {
        return rank.equals("2") || rank.equals("3") || rank.equals("Ace") || 
               rank.equals("Jack") || rank.equals("Queen") || rank.equals("King") || 
               rank.equals("Joker");
    }
}
