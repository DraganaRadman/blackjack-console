import java.util.*;

// Represents a Blackjack game where a player can interactively play against the dealer.
public class BlackjackGame {
    private int bank;
    private List<Card> deck;
    private Scanner scanner;

    // Constructs a Blackjack game with a default bank balance of $1000 and a standard deck of cards.
    public BlackjackGame() {
        this(1000, Card.getStandardDeck());
        startGame();
    }

    // Constructs a Blackjack game with a specified initial bank balance and a standard deck of cards.
    public BlackjackGame(int bank) {
        this(bank, Card.getStandardDeck());
        startGame();
    }

    // Constructs a Blackjack game with a specified initial bank balance and deck of cards.
    public BlackjackGame(int bank, List<Card> deck) {
        this.bank = bank;
        this.deck = new ArrayList<>(deck);
        this.scanner = new Scanner(System.in);
        startGame();
    }

    /*
      Starts the Blackjack game by displaying the welcome message and managing user input to start or quit the game.
      This method serves as the entry point for the game, prompting the user to either play or quit.
      It loops indefinitely until the user chooses to quit the game, ensuring continuous gameplay.
     */    
    private void startGame() {

        displayWelcomeMessage();

        while (true) {
            System.out.println("\nEnter P to PLAY or Q to QUIT: ");
            String userInput = scanner.nextLine().trim().toUpperCase();

            if (userInput.equals("P")) {
                playBlackjack();
            } else if (userInput.equals("Q")) {
                System.exit(0);
            } else {
                System.out.println("Invalid input.");
            }
        }
    }

    // Displays the welcome message for the Blackjack game.
    private void displayWelcomeMessage() {
        System.out.println();
        System.out.printf("%28s \n%s\n", "-- Welcome to Blackjack Game --", "-".repeat(31));
    }

    /*
      Manages the main gameplay loop of Blackjack, including betting, rounds, and bank calculations.
      This method controls the flow of the game after it has been initialized.
      It loops until the player chooses to quit or runs out of money.
     */
    private void playBlackjack() {
        int currentGameBank = bank;
        while (true) {
            // Get player's bet amount
            System.out.println("\nYou have $" + currentGameBank);
            int bet;
            System.out.println("What amount do you want to bet? ");

            do {
                // Validate and process the player's bet input
                String input = scanner.nextLine().trim();
                try {
                    bet = Integer.parseInt(input);
                    if (bet < 0 || bet > currentGameBank) {
                        System.out.println("Please enter a valid amount (between 0 and $" + currentGameBank + "): ");
                        continue;
                    }
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Please enter a valid number: ");
                }
            } while (true);

            if (bet == 0) {
                break; // End game if the player decides not to bet
            }

            // Play a round of Blackjack
            int result = playRound(deck);

            // Update player's bank balance based on the round result
            if (result == 0) {
                currentGameBank += 0;
            } else if (result == 1) {
                currentGameBank -= bet;
            } else {
                currentGameBank += bet;
            }

            // Check if player runs out of money
            System.out.println();
            if (currentGameBank == 0) {
                System.out.println("You have lost all your money!!!");
                System.out.println("-".repeat(31) + "\n-----------GAME OVER-----------\n");
                break; // End game if player runs out of money
            }
        }
    }

