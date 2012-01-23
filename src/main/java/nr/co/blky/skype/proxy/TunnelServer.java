package nr.co.blky.skype.proxy; 
import java.io.PrintStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory; 
import com.skype.ApplicationListener;
import com.skype.SkypeException;
import com.skype.Stream;

/**
 * server tunnel the Skype-messages into external socket-connection.
 * 
 * @author vipup
 *
 */
public class TunnelServer implements ApplicationListener {

	static final String HOST = "host";
	static final String LISTENHOST = "listenhost";
	static final String PORT = "port";
	static final String LISTENPORT	= "listenport";
	public static final String EOL = "\n";
	public static final String ID = "SiD";
	
	static Log log = LogFactory.getLog(TunnelServer.class);
  	private static int id = 0;
	PrintStream out = System.out;
  	SkypeRelay  sr =null; 
	public TunnelServer(PrintStream out) {
		log.debug("create TunnelServer");
		this.out = out;
 	} 
	/**
	 * accept all connections
	 */
	public void connected(Stream stream) throws SkypeException{ 
	      // connect to the thing I'm tunnelling for 
			log.debug("connected: A::"+stream.getApplication()+" SiD::"+stream.getId()+"{"+stream+"}");
			sr = new SkypeRelay ( 	stream,  	this.out  	); 
			stream.addStreamListener(sr);
			new Thread(sr,"@skypeproxy#"+id ++).start(); 
	}

	public void disconnected(Stream arg0) throws SkypeException {
		sr.destroy();
	}

}


 