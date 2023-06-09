package br.dev.brunoxkk0.socket.http.core;

import br.dev.brunoxkk0.socket.crud.Crud;
import br.dev.brunoxkk0.socket.http.HTTPProtocol;
import br.dev.brunoxkk0.socket.server.ServerClientConnection;
import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;

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

    public void writeHeaders(HashMap<String, String> headers) throws IOException {
        for(String key : headers.keySet()){
            bufferedWriter.write(String.format("%s:%s%s", key, headers.get(key), HTTPProtocol.LINE_BREAK));
        }
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

    public void writeOKAndJsonContent(String json) throws IOException {
        writeStatus(StatusCode.OK);
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Server", "TestTCP");
        headers.put("Content-Type", "application/json");
        writeHeaders(headers);
        blankLine();
        write(json);
        blankLine();
    }

    public void writeCreatedAndJsonContent(String json) throws IOException {
        writeStatus(StatusCode.CREATED);
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Server", "TestTCP");
        headers.put("Content-Type", "application/json");
        writeHeaders(headers);
        blankLine();
        write(json);
        blankLine();
    }

    public void writeNotFound() throws IOException {
        writeStatus(StatusCode.NOT_FOUND);
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Server", "TestTCP");
        headers.put("Content-Type", "application/json");
        writeHeaders(headers);
        blankLine();
        write(Crud.gson.toJson(StatusCode.NOT_FOUND));
        blankLine();
    }

    public void writeNoContent() throws IOException {
        writeStatus(StatusCode.NO_CONTENT);
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Server", "TestTCP");
        headers.put("Content-Type", "application/json");
        writeHeaders(headers);
        blankLine();
        write(Crud.gson.toJson(StatusCode.NO_CONTENT));
        blankLine();
    }

}
