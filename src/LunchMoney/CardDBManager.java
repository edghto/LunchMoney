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
		
		if(!open())
			return false;
				
		if(eventType == CardController.EDIT_CARD)
			result = editRecord(recordId, recordData);
		else if(eventType == CardController.NEW_CARD)
			result = addRecord(recordData);
		else if(eventType == CardController.DEL_CARD)
			delRecord(recordId);
		else
			return false;

		close();
		
		return result;
	}
}
