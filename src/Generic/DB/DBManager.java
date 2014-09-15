package Generic.DB;

import javax.microedition.rms.InvalidRecordIDException;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.rms.RecordStoreNotOpenException;

public class DBManager {

	protected RecordStore rs = null;
	protected String recordName = null;

	public DBManager(String dbName) {
		this.recordName = dbName;
	}

	public boolean open() {
		boolean result = true;
		try {
			rs = RecordStore.openRecordStore(recordName, true);
						
		} catch (RecordStoreFullException e) {
			//TODO notify main controller
			e.printStackTrace();
			result = false;
		} catch (RecordStoreException e) {
			e.printStackTrace();
			result = false;
		}

		return result;
	}
	
	public void close() {
		try {
			rs.closeRecordStore();
		} catch (RecordStoreException e) {
			e.printStackTrace();
		}
	}

	public boolean addRecord(byte[] recordData) {
		boolean result = true;
		try {
			rs.addRecord(recordData, 0, recordData.length);
		} catch (RecordStoreException e) {
			e.printStackTrace();
			result = false;
		}
		return result;
	}

	public boolean delRecord(int recordId) {
		boolean result = true;
		try {
			rs.deleteRecord(recordId);
		} catch (RecordStoreException e) {
			e.printStackTrace();
			result = false;
		}
		return result;
	}

	public boolean editRecord(int recordId, byte[] recordData) {
		boolean result = true;
		try {
			rs.setRecord(recordId, recordData, 0, recordData.length);
		} catch (RecordStoreException e) {
			e.printStackTrace();
			result = false;
		}
		return result;
	}

	public byte[] getRecord(int recordId) {
		byte[] b = null;
		if(!validId(recordId))
			return b;
	
		try {
			b = rs.getRecord(recordId);
		} catch (InvalidRecordIDException e) {
			e.printStackTrace();
		} catch (RecordStoreException e) {
			e.printStackTrace();
		}
		
		return b;
	}

	public DBIterator iterate() {
		DBIterator iter = null;
		try {
			iter = new DBIterator(rs.enumerateRecords(null, null, false));
		} catch (RecordStoreNotOpenException e) {
			e.printStackTrace();
		}
		
		return iter;
	}
	
	protected boolean validId(int recordId) {
		return recordId > 0;
	}
}
