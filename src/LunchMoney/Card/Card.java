package LunchMoney.Card;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

public class Card  {
	
	public String code;
	public String cardNumber;
	public double balance;
	public int id = -1;
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
		//TODO ugly piece of code, need to refactor 
		try {
			String message = 
					"action=mobileweb&code=" + code + 
					"&cardNumber=" + cardNumber + "" +
					"&lang=pl";
			HttpConnection hc = (HttpConnection) 
					Connector.open("http://www.edenred.pl/mobileapp/", 
					Connector.READ_WRITE);
			hc.setRequestMethod(HttpConnection.POST);
			hc.setRequestProperty("Content-Type", 
					"application/x-www-form-urlencoded");
			DataOutputStream out = hc.openDataOutputStream();
			out.write(message.getBytes());
			DataInputStream in = hc.openDataInputStream();
			int length = (int) hc.getLength();
			byte[] data = new byte[length];
			in.read(data);
			String response = new String(data);
			
			//ugly workaround, regex or json parser needed
			response = response.substring(
					response.indexOf("amount\":")+8,
					response.indexOf("}"));
			System.out.println(response);
			
			balance = Double.parseDouble(response.trim());
			
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
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
		return "id=" + id + ", recordId="+recordId+",code="+code;
	}
}
