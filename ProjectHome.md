[!!! Skype@Microsoft says it will kill its Desktop API by end of 2013. Long Life SkypeAPI :'( !!!](http://gigaom.com/2013/07/13/skype-says-it-will-kill-desktop-api-by-end-of-2013/)


Most network administrators at work, university or school deny access to file sharing, instant messaging or social networks such as facebook or myspace with a firewall or proxy server. If you are constantly getting a message saying "Can't connect" or something similar, the service you are trying to connect to has probably been blocked by your network administrator.

Skype tunnel works in very similar way as well-known ["TCP Tunnel/Monitor"](http://articles.techrepublic.com.com/5100-10878_11-1049605.html)...
with only one diff:
the transport for it - Skype Network.

For ex: how works ssh-client-tunneling from office to home linux:
```
Office PC with Skype     Office  Network            Home Skype (with sshd on 127.0.0.1:22)

   +-------+    Connect to                          +-------+    Home PC
   |Putty  |--+ 127.0.0.1:2222                      |sshd   |<-+ 
   |-------|  |                +-----+              |-------|  |(Port 22)  
   |skype- |<-+ Port 2222      |Skype|              |skype- |  |
   |proxy  |------------------>|Net  |------------->|proxy  |--+
   |(send) |                   |     |              |(listen| 
   +-------+ SkypeID_A         +-----+    SkypeID_B +-------+

#B#java -jar <jarname> listen  
  - listen all skipe-friends
#A#java -jar <jarname> send wolli-home-gw 2222 127.0.0.1 22
  - initiate the tunnel to wolli's local sshd with local port 2222. for proxing use 'ssh -D 9999 localhost 2222' ;)
```


prerequisites:

a) Preinstalled Skipe vv. 2,3,4,5 on Linux, Windows, MacOS.

b) two skypeId ( 1st is server, 2nd is client )

Just run :

1. on server:
```
   #java -jar 0.0.X-yyy-zz.jar listen  
```

2. on client:
```
    #java -jar 0.0.X-yyy-zz.jar send <to_skypeId_contact> <local_tcp_port> <remote_host> <remote_port>
```

and Voila....

Proxy, Socks,ICQ, SSH, Windows Remote Desktop, VNC, Radmin, X-server and much much more aplications will be accessible again from any place where you use Skype ;)


&lt;wiki:gadget url="http://www.ohloh.net/p/87300/widgets/project\_basic\_stats.xml" height="220" border="1"/&gt;