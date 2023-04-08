package br.dev.brunoxkk0.socket.http.core;

import br.dev.brunoxkk0.socket.http.HTTPProtocol;
import br.dev.brunoxkk0.socket.server.ServerClientConnection;

import java.io.BufferedWriter;
import java.io.IOException;

public class Response {

    private final BufferedWriter bufferedWriter;
    private final ServerClientConnection serverClientConnection;

    public Response(BufferedWriter bufferedWriter, ServerClientConnection serverClientConnection) {
        this.bufferedWriter = bufferedWriter;
        this.serverClientConnection = serverClientConnection;
    }

    public BufferedWriter getBufferedWriter() {
        return bufferedWriter;
    }

    public ServerClientConnection getServerClientConnection() {
        return serverClientConnection;
    }

    public void write(String data) throws IOException {
        bufferedWriter.write(String.format("%s%s", data, HTTPProtocol.LINE_BREAK));
    }

    public void blankLine() throws IOException {
        bufferedWriter.write(HTTPProtocol.LINE_BREAK);
    }


    public void writeStatus(StatusCode status) throws IOException {
        write(String.format("%s %d %s", HTTPProtocol.VERSION, status.getCode(), status.getMessage()));
    }

}
