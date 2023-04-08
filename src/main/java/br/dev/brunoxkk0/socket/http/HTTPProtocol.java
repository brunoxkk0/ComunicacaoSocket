package br.dev.brunoxkk0.socket.http;

import br.dev.brunoxkk0.socket.http.core.Request;
import br.dev.brunoxkk0.socket.http.core.Response;
import br.dev.brunoxkk0.socket.http.core.StatusCode;
import br.dev.brunoxkk0.socket.server.ServerClientConnection;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class HTTPProtocol {

    public static final String VERSION = "HTTP/1.1";
    public static final String LINE_BREAK = "\r\n";
    public static final String BLANK_LINE = "";

    public static void processData(List<String> lines, BufferedWriter output, ServerClientConnection serverClientConnection) throws IOException {

        if(!lines.isEmpty()){

            String[] methodAndTargetAndVersion = lines.get(0).split(" ", 3);

            String method = methodAndTargetAndVersion[0];
            String target = methodAndTargetAndVersion[1];
            String version = methodAndTargetAndVersion[2];

            if(!version.equalsIgnoreCase(HTTPProtocol.VERSION)){
                //TODO: Opa, isso não é compatível com nosso servidor.
            }

            HashMap<String, String> params = loadParams(target);

            if(!params.isEmpty()){
                target = target.substring(0, target.indexOf('?'));
            }

            List<String> rawHeaders = lines.subList(1, lines.size());

            HashMap<String, String> headers = loadHeaders(rawHeaders);

            Request request = new Request(method, target, params, version, headers, serverClientConnection);
            Response response = new Response(output, serverClientConnection);

            HTTPProtocol.handle(request, response);

        }

    }

    private static HashMap<String, String> loadHeaders(List<String> list){

        HashMap<String, String> headers = new HashMap<>();

        for(String ln : list){

            String[] parts = ln.split(":", 2);

            if(parts.length == 2){
                headers.put(parts[0], parts[1].trim());
            }

        }

        return headers;
    }

    private static HashMap<String, String> loadParams(String target){

        HashMap<String, String> params = new HashMap<>();

        int index = target.indexOf('?');

        System.out.println(index);

        if(index != -1){

            String targetParams = target.substring(index + 1);
            String[] paramsAndValues = targetParams.split("&");

            for(String ln : paramsAndValues){

                String[] parts = ln.split("=", 2);

                if(parts.length == 2){
                    params.put(parts[0], parts[1]);
                }

            }

        }

        return params;
    }


    public static void handle(Request request, Response response) throws IOException {
        System.out.println(request);
        response.writeStatus(StatusCode.NOT_IMPLEMENTED);
        response.blankLine();
    }

}
