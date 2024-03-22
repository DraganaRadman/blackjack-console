## Blackjack Console Game

A Java-based console version of the classic card game Blackjack.


**Features:**

* Single player game with standard deck of 52 cards.
* Initial bank amount of $1000 for betting (with the flexibility to customize).
* Play continues until the player runs out of money or chooses to exit.
* Follows traditional Blackjack rules:
  * Receive two cards initially.
  * Check for Blackjack: dealer or player wins with a hand value of 21.
  * Decide to Hit (draw another card) or Stand (keep current hand).
  * Dealer plays by set rules: hits until reaching 17 or outscoring player.
  * Determine the outcome by comparing hand values.


**Project Structure:**

* Main class: Launches the BlackjackGame.
* BlackjackGame class: Houses the game logic.
* BlackjackHand class: Manages and displays player's and dealer's cards.
* Card class: Represents individual cards in the deck.


**How to Run:**

* Compile the Java files.
* Run the Main class.
