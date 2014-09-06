package LunchMoney.Screen;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.TextField;

import LunchMoney.Card;
import LunchMoney.CardList;
import LunchMoney.LunchMoneyController;


public class NewCardScreen extends Form {

	protected LunchMoneyController lunchMoneyController;
	protected TextField codeField = 
			new TextField("Code", null, 4, TextField.DECIMAL);
	protected TextField cardNumberField = 
			new TextField("Card No", null, 10, TextField.DECIMAL);
	private Command okCmd = new Command("OK", Command.OK, 0);
	private Command cancelCmd = new Command("Cancel", Command.EXIT, 1);
	protected boolean isNew = true;
	protected Card card;
	
	public NewCardScreen(final LunchMoneyController lunchMoneyController) {
		super("Your card");
		this.lunchMoneyController = lunchMoneyController;
		
		append(codeField);
		append(cardNumberField);
		addCommand(okCmd);
		addCommand(cancelCmd);
		setCommandListener(new CommandListener() {

			public void commandAction(Command command,
					Displayable currentDisplay) {
				if (okCmd == command) {
					card.code = codeField.getString();
					card.cardNumber = cardNumberField.getString();
					CardList cardList = CardList.getInstance(); 
					if(isNew) {
						cardList.addElement(card);
					}
					card = null;
					
				}
				lunchMoneyController.request(
						LunchMoneyController.CARD_ALTERED);
			}
		});
	}

	public void fill(Card card) {
		this.card = card;
		codeField.setString(card.code);
		cardNumberField.setString(card.cardNumber);
		isNew = false;
	}

	public void reset() {
		card = new Card();
		codeField.setString("");
		cardNumberField.setString("");
		isNew = true;
	}

}
