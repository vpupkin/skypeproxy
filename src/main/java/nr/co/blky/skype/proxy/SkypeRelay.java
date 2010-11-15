package nr.co.blky.skype.proxy;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Properties;
import java.util.Vector; 
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

	/**
	 * start pure relay
	 * 
	 * @param stream
	 * @param outputStream
	 * @param inputStream
	 * @param out
	 */
	public SkypeRelay(Stream stream, OutputStream outputStream,
			InputStream inputStream, PrintStream out) {
		this.skypeStream = stream;
		this.outputStream = outputStream;
		this.inputStream = inputStream;
		this.out = out;
		inited = true;
	}

	private final synchronized void initSocket(String tunnelhost, int tunnelport)
			throws UnknownHostException, IOException {
		log.debug("["+this.relayId+"] creating:" + tunnelhost + ":" + tunnelport + " ...");
		Socket st = new Socket(tunnelhost, tunnelport);
		log.debug("["+this.relayId+"] RELAY to " + tunnelhost + ":" + tunnelport + " STARTED.");
		this.outputStream = st.getOutputStream();
		this.inputStream = st.getInputStream();
		String stringFromTo = "SkypeTunnel: tunnelling  :>" + tunnelport + ":"
				+ tunnelhost;

		log.debug("["+this.relayId+"]INITED:" + stringFromTo);

	}

	/**
	 * start delayed relay (Server): <listen>-mode 
	 * 
	 * @param stream
	 * @param out
	 */
	public SkypeRelay(Stream stream, PrintStream out) {
		log.debug("["+this.relayId+"]RELAY STARTED..." + stream);
		this.skypeStream = stream;
		this.out = out;
	}

	public void datagramReceived(String arg0) throws SkypeException {
		textReceived(arg0);
	}

	private int MAX_STACK_SIZE = 100;
	String udpBufs[] = new String[MAX_STACK_SIZE];
	short udpIndex = 0;
	short inCount = 0;
	short outCount = 0;
	Vector<String> text = new Vector<String>();

	public void textReceived(String arg0) throws SkypeException {
		if (!text.contains(""+arg0))
			synchronized (text) {
				if (!text.contains(""+arg0)) {
					inCount++;
					text.add(""+arg0);
					if (log.isDebugEnabled())
						log.debug("SiD::"+SiD+"---text#" + inCount + " RCV:" + arg0 + ";");

					if (inited && arg0.indexOf(SiD + "#") == 0) { // do main job
						try {
							byte[] s2bTmp = s2b(arg0);
							udpBufs[inCount % MAX_STACK_SIZE] = arg0;
							this.outputStream.write(s2bTmp, 0, s2bTmp.length);

						} catch (IOException e) {
							log.debug("" + this.SiD + this, e);
						} catch (Exception e) {
							log.debug("" + this.SiD + this, e);
						}
					} else if (!inited) { // init myself
						performInit(arg0);
					} else if (arg0.indexOf("#") == -1) { // do not my job..
						try {
							fork(arg0);
						} catch (UnknownHostException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return; // ignore
					} else {
						 log.debug( "SiD:=="+SiD+"????????<<<["+arg0+"]>>>????????????????");
					}
				}
			}
	}

	static final Map<String, SkypeRelay> relaysRepo = new java.util.Hashtable<String, SkypeRelay>();

	private synchronized void performInit(String arg0) throws SkypeException {
		if (!this.inited) {
			synchronized (arg0) {
				if (!this.inited) {
					Properties pTmp = new Properties();
					try {
						pTmp.load(new ByteArrayInputStream(arg0.getBytes()));
						String sHost = pTmp.getProperty(TunnelServer.HOST);
						int iPort = Integer.parseInt(pTmp
								.getProperty(TunnelServer.PORT));
						SiD = pTmp.getProperty(TunnelServer.ID);
						log.debug("PROPS for relay:" + pTmp + "SiD::" + SiD);
						initSocket(sHost, iPort);
						inited = true;

					} catch (IOException e) {
						skypeStream.send(e.getMessage());
						log.debug("#" + this.SiD + "#"+ this, e);
					} catch (Throwable e) {
						log.debug("##" + this.SiD +"##"+ this, e);
					}
				}else{
					log.debug("!!!DONE BEFORE!!!");
				}
			}
		}
	}

	
	
	private void fork(String arg0) throws UnknownHostException, IOException,
			SkypeException {

		SkypeRelay sr = relaysRepo.get(arg0);
		if (sr == null)
			synchronized (arg0) {
				if (sr == null) {
					
					sr = new SkypeRelay(this.skypeStream, this.out );
					// sr.performInit(arg0);
					sr.textReceived(arg0);
					skypeStream.addStreamListener(sr);
					relaysRepo.put(arg0, sr);
					new Thread(sr, "@skypeproxy#" + sr.SiD).start();
					log.debug("Keep cool! B-D ");
				} else {
					log.debug("hehe ;)" +sr);
				}
			}
		else {
			log.debug(">8-0"+sr);
		}

	}
 

	public final static int BUFSIZ = 1024;
	byte buf[] = new byte[BUFSIZ];

	public void run() {
		int n;
		// wait till socket...
		while (!inited) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				log.error(this, e);
			}
		}
		// do job..
		try {
			while (isAlive && ((n = this.inputStream.read(buf)) > 0)) {
				skypeStream.write(b2s(buf, 0, n));
				if (out != null)
					out.write(buf, 0, n);
			}
		} catch (IOException e) {
			log.error(this, e);
		} catch (SkypeException e) {
			log.error(this, e);
		} finally {
			try {
				inputStream.close();
				// stream.close ();
			} catch (IOException e) {
				log.error(this, e);
			}
		}
	}

	private static int relayCounter = 0;
	private int relayId = relayCounter++;
	String SiD = null;
	// use Base64 as default encoding
	String2ByteConverter myConverter = new Base64Converter();
	private boolean isAlive = true;

	private String b2s(byte[] buf2, int i, int n) {
		String retval = myConverter.b2s(buf2, i, n);
		return SiD + "#" + retval;
	}

	private byte[] s2b(String arg0) {
		byte[] retval = myConverter.s2b(arg0);

		try {
			
			String data = arg0.substring(arg0.indexOf("#") + 1);
			retval = myConverter.s2b(data);
		} catch (Throwable e) {
		}
		return retval;
	}

	public void destroy() {
		this.isAlive  = false;
		
	}
}
