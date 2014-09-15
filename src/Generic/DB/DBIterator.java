package Generic.DB;

import javax.microedition.rms.InvalidRecordIDException;
import javax.microedition.rms.RecordEnumeration;

public class DBIterator {

	private RecordEnumeration iter = null;
	
	public DBIterator(RecordEnumeration enumerateRecords) {
		iter = enumerateRecords;
	}

	public boolean hasNextElement() {
		return iter.hasNextElement();
	}

	public int nextRecordId() throws InvalidRecordIDException {
		return iter.nextRecordId();
	}

	public int numRecords() {
		return iter.numRecords();
	}
}
