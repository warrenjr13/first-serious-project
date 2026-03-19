import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GameFrame extends JFrame {
    private GameLogic game;
    private GamePanel gamePanel;
    
    public GameFrame() {
        setTitle("Card Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);
        setResizable(true);
        
        // Ask for number of players
        String numPlayersStr = JOptionPane.showInputDialog(null, 
            "How many players are playing?", "2");
        int numPlayers = Integer.parseInt(numPlayersStr);
        
        // Ask for number of cards per player
        String numCardsStr = JOptionPane.showInputDialog(null, 
            "How many cards does each player get?", "5");
        int numCards = Integer.parseInt(numCardsStr);
        
        // Create players
        List<Player> players = new ArrayList<>();
        for (int i = 1; i <= numPlayers; i++) {
            String playerName = JOptionPane.showInputDialog(null, 
                "Enter name for Player " + i + ":", "Player " + i);
            players.add(new Player(playerName != null ? playerName : "Player " + i));
        }
        
        // Initialize game
        game = new GameLogic(players);
        game.dealCards(numCards);
        
        // Create and add game panel
        gamePanel = new GamePanel(game);
        add(gamePanel);
        
        setVisible(true);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GameFrame());
    }
}
