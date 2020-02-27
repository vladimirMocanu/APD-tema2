.PHONY: all

all: build

build: CommunicationChannel.java HeadQuarter.java Message.java SpaceExplorer.java Homework.java 

	 javac CommunicationChannel.java HeadQuarter.java Message.java SpaceExplorer.java Homework.java 

# solves a test-case, given its prefix
# if 3rd argument given, prints the solution in the given file suffix (should be identical to the 1st param)
run: 
	time java Homework test01 100000 4 4
	
clean:
	rm *.class
