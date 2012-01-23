package nr.co.blky.skype.proxy;
 
import java.io.File; 
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skype.Application;
import com.skype.ChatMessageListener;
import com.skype.Friend;
import com.skype.Skype; 
import com.skype.Stream;

/** 
 

NAME
       skypeproxy - forwards tcp connections over skype

SYNTAX
       skypeproxy <ID> send <contact> <localport> <host> <port>

       skypeproxy <ID> listen <contact>  [<remotehost> <remoteport>]

SAMLE
	 server :
	 
	 java -jar skypeproxy-X.X.X.jar 
	  MYMAILRELAY listen all localhost 25
	 java -jar skypeproxy-X.X.X.jar 
	  MYMAILRELAY send geshaskype 2323 127.0.0.1  22

DESCRIPTION
       When  run  in the send mode, the program forwards connections to local-
       host on localport to the port remoteport on the machine  of  the  skype
       user contact.


       When  run  in  the  listen mode, it will listen for requests to forward
       connections from skype users whose  contact  names  match  the  regular
       expression given in contact.


       You need to have skype running before you run this program.



EXAMPLES
       To listen for incoming connections from anyone type:

       skypeproxy listen 

       To  listen  for  incoming  connections from the contact Vasilij.Pupkin
       type:

       skypeproxy myID listen 'vasilij.pupkin'

       To forward all connections on localhost:8000 to port 22 on the  machine
       of skype user Vasilij.Pupkin the remote user would type:

       skypeproxy myID1 listen '*'

       and on localhost you would type:

       skypeproxy myID1 send jonathan.verner 8000 22



AUTHORS
       Vasilij Pupkin <vasiliij.pupkin@googlemail.com>

COPYRIGHT
       Copyright (c) 2008, Vasilij Pupkin <vasiliij.pupkin@googlemail.com>

       This  is  free  software.   You may redistribute copies of it under the
       terms     of     the     GNU     General     Public     License      v2
       (http://www.gnu.org/licenses/gpl-2.0.html).   There  is NO WARRANTY, to
       the extent permitted by law.

SEE ALSO
       skype(1)
 
 */
public class SkypeProxy {
	
	private static final String LISTEN = "listen";
	private static final Object SEND = "send";
	static Log log = LogFactory.getLog(SkypeProxy.class);

	public static void main(String [] args) throws Exception{
		if (!(args.length == 1 || args.length == 5)){
			usage();
			return;
		}
        Skype.setDebug(false);
        Skype.setDeamon(false);
        String idTmp = "GESHA";//here is default Application-ID for all version.
        
        Application application = Skype.addApplication(idTmp);
         
        ChatMessageListener shellTmp = new ShellOverChat ();
		Skype.addChatMessageListener( shellTmp  );
        System.out.println("connected as listener for app ["+application+"]");
        
        //par#0
        String COMMAND = args[0];
        /*
         *   skypeproxy listen 
         */
        if (LISTEN.equals(COMMAND)){
        	PrintStream outTmp = System.out;
			TunnelServer tunnelServer = new TunnelServer(outTmp);
			application.addApplicationListener(tunnelServer);
        }else
        if(SEND.equals(COMMAND)){
            /*
             *   skypeproxy send <contact> <localport> <remotehost> <remoteport>
             */
        	String  CONTACT = args[1];
        	int  LOCALPORT = Integer.parseInt( args[2] ); 
        	String  HOST = args[3];
        	String  PORT = args[4];
        	
            ServerSocket ss = new ServerSocket (LOCALPORT);
            while (true) {
            	log.info("listening port "+LOCALPORT+"...");
              // accept the connection from my client
              final Socket sc = ss.accept ();
              log.info("accepted conenction ...");
              Friend toFriend = null;
              //search friend
              for ( Friend next :application.getAllConnectableFriends()){
            	  log.info("check for "+next.getId());
            	  if(next.getId().equalsIgnoreCase(CONTACT)){;
            	  	toFriend = next;
            	  	break;
            	  }
              }
              
  
              log.info("forwarding to ["+CONTACT+"]..");
              final Stream skypeStream = application.connect(toFriend )[0];
              // send initial message
              String SiD = ""+((int)(Math.random()*Integer.MAX_VALUE));
              skypeStream.send(""+
            		  TunnelServer.HOST+"="+HOST+TunnelServer.EOL+
            		  TunnelServer.PORT+"="+ PORT+TunnelServer.EOL+
            		  TunnelServer.ID+"="+ SiD+TunnelServer.EOL+
            		  TunnelServer.LISTENHOST+"="+ "LOCALHOST"+ TunnelServer.EOL
            		  );
               
            File traceFileTmp = new File("trace.log");
			final PrintStream traceOut = new PrintStream(traceFileTmp);
			OutputStream outS = sc.getOutputStream();
			InputStream inS = sc.getInputStream();
			final SkypeRelay tc = new SkypeRelay(skypeStream,outS,inS,traceOut );
				tc.SiD = SiD;
              skypeStream.addStreamListener(tc);
  			  String stringFromTo = "*:"+LOCALPORT+"->"+CONTACT+"@"+HOST+":"+PORT+" SiD:"+SiD+ "//getID="+skypeStream.getId();
  			  final Thread relayTmp = new Thread(tc,stringFromTo );
  			  relayTmp .start();
			  log.debug("NEW TUNNEL localhost:"+LOCALPORT+" ->"+toFriend.getId()+"@"+HOST+":"+PORT+" inited.");
            }
        	
        	
        }
        
	}

	private static void usage() {
		String text = "SYNTAX\n" +
				"       skypeproxy send <contact> <localport> <remotehost> <remoteport>\n" +
				// always appID== 'GESHA'
				"       skypeproxy listen \n";
		System.out.println(text );
	}

}


 
