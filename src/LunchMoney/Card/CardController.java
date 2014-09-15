package LunchMoney.Card;

import java.util.Vector;

public class CardController {
	
	public final static int NEW_CARD = 0;
	public final static int EDIT_CARD = 1;
	public final static int DEL_CARD = 2;

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
