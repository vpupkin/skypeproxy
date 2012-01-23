package nr.co.blky.skype.proxy;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skype.ChatMessage;
import com.skype.ChatMessageListener;
import com.skype.SkypeException;

/** 
 * <b>Description:TODO</b>
 * @author      vipup<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  23.01.2012::11:23:29<br> 
 */
public class ShellOverChat implements ChatMessageListener {

	Properties commands = new Properties();
	static Log log = LogFactory.getLog(ShellOverChat.class);
	public ShellOverChat (){
		InputStream  cfg = this.getClass().getClassLoader().getResourceAsStream("shellOverChat.properties");
		try {
			commands.load(cfg );
		} catch (Exception e) {
			log.warn("loading error:", e);
		}
	}
	
	public void chatMessageReceived(ChatMessage arg0) throws SkypeException {
		String cmdTmp = arg0.getContent();
		log.info( cmdTmp);
		System.out.println(cmdTmp );
		String cmd2exec = commands.getProperty(cmdTmp, cmdTmp);
		WorkerMgr mgr = new WorkerMgr();
		StringBuffer procOut = new StringBuffer();
		try{
			int x = mgr.executeCommandLine(cmd2exec, true, true, 1000, procOut);
			System.out.println(procOut );
			arg0.getChat().send(procOut .toString());
		}catch(Throwable e){
			System.out.println(procOut );
			log.error("exec error:"+procOut, e);
		}
	}

	public void chatMessageSent(ChatMessage arg0) throws SkypeException {
		String cmdTmp = arg0.getContent();
		System.out.println(cmdTmp );
		log.info( cmdTmp);
	}

}


 