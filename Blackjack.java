import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;
/* Grace Mahoney
 * This program simulates sitting at a blackjack table. When Player
 * sits down at the table, Player starts with a $100 balance in
 * chips. Player must place a bet to play a game of Blackjack with
 * a minimum bet of 5. Cards are reshuffled randomly when there are
 * less than 12 cards left.
 */
public class Blackjack implements ActionListener {
    boolean active = false; // True when game is currently active
    boolean showDCard = false; // True when all cards are shown
    boolean canSplit = false; // True when Player's first two cards are of the same denomination
    boolean canDoubleDown = false; // True when Player's first two cards total 9, 10, or 11
    boolean hidePCard = false; // True when Player chooses to double down
    boolean canInsure = false; // True when Dealer's face-up card is an Ace
    boolean firstTurn = true; // True when it is Player's first turn
    boolean split = false; // True when Player chooses to split
    boolean hand2 = false; // True when Player's first hand's turn is over
    int pBal = 100; // Player starts game with $100
    int bet = 5; // Bet minimum of $5
    int insurance = 0;
    ArrayList<Card> deck = new ArrayList<Card>();
    ArrayList<Card> discardPile = new ArrayList<Card>();
    ArrayList<Card> pHand = new ArrayList<Card>(); // Player's Hand
    ArrayList<Card> pHand2 = new ArrayList<Card>(); // Player's Second Hand when Split
    ArrayList<Card> dHand = new ArrayList<Card>(); // Dealer's Hand
    int pTotal = 0; // Player's total
    int pTotal2 = 0; // Player's Second Hand Total when Split
    int dTotal = 0; // Dealer's total
    
    String resultMsg = "";
    String settlement = "";
    String insuranceMsg = "";
    String resultMsg2 = "";
    String settlement2 = "";
    
    // GUI
    JFrame frame = new JFrame("Blackjack");
    JPanel bet_panel = new JPanel();
    JLabel bet_title = new JLabel("PLACE YOUR BETS    ");
    JPanel display_panel = new JPanel();
    JTextArea displayText = new JTextArea("Balance: $100\nBet: $5",2,1);
    JLabel displayIcon = new JLabel(new ImageIcon(new ImageIcon("chipsPurp.png").getImage().getScaledInstance(50,50, Image.SCALE_DEFAULT)));
    JPanel result_panel = new JPanel();
    JTextArea resultText = new JTextArea();
    JTextArea resultText2 = new JTextArea();
    JPanel player_panel = new JPanel();
    ArrayList<JLabel> pHandDisp = new ArrayList<JLabel>();
    JLabel playerTotal = new JLabel();
    JPanel player_panel2 = new JPanel();
    ArrayList<JLabel> pHandDisp2 = new ArrayList<JLabel>();
    JLabel playerTotal2 = new JLabel();
    JPanel dealer_panel = new JPanel();
    ArrayList<JLabel> dHandDisp = new ArrayList<JLabel>();
    JLabel dealerTotal = new JLabel();
    JPanel button_panel = new JPanel();
    JButton chip1 = new JButton(new ImageIcon(new ImageIcon("chip1.png").getImage().getScaledInstance(50,50, Image.SCALE_DEFAULT)));
    JButton chip5 = new JButton(new ImageIcon(new ImageIcon("chip5.png").getImage().getScaledInstance(50,50, Image.SCALE_DEFAULT)));
    JButton chip10 = new JButton(new ImageIcon(new ImageIcon("chip10.png").getImage().getScaledInstance(50,50, Image.SCALE_DEFAULT)));
    JButton chip25 = new JButton(new ImageIcon(new ImageIcon("chip25.png").getImage().getScaledInstance(50,50, Image.SCALE_DEFAULT)));
    JButton chip100 = new JButton(new ImageIcon(new ImageIcon("chip100.png").getImage().getScaledInstance(50,50, Image.SCALE_DEFAULT)));
    JButton undo = new JButton(new ImageIcon(new ImageIcon("undo.png").getImage().getScaledInstance(50,50, Image.SCALE_DEFAULT)));
    JButton betButton = new JButton("BET");
    JButton standButton = new JButton("STAND");
    JButton hitButton = new JButton("HIT");
    JButton splitButton = new JButton("SPLIT");
    JButton doubleDownButton = new JButton("DOUBLE DOWN");
    JButton insuranceButton = new JButton("INSURANCE");
    
