package LunchMoney.Card;

import java.util.Vector;

public class CardController {
	
	public final static int NEW_CARD = 0;
	public final static int EDIT_CARD = 1; /* card number/code was changed */
	public final static int DEL_CARD = 2;
	public final static int CARD_ERROR = 3;
	public static final int CARD_UPDATED = 4; /* card balance was updated */

	private Vector listeners = new Vector();
	
	public CardController() {
		registerListener(CardList.getInstance());
	}
	
	public Card getNewCard() {
		return new Card(this);
	}
	
	public CardList getCardList() {
		return CardList.getInstance();
	}

	public void registerListener(CardListener listener) {
		listeners.addElement(listener);
	}
	
	public void unregisterListener(CardListener listener) {
		listeners.removeElement(listener);
	}
	
	public void notifyEvent(Card card, int eventType) {
		for(int i = 0; i < listeners.size(); ++i) {
			CardListener l = (CardListener) listeners.elementAt(i);
			l.processEvent(card, eventType);
		}
	}
	
	
}
