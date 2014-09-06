package LunchMoney;

import java.util.Vector;

public class CardList extends Vector {

	protected static CardList instance = null; 
	
	private CardList() {}
	
	public static CardList getInstance() {
		if( instance == null) {
			instance = new CardList();
		}
		return instance;
	}
}
