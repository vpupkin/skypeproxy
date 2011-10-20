package nr.co.blky.skype.proxy;

import java.util.zip.Deflater;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Base64Converter implements String2ByteConverter {

	static Log log = LogFactory.getLog(Base64Converter.class);
	public final String b2s(byte data[], int off, int len){
		//return new String(buf,start, lenght);
		return org.apache.axis.encoding.Base64.encode(data, off, len);
	}
	static Deflater compressor = new Deflater();
	static int diffTotal = 0;
	static int sizeTotal = 0;
	static{
	    compressor.setLevel(Deflater.BEST_COMPRESSION);
	}
	public final byte[] s2b(String data){
		//return str.getBytes();
	    // Create the compressor with highest level of compression
	    
		byte[] retval = org.apache.axis.encoding.Base64.decode(data);
	    byte[] compressedBuff  = org.apache.axis.encoding.Base64.decode(data);
	    int count = compressor.deflate( data.getBytes() );
	    compressedBuff = org.apache.axis.encoding.Base64.decode(new String(compressedBuff));
	    int diff = retval.length - compressedBuff.length;
	    log.trace("diff:"+ (diff) +"   ((("+diffTotal + "/"+sizeTotal +"))     count = "+ count );
		sizeTotal += retval.length; 
	    diffTotal +=diff;
	    
		return retval;
	}	
}
