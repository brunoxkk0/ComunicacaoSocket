package br.dev.brunoxkk0.socket.http.core;

import br.dev.brunoxkk0.socket.server.ServerClientConnection;

import java.util.HashMap;

public class Request {

    private final String method;
    private final String target;
    private final HashMap<String, String> parameters;
    private final String protocol;
    private final HashMap<String, String> headers;

    private final ServerClientConnection serverClientConnection;

    public Request(String method, String target, HashMap<String, String> parameters, String protocol, HashMap<String, String> headers, ServerClientConnection serverClientConnection) {
        this.method = method;
        this.target = target;
        this.parameters = parameters;
        this.protocol = protocol;
        this.headers = headers;
        this.serverClientConnection = serverClientConnection;
    }

    public String getMethod() {
        return method;
    }

    public String getTarget() {
        return target;
    }

    public HashMap<String, String> getParameters() {
        return parameters;
    }

    public String getProtocol() {
        return protocol;
    }

    public HashMap<String, String> getHeaders() {
        return headers;
    }

    public String bodyAsText(){

        int length = Integer.parseInt(headers.getOrDefault("Content-Length", "-1"));

        try{
            return String.join("", serverClientConnection.readContentAsText(length));
        }catch (Exception ignored){}

        return "";
    }

    @Override
    public String toString() {
        return "Request{" +
                "method='" + method + '\'' +
                ", target='" + target + '\'' +
                ", parameters=" + parameters +
                ", protocol='" + protocol + '\'' +
                ", headers=" + headers +
                ", serverClientConnection=" + serverClientConnection +
                '}';
    }
}
