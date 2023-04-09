JAVAC=javac

default: run

run: all

clean: 
		rm *.class

.SUFFIXES: $(SUFFIXES) .class .java

FILE= Server.java Client.java ConnectionException.java CalculatorThread.java Logger.java

all: $(FILE:java=class)

.java.class:
		$(JAVAC) $*.java
