make:
	@javac Node.java fetchFolderInfo.java udpTest.java
	@java Node

test:
	@java udpTest

clean:
	@rm *.class
	# cleaning up all th extra files