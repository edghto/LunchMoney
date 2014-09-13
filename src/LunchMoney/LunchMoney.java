package LunchMoney;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.MIDlet;
import javax.microedition.rms.RecordStore;

import LunchMoney.Screen.ListCardScreen;
import LunchMoney.Screen.NewCardScreen;

public class LunchMoney extends MIDlet implements LunchMoneyController {

	protected ListCardScreen listCardScreen;
	protected NewCardScreen newCardScreen;
	protected Alert alert;

	public LunchMoney() {
		listCardScreen = new ListCardScreen(this);
		newCardScreen = new NewCardScreen(this);
		alert = new Alert("Internal error occurred");
	}

	protected void destroyApp(boolean arg0) {
		if(!saveCards())
			request(NOTIFY_ERROR);
	}

	protected void pauseApp() {
	}

	protected void startApp() {
		if(!loadCards())
			request(NOTIFY_ERROR);
		changeDisplay(listCardScreen);
	}

	private boolean saveCards() {
		RecordStore rs = null;
		try {
			rs = RecordStore.openRecordStore(
					"LunchMoney.cards.dat", true);
			CardList cardList = CardList.getInstance();
			
			for(int i = 1; i <= cardList.size(); ++i) {
				Card card = (Card) cardList.elementAt(i);
				byte[] b = card.toByte();
				rs.addRecord(b, 0, b.length);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				rs.closeRecordStore();
			}catch(Exception e){
			}
		}
		
		return true;
	}
	
	private boolean loadCards() {
		RecordStore rs = null;
		try {
			rs = RecordStore.openRecordStore(
					"LunchMoney.cards.dat", false);
			CardList cardList = CardList.getInstance();
			
			for(int i = 1; i <= rs.getNumRecords(); ++i) {
				Card card = new Card(rs.getRecord(i));
				cardList.addElement(card);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				rs.closeRecordStore();
			}catch(Exception e){
			}
		}
		
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
			if(!saveCards())
				request(NOTIFY_ERROR);
			listCardScreen.refresh();
			changeDisplay(listCardScreen);
		case NOTIFY_ERROR:
		default:
			alert.setTimeout(5000);
		    alert.setType(AlertType.ERROR);
			break;				
		}
	}
}
