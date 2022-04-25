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
import java.util.ArrayList;
import java.util.List;

public class PushDataReceiver implements Runnable{

    String filename;
    Node fromNode;

    public PushDataReceiver(
    String filename,
    Node fromNode)
    {
        this.filename = filename;
        this.fromNode = fromNode;
    }

    @Override
    public void run() {
        
        try {

            ServerSocket servertSocket = new ServerSocket(fromNode.port); 

            // create text reader and writer
            DataInputStream inStream;

            Socket clientSocket = servertSocket.accept();

            inStream = new DataInputStream(clientSocket.getInputStream());

            (new fetchFolderInfo(fromNode.name)).getFiles(); // this should create the folder necessary


            File newFile = new File(fromNode.name , filename);

            OutputStream os = new FileOutputStream(newFile);


            byte buf = 0;
            try{
                while (true)
                {
                    buf = inStream.readByte();
                    os.write(buf);
                }
            }catch (EOFException e){} // should end when file has ended

            os.close();

            // inStream;
            // byte[] buf = new byte[2];

            clientSocket.close();
            servertSocket.close();

            System.out.println("Successfully received " + filename);

    } catch (IOException e) {
        System.out.println("Something went wrong in push data sender");;
    }

    }

}
