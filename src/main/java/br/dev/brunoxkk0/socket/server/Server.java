package br.dev.brunoxkk0.socket.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class Server {

    //Log para fazer o logging do servidor.
    private static final Logger LOGGER = Logger.getLogger("Server");

    //Socket base, a qual temos o TCP
    private final ServerSocket SERVER_SOCKET;

    //Threadpool para processar nossas conexões
    // Utilizamos uma threadpool, pois assim podemos lidar com várias conexões de maneira mais simples
    // sem ter que se preocupar com as conexões.
    private final ExecutorService THREAD_POOL;

    //Defina uma porta para executar o servidor, e defina
    //o número de threads para processar as conexões.
    public Server(int port, int threads) throws IOException {
        this.SERVER_SOCKET = new ServerSocket(port);
        this.THREAD_POOL = Executors.newFixedThreadPool(threads);
    }

    //Começa a escutar as conexões, ao chamar o listen, o servidor começa a estar apto a receber conexões.
    public void listen(){

        LOGGER.info("Começar a fazer a leitura no endereço: " + SERVER_SOCKET.getInetAddress().getHostAddress() + ":" + SERVER_SOCKET.getLocalPort());

        //enquanto o servidor estiver rodando tente criar conexões
        while (SERVER_SOCKET.isBound()){

            try{

                // Aceita a conexão, quando ela vier
                // Esse método ao ser chamado, fica esperando até uma comunicação acontencer.
                Socket socket = SERVER_SOCKET.accept();

                //Implementação de um 'cliente' que vai encapsular o Socket responsavel pela comunicação.
                ServerClientConnection client = new ServerClientConnection(socket);

                //Passa o cliente para nossa Threadpool, para ser processado.
                THREAD_POOL.submit(client);

            } catch (IOException e) {

                LOGGER.warning("Erro ao processar a comunicação. \nMensagem: " + e.getMessage());

            }

        }

    }

}
