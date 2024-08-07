import java.util.ArrayList;
import java.util.List;

// Represents a playing card in a standard deck, defined by its suit and face value
// Uses Java 14+ record feature for immutability
public record Card(Suit suit, String face) {

    // Enum representing the four suits of a standard deck of cards.
    public enum Suit {
        CLUB, DIAMOND, HEART, SPADE;
        
        // Gets the Unicode symbol for the suit
        public char getImage() {
            return (new char[]{9827, 9830, 9829, 9824})[this.ordinal()];
        }
    }

    // Returns String representation of the card
    // The format is [face][suit image], e.g., "2♣" for the 2 of Clubs.
    @Override
    public String toString() {
        int index = face.equals("10") ? 2 : 1;
        String faceString = face.substring(0, index);
        return faceString + suit.getImage();
    }

    // Creates a numeric card (2-10) of the given suit.
    public static Card getNumericCard(Suit suit, int cardNumber) {
        if (cardNumber > 1 && cardNumber < 11) {
            return new Card(suit, String.valueOf(cardNumber));
        }

        System.out.println("Invalid Numeric card selected");
        return null;
    }

    // Creates a face card (Jack, Queen, King, Ace) of the given suit.
    // suit: The suit of the card.
    // abbrFace: The abbreviated face value of the card ('J', 'Q', 'K', 'A').
    public static Card getFaceCard(Suit suit, char abbrFace) {

        if (abbrFace == 'J' || abbrFace == 'Q' || abbrFace == 'K' || abbrFace == 'A') {
            return new Card(suit, String.valueOf(abbrFace));
        }
        System.out.println("Invalid Face card selected");
        return null;
    }

    // Generates and returns a standard deck of 52 cards with all suits and values.
    public static List<Card> getStandardDeck() {

        List<Card> deck = new ArrayList<>(52);

        for (Suit suit : Suit.values()) {
            deck.add(getFaceCard(suit, 'A'));
            for (int i = 2; i <= 10; i++) {
                deck.add(getNumericCard(suit, i));
            }
            for (char c : new char[] {'J', 'Q', 'K'}) {
                deck.add(getFaceCard(suit, c));
            }
        }
        return deck;
    }

    // Prints the cards in a standard deck in a visually formatted manner.
    // The deck is divided into four rows, each representing a suit, with cards separated by spaces.
    public static void printStandardDeck(List<Card> deck) {
        System.out.println("---------------------------");

        System.out.println("Standard Deck");

        int cardsInRow = deck.size() / 4;
        for (int i = 0; i < 4; i++) {
            int startIndex = i * cardsInRow;
            int endIndex = startIndex + cardsInRow;
            deck.subList(startIndex, endIndex).forEach(c -> System.out.print(c + " "));
            System.out.println();
        }
    }

}
