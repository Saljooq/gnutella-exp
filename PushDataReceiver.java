import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PushDataReceiver implements Runnable {

    String filename;
    Node fromNode;
    int BUFFER_SIZE = 65536;

    public PushDataReceiver(
            String filename,
            Node fromNode) {
        this.filename = filename;
        this.fromNode = fromNode;
    }

    @Override
    public void run() {

        // create text reader and writer
        DataInputStream inStream = null;
        OutputStream os = null;
        ServerSocket servertSocket = null;
        Socket clientSocket = null;

        try {

            servertSocket = new ServerSocket(fromNode.port);

            clientSocket = servertSocket.accept();

            inStream = new DataInputStream(clientSocket.getInputStream());

            (new fetchFolderInfo(fromNode.name)).getFiles(); // this should create the folder necessary

            File newFile = new File(fromNode.name, filename);

            os = new FileOutputStream(newFile);

            ByteBuffer sizeBuff = ByteBuffer.allocate(Long.BYTES);
            byte[] sizeByte = new byte[Long.BYTES];
            inStream.read(sizeByte);
            sizeBuff.put(sizeByte);
            sizeBuff.flip();
            long fileSize = sizeBuff.getLong();
            // System.out.println("File Size incoming : " + fileSize);

            byte[] buf = new byte[BUFFER_SIZE];
            int inputSize = 0;
            Long start = (new Date()).getTime();
            float percentDone = 0;
            long fileSizeinKB = fileSize / 1000;

            try {

                while (true) {
                    inputSize = inStream.read(buf);
                    if (inputSize == -1) {
                        System.out.print("PROGRESS :  100% of " + fileSizeinKB + " KB done         \r");
                        break;
                    }

                    os.write(buf, 0, inputSize);
                    percentDone = percentDone + ((float) (inputSize * 100) / (float) fileSize);
                    System.out.print("PROGRESS : " + percentDone + "% of " + fileSizeinKB + " KB downloaded       \r");
                }
            } catch (Exception e) {
            } // should end when file has ended
            System.out.println();

            Long total = (new Date()).getTime() - start;
            float time = total / 1000;

            System.out.println("Successfully received " + filename + " in " + time + " seconds");

        } catch (IOException e) {
            System.out.println("Something went wrong in push data receiver");
        }

        try

        {

            os.close();
            clientSocket.close();
            servertSocket.close();
            inStream.close();
        } catch (Exception e) {
        }
    }

}
