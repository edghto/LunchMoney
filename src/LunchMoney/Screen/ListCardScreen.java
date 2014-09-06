package LunchMoney.Screen;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;

import LunchMoney.Card;
import LunchMoney.CardList;
import LunchMoney.LunchMoneyController;


public class ListCardScreen extends List {

	protected LunchMoneyController lunchMoneyController;
	protected Command updateCmd = new Command("Update", Command.OK, 0);
	protected Command addCmd = new Command("Add", Command.OK, 0);
	protected Command delCmd = new Command("Delete", Command.OK, 0);
	protected Command editCmd = new Command("Edit", Command.OK, 0);
	
	public String getSelectedItem() {
		return getString(getSelectedIndex());
	}

	public ListCardScreen(final LunchMoneyController lunchMoneyController) {
		super("Your Lunch Cards", List.IMPLICIT);
		this.lunchMoneyController = lunchMoneyController;

		refresh();
		
		addCommand(updateCmd);
		addCommand(addCmd);
		addCommand(delCmd);
		addCommand(editCmd);
		setCommandListener(new CommandListener() {

			public void commandAction(Command command,
					Displayable currentDisplay) {
				if (updateCmd == command) {
					int index = getSelectedIndex();
					if(index >= 0) {
						Card card = (Card)CardList.getInstance()
								.elementAt(index);
						card.update();
						refresh(getSelectedIndex(), card);
					}
				} else if (addCmd == command) {
					lunchMoneyController.request(
							LunchMoneyController.NEW_CARD);
				} else if (delCmd == command) {
					CardList.getInstance().removeElementAt(
							getSelectedIndex());
					delete(getSelectedIndex());
				} else if (editCmd == command) {
					lunchMoneyController.request(
							LunchMoneyController.EDIT_CARD);
				}
			}
		});
	}
	
	public void delete(int index) {
		super.delete(index);
	}
	
	public void refresh(int index, Card card) {
		this.set(index, card.toString(), null);
	}
	
	public void refresh() {
		this.deleteAll();
		CardList cardList = CardList.getInstance();
		for(int i = 0; i < cardList.size(); ++i) {
			Card card = (Card) cardList.elementAt(i);
			append(card.toString(), null);
		}
	}
}
