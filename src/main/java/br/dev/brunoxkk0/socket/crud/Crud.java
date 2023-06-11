package br.dev.brunoxkk0.socket.crud;

import br.dev.brunoxkk0.socket.http.core.Request;
import br.dev.brunoxkk0.socket.http.core.Response;
import br.dev.brunoxkk0.socket.http.core.StatusCode;
import com.google.gson.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

public class Crud {

    public static Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();

    private static final LinkedList<SuperMarketListItem> items = new LinkedList<>();
    private static final AtomicInteger atomicInteger = new AtomicInteger(0);

    public static void handleConnection(Request request, Response response) throws IOException {

        switch (request.getMethod()){
            case "POST" ->      POST_ITEM(request, response);
            case "GET" ->       GET_ITEMS(request, response);
            case "PUT" ->       UPDATE_ITEM(request, response);
            case "DELETE" ->    DELETE_ITEM(request, response);
            case "OPTIONS" ->   OPTIONS(response);
        }

    }

    private static void OPTIONS(Response response) throws IOException {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE");
        response.writeStatus(StatusCode.OK);
        response.writeHeaders(headers);
        response.blankLine();
    }

    private static void DELETE_ITEM(Request request, Response response) throws IOException {
        int id;

        try{
            id = Integer.parseInt(request.getTarget().substring(1));
        }catch (Exception exception){
            response.writeNotFound();
            return;
        }

        SuperMarketListItem superMarketListItem = null;

        for(SuperMarketListItem item : items){
            if(item.getId() == id){
                superMarketListItem = item;
                break;
            }
        }

        if(superMarketListItem == null){
            response.writeNotFound();
            return;
        }

        items.removeIf(el -> el.getId() == id);
        response.writeOKAndJsonContent(gson.toJson(superMarketListItem));
    }

    private static void UPDATE_ITEM(Request request, Response response) throws IOException {
        int id;

        try{
            id = Integer.parseInt(request.getTarget().substring(1));
        }catch (Exception exception){
            response.writeNotFound();
            return;
        }

        SuperMarketListItem superMarketListItem = null;

        for(SuperMarketListItem item : items){
            if(item.getId() == id){
                superMarketListItem = item;
                break;
            }
        }

        if(superMarketListItem == null){
            response.writeNotFound();
            return;
        }

        String body = request.bodyAsText();
        JsonObject item = JsonParser.parseString(body).getAsJsonObject();

        if(item.has("name")){
            superMarketListItem.setName(item.get("name").getAsString());
        }

        if(item.has("quantity")){
            superMarketListItem.setQuantity(item.get("quantity").getAsInt());
        }

        if(item.has("checked")){
            superMarketListItem.setChecked(item.get("checked").getAsBoolean());
        }

        items.removeIf(el -> el.getId() == id);
        items.add(superMarketListItem);
        response.writeOKAndJsonContent(gson.toJson(superMarketListItem));
    }

    private static void POST_ITEM(Request request, Response response) throws IOException {
        String body = request.bodyAsText();
        JsonObject item = JsonParser.parseString(body).getAsJsonObject();

        if(item.has("name") && item.has("quantity") && item.has("checked")){

            String name = item.get("name").getAsString();
            int quantity = item.get("quantity").getAsInt();
            boolean checked = item.get("checked").getAsBoolean();

            SuperMarketListItem superMarketListItem = new SuperMarketListItem(
                    atomicInteger.incrementAndGet(),
                    name,
                    quantity,
                    checked
            );

            items.add(superMarketListItem);
            response.writeCreatedAndJsonContent(gson.toJson(superMarketListItem));
        }
    }

    private static void GET_ITEMS(Request request, Response response) throws IOException {
        if(request.getTarget().equals("/")){
            response.writeOKAndJsonContent(gson.toJson(items));
            return;
        }

        int id = -1;

        try{
            id = Integer.parseInt(request.getTarget().substring(1));
        }catch (Exception exception){
            response.writeNotFound();
            return;
        }

        SuperMarketListItem superMarketListItem = null;

        for(SuperMarketListItem item : items){
            if(item.getId() == id){
                superMarketListItem = item;
                break;
            }
        }

        if(superMarketListItem != null){
            response.writeOKAndJsonContent(gson.toJson(superMarketListItem));
        }else{
            response.writeNotFound();
        }
    }

}