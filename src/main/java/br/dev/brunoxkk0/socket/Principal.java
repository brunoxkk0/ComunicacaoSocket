package br.dev.brunoxkk0.socket;

import br.dev.brunoxkk0.socket.server.Server;

import java.io.IOException;

public class Principal {

    public static void main(String[] args) throws IOException {
        Server server = new Server(9797, 4);
        server.listen();
    }

}
