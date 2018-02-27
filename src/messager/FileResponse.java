package messager;

public class FileResponse {
    /*
    ????
    */

    private int port;
    private boolean acceptedFileRequest;

    public FileResponse(boolean acceptedFileRequest, int port) {
        this.acceptedFileRequest = acceptedFileRequest;
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    public boolean acceptedFileRequest() {
        return acceptedFileRequest;
    }

}
