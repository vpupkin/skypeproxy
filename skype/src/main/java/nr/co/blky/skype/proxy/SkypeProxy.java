package nr.co.blky.skype.proxy;
 
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skype.Application;
import com.skype.Friend;
import com.skype.Skype;
import com.skype.SkypeException;
import com.skype.Stream;

/** 
 * skypeproxy(1)                    communication                   skypeproxy(1)



NAME
       skypeproxy - forwards tcp connections over skype

SYNTAX
       skypeproxy <ID> send <contact> <localport> [<remotehost>] [<remoteport>]

       skypeproxy <ID> listen <contact>  <host> <port>


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

       skypeproxy myID listen '*'

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



Jonathan Verner                      0.1.0                       skypeproxy(1) 
 */
public class SkypeProxy {
	
	private static final String LISTEN = "listen";
	private static final Object SEND = "send";
	static Log log = LogFactory.getLog(SkypeProxy.class);

	public static void main(String [] args) throws Exception{
		if (!(args.length == 4 || args.length == 6)){
			usage();
			return;
		}
        Skype.setDebug(true);
        Skype.setDeamon(false);
		// par#0
        String ID = args[0];
        Application application = Skype.addApplication(ID);
        
        //par#1
        String COMMAND = args[1];
        /*
         *   skypeproxy <ID> listen <contact>
         */
        if (LISTEN.equals(COMMAND)){
        	String  CONTACT = args[2];
        	String  HOST = args[3];
        	String  PORT = args[4];

        	application.addApplicationListener(new TunnelServer(CONTACT, HOST, PORT));
        }else
        /*
         *   skypeproxy <ID> send <contact> <localport> <remotehost> <remoteport>
         */
        if(SEND.equals(COMMAND)){
        	String  CONTACT = args[2];
        	int  LOCALPORT = Integer.parseInt( args[3] ); 
        	String  HOST = args[4];
        	String  PORT = args[5];
        	
            ServerSocket ss = new ServerSocket (LOCALPORT);
            while (true) {
            	log.info("listening port "+LOCALPORT+"...");
              // accept the connection from my client
              Socket sc = ss.accept ();
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
              
              application.setData(TunnelServer.HOST, HOST);
              application.setData(TunnelServer.PORT, PORT);
              application.setData(TunnelServer.LISTENHOST, "LOCALHOST");
              application.setData(TunnelServer.LISTENPORT, ""+LOCALPORT);
              log.info("forwarding to ["+CONTACT+"]..");
              Stream skypeStream = application.connect(toFriend )[0]; 
              skypeStream.setData(TunnelServer.HOST, HOST);
              skypeStream.setData(TunnelServer.PORT, PORT);
              skypeStream.setData(TunnelServer.LISTENHOST, "LOCALHOST");
              skypeStream.setData(TunnelServer.LISTENPORT, ""+LOCALPORT);
              
              SkypeRelay tc = new SkypeRelay(skypeStream,sc.getOutputStream(),sc.getInputStream(),System.out);
              skypeStream.addStreamListener(tc);
  			  String stringFromTo = "*:"+LOCALPORT+"->"+CONTACT+"@"+HOST+":"+PORT;
			  new Thread(tc,stringFromTo ).start();	
            }
        	
        	
        }
        
	}

	private static void usage() {
		String text = "SYNTAX\n" +
				"       skypeproxy <ID> send <contact> <localport> <remoteport>\n" +
				"       skypeproxy <ID> listen <contact>\n";
		System.out.println(text );
	}

}


 