package nr.co.blky.skype.proxy; 
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory; 
import com.skype.ApplicationListener;
import com.skype.SkypeException;
import com.skype.Stream;

public class TunnelServer implements ApplicationListener {

	static final String HOST = "host";
	static final String LISTENHOST = "listenhost";
	static final String PORT = "port";
	static final String LISTENPORT	= "listenport";
	public static final String EOL = "\n";
	
	static Log log = LogFactory.getLog(TunnelServer.class);
	private String contact;
	String tunnelhost = "127.0.0.1"; 
	int tunnelport = Integer.parseInt( "22");
	private static int id = 0;
 	
	
	public TunnelServer(String contact, String host2, String port2) {
		this.contact = contact;
		this.tunnelhost = host2;
		this.tunnelport = Integer.parseInt(port2);
	}

	/**
	 * accept all connections 
	 * TODO use contact
	 */
	public void connected(Stream stream) throws SkypeException{ 
	      // connect to the thing I'm tunnelling for 
			log.debug("connected:"+stream.getApplication()+stream);
			SkypeRelay sr = new SkypeRelay ( 	stream,  	System.out  	); 
			stream.addStreamListener(sr);
			new Thread(sr,contact+"@skypeproxy#"+id ++).start(); 
	    }

	public void disconnected(Stream arg0) throws SkypeException {
		// TODO
	}

}


 