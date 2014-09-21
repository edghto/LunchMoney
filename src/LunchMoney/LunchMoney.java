package LunchMoney;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.MIDlet;
import javax.microedition.rms.InvalidRecordIDException;

import Generic.DB.DBIterator;
import LunchMoney.Card.Card;
import LunchMoney.Card.CardController;
import LunchMoney.Card.CardList;
import LunchMoney.UI.ListCardScreen;
import LunchMoney.UI.NewCardScreen;

public class LunchMoney extends MIDlet implements LunchMoneyController {

	private ListCardScreen listCardScreen;
	private NewCardScreen newCardScreen;
	private Alert operationError;
	private Alert operationInprogress;
	private String dbName = "LunchMoney.cards.dat";
	private CardDBManager dbManager = new CardDBManager(dbName);
	private CardController cardController = new CardController();

	public LunchMoney() {
		listCardScreen = new ListCardScreen(this);
		newCardScreen = new NewCardScreen(this, cardController);
		
		cardController.registerListener(listCardScreen);
		
		/*
		 * First load than register DB, to prevent
		 * loop of event notifications
		 */
		loadCards();
		cardController.registerListener(dbManager);
		
		operationError = new Alert("", "Internal error occurred",
				null, AlertType.ERROR);
		operationInprogress = new Alert("", "Connection in progress",
				null, AlertType.INFO);
	}

	protected void destroyApp(boolean arg0) {
	}

	protected void pauseApp() {
	}

	protected void startApp() {
		changeDisplay(listCardScreen);
	}
	
	private boolean loadCards() {
		if(!dbManager.open()) {
			request(NOTIFY_ERROR);
			return false;
		}
		
		DBIterator iter = dbManager.iterate();
		System.out.println("loadCards: numOfRecords="+iter.numRecords());
		while(iter.hasNextElement()) {
			try {
				int recordId = iter.nextRecordId();
				System.out.println("loadCards: recordId="+recordId);
				Card card = cardController.getNewCard();
				card.fromByte(dbManager.getRecord(recordId));
				card.recordId = recordId;
				card.notifyEvent(CardController.NEW_CARD);
			} catch (InvalidRecordIDException e) {
				e.printStackTrace();
			}
		}
	
		dbManager.close();
		
		return true;
	}

	public void changeDisplay(Displayable display) {
		if (null == display) {
			destroyApp(false);
			notifyDestroyed();
		}
		Display.getDisplay(this).setCurrent(display);
	}

	public void request(int requestNumber) {
		switch(requestNumber){
		case NEW_CARD: 
			newCardScreen.reset();
			changeDisplay(newCardScreen);
			break;
		case EDIT_CARD: 
			Card card = (Card) CardList.getInstance().elementAt(
					listCardScreen.getSelectedIndex());
			newCardScreen.fill(card);
			changeDisplay(newCardScreen);
			break;
		case CARD_ALTERED:
			changeDisplay(listCardScreen);
			break;
		case INPROGRESS:
			operationInprogress.setTimeout(Alert.FOREVER);
			Display.getDisplay(this)
				.setCurrent(operationInprogress, listCardScreen);
			break;
		case NOTIFY_ERROR:
		default:
			/* when error occurs it goes back to list screen */
			operationError.setTimeout(Alert.FOREVER);
			Display.getDisplay(this)
				.setCurrent(operationError, listCardScreen);
			break;
		}
	}
}
