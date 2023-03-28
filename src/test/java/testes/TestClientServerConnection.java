package testes;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class TestClientServerConnection {

    @Test
    public void connect_and_check_the_response() throws IOException, InterruptedException {

        String entrada = "olá bom dia";
        String saida = "";

        Socket socket = new Socket("127.0.0.1", 9797);

        PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8),true);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));

        printWriter.println(entrada);
        printWriter.println('\0'); // <-- Enviando esse caractere \0, podemos detectar no servidor o final da leitura dos dados.

        while (!bufferedReader.ready()){
            // esperando o buffer estar pronto
            Thread.sleep(100);
        }

        String ln;
        while ((ln = bufferedReader.readLine()) != null)
            saida = ln;

        socket.close();

        assertEquals(saida, entrada.toUpperCase());
    }

}
