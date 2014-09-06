package LunchMoney;

public class Card {
	
	public String code;
	public String cardNumber;
	public double balance;
	
	public Card() {
		code = "null";
		cardNumber = "null";
		balance = 0;
	}
	
	public boolean update() {
		balance = balance + 5.0;
		//TODO real balance update
		return true;
	}
	
	public String toString() {
		return code + " " + cardNumber 
				+ " " + balance + "zl";
	}
}
