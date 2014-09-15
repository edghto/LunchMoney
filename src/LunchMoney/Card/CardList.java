package LunchMoney.Card;

import java.util.Vector;

public class CardList extends Vector implements CardListener {

	protected static CardList instance = null; 
	
	private CardList() {}
	
	public static CardList getInstance() {
		if( instance == null) {
			instance = new CardList();
		}
		return instance;
	}

	public boolean processEvent(Card card, int eventType) {
		if(eventType == CardController.EDIT_CARD)
			;//nothing to do
		else if(eventType == CardController.NEW_CARD)
			addElement(card);
		else if(eventType == CardController.DEL_CARD)
			removeElement(card);
		else
			return false;
		
		return true;
	}
}
