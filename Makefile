make:
	@javac Node.java fetchFolderInfo.java udpTest.java

test:
	@java udpTest

clean:
	@rm *.class
	# cleaning up all th extra files