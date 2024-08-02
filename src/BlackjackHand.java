import java.util.ArrayList;
import java.util.List;

// Represents a hand in a Blackjack game, which holds a collection of cards
public class BlackjackHand {
    private List<Card> hand; // List to store the cards in the hand

    // Constructor to initialize a new BlackjackHand object
    // Initializes the hand as an empty ArrayList
    public BlackjackHand() {
        this.hand = new ArrayList<>();
    }

    // Getter to retrieve the list of cards in the hand
    public List<Card> getHand() {
        return hand;
    }

    // Calculates the total value of the cards in the hand.
    // Aces are counted as 11 if the total is less than or equal to 10; otherwise, they are counted as 1.
    // Face cards (Jack, Queen, King) are counted as 10.
    // Numeric cards are counted at their face value.
    public int sumCards() {
        if (hand != null) {
            int total = 0;
            for (Card card : hand) {
                if (card.face().equals("J") || card.face().equals("Q") || card.face().equals("K")) {
                    total += 10;
                } else if (card.face().equals("A")) {
                    if (total <= 10) {
                        total += 11;
                    } else {
                        total += 1;
                    }
                } else {
                    total += Integer.parseInt(card.face());
                }
            }
            return total;
        }
        return 0;
    }

    // Prints out the cards in the hand.
    public void printCards() {
        for (Card c : this.hand) {
            System.out.print(c + " ");
        }
    }
}