    /*
      Plays a single round of Blackjack, including dealing cards, player and dealer turns, and determining the winner.      
      Returns 0 for tie, 1 for dealer win, 2 for player win.
     */
    private int playRound(List<Card> deck) {

        Collections.shuffle(deck);

        // Initialize player and dealer hands
        BlackjackHand playerHand = new BlackjackHand();
        BlackjackHand dealerHand = new BlackjackHand();

        // Deal initial cards to player and dealer
        boolean reloaded = reloadDeck(deck);
        if (reloaded) {
            System.out.println("Deck reloaded.");
        }
        dealInitialCards(playerHand, dealerHand, deck);

        // Handle immediate win conditions
        if (hasBlackjack(dealerHand) && hasBlackjack(playerHand)) {
            System.out.println("Dealer`s cards are: " + dealerHand.getHand().get(0) + " " + dealerHand.getHand().get(1)
                    + " / Dealer`s total = 21");
            System.out.println("Your cards are: " + playerHand.getHand().get(0) + " " + playerHand.getHand().get(1) +
                    " / Your total = 21");
            System.out.println("YOU BOTH GOT BLACKJACK!!! It`s a tie.");
            return 0;
        } else if (hasBlackjack(dealerHand) && !hasBlackjack(playerHand)) {
            System.out.println("Dealer`s cards are: " + dealerHand.getHand().get(0) + " " + dealerHand.getHand().get(1)
                    + " / Dealer`s total = 21");
            System.out.println("Your cards are: " + playerHand.getHand().get(0) + " " + playerHand.getHand().get(1) +
                    " / Your total = " + playerHand.sumCards());
            System.out.println("DEALER GOT BLACKJACK!!! You lose.");
            return 1;
        } else if (!hasBlackjack(dealerHand) && hasBlackjack(playerHand)) {
            System.out.println("Dealer`s cards are: " + dealerHand.getHand().get(0) + " " + dealerHand.getHand().get(1)
                    + " / Dealer`s total = " + dealerHand.sumCards());
            System.out.println("Your cards are: " + playerHand.getHand().get(0) + " " + playerHand.getHand().get(1) +
                    " / Your total = 21");
            System.out.println("YOU GOT BLACKJACK!!! You win.");
            return 2;
        }

        // Main game loop for player's turns
        while (true) {
            // Display player's hand and prompt for Hit or Stand
            System.out.print("\nYour cards are: ");
            for (Card c : playerHand.getHand()) {
                System.out.print(c + " ");
            }
            System.out.print("/ Your total = " + playerHand.sumCards());
            System.out.println();
            System.out.println("Dealer is showing the card: " + dealerHand.getHand().get(0));
            System.out.println("Hit or Stand? (Enter H or S): ");
            String choice;

            // Loop until valid input (H or S) is provided by the player
            do {
                choice = scanner.nextLine();
                if (!choice.equalsIgnoreCase("H") && !choice.equalsIgnoreCase("S")) {
                    System.out.println("Please enter a valid character (H or S): ");
                }
            } while (!choice.equalsIgnoreCase("H") && !choice.equalsIgnoreCase("S"));

            // If player stands, break out of the loop
            if (choice.equalsIgnoreCase("S")) {
                break;
            } else { // If player hits
                // Reload the deck if necessary
                reloaded = reloadDeck(deck);
                if (reloaded) {
                    System.out.println("Deck reloaded.");
                }
                // Draw a card for the player
                Card drawnCard = getRandomCard(deck);
                deck.remove(drawnCard);
                playerHand.getHand().add(drawnCard);
                System.out.print("You drew: " + drawnCard);

                // Handle win or loss conditions after drawing a card
                if (isBusted(playerHand) && dealerHand.sumCards() < 21) {
                    System.out.print(" / Your total = " + playerHand.sumCards() + "\n");
                    System.out.println("Dealer's other card was: " + dealerHand.getHand().get(1) +
                            " / Dealer`s total = " + dealerHand.sumCards());
                    System.out.println("You got over 21!!! You lose.");
                    return 1;
                }
                if (hasBlackjack(dealerHand) && !hasBlackjack(playerHand)) {
                    System.out.print(" / Your total = " + playerHand.sumCards() + "\n");
                    System.out.println("Dealer's other card was: " + dealerHand.getHand().get(1) +
                            " / Dealer`s total = " + dealerHand.sumCards());
                    System.out.println("DEALER GOT BLACKJACK!!! You lose.");
                    return 1;
                }
                if (hasBlackjack(playerHand) && !hasBlackjack(dealerHand)) {
                    System.out.print(" / Your total = " + playerHand.sumCards() + "\n");
                    System.out.println("Dealer's other card was: " + dealerHand.getHand().get(1) +
                            " / Dealer`s total = " + dealerHand.sumCards());
                    System.out.println("YOU GOT BLACKJACK!!! You win.");
                    return 2;
                }
                if (hasBlackjack(playerHand) && hasBlackjack(dealerHand)) {
                    System.out.print(" / Your total = " + playerHand.sumCards() + "\n");
                    System.out.println("Dealer's other card was: " + dealerHand.getHand().get(1) +
                            " / Dealer`s total = " + dealerHand.sumCards());
                    System.out.println("YOU BOTH GOT BLACKJACK!!! It`s a tie.");
                    return 0;
                }
            }
        }

        // Dealer's turn
        System.out.println("Dealer's cards are: " + dealerHand.getHand().get(0) + " " + dealerHand.getHand().get(1) +
                " / Dealer`s total = " + dealerHand.sumCards());

        // Loop until the dealer's hand total reaches 17 or exceeds the player's hand total
        while (dealerHand.sumCards() < 17 ||
                dealerHand.sumCards() < playerHand.sumCards()) {
            // Reload the deck if necessary
            reloaded = reloadDeck(deck);
            if (reloaded) {
                System.out.println("Deck reloaded.");
            }
            // Draw a card for the dealer
            Card drawnCard = getRandomCard(deck);
            deck.remove(drawnCard);
            dealerHand.getHand().add(drawnCard);
            System.out.println("Dealer hits and gets the card: " + drawnCard +
                    " / Dealer`s total = " + dealerHand.sumCards());

            // Check if dealer's hand total exceeds 21 (bust)
            if (dealerHand.sumCards() > 21) {
                System.out.println("Dealer got over 21!!! You win!!!");
                return 2;
            }

            // Check if dealer got Blackjack
            if (dealerHand.sumCards() == 21) {
                System.out.println("DEALER GOT BLACKJACK!!! You lose.");
            }
        }

        // Compare dealer's hand total with player's hand total to determine the winner
        if (dealerHand.sumCards() == playerHand.sumCards()) {
            System.out.println("You both have " + dealerHand.sumCards() + " - it`s a tie.");
            return 0;
        } else if (dealerHand.sumCards() > playerHand.sumCards()) {
            System.out.println("Dealer`s total = " + dealerHand.sumCards() + " / " +
                    "Your total = " + playerHand.sumCards());
            System.out.println("Dealer wins!!! You lose.");
            return 1;
        } else {
            System.out.println("Dealer`s total = " + dealerHand.sumCards() + " / " +
                    "Your total = " + playerHand.sumCards());
            System.out.println("Dealer lose. You win!!!");
            return 2;
        }
    }

