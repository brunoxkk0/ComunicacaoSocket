package br.dev.brunoxkk0.socket.server;

import br.dev.brunoxkk0.socket.http.HTTPProtocol;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.logging.Logger;

public class ServerClientConnection implements Runnable {

    private static final Logger LOGGER = Logger.getLogger("Client");

    private final Socket socket;

    private final BufferedInputStream bufferedInputStream;

    private final BufferedOutputStream bufferedOutputStream;

    private BufferedReader reader;

    public ServerClientConnection(Socket socket) throws IOException {

        this.socket = socket;

        this.bufferedInputStream = new BufferedInputStream(socket.getInputStream());
        this.bufferedOutputStream = new BufferedOutputStream(socket.getOutputStream());

        LOGGER.info("Conexão aceita e cliente criado com sucesso [" + socket.getInetAddress().getHostAddress() + ":" + socket.getPort() + "]");

    }


    @Override
    public void run() {

        try {

            LOGGER.info("Inciando o processamento da conexão...");


            ArrayList<String> lines;

            reader = new BufferedReader(new InputStreamReader(bufferedInputStream, StandardCharsets.UTF_8));

            // Corrigindo bug das Threads ficarem presas
            // while (!reader.ready())
            //    Thread.sleep(100);


            LOGGER.info("Leitor pronto para ler os dados...");


            {
                lines = readUpTo(reader, HTTPProtocol.BLANK_LINE);
            }

            LOGGER.info("Mensagem recebida: " + lines);

            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(bufferedOutputStream, StandardCharsets.UTF_8));

            {
                HTTPProtocol.processData(lines, bufferedWriter, this);
            }

            bufferedWriter.flush();

            LOGGER.info("Mensagem Enviada...");

            reader.close();
            bufferedInputStream.close();
            bufferedWriter.close();
            bufferedOutputStream.close();
            socket.close();

            LOGGER.info("Cliente desconectado com sucesso. [" + socket.getInetAddress().getHostAddress() + ":" + socket.getPort() + "]");

        } catch (Exception exception) {
            LOGGER.warning("Falha ao processar a comunicação, erro: " + exception.getMessage());
        }

    }

    public ArrayList<String> readUpTo(String breakPoint) throws IOException {
        return readUpTo(reader, breakPoint);
    }

    private ArrayList<String> readUpTo(BufferedReader input, String breakPoint) throws IOException {
        ArrayList<String> lines = new ArrayList<>();

        String line;
        while ((line = input.readLine()) != null && !line.equals(breakPoint)) {
            lines.add(line);
        }

        return lines;
    }

    public String readContentAsText(int contentLength) throws IOException {

        StringBuilder stringBuilder = new StringBuilder();
        char[] buffer = new char[4096];

        int leftToRead = contentLength;
        int toReadNextLoop = Math.min(leftToRead, buffer.length);

        int read;
        while (toReadNextLoop > 1 && (read = reader.read(buffer, 0, toReadNextLoop)) != -1) {
            stringBuilder.append(buffer, 0, read);
            leftToRead -= read;
            toReadNextLoop = Math.min(leftToRead, buffer.length);
        }

        return stringBuilder.toString();
    }


}