    public Blackjack() {
        // GUI
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setSize(800, 550);
        frame.getContentPane().setBackground(new Color(9,83,61));
        frame.setLayout(new BorderLayout());
        frame.setVisible(true);
        
        bet_panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        bet_panel.setBackground(new Color(25,54,25));
        
        bet_title.setBackground(new Color(6,54,39));
        bet_title.setForeground(new Color(255,255,255));
        bet_title.setHorizontalAlignment(JLabel.CENTER);
        bet_title.setOpaque(true);
        
        bet_panel.add(bet_title);
        undo.setPreferredSize(new Dimension(50,50));
        undo.setContentAreaFilled(false);
        undo.setBorderPainted(false);
        undo.addActionListener(this);
        chip1.setPreferredSize(new Dimension(50,50));
        chip1.setContentAreaFilled(false);
        chip1.setBorderPainted(false);
        chip1.addActionListener(this);
        chip5.setPreferredSize(new Dimension(50,50));
        chip5.setContentAreaFilled(false);
        chip5.setBorderPainted(false);
        chip5.addActionListener(this);
        chip10.setPreferredSize(new Dimension(50,50));
        chip10.setContentAreaFilled(false);
        chip10.setBorderPainted(false);
        chip10.addActionListener(this);
        chip25.setPreferredSize(new Dimension(50,50));
        chip25.setContentAreaFilled(false);
        chip25.setBorderPainted(false);
        chip25.addActionListener(this);
        chip100.setPreferredSize(new Dimension(50,50));
        chip100.setContentAreaFilled(false);
        chip100.setBorderPainted(false);
        chip100.addActionListener(this);
        chip100.setVisible(false);
        betButton.addActionListener(this);
        
        bet_panel.add(undo);
        bet_panel.add(chip1);
        bet_panel.add(chip5);
        bet_panel.add(chip10);
        bet_panel.add(chip25);
        bet_panel.add(chip100);
        bet_panel.add(betButton);
        
        button_panel.setLayout(new GridLayout(5,1));
        button_panel.setBackground(new Color(9,83,61));
        button_panel.setVisible(false);
        
        button_panel.add(insuranceButton);
        button_panel.add(doubleDownButton);
        button_panel.add(splitButton);
        button_panel.add(hitButton);
        button_panel.add(standButton);
        
        //standButton.setBounds(610, 450, 180, 40);
        standButton.setFocusable(false);
        standButton.addActionListener(this);
        standButton.setVisible(true);
        //hitButton.setBounds(610, 400, 180, 40);
        hitButton.setFocusable(false);
        hitButton.addActionListener(this);
        hitButton.setVisible(true);
        //splitButton.setBounds(10, 400, 180, 40);
        splitButton.setFocusable(false);
        splitButton.addActionListener(this);
        splitButton.setVisible(false);
        //doubleDownButton.setBounds(10, 450, 180, 40);
        doubleDownButton.setFocusable(false);
        doubleDownButton.addActionListener(this);
        doubleDownButton.setVisible(false);
        //insuranceButton.setBounds(10, 350, 180, 40);
        insuranceButton.setFocusable(false);
        insuranceButton.addActionListener(this);
        insuranceButton.setVisible(false);
        
        display_panel.setLayout(new BorderLayout());
        display_panel.setBounds(10,410,650,40);
        display_panel.setBackground(new Color(9,83,61));
        display_panel.setVisible(true);
        
        display_panel.add(displayText);
        
        displayText.setBackground(new Color(9,83,61));
        displayText.setForeground(new Color(255,255,255));
        displayText.setOpaque(true);
        displayText.setEditable(false);

        
        
        player_panel.setLayout(new BoxLayout(player_panel, BoxLayout.LINE_AXIS));
        player_panel.setBounds(0,195,750,100);
        player_panel.setBackground(new Color(9,83,61));
        player_panel.setVisible(true);
        
        playerTotal.setText("Player Total: " + pTotal);
        playerTotal.setBackground(new Color(9,83,61));
        playerTotal.setForeground(new Color(255,255,255));
        playerTotal.setOpaque(true);
        
        // When Split
        player_panel2.setLayout(new BoxLayout(player_panel2, BoxLayout.LINE_AXIS));
        player_panel2.setBounds(0,300,700,140);
        player_panel2.setBackground(new Color(9,83,61));
        player_panel2.setVisible(false);
        
        playerTotal2.setText("Player Total (Hand 2): " + pTotal2);
        playerTotal2.setBackground(new Color(9,83,61));
        playerTotal2.setForeground(new Color(255,255,255));
        playerTotal2.setOpaque(true);
        
        dealer_panel.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 10));
        dealer_panel.setBounds(0,10,750,200);
        dealer_panel.setBackground(new Color(9,83,61));
        dealer_panel.setVisible(true);
        
        dealerTotal.setText("Dealer Total: " + dTotal);
        dealerTotal.setBackground(new Color(9,83,61));
        dealerTotal.setForeground(new Color(255,255,255));
        dealerTotal.setOpaque(true);
        
        result_panel.setLayout(new GridLayout(1,2));
        result_panel.setBounds(10,120,720,65);
        result_panel.setBackground(new Color(6,54,39)); 
        result_panel.setVisible(false);
        
        result_panel.add(resultText);
        result_panel.add(resultText2);
        
        resultText.setBackground(new Color(9,83,61));
        resultText.setForeground(new Color(255,255,255));
        resultText.setOpaque(true);
        resultText.setEditable(false);
        resultText.setLineWrap(true);
        
        // When Split
        resultText2.setBackground(new Color(9,83,61));
        resultText2.setForeground(new Color(255,255,255));
        resultText2.setOpaque(true);
        resultText2.setEditable(false);
        resultText2.setLineWrap(true);
        
        frame.add(bet_panel, BorderLayout.SOUTH);
        frame.add(result_panel);
        frame.add(button_panel, BorderLayout.EAST);
        frame.add(display_panel);
        frame.add(player_panel);
        frame.add(player_panel2);
        frame.add(dealer_panel);
        
        // Creates and shuffles a deck of 52 cards
        for(int sNum = 0; sNum < 4; sNum++) { // suit type
            for(int val = 0; val < 13; val++) { // card value
                deck.add(new Card(sNum,val+1));
            }
        }
        shuffleDeck(deck);
        
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == undo) {
            System.out.println("Amount bet is reset to $5.");
            bet = 5;
            updateDisplay();
        }
        if(e.getSource() == chip1) {
            bet += 1;
            updateDisplay();
        }
        if(e.getSource() == chip5) {
            bet += 5;
            updateDisplay();
        }
        if(e.getSource() == chip10) {
            bet += 10;
            updateDisplay();
        }
        if(e.getSource() == chip25) {
            bet += 25;
            updateDisplay();
        }
        if(e.getSource() == chip100) {
            bet += 100;
            updateDisplay();
        }
        // Minimum bet of 5
        if(e.getSource() == betButton) {
            System.out.println("Current Balance: $" + pBal);
            System.out.println("Betting $" + bet + "...");
            updateDisplay();
            if(deck.size() < (int)(Math.random()*6+6)) { // Shuffle randomly when there is less than 12 cards in the deck
                restockDeck(discardPile);
                shuffleDeck(deck);
            }
            deal();
            displayHands();
        }
        if(e.getSource() == standButton) {
            displayHands();
            stand();
        }
        if(e.getSource() == hitButton) {
            hit();
            firstTurn = false;
        }
        if(e.getSource() == splitButton) {
            firstTurn = false;
            split();
            displayHands();
        }
        if(e.getSource() == doubleDownButton) {
            doubleDown();
        }
        if(e.getSource() == insuranceButton) {
            insurance(bet/2);
            insuranceButton.setVisible(false);
            if(dHand.get(1).getValue() == 10) {
                insuranceMsg = "Player wins insurance. Double their insurance bet is won. (+$" + (insurance*2) + ")";
                pBal += insurance*2;
                dealerTurn();
                setStatus(false);
                return;
            } else {
                insuranceMsg = "No ten-card. Insurance lost. (-$" + insurance + ")";
                pBal -= insurance;
            }
            result();
            firstTurn = false;
        }
    }
    
    // Player's turn options
    public void stand() {
        System.out.println("Stand...");
        if(split && !hand2) {
            hand2 = true;
            hit();
        } else {
            dealerTurn();
        }
    }
    public void hit() {
        doubleDownButton.setVisible(false);
        splitButton.setVisible(false);
        insuranceButton.setVisible(false);
        if(deck.size() == 0) { // Checks for empty deck
            System.out.println("Insufficient amount of cards. Shuffling...");
            restockDeck(discardPile);
            shuffleDeck(deck);
        }
        Card temp = deck.remove(0);
        if(!hand2) {
            pHand.add(temp);
        } else {
            pHand2.add(temp);
        }
        if(!hidePCard) {
            System.out.println("Hit...\nPlayer recieved: " + temp.toString());
            updateTotal();
        }
        displayHands();
    }
    public void split() {
        // Code to come - dev is pondering life choices
        System.out.println("Splits...");
        split = true;
        splitButton.setVisible(false);
        player_panel2.setVisible(true);
        Card temp = pHand.remove(1);
        pHand2.add(temp);
        if(pHand.get(0).getName() == "Ace") {
            pHand.get(0).setValue(11);
            pHand2.get(0).setValue(11);
        }
        pTotal2 = temp.getValue();
        hit();
    }
    // Bet is doubled, Player is dealt a face down card, and Dealer takes its turn
    public void doubleDown() {
        bet = bet*2;
        insuranceMsg = "Double Down. Bet is $" + bet;
        System.out.println("Double Down...\nBet is now $" + bet);
        doubleDownButton.setVisible(false);
        hidePCard = true;
        hit();
        dealerTurn();
    }
    // b can be up to half the value of bet
    // When the dealer's face-up card is an ace, players may make a side bet that the face-down card is a ten-card.
    public void insurance(int b) {
        insurance = b;
        System.out.println("Insurance...\nSide bet of $" + b);
    }
    
    // Dealer's turn
    public void dealerTurn() {
        showDCard = true;
        countDealer();
        displayHands();
        // Dealer must take a card when total is 16 or under
        // Dealer must stand when total is 17 or more
        while(dTotal <= 16) {
            if(deck.size() == 0) { // Checks for empty deck
                restockDeck(discardPile);
                shuffleDeck(deck);
            }
            dHand.add(deck.remove(0));
            countDealer();
            displayHands();
        }
        // Dealer's turn ends and winner is determined
        // If Player doubled down, face down card is revealed and added to Player total
        if(hidePCard) {
            hidePCard = false;
            countPlayer();
            displayHands();
        }
        if(active) {
            if(pTotal > 21) {
                resultMsg = "Player busts. The House wins.";
                settlement = "(-$" + bet + ")";
                // Bet is settled during Player's Turn
            } else if(dTotal > 21) {
                resultMsg = "Dealer Busts. Player wins.";
                settlement = "(+$" + bet + ")";
                pBal += bet;
            } else if((dHand.get(0).getValue() + dHand.get(1).getValue()) == 21) {
                resultMsg = "Dealer has blackjack. The House wins.";
                settlement = "(-$" + bet + ")";
                pBal -= bet;
            } else if(pTotal == dTotal) {
                resultMsg = "Stand-off.";
                settlement = "Bets are returned";
            } else if(pTotal > dTotal) {
                resultMsg = "Player wins.";
                settlement = "(+$" + bet + ")";
                pBal += bet;
            } else {
                resultMsg = "The House wins.";
                settlement = "(-$" + bet + ")";
                pBal -= bet;
            }
            if(split) {
                if(pTotal2 > 21) {
                    resultMsg2 = "Player busts. The House wins.";
                    settlement2 = "(-$" + bet + ")";
                    // Bet is settled during Player's Turn
                } else if(dTotal > 21) {
                    resultMsg2 = "Dealer Busts. Player wins.";
                    settlement2 = "(+$" + bet + ")";
                    pBal += bet;
                } else if(pTotal2 == dTotal) {
                    resultMsg2 = "Stand-off.";
                    settlement2 = "Bets are returned";
                } else if(pTotal2 > dTotal) {
                    resultMsg2 = "Player wins.";
                    settlement2 = "(+$" + bet + ")";
                    pBal += bet;
                } else {
                    resultMsg2 = "The House wins.";
                    settlement2 = "(-$" + bet + ")";
                    pBal -= bet;
                }
            }
            setStatus(false);
        }
    }
    
    public void displayHands() {
        // Player's Cards
        while(pHandDisp.size() > 0) {
            player_panel.remove(pHandDisp.remove(0));
        }
        if(hidePCard) {
            pHandDisp.add(new JLabel());
            //pHandDisp.get(0).setText(pHand.get(0).toString()); // Card name
            pHandDisp.get(0).setIcon(new ImageIcon(new ImageIcon(dHand.get(0).getImg()).getImage().getScaledInstance(100,100, Image.SCALE_DEFAULT)));
            pHandDisp.get(0).setHorizontalTextPosition(JLabel.CENTER);
            pHandDisp.get(0).setVerticalTextPosition(JLabel.BOTTOM);
            pHandDisp.get(0).setVerticalAlignment(JLabel.CENTER);
            pHandDisp.get(0).setHorizontalAlignment(JLabel.CENTER);
            player_panel.add(pHandDisp.get(0));
            // Hidden Card
            pHandDisp.add(new JLabel());
            //pHandDisp.get(1).setText("???"); // Card name
            pHandDisp.get(1).setIcon(new ImageIcon(new ImageIcon("card.png").getImage().getScaledInstance(100,100, Image.SCALE_DEFAULT)));
            pHandDisp.get(1).setHorizontalTextPosition(JLabel.CENTER);
            pHandDisp.get(1).setVerticalTextPosition(JLabel.BOTTOM);
            pHandDisp.get(1).setVerticalAlignment(JLabel.CENTER);
            pHandDisp.get(1).setHorizontalAlignment(JLabel.CENTER);
            player_panel.add(pHandDisp.get(1));
        } else {
            for(int i = 0; i < pHand.size(); i++) {
                pHandDisp.add(new JLabel());
                //pHandDisp.get(i).setText(pHand.get(i).toString()); // Card name
                pHandDisp.get(i).setIcon(new ImageIcon(new ImageIcon(pHand.get(i).getImg()).getImage().getScaledInstance(100,100, Image.SCALE_DEFAULT)));
                pHandDisp.get(i).setHorizontalTextPosition(JLabel.CENTER);
                pHandDisp.get(i).setVerticalTextPosition(JLabel.BOTTOM);
                pHandDisp.get(i).setVerticalAlignment(JLabel.CENTER);
                pHandDisp.get(i).setHorizontalAlignment(JLabel.CENTER);
                player_panel.add(pHandDisp.get(i));
            }
        }
        playerTotal.setText("Player Total: " + pTotal);
        player_panel.add(playerTotal);
        // Player's Second Hand (When Split)
        while(pHandDisp2.size() > 0) {
            player_panel2.remove(pHandDisp2.remove(0));
        }
        for(int k = 0; k < pHand2.size(); k++) {
            pHandDisp2.add(new JLabel());
            //pHandDisp2.get(k).setText(pHand2.get(k).toString()); // Card name
            pHandDisp2.get(k).setIcon(new ImageIcon(new ImageIcon(pHand2.get(k).getImg()).getImage().getScaledInstance(100,100, Image.SCALE_DEFAULT)));
            pHandDisp2.get(k).setHorizontalTextPosition(JLabel.CENTER);
            pHandDisp2.get(k).setVerticalTextPosition(JLabel.BOTTOM);
            pHandDisp2.get(k).setVerticalAlignment(JLabel.CENTER);
            pHandDisp2.get(k).setHorizontalAlignment(JLabel.CENTER);
            player_panel2.add(pHandDisp2.get(k));
        }
        playerTotal2.setText("Player Total (Hand 2): " + pTotal2);
        player_panel2.add(playerTotal2);
        // Dealer's Cards
        while(dHandDisp.size() > 0) {
            dealer_panel.remove(dHandDisp.remove(0));
        }
        if(!showDCard) {
            dHandDisp.add(new JLabel());
            //dHandDisp.get(0).setText(dHand.get(0).toString()); // Card name
            dHandDisp.get(0).setIcon(new ImageIcon(new ImageIcon(dHand.get(0).getImg()).getImage().getScaledInstance(100,100, Image.SCALE_DEFAULT)));
            dHandDisp.get(0).setHorizontalTextPosition(JLabel.CENTER);
            dHandDisp.get(0).setVerticalTextPosition(JLabel.BOTTOM);
            dHandDisp.get(0).setVerticalAlignment(JLabel.CENTER);
            dHandDisp.get(0).setHorizontalAlignment(JLabel.CENTER);
            dealer_panel.add(dHandDisp.get(0));
            // Hidden Card
            dHandDisp.add(new JLabel());
            //dHandDisp.get(1).setText("???"); // Card name
            dHandDisp.get(1).setIcon(new ImageIcon(new ImageIcon("card.png").getImage().getScaledInstance(100,100, Image.SCALE_DEFAULT)));
            dHandDisp.get(1).setHorizontalTextPosition(JLabel.CENTER);
            dHandDisp.get(1).setVerticalTextPosition(JLabel.BOTTOM);
            dHandDisp.get(1).setVerticalAlignment(JLabel.CENTER);
            dHandDisp.get(1).setHorizontalAlignment(JLabel.CENTER);
            dealer_panel.add(dHandDisp.get(1));
        } else {
            for(int j = 0; j < dHand.size(); j++) {
                dHandDisp.add(new JLabel());
                //dHandDisp.get(j).setText(dHand.get(j).toString()); // Card name
                dHandDisp.get(j).setIcon(new ImageIcon(new ImageIcon(dHand.get(j).getImg()).getImage().getScaledInstance(100,100, Image.SCALE_DEFAULT)));
                dHandDisp.get(j).setHorizontalTextPosition(JLabel.CENTER);
                dHandDisp.get(j).setVerticalTextPosition(JLabel.BOTTOM);
                dHandDisp.get(j).setVerticalAlignment(JLabel.CENTER);
                dHandDisp.get(j).setHorizontalAlignment(JLabel.CENTER);
                dealer_panel.add(dHandDisp.get(j));
            }
        }
        dealerTotal.setText("Dealer Total: " + dTotal);
        dealer_panel.add(dealerTotal);
    }
    
    // Updates bet display
    public void updateDisplay() {
        if(pBal < 5) { // Checks if funds meet minimum of $5
            displayText.setForeground(Color.RED);
            resultText.setText("You do not have the funds to continue playing. Sorry, please exit.");
            bet_panel.setVisible(false);
            display_panel.setVisible(false);
            result_panel.setVisible(true);
            button_panel.setVisible(false);
        }
        if(pBal - bet >= 100) {
            chip100.setVisible(true);
            chip25.setVisible(true);
            chip10.setVisible(true);
            chip5.setVisible(true);
            chip1.setVisible(true);
        } else if(pBal - bet >= 25) {
            chip100.setVisible(false);
            chip25.setVisible(true);
            chip10.setVisible(true);
            chip5.setVisible(true);
            chip1.setVisible(true);
        } else if(pBal - bet >= 10) {
            chip100.setVisible(false);
            chip25.setVisible(false);
            chip10.setVisible(true);
            chip5.setVisible(true);
            chip1.setVisible(true);
        } else if(pBal - bet >= 5) {
            chip100.setVisible(false);
            chip25.setVisible(false);
            chip10.setVisible(false);
            chip5.setVisible(true);
            chip1.setVisible(true);
        } else if(pBal - bet >= 1) {
            chip100.setVisible(false);
            chip25.setVisible(false);
            chip10.setVisible(false);
            chip5.setVisible(false);
            chip1.setVisible(true);
        } else {
            chip100.setVisible(false);
            chip25.setVisible(false);
            chip10.setVisible(false);
            chip5.setVisible(false);
            chip1.setVisible(false);
        }
        String txt = "Balance: $" + pBal + "\nBet: $" + bet;
        displayText.setText(txt);
    }
    // Calculates and stores Player's total and Dealer's total
    // Checks for busts and aces
    public void updateTotal() {
        countPlayer();
        countDealer();
    }
    public void result() {
        String txt = resultMsg + "\n" + settlement + "\n" + insuranceMsg;
        String txt2 = resultMsg2 + "\n" + settlement2;
        if(split) {
            txt = "Hand 1:\n" + txt;
            txt2 = "Hand 2:\n" + txt2;
        }
        resultText.setText(txt);
        System.out.println(txt);
        resultText2.setText(txt2);
        System.out.println(txt2);
        result_panel.setVisible(true);
    }
    
    public void countPlayer() {
        // Counts Player's hand total
        if(!hand2) {
            boolean pAce = false; // true when hand includes an "Ace" with a value of 11
            int pAceIndex = -1; // index of first occurence of "Ace" with a value of 11
            pTotal = 0;
            for(int i = 0; i < pHand.size(); i++) {
                Card c = pHand.get(i);
                pTotal += c.getValue();
                if(c.getName() == "Ace" && c.getValue() == 11) {
                    pAce = true;
                    pAceIndex = i;
                }
                // Check for bust
                if(pTotal > 21) {
                    // Check for ace and ace's value
                    if(pAce) {
                        pHand.get(pAceIndex).setValue(1);
                        countPlayer();
                    } else { // Player busts -> Play ends
                        resultMsg = "Player busts. The House wins.";
                        settlement = "(-$" + bet + ")";
                        pBal -= bet;
                        result_panel.setVisible(true);
                        if(!split) {
                            showDCard = true;
                            countDealer();
                            setStatus(false);
                            return;
                        } else {
                            hand2 = true;
                            hit();
                        }
                    }
                }
            }
        } else {
            // Counts Player's second hand total
            boolean pAce2 = false; // true when hand includes an "Ace" with a value of 11
            int pAceIndex2 = -1; // index of first occurence of "Ace" with a value of 11
            pTotal2 = 0;
            for(int j = 0; j < pHand2.size(); j++) {
                Card c = pHand2.get(j);
                pTotal2 += c.getValue();
                if(c.getName() == "Ace" && c.getValue() == 11) {
                    pAce2 = true;
                    pAceIndex2 = j;
                }
                // Check for bust
                if(pTotal2 > 21) {
                    // Check for ace and ace's value
                    if(pAce2) {
                        pHand2.get(pAceIndex2).setValue(1);
                        countPlayer();
                    } else { // Player busts -> Play ends
                        resultMsg2 = "Player busts. The House wins.";
                        settlement2 = "(-$" + bet + ")";
                        pBal -= bet;
                        showDCard = true;
                        if(pTotal > 21) {
                            countDealer();
                            setStatus(false);
                            return;
                        }
                        dealerTurn();
                    }
                }
            }
        }
    }
    public void countDealer() {
        // Counts Dealer's hand total
        if(showDCard) {
            boolean dAce = false; // true when hand includes an "Ace" with a value of 11
            int dAceIndex = -1; // index of first occurence of "Ace" with a value of 11
            dTotal = 0;
            for(int i = 0; i < dHand.size(); i++) {
                Card c = dHand.get(i);
                dTotal += c.getValue();
                if(c.getName() == "Ace" && c.getValue() == 11) {
                    dAce = true;
                    dAceIndex = i;
                }
                // Check for bust
                if(dTotal > 21) {
                    // Check for ace and ace's value
                    if(dAce) {
                        dHand.get(dAceIndex).setValue(1);
                        countDealer();
                    } else { // Player busts -> Play ends
                        return;
                    }
                }
            }
        } else {
            dTotal = dHand.get(0).getValue();
            if(firstTurn && (dHand.get(0).getName() == "Ace")) { // Check for Ace showing
                if(pBal >= bet + bet/2) {
                    canInsure = true;
                    insuranceButton.setVisible(true);
                }
            }
        }
    }
    
    public void deal() {
        setStatus(true); // Game has started
        
        pHand.add(deck.remove(0));
        dHand.add(deck.remove(0)); // Face up
        pHand.add(deck.remove(0));
        dHand.add(deck.remove(0)); // Face down
        
        updateTotal();
        // Checks for naturals (An Ace and a 10-pt card)
        if(pTotal == 21) {
            if(dTotal == 21) {
                resultMsg = "Stand-off.";
                settlement = "Bets are returned";
                setStatus(false);
                return;
            } else {
                resultMsg = "Player has Blackjack. Player wins 1.5x their bet.";
                settlement = "(+$" + (bet+bet/2) + ")";
                pBal += (int)((double)(bet)*(1.5));
                showDCard = true;
                countDealer();
                setStatus(false);
                return;
            }
        }
        // Checks for Split option
        if((pHand.get(0).getName() == pHand.get(1).getName()) && (bet*2 <= pBal)) {
            canSplit = true;
            splitButton.setVisible(true);
        }
        // Checks for double down
        // When original two cards total 9, 10, or 11
        if(((pTotal >= 9) && (pTotal <= 11)) && (pBal >= bet*2)) {
            canDoubleDown = true;
            doubleDownButton.setVisible(true);
        }
        displayHands();
        button_panel.setVisible(true);
        player_panel.setVisible(true);
    }
    // Removes all cards from play and puts them into the discard pile 'pile'
    public void discard() {
        while(pHand.size() > 0) {
            if(pHand.get(0).getName() == "Ace") {
                  pHand.get(0).setValue(11);
            }
            discardPile.add(pHand.remove(0));
        }
        while(pHand2.size() > 0) {
            if(pHand2.get(0).getName() == "Ace") {
                  pHand2.get(0).setValue(11);
            }
            discardPile.add(pHand2.remove(0));
        }
        while(dHand.size() > 0) {
            if(dHand.get(0).getName() == "Ace") {
                  dHand.get(0).setValue(11);
            }
            discardPile.add(dHand.remove(0));
        }
    }
    // Randomizes order of cards in ArrayList 'cardDeck'
    public void shuffleDeck(ArrayList<Card> cardDeck) {
        System.out.println("Shuffling deck...");
        ArrayList<Card> temp = new ArrayList<Card>();
        while(cardDeck.size() > 0) {
            temp.add(cardDeck.remove(0));
        }
        while(temp.size() > 0) {
            cardDeck.add(temp.remove((int)(Math.random()*temp.size())));
        }
    }
    public void restockDeck(ArrayList<Card> cardDeck) {
        while(cardDeck.size() > 0) {
            deck.add(cardDeck.remove(0));
        }
    }
    // True when game is in session, False when waiting for a bet to be set
    public void setStatus(boolean b) {
        if(b) {
            // Game settings are reset
            active = true;
            showDCard = false;
            canSplit = false;
            splitButton.setVisible(false);
            player_panel2.setVisible(false);
            canDoubleDown = false;
            doubleDownButton.setVisible(false);
            hidePCard = false;
            canInsure = false;
            insuranceButton.setVisible(false);
            firstTurn = true;
            split = false;
            hand2 = false;
            insurance = 0;
            discard();
            pTotal = 0;
            pTotal2 = 0;
            dTotal = 0;
            bet_panel.setVisible(false);
            display_panel.setVisible(false);
            result_panel.setVisible(false);
            resultMsg = "";
            settlement = "";
            insuranceMsg = "";
            resultMsg2 = "";
            settlement2 = "";
            // Shows green light
        } else {
            displayHands();
            button_panel.setVisible(false);
            result();
            active = false;
            bet = 5;
            bet_panel.setVisible(true);
            display_panel.setVisible(true);
            result_panel.setVisible(true);
            updateDisplay();
            System.out.println("");
        }
    }
    
    public String toString() {
        return "\nPlayer's Hand: " + pHand + "\nThe House: " + dHand;
    }
}