    // Deals initial cards to the player and dealer from the deck
    private void dealInitialCards(BlackjackHand playerHand, BlackjackHand dealerHand, List<Card> deck) {
        // Deal two cards to the player
        for (int i = 0; i < 2; i++) {
            Card randomCard = getRandomCard(deck);
            deck.remove(randomCard);
            playerHand.getHand().add(randomCard);
        }

        // Deal two cards to the dealer
        for (int i = 0; i < 2; i++) {
            Card randomCard = getRandomCard(deck);
            deck.remove(randomCard);
            dealerHand.getHand().add(randomCard);
        }
    }

    // Checks if the given hand has a Blackjack (a total of 21).
    private static boolean hasBlackjack(BlackjackHand hand) {
        return hand.sumCards() == 21;
    }

    // Checks if the given hand is busted (has a total exceeding 21).
    private static boolean isBusted(BlackjackHand hand) {
        return hand.sumCards() > 21;
    }
    
    // Retrieves a random card from the given deck
    // If the deck is empty, it reloads it with a standard deck and shuffles.
    public static Card getRandomCard(List<Card> deck) {
        if (deck.isEmpty()) {
            reloadDeck(deck);
        }
        Random r = new Random();
        return deck.get(r.nextInt(deck.size()));
    }

    // Reloads the given deck if it is empty.
    // A standard deck of cards is added to the deck and shuffled.
    public static boolean reloadDeck(List<Card> deck) {
        if (deck.isEmpty()) {
            deck.addAll(Card.getStandardDeck());
            Collections.shuffle(deck);
            return true;
        }
        return false;
    }

}


