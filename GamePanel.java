import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class GamePanel extends JPanel {
    private GameLogic game;
    private List<Player> players;
    private int selectedCardIndex = -1;
    private JButton playCardButton;
    private JButton pickCardButton;
    private JButton endTurnButton;
    private JLabel statusLabel;
    private JLabel currentPlayerLabel;
    private JTextArea gameLogArea;
    
    public GamePanel(GameLogic game) {
        this.game = game;
        this.players = game.getPlayers();
        
        setLayout(new BorderLayout());
        setBackground(new Color(0, 100, 0));
        
        // Top panel - status
        JPanel topPanel = createTopPanel();
        add(topPanel, BorderLayout.NORTH);
        
        // Center panel - game board
        JPanel centerPanel = createCenterPanel();
        add(centerPanel, BorderLayout.CENTER);
        
        // Bottom panel - controls
        JPanel bottomPanel = createBottomPanel();
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createTopPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(34, 139, 34));
        panel.setLayout(new BorderLayout());
        
        currentPlayerLabel = new JLabel("Current Player: " + game.getCurrentPlayer().getName());
        currentPlayerLabel.setForeground(Color.WHITE);
        currentPlayerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        statusLabel = new JLabel(game.getGameStatus());
        statusLabel.setForeground(Color.YELLOW);
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        panel.add(currentPlayerLabel, BorderLayout.WEST);
        panel.add(statusLabel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createCenterPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(new Color(0, 100, 0));
        
        // Discard pile and deck display
        JPanel cardsPanel = new JPanel();
        cardsPanel.setBackground(new Color(0, 100, 0));
        cardsPanel.setLayout(new FlowLayout());
        
        JLabel deckLabel = new JLabel("Deck: " + game.getDeck().size() + " cards");
        deckLabel.setForeground(Color.WHITE);
        deckLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        JLabel discardLabel = new JLabel("Top Card: ");
        discardLabel.setForeground(Color.WHITE);
        
        if (game.getTopCard() != null) {
            discardLabel.setText("Top Card: " + game.getTopCard().toString());
        }
        
        cardsPanel.add(deckLabel);
        cardsPanel.add(discardLabel);
        
        // Game log
        gameLogArea = new JTextArea(10, 40);
        gameLogArea.setEditable(false);
        gameLogArea.setBackground(Color.BLACK);
        gameLogArea.setForeground(Color.WHITE);
        gameLogArea.setText("Game started!\n");
        JScrollPane scrollPane = new JScrollPane(gameLogArea);
        
        panel.add(cardsPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createBottomPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(34, 139, 34));
        panel.setLayout(new BorderLayout());
        
        // Player's hand display
        JPanel handPanel = new JPanel();
        handPanel.setBackground(new Color(34, 139, 34));
        handPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        
        JLabel handLabel = new JLabel("Your Cards: ");
        handLabel.setForeground(Color.WHITE);
        handPanel.add(handLabel);
        
        updateHandDisplay(handPanel);
        
        // Buttons panel
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setBackground(new Color(34, 139, 34));
        buttonsPanel.setLayout(new FlowLayout());
        
        playCardButton = new JButton("Play Card");
        playCardButton.addActionListener(e -> playSelectedCard());
        
        pickCardButton = new JButton("Pick Card");
        pickCardButton.addActionListener(e -> pickCard());
        
        endTurnButton = new JButton("End Turn");
        endTurnButton.addActionListener(e -> endTurn());
        
        buttonsPanel.add(playCardButton);
        buttonsPanel.add(pickCardButton);
        buttonsPanel.add(endTurnButton);
        
        panel.add(handPanel, BorderLayout.CENTER);
        panel.add(buttonsPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private void updateHandDisplay(JPanel handPanel) {
        Component[] components = handPanel.getComponents();
        for (int i = 1; i < components.length; i++) {
            handPanel.remove(i);
        }
        
        Player currentPlayer = game.getCurrentPlayer();
        for (int i = 0; i < currentPlayer.getHandSize(); i++) {
            JButton cardButton = new JButton((i + 1) + ": " + currentPlayer.getCard(i).toString());
            final int index = i;
            cardButton.addActionListener(e -> selectCard(index));
            if (selectedCardIndex == i) {
                cardButton.setBackground(Color.YELLOW);
                cardButton.setOpaque(true);
            }
            handPanel.add(cardButton);
        }
        
        handPanel.revalidate();
        handPanel.repaint();
    }
    
    private void selectCard(int index) {
        selectedCardIndex = index;
        repaint();
    }
    
    private void playSelectedCard() {
        Player currentPlayer = game.getCurrentPlayer();
        if (selectedCardIndex < 0 || selectedCardIndex >= currentPlayer.getHandSize()) {
            gameLogArea.append("No card selected!\n");
            return;
        }
        
        Card card = currentPlayer.getCard(selectedCardIndex);
        if (!game.canPlayCard(card)) {
            gameLogArea.append("Cannot play " + card + "!\n");
            return;
        }
        
        currentPlayer.removeCard(card);
        game.playCard(card);
        gameLogArea.append(currentPlayer.getName() + " played: " + card + "\n");
        
        // Check if cardless
        if (currentPlayer.getHandSize() == 0) {
            if (card.isSpecialCard()) {
                currentPlayer.setCardless(true);
                gameLogArea.append(currentPlayer.getName() + " is CARDLESS! Must pick another card.\n");
                pickCard();
            }
        }
        
        selectedCardIndex = -1;
        game.nextPlayer();
        updateUI();
    }
    
    private void pickCard() {
        Player currentPlayer = game.getCurrentPlayer();
        int pickCount = game.getCardsToPickCount() > 0 ? game.getCardsToPickCount() : 1;
        game.pickCards(currentPlayer, pickCount);
        gameLogArea.append(currentPlayer.getName() + " picked " + pickCount + " card(s).\n");
        game.nextPlayer();
        updateUI();
    }
    
    private void endTurn() {
        game.nextPlayer();
        updateUI();
    }
    
    private void updateUI() {
        currentPlayerLabel.setText("Current Player: " + game.getCurrentPlayer().getName());
        statusLabel.setText(game.getGameStatus());
        
        if (game.getTopCard() != null) {
            statusLabel.setText(statusLabel.getText() + " | Top Card: " + game.getTopCard().toString());
        }
        
        repaint();
    }
}
