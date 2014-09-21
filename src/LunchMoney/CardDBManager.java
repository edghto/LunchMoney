package LunchMoney;

import Generic.DB.DBManager;
import LunchMoney.Card.Card;
import LunchMoney.Card.CardController;
import LunchMoney.Card.CardListener;

public class CardDBManager extends DBManager implements CardListener {

	public CardDBManager(String dbName) {
		super(dbName);
	}

	public boolean processEvent(Card card, int eventType) {
		boolean result = false;
		int recordId = card.recordId;
		byte[] recordData = card.toByte();
		
		//TODO open should be performed only if event type is correct	
		if(!open())
			return false;
		
		switch(eventType)
		{
		case CardController.EDIT_CARD:
		case CardController.CARD_UPDATED:
			result = editRecord(recordId, recordData);
			break;
		case CardController.NEW_CARD:
			result = addRecord(recordData);
			break;
		case CardController.DEL_CARD:
			delRecord(recordId);
			break;
		default:
		}

		close();
		
		return result;
	}
}
