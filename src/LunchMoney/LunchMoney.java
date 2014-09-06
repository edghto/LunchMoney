package LunchMoney;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.MIDlet;

import LunchMoney.Screen.ListCardScreen;
import LunchMoney.Screen.NewCardScreen;

public class LunchMoney extends MIDlet implements LunchMoneyController {

	protected ListCardScreen listCardScreen;
	protected NewCardScreen newCardScreen;

	public LunchMoney() {
		listCardScreen = new ListCardScreen(this);
		newCardScreen = new NewCardScreen(this);
	}

	protected void destroyApp(boolean arg0) {
	}

	protected void pauseApp() {
	}

	protected void startApp() {
		changeDisplay(listCardScreen);
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
			listCardScreen.refresh();
			changeDisplay(listCardScreen);
		default:
			break;				
		}
	}
}
