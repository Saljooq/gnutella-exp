make:
	@javac Main.java fetchFolderInfo.java udpTest.java
	@java Main

test:
	@java udpTest

clean:
	@rm *.class
	# cleaning up all th extra files