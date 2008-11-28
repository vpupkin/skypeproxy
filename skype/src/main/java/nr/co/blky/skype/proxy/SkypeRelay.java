package nr.co.blky.skype.proxy;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream; 
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Properties;

import org.apache.axis.utils.ByteArray;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory; 
import com.skype.SkypeException;
import com.skype.Stream;
import com.skype.StreamListener;
  

 
public class SkypeRelay implements StreamListener, Runnable {

	static Log log = LogFactory.getLog(SkypeRelay.class);
	private Stream skypeStream;
	private OutputStream outputStream;
	private InputStream inputStream;
	private PrintStream out;
	private boolean inited = false;

	public SkypeRelay(Stream stream, OutputStream outputStream,
			InputStream inputStream, PrintStream out) {
		this.skypeStream = stream;
		this.outputStream = outputStream;
		this.inputStream = inputStream;
		this.out = out;
		inited = true;
	}
	private final void initSocket(String tunnelhost, int tunnelport) throws UnknownHostException, IOException{
		System.out.println("creating:"+tunnelhost+":"+tunnelport+" ...");
		Socket st = new Socket (tunnelhost, tunnelport);
		log.fatal("RELAY to "+tunnelhost+":"+tunnelport+" STARTED.");
		this.outputStream = st.getOutputStream();
		this.inputStream = st.getInputStream();
		String stringFromTo = "SkypeTunnel: tunnelling  :>"+ tunnelport + ":" + tunnelhost;
		log.debug("INITED:"+stringFromTo);
		
	}

	public SkypeRelay(Stream stream, PrintStream out) {
		log.debug("RELAY STARTED..."+stream);
		this.skypeStream = stream;
		this.out = out;
	}

	public void datagramReceived(String arg0) throws SkypeException {
		textReceived(arg0);
	}
	private int MAX_STACK_SIZE = 100;
	String udpBufs []= new String[MAX_STACK_SIZE];
	short udpIndex = 0;
	short inCount = 0;
	short outCount = 0;
	
	public void textReceived(String arg0) throws SkypeException {
		inCount ++;
		log.debug("text#"+inCount+" RCV:"+arg0+";");
		if (inited){
			try { 
				byte[] s2bTmp = s2b(arg0);
	 			
	 			udpBufs[inCount%MAX_STACK_SIZE] = arg0;
				this.outputStream.write(s2bTmp,0,s2bTmp.length);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}else{
			Properties pTmp = new Properties();
			try{
				pTmp .load(new ByteArrayInputStream(arg0.getBytes()));
				String sHost = pTmp.getProperty(TunnelServer.HOST);
				int iPort = Integer.parseInt(pTmp.getProperty(TunnelServer.PORT));
				log.debug("PROPS for relay:"+pTmp);
				initSocket(sHost, iPort);
				inited = true;
				
			}catch(IOException e){
				skypeStream.send(e.getMessage());
				log.fatal(e.getMessage(),e);
			}
		}
	}
	
	
	private static final String b2s(byte data[], int off, int len){
		//return new String(buf,start, lenght);
		return org.apache.axis.encoding.Base64.encode(data, off, len);
	}
	private static final byte[] s2b(String data){
		//return str.getBytes();
		return org.apache.axis.encoding.Base64.decode(data);
	}	
	
	  final static int BUFSIZ = 1000; 
	  byte buf[] = new byte[BUFSIZ];

	  public void run () {
		    int n;
		    // wait till socket...
		    while(!inited){
		    	try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		    try {
		      while ((n = this.inputStream.read (buf)) > 0) {
		        skypeStream.write (b2s(buf, 0, n)); 
		        if (out != null)
		        	out.write(buf, 0, n);
		      }
		    } catch (IOException e) {
		    	log.error(e);
		    } catch (SkypeException e) {
		    	log.error(e);
			} finally {
		      try {
		    	  inputStream.close ();
//		    	  stream.close ();
		      } catch (IOException e) {
		      }
		    }
		  }

}


 