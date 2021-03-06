package LunchMoney.UI;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.TextField;

import LunchMoney.LunchMoneyController;
import LunchMoney.Card.Card;
import LunchMoney.Card.CardController;

public class NewCardScreen extends Form {

	private CardController cardController = null;

	private TextField codeField = new TextField("Code", null, 4,
			TextField.DECIMAL);
	private TextField cardNumberField = new TextField("Card No", null, 10,
			TextField.DECIMAL);

	private Command okCmd = new Command("OK", Command.SCREEN, 0);
	private Command cancelCmd = new Command("Cancel", Command.EXIT, 1);

	private boolean isNew = true;
	private Card card;

	public NewCardScreen(final LunchMoneyController lunchMoneyController,
			final CardController cardController) {
		super("Your card");
		this.cardController = cardController;

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
					card.notifyEvent(isNew ? CardController.NEW_CARD
							: CardController.EDIT_CARD);
					card = null;

				}
				
				lunchMoneyController.request(LunchMoneyController.CARD_ALTERED);
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
		card = cardController.getNewCard();
		codeField.setString("");
		cardNumberField.setString("");
		isNew = true;
	}

}
