package assignment2;

import java.util.Random;

public class Deck {
 public static String[] suitsInOrder = {"clubs", "diamonds", "hearts", "spades"};
 public static Random gen = new Random();

 public int numOfCards; // contains the total number of cards in the deck
 public Card head; // contains a pointer to the card on the top of the deck

 /* 
  * TODO: Initializes a Deck object using the inputs provided
  */
 public Deck(int numOfCardsPerSuit, int numOfSuits) {

  if (numOfCardsPerSuit <1 || numOfCardsPerSuit >13 || numOfSuits<1 || numOfSuits>suitsInOrder.length){
   throw new IllegalArgumentException("Invalid arguments");
  }

  for (int i=0;i<numOfSuits;i++){
   for (int j=1;j<=numOfCardsPerSuit;j++){
    PlayingCard newCard = new PlayingCard(suitsInOrder[i], j); //Create new node
    addCard(newCard);
   }
  }
  addCard(new Joker("red"));
  addCard(new Joker("black"));

 }

 /*
  * TODO: Implements a copy constructor for Deck using Card.getCopy().
  * This method runs in O(n), where n is the number of cards in d.
  */
 public Deck(Deck d) {
  Card temp = d.head; //pointer for d

  for (int i=0;i<d.numOfCards;i++){
   this.addCard(temp.getCopy());
   temp = temp.next;
  }
 }

 /*
  * For testing purposes we need a default constructor.
  */
 public Deck() {}

 /* 
  * TODO: Adds the specified card at the bottom of the deck. This 
  * method runs in $O(1)$. 
  */
 public void addCard(Card c) {
  if (this.head == null){ //The deck is empty
   this.head = c;
   //make it circular
   c.next=c;
   c.prev=c;
  }else{
   head.prev.next = c; //head.prev is the last card
   c.next=head;
   c.prev=head.prev;
   head.prev = c;
  }

  numOfCards++;
 }

 /*
  * TODO: Shuffles the deck using the algorithm described in the pdf. 
  * This method runs in O(n) and uses O(n) space, where n is the total 
  * number of cards in the deck.
  */
 public void shuffle() {

  if (numOfCards == 0) {return;} //Check if the deck is empty

  Card tempPointer = head;
  Card[] copyCards = new Card[numOfCards];

  for (int i=0; i<numOfCards;i++){
   copyCards[i] = tempPointer;
   tempPointer=tempPointer.next;
  }

  //shuffle the deck
  for (int i=numOfCards-1;i>=1;i--){
   int j = gen.nextInt(i+1); //We want i to be inclusive
   //swap
   Card temp = copyCards[i];
   copyCards[i] = copyCards[j];
   copyCards[j]=temp;
  }

  //Rebuild the deck
  head = null;
  numOfCards = 0;
  for (int i=0; i<copyCards.length;i++){
   addCard(copyCards[i]);
  }
 }

 /*
  * TODO: Returns a reference to the joker with the specified color in 
  * the deck. This method runs in O(n), where n is the total number of 
  * cards in the deck. 
  */
 public Joker locateJoker(String color) {
  Card temp=head;
  for (int i=0;i<numOfCards;i++){
   if (temp instanceof Joker && ((Joker) temp).redOrBlack == color){return (Joker) temp;}
   temp=temp.next;
  }
  return null;
 }

 /*
  * TODO: Moved the specified Card, p positions down the deck. You can 
  * assume that the input Card does belong to the deck (hence the deck is
  * not empty). This method runs in O(p).
  */
 public void moveCard(Card c, int p) {
  if (p%numOfCards==0) {return;} //The deck doesn't change

  //Take out the card
  c.prev.next = c.next;
  c.next.prev = c.prev;

  //Move it "p" cards down
  Card temp = c;
  for (int i=0;i<p;i++){
   temp = temp.next;
  }


  //Insert it in the desired position
  c.next = temp.next;
  c.prev = temp;
  temp.next.prev = c;
  temp.next = c;

 }

 /*
  * TODO: Performs a triple cut on the deck using the two input cards. You 
  * can assume that the input cards belong to the deck and the first one is 
  * nearest to the top of the deck. This method runs in O(1)
  */
 public void tripleCut(Card firstCard, Card secondCard) {

  //Edge case 1: First joker is the top card and second joker is the last card
  if (firstCard == head && secondCard == head.prev){
   return; //Don't do anything
  }
  //Edge case 2: First joker is the top card and second joker is not at the bottom
  else if(firstCard == head){
   head = secondCard.next;
   return;
  }
  //Edge case 3: First joker is not at the top and the second joker is at the bottom
  else if (secondCard == head.prev){
   head = firstCard;
   return;
  }

  Card newHead = secondCard.next; //This points to the card that will be on top after the triple cut

  //Triple cut
  secondCard.next.prev = firstCard.prev;
  firstCard.prev.next = secondCard.next;
  firstCard.prev = head.prev;
  secondCard.next = head;
  head.prev.next = firstCard;
  head.prev = secondCard;

  //Update head
  head = newHead;

 }

