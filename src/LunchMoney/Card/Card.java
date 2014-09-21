package LunchMoney.Card;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import Generic.HTTP.HttpPOSTCallback;
import Generic.HTTP.HttpPOSTWorker;

public class Card implements HttpPOSTCallback {
	
	public String code;
	public String cardNumber;
	public double balance;
	public int recordId = -1;
	//public DateTime date; //TODO add ;)
	private CardController cardController;
	
	public Card() {
		code = "null";
		cardNumber = "null";
		balance = 0;
	}
	
	public Card(CardController cardController) {
		this.cardController= cardController; 
	}

	public boolean update() {
		HttpPOSTWorker httpWorker = HttpPOSTWorker.getWorker();
		String url = "http://www.edenred.pl/mobileapp/";
		String message = 
				"action=mobileweb&code=" + code + 
				"&cardNumber=" + cardNumber + "" +
				"&lang=pl";
		httpWorker.connect(url, message.getBytes(), this);
		
		return true;
	}
	
	public void notifyEvent(int eventType) {
		cardController.notifyEvent(this, eventType);
	}
	
	public String toString() {
		return code //+ " " + cardNumber 
				+ "    " + balance + "zl";
	}

	public byte[] toByte() {
		ByteArrayOutputStream streamBytes = new ByteArrayOutputStream();
		DataOutputStream outputStream = new DataOutputStream(streamBytes);
		
		try {
			outputStream.writeUTF(code);
			outputStream.writeUTF(cardNumber);
			outputStream.writeUTF(balance+"");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return streamBytes.toByteArray();
	}

	public void fromByte(byte[] record) {
		String balanceStr = null;
		ByteArrayInputStream strmBytes = new ByteArrayInputStream(record);
	    DataInputStream inputStream = new DataInputStream(strmBytes);

	    try {
		    code = inputStream.readUTF();
		    cardNumber = inputStream.readUTF();
		    balanceStr = inputStream.readUTF();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    
		try {
			balance = Double.parseDouble(balanceStr.trim());
		} catch(NumberFormatException e) {
			e.printStackTrace();
		}
	}

	public String dump() {
		return "recordId="+recordId+",code="+code+",balance="+balance;
	}

	public void handleHttpPOSTRespone(byte[] message) {
		String response = new String(message);
		System.out.println(response);
		response = response.substring(
				response.indexOf("amount\":")+8,
				response.indexOf("}"));
		
		try {
			balance = Double.parseDouble(response.trim());
			notifyEvent(CardController.CARD_UPDATED);
		} catch(NumberFormatException e) {
			e.printStackTrace();
			notifyEvent(CardController.CARD_ERROR);
		}
	}
}
