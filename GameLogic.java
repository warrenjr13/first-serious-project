import java.util.*;

public class GameLogic {
    private List<Player> players;
    private Queue<Card> deck;
    private Stack<Card> discardPile;
    private int currentPlayerIndex;
    private int direction; // 1 for forward, -1 for backward
    private int cardsToPickCount;
    private boolean needsAnswer;
    private String gameStatus;
    
    public GameLogic(List<Player> players) {
        this.players = players;
        this.deck = new LinkedList<>();
        this.discardPile = new Stack<>();
        this.currentPlayerIndex = 0;
        this.direction = 1;
        this.cardsToPickCount = 0;
        this.needsAnswer = false;
        this.gameStatus = "Game Started";
        initializeDeck();
    }
    
    private void initializeDeck() {
        String[] suits = {"Spades", "Hearts", "Diamonds", "Clubs"};
        String[] ranks = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King", "Ace"};
        
        for (String suit : suits) {
            for (String rank : ranks) {
                deck.add(new Card(rank, suit));
            }
        }
        
        // Add jokers
        deck.add(new Card("Joker", "Red"));
        deck.add(new Card("Joker", "Black"));
        
        Collections.shuffle((List<Card>) deck);
    }
    
    public void dealCards(int numCards) {
        for (Player player : players) {
            for (int i = 0; i < numCards; i++) {
                if (!deck.isEmpty()) {
                    player.addCard(deck.poll());
                }
            }
        }
    }
    
    public boolean canPlayCard(Card card) {
        if (discardPile.isEmpty()) return true;
        
        Card topCard = discardPile.peek();
        
        // If answer is needed, only specific cards can be played
        if (needsAnswer) {
            return card.getRank().equals("Ace") || card.getRank().equals("4") || 
                   card.getRank().equals("5") || card.getRank().equals("6") || 
                   card.getRank().equals("7") || card.getRank().equals("9") || 
                   card.getRank().equals("10") || card.getRank().equals("Queen");
        }
        
        // Joker can only be played if same color as previous card
        if (card.getRank().equals("Joker")) {
            return card.getColor().equals(topCard.getColor()) || topCard.getRank().equals("Joker");
        }
        
        // Standard rules: same rank or suit
        return card.getRank().equals(topCard.getRank()) || card.getSuit().equals(topCard.getSuit());
    }
    
    public void playCard(Card card) {
        discardPile.push(card);
        applyCardEffect(card);
    }
    
    private void applyCardEffect(Card card) {
        String rank = card.getRank();
        
        if (rank.equals("King")) {
            // Kickback - reverse direction
            direction *= -1;
            gameStatus = getCurrentPlayer().getName() + " played King! Direction reversed!";
        } else if (rank.equals("Jack")) {
            // Jump - skip next player
            nextPlayer();
            gameStatus = getCurrentPlayer().getName() + " was jumped! " + players.get(currentPlayerIndex).getName() + " plays!";
        } else if (rank.equals("Queen")) {
            // Question - next player needs answer
            needsAnswer = true;
            gameStatus = getCurrentPlayer().getName() + " played Queen! Answer needed!";
        } else if (rank.equals("2") || rank.equals("3")) {
            // Add to pick count
            cardsToPickCount += rank.equals("2") ? 2 : 3;
            gameStatus = getCurrentPlayer().getName() + " played " + rank + "! Pick " + cardsToPickCount + " cards!";
        } else if (rank.equals("Joker")) {
            // Joker - pick 6 cards
            cardsToPickCount += 6;
            gameStatus = getCurrentPlayer().getName() + " played Joker! Pick 6 cards!";
        } else if (rank.equals("Ace")) {
            // Ace terminates picking
            cardsToPickCount = 0;
            needsAnswer = false;
            gameStatus = getCurrentPlayer().getName() + " played Ace! Picking terminated!";
        }
    }
    
    public void pickCards(Player player, int count) {
        for (int i = 0; i < count && !deck.isEmpty(); i++) {
            player.addCard(deck.poll());
        }
        cardsToPickCount = 0;
    }
    
    public void nextPlayer() {
        currentPlayerIndex += direction;
        
        if (currentPlayerIndex >= players.size()) {
            currentPlayerIndex = 0;
        } else if (currentPlayerIndex < 0) {
            currentPlayerIndex = players.size() - 1;
        }
    }
    
    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }
    
    public Card getTopCard() {
        return discardPile.isEmpty() ? null : discardPile.peek();
    }
    
    public int getCardsToPickCount() {
        return cardsToPickCount;
    }
    
    public boolean doesNeedAnswer() {
        return needsAnswer;
    }
    
    public void setNeedsAnswer(boolean value) {
        needsAnswer = value;
    }
    
    public String getGameStatus() {
        return gameStatus;
    }
    
    public boolean isGameOver() {
        for (Player player : players) {
            if (player.hasWon()) {
                gameStatus = player.getName() + " has won the game!";
                return true;
            }
        }
        return false;
    }
    
    public List<Player> getPlayers() {
        return players;
    }
    
    public Queue<Card> getDeck() {
        return deck;
    }
}
