package nr.co.blky.skype.proxy;

public interface String2ByteConverter {

	/**
	 * 
	 * @param data
	 * @param off
	 * @param len
	 * @return
	 */
	String b2s(byte data[], int off, int len);

	/**
	 * 
	 * @param data
	 * @return
	 */
	byte[] s2b(String data);

}
