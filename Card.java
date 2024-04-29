public class Card {
    private String suit;
    private int value; // Blackjack value -> Ace is assigned '11' to start
    private String name;
    
    private final String[] SUITS = {"Clubs", "Hearts", "Spades", "Diamonds"};
    private final String[] NAME = {"Ace", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Jack", "Queen", "King"};
    private final String IMAGE_TAG = ".png";
    
    public Card() {
        suit = (SUITS[(int)(Math.random()*(SUITS.length))]);
        value = (int)(Math.random()*(NAME.length)+1); // Temporary value
        name = NAME[value-1];
        if(value > 10)
            value = 10;
        if(value == 1) // If card is 'Ace' -> assign value 11
            value = 11;
    }
    // s (suit) must be 0-3
    // v (value) must be 1-13
    public Card(int s, int v) {
        suit = SUITS[s];
        value = v; // Temporary value
        name = NAME[value-1];
        if(value > 10)
            value = 10;
        if(name == NAME[0]) // If card is 'Ace' -> assign value 11
            value = 11;
    }
    // Get methods
    public String getSuit() {
      return suit;
    }
    public int getValue() {
      return value;
    }
    public String getName() {
      return name;
    }
    public String getImg() {
        return name + suit + IMAGE_TAG;
    }
     
    // Set methods
    public void setValue(int v) {
      value = v;
    }
    
    public String toString() {
        return "\n" + name + " of " + suit;
    }
}