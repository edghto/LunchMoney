package LunchMoney;

public interface LunchMoneyController {
	
	public final int NEW_CARD = 1;
	public final int EDIT_CARD = 2;
	public final int CARD_ALTERED = 3;
	public final int NOTIFY_ERROR = 4;
	public final int INPROGRESS = 5;
	
	public void request(int requestNumber);
}
