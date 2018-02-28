package messager;

public class FileResponse {
    /*
    Denna klass är på många sätt likt FileRequest-klassen. Den största anledningen till att detta fick bli en egen klass
    är att inte få så oerhört många fält i Message-klassen.
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
