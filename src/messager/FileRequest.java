package messager;

public class FileRequest {
    /*
    Denna klass är en liten klass vars jobb är att spara information som är relevant då en vill skicka/ta emot ett
    filerequest. Observera att denna klass egentligen bara har två fält som kan hämtas. Den största anledningen att
    detta fick bli en egen klass är för att inte behöva ha så oerhört många fölt hoss Message-klassen.
    */

    private int fileSize;
    private String fileName;
    private String type;
    private byte[] key;
    private boolean usingEncryption;

    public FileRequest(String fileName, int fileSize) {
        this.fileName = fileName;
        this.fileSize = fileSize;
    }

    public FileRequest(String fileName, int fileSize, byte[] key, String type) {
        this(fileName, fileSize);
        this.key = key;
        this.type = type;

        usingEncryption = true;
    }

    public int getFileSize() {
        return fileSize;
    }

    public String getFileName() {
        return fileName;
    }

    public String getType() {
        return type;
    }

    public byte[] getKey() {
        return key;
    }

    public boolean isUsingEncryption() {
        return usingEncryption;
    }
}
