#SkypeAppender for log4j.

# pom.xml #

```
...
    <dependency>
	<groupId>nr.co.blky</groupId>
	<artifactId>skypeproxy</artifactId>
	<version>0.0.8</version>
    </dependency>
...

```


# log4j.properties #

```
log4j.appender.SKYPE=eu.blky.log4j.SkypeAppender
log4j.appender.SKYPE.layout=org.apache.log4j.PatternLayout
log4j.appender.skype.layout.ConversionPattern=[%p] %c - %m
log4j.appender.SKYPE.receiver=wolli-home-gw

log4j.category.eu.blky.log4j.SkypeAppenderTest==DEBUG, SKYPE
```


# Output #

```
[14:00:09] junitskype: test
[14:00:09] junitskype: testI
```