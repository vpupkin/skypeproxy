export DISPLAY=127.0.0.1:0
cp lib/linux/libJSA.so.2 /tmp/libJSA.so
#TODo - fix parms list : #1,3,4,5 not used!
java -jar  target/skypeproxy-0.0.5.jar GESHA listen A localhost 22  >/dev/null
