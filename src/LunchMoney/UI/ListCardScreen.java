package LunchMoney.UI;

import java.util.Vector;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;

import LunchMoney.LunchMoneyController;
import LunchMoney.Card.Card;
import LunchMoney.Card.CardController;
import LunchMoney.Card.CardList;
import LunchMoney.Card.CardListener;

public class ListCardScreen extends List implements CardListener  {

	private Command updateCmd = new Command("Update", Command.SCREEN, 0);
	private Command addCmd = new Command("Add", Command.OK, 0);
	private Command delCmd = new Command("Delete", Command.OK, 0);
	private Command editCmd = new Command("Edit", Command.OK, 0);
	private Vector cardsId = new Vector();
	private LunchMoneyController lunchMoneyController = null;

	public String getSelectedItem() {
		return getString(getSelectedIndex());
	}

	public ListCardScreen(final LunchMoneyController lunchMoneyController) {
		super("Your Lunch Cards", List.IMPLICIT);
		
		this.lunchMoneyController  = lunchMoneyController;
		addCommand(updateCmd);
		addCommand(addCmd);
		addCommand(delCmd);
		addCommand(editCmd);
		setCommandListener(new CommandListener() {

			public void commandAction(Command command,
					Displayable currentDisplay) {
				if (updateCmd == command) {
					int index = getSelectedIndex();
					if (index >= 0) {
						Card card = (Card) CardList.getInstance().elementAt(
								index);
						if (!card.update())
							lunchMoneyController.request(
									LunchMoneyController.NOTIFY_ERROR);
						else
							lunchMoneyController.request(
									LunchMoneyController.INPROGRESS);
					}
				} else if (addCmd == command) {
					lunchMoneyController.request(
							LunchMoneyController.NEW_CARD);
				} else if (delCmd == command) {
					Card card = (Card) CardList.getInstance().elementAt(
							getSelectedIndex());
					card.notifyEvent(CardController.DEL_CARD);
				} else if (editCmd == command) {
					lunchMoneyController
							.request(LunchMoneyController.EDIT_CARD);
				}
			}
		});
	}

	public boolean processEvent(Card card, int eventType) {
		int index = getIndex(card.recordId);
				
		switch(eventType)
		{
		case CardController.EDIT_CARD:
			set(index, card.toString(), null);
			break;
		case CardController.CARD_UPDATED:
			set(index, card.toString(), null);
			lunchMoneyController.request(
					LunchMoneyController.CARD_ALTERED);
			break;
		case CardController.NEW_CARD:
			append(card.toString(), null);
			cardsId.addElement(new Integer(card.recordId));
			break;
		case CardController.DEL_CARD:
			delete(index);
			cardsId.removeElementAt(index);
			break;
		case CardController.CARD_ERROR:
			lunchMoneyController.request(
					LunchMoneyController.NOTIFY_ERROR);
			break;
		default:
			return false;
		}
		
		return true;
	}
	
	private int getIndex(int cardId) {
		int i = 0;
		for(; i < cardsId.size(); ++i) 
			if(cardId == ((Integer)cardsId.elementAt(i)).intValue())
				return i;

		return -1;
	}
}
