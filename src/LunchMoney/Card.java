package LunchMoney;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

public class Card  {
	
	public String code;
	public String cardNumber;
	public double balance;
	//public DateTime date; //TODO add ;)
	
	public Card() {
		code = "null";
		cardNumber = "null";
		balance = 0;
	}
	
	public Card(byte[] record) {
		String recordStr = new String(record);
		int codeIdx = 0;
		int cardNoIdx = recordStr.indexOf(";") + 1;
		int balanceIdx = recordStr.indexOf(";", cardNoIdx) + 1;
		
		code = recordStr.substring(codeIdx, cardNoIdx-2);
		cardNumber = recordStr.substring(cardNoIdx, balanceIdx - 2);
		try {
			balance = Double.parseDouble(
					recordStr.substring(balanceIdx).trim());
		} catch(NumberFormatException e) {
			e.printStackTrace();
		}
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
	
	public String toString() {
		return code //+ " " + cardNumber 
				+ "    " + balance + "zl";
	}

	public byte[] toByte() {
		String str = code + ";"  + cardNumber + ";" + balance;
		return str.getBytes();
	}
}
