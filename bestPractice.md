Assumes that
  * hostPC and guestPC has preinstalled Skype-software
  * and preconfigured default-users with auto-login.
  * hostPC has started http-proxy at 192.168.2.101:9666
  * guestPC has preconfigured browser with proxy at 127.0.0.1:3333



# Step0@hostPC #
```
"C:\Program Files\Skype\Phone\Skype.exe /minimized"
```

# Step0@guestPC #
```
"C:\Program Files\Skype\Phone\Skype.exe /minimized"
```


# Step1@hostPC #
this will start the "server"
```
C:\Documents and Settings\Admin\Рабочий стол>java -jar skypeproxy-0.0.8-SNAPSHOT-jar-with-dependencies.jar listen >111.log
```

# Step2@guestPC #
this will initiate the "conection-2-proxyserver-at-host...
```
D:\gby>java -jar skypeproxy-0.0.8-SNAPSHOT-jar-with-dependencies.jar send geshaskype 3333 192.168.2.101  9666
connected as listener for app [GESHA]
```


# Step3@guestPC #
start browser, and goto http://somebody.net/info.html



# Details #
```
19.10.2011 23:46:56 nr.co.blky.skype.proxy.SkypeProxy main
INFO: listening port 3333...
19.10.2011 23:48:18 nr.co.blky.skype.proxy.SkypeProxy main
INFO: accepted conenction ...
19.10.2011 23:48:18 nr.co.blky.skype.proxy.SkypeProxy main
INFO: check for geshaskype
```



---


the browser have to display the host-Data:
```
 :)))
You are coming from: 	923.129.493.561
 (((:
```



# Links #
  * [command-line](https://support.skype.com/en/faq/FA171/Can-I-run-Skype-for-Windows-from-the-command-line)
  * [Skype\_commands](http://forum.skype.com/index.php?showtopic=137441)