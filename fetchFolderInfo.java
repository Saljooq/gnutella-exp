import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class fetchFolderInfo {

    private String folderName;

    public fetchFolderInfo(String folderName){
        this.folderName = folderName;
    }

    public ArrayList<String> getFileNames(){
        File directory = new File(folderName);

        if (!directory.exists()) {
            directory.mkdir();
        }

        return new ArrayList<String>(Arrays.asList(directory.list()));
    }
}
