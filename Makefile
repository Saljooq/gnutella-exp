make:
	@javac Node.java fetchFolderInfo.java udpTest.java
	# generating all the necessary class files - To run enter 'java Node address=localhost:8000,name=Musk'

test:
	@java udpTest

clean:
	@rm *.class
	# cleaning up all th extra files