 /*
  * TODO: Performs a count cut on the deck. Note that if the value of the 
  * bottom card is equal to a multiple of the number of cards in the deck, 
  * then the method should not do anything. This method runs in O(n).
  */
 public void countCut() {

  int count = head.prev.getValue() % numOfCards; //get the number of cards to cut

  if (count==0 || count == numOfCards-1) {return;}

  Card headPointer = head;
  Card tailPointer = head.prev; //Points to the bottom card

  for (int i=0;i<count;i++){
   headPointer = headPointer.next;
  }

  headPointer.prev.next=tailPointer;
  tailPointer.prev.next=head;

  head.prev = tailPointer.prev;
  tailPointer.prev=headPointer.prev;

  headPointer.prev = tailPointer;
  tailPointer.next = headPointer;
  head = headPointer;

 }

 /*
  * TODO: Returns the card that can be found by looking at the value of the 
  * card on the top of the deck, and counting down that many cards. If the 
  * card found is a Joker, then the method returns null, otherwise it returns
  * the Card found. This method runs in O(n).
  */
 public Card lookUpCard() {
  int count = head.getValue();
  Card temp = head;

  for (int i=0;i<count;i++){
   temp=temp.next;
  }

  if (!(temp instanceof Joker)){return temp;}
  return null;
 }

 /*
  * TODO: Uses the Solitaire algorithm to generate one value for the keystream 
  * using this deck. This method runs in O(n).
  */
 public int generateNextKeystreamValue() {

  //Just keep repeating until we hit a return statement, AKA a non-joker card
  while (true) {

   //locate
   Joker redJoker = locateJoker("red");
   Joker blackJoker = locateJoker("black");

   //move
   if (redJoker != null){moveCard(redJoker, 1);}
   if (blackJoker != null){moveCard(blackJoker, 2);}

   //triple cut
   Card temp = head;
   while (!(temp instanceof Joker)) {
    temp = temp.next;
   }

   if ((((Joker) temp).redOrBlack).equals("red")) {
    tripleCut(temp, blackJoker);
   } else {
    tripleCut(temp, redJoker);
   }

   countCut();

   if (lookUpCard() != null) {
    return lookUpCard().getValue();
   }

  }
 }


 public abstract class Card { 
  public Card next;
  public Card prev;

  public abstract Card getCopy();
  public abstract int getValue();

 }

 public class PlayingCard extends Card {
  public String suit;
  public int rank;

  public PlayingCard(String s, int r) {
   this.suit = s.toLowerCase();
   this.rank = r;
  }

  public String toString() {
   String info = "";
   if (this.rank == 1) {
    //info += "Ace";
    info += "A";
   } else if (this.rank > 10) {
    String[] cards = {"Jack", "Queen", "King"};
    //info += cards[this.rank - 11];
    info += cards[this.rank - 11].charAt(0);
   } else {
    info += this.rank;
   }
   //info += " of " + this.suit;
   info = (info + this.suit.charAt(0)).toUpperCase();
   return info;
  }

  public PlayingCard getCopy() {
   return new PlayingCard(this.suit, this.rank);   
  }

  public int getValue() {
   int i;
   for (i = 0; i < suitsInOrder.length; i++) {
    if (this.suit.equals(suitsInOrder[i]))
     break;
   }

   return this.rank + 13*i;
  }

 }

 public class Joker extends Card{
  public String redOrBlack;

  public Joker(String c) {
   if (!c.equalsIgnoreCase("red") && !c.equalsIgnoreCase("black")) 
    throw new IllegalArgumentException("Jokers can only be red or black"); 

   this.redOrBlack = c.toLowerCase();
  }

  public String toString() {
   //return this.redOrBlack + " Joker";
   return (this.redOrBlack.charAt(0) + "J").toUpperCase();
  }

  public Joker getCopy() {
   return new Joker(this.redOrBlack);
  }

  public int getValue() {
   return numOfCards - 1;
  }

  public String getColor() {
   return this.redOrBlack;
  }
 }

}
