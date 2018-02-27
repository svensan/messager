package messager;

public class FileRequest {
    /*
    unironcially vad är de här
    */

    private int fileSize;
    private String fileName;

    public FileRequest(String fileName, int fileSize) {
        this.fileName = fileName;
        this.fileSize = fileSize;
    }

    public int getFileSize() {
        return fileSize;
    }

    public String getFileName() {
        return fileName;
    }

}
