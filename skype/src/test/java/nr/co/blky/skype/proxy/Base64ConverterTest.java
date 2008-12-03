package nr.co.blky.skype.proxy;

import java.util.Arrays;

import junit.framework.TestCase;

public class Base64ConverterTest extends TestCase {

	public void testB2S() {
		Base64Converter b64Tmp = new Base64Converter ();
		String data = "test";
		byte[] bTmp = b64Tmp .s2b(data );
		String expected = data;
		String actual = b64Tmp.b2s(bTmp, 0, bTmp.length);
		assertEquals(expected+"!="+actual,expected, actual);
	}
	public void testS2B() {
		Base64Converter b64Tmp = new Base64Converter ();
		byte[] data = "azzzssAzzzzsaaaA=!sd".getBytes();
		String dataS = b64Tmp .b2s(data ,0,data.length);
		byte[]  expected = data;
		byte[] actual = b64Tmp.s2b(dataS);
		assertEquals(new String(expected)+"!="+new String(actual),new String(expected), new String(actual));
	}

	public void testS2B_1000() {
		Base64Converter b64Tmp = new Base64Converter ();
		
		for(int i=0;i<1024;i++){
			byte[] data = createTestData(i);
			String dataS = b64Tmp .b2s(data ,0,data.length);
			byte[]  expected = data;
			byte[] actual = b64Tmp.s2b(dataS);
			System.out.println(new String(expected));
			assertEquals(new String(expected)+"!="+new String(actual),new String(expected), new String(actual));
		}
	}
	private byte[] createTestData(int len) {
		char[] dataC  = new char[len]; 
		for(int i = 0;i<dataC.length;i++)dataC[i]= (char)((Math.random()*255));
		byte[] data = ("%"+len+":"+new String(dataC)).getBytes();
		return data;
	}
}
