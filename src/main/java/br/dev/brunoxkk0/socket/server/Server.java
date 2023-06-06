package br.dev.brunoxkk0.socket.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class Server {

    private static final Logger LOGGER = Logger.getLogger("Server");

    private final ServerSocket SERVER_SOCKET;

    private final ExecutorService THREAD_POOL;

    public Server(int port, int threads) throws IOException {
        this.SERVER_SOCKET = new ServerSocket(port);
        this.THREAD_POOL = Executors.newFixedThreadPool(threads);
    }

    public void listen(){

        LOGGER.info("Começar a fazer a leitura no endereço: " + SERVER_SOCKET.getInetAddress().getHostAddress() + ":" + SERVER_SOCKET.getLocalPort());

        while (SERVER_SOCKET.isBound()){

            try{

                Socket socket = SERVER_SOCKET.accept();

                ServerClientConnection client = new ServerClientConnection(socket);

                THREAD_POOL.submit(client);

            } catch (IOException e) {

                LOGGER.warning("Erro ao processar a comunicação. \nMensagem: " + e.getMessage());

            }

        }

    }

}
