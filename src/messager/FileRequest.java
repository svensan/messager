package messager;

public class FileRequest {

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
