# Introduction #

till auto-install not finished the next "steps" is reasonable to use:

# Prerequisite #

enviroment for building:
  * ['svn client'](http://subversion.tigris.org/getting.html#binary-packages)
  * jvm >=1.5 ([Sun](http://java.sun.com/javase/downloads/index.jsp) preferable)
  * ['maven 2'](http://maven.apache.org/download.html) ( tested with  >= 2.0.9  )

# Build #

  * svn co http://skypeproxy.googlecode.com/svn/trunk/ skypeproxy
  * cd skypeproxy
  * mvn package
  * mvn jar:jar

# Running #

  * for server: srvXXX.sh | srvXXX.cmd  (win32)
  * for client: clXXX.sh  | clXXX.cmd  (win32)