package br.dev.brunoxkk0.socket.server;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

//Conexão do cliente com o servidor (Reponsável por processador a comunicação com o cliente do lado do servidor)
public class ServerClientConnection implements Runnable {

    //Log para fazer o logging do cliente.
    private static final Logger LOGGER = Logger.getLogger("Client");

    //O objeto socket representa a conexão em si.
    private final Socket socket;

    //BufferedInputStream, esta classe é utilizada para fazer a entrada de dados
    //SERVIDOR --> CLIENTE (é um pouco confuso o conceito, porem nessa inputstream lemos dados);
    private final BufferedInputStream bufferedInputStream;

    //BufferedOutputStream, esta classe é utilizada para fazer saida de dados
    //CLIENTE --> SERVIDOR (é nessa output stream que escrevemos os dados);
    private final BufferedOutputStream bufferedOutputStream;

    public ServerClientConnection(Socket socket) throws IOException {

        this.socket = socket;

        this.bufferedInputStream = new BufferedInputStream(socket.getInputStream());
        this.bufferedOutputStream = new BufferedOutputStream(socket.getOutputStream());

        LOGGER.info("Conexão aceita e cliente criado com sucesso [" + socket.getInetAddress().getHostAddress() + ":" + socket.getPort() + "]");

    }

    //Este método é executado quando de maneira asincrona
    @Override
    public void run() {

        //Envolvendo o código da função, para facilitar o tratamento de erros.
        try {

            LOGGER.info("Inciando o processamento da conexão...");


            //Lista com todas as linhas de mensagens recebidas;
            ArrayList<String> lines;

            //O Charset é utilizado para garantir que os carateres lidos estejam na codificação correta.
            //
            //Leitor com buffer                        |Leitor de InputStream   |InputStream da conexão. |Chatset
            BufferedReader reader = new BufferedReader(new InputStreamReader(bufferedInputStream, StandardCharsets.UTF_8));

            // Corrigindo bug das Threads ficarem presas.
            // vamos esperar até ele ficar pronto
            // ! podemos utilizando um sleep, para fazer ele esperar e diminuir a quantia de chamadas !
            //while (!reader.ready())
            //    Thread.sleep(100);


            LOGGER.info("Leitor pronto para ler os dados...");


            {
                // Leia até chegar ao fim (linha nula ou chegar no caratere com \0)
                // Atenção: Esta parte faz a leitura dos dados enviado pela conexão
                // se o fluxo de dados foi muito grande, pode demorar um pouco para terminar
                // É possível implementar regras de leitura para otimizar e só ler oque é necessário
                //
                // O \0 nos ajuda a delimitar o fim da cadeia de caracteres em nosso exemplo
                // o padrão http faz algo parecido utilizando \r\n para delimitar o fim
                //
                // Além disso, podemos reaproveitar para ler algo mais que esteja no buffer,
                // como por emxemplo uma imagem ou algo parecido.
                lines = readUpTo(reader, "\0");
            }

            LOGGER.info("Mensagem recebida: " + lines);

            //Implementamos o chatset como feito anteriormente para evitar que seja decodificado de maneira encorreta
            //a resposta enviada pelo servidor.
            //
            //Escritor com buffer                              |Escritor de OutputStream  |OutputStream da conexão. |Charset
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(bufferedOutputStream, StandardCharsets.UTF_8));


            {

                // Podemos então chamar nossa função para processar os dados recebidos e enviar uma resposta
                // o método processarDados faz esse processamento e devolve a saida para a conexão.

                processData(lines, bufferedWriter);

            }


            //force que os dados sejam escritos na saída de nossa conexão
            //para garantir que os nossos dados vão ser enviados.
            bufferedWriter.flush();

            LOGGER.info("Mensagem Enviada...");

            // Fechamos o nosso leitor
            reader.close();
            // Fechamos a nossa InputStream (A porta de entrada da conexão)
            bufferedInputStream.close();
            // Fechamos o nosso escritor
            bufferedWriter.close();
            // Fechamos a nossa OutputStream (A porta de saida da conexão)
            bufferedOutputStream.close();
            // Fechamos o socket, ou seja a conexão em si
            socket.close();

            LOGGER.info("Cliente desconectado com sucesso. [" + socket.getInetAddress().getHostAddress() + ":" + socket.getPort() + "]");

        } catch (Exception exception) {
            //exception.printStackTrace();
            LOGGER.warning("Falha ao processar a comunicação, erro: " + exception.getMessage());
        }

    }

    public ArrayList<String> readUpTo(BufferedReader input, String breakPoint) throws IOException {
        ArrayList<String> lines = new ArrayList<>();

        String line;
        while ((line = input.readLine()) != null && !line.equals(breakPoint)) {
            lines.add(line);
        }

        return lines;
    }

    public void processData(List<String> lines, BufferedWriter output) throws IOException {

        //Podemos fazer um simples código para simplesmente responder à mensagem enviada
        //transformando todos os caracteres em Maiúsculo
        for (String line : lines) {

            //Escrevendo na saída da conexão, o conteudo da linha em maíusculo.
            output.write(line.toUpperCase());

        }

    }

}
