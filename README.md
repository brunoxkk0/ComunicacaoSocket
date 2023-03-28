## Passo a Passo - Implementação completa
### Servidor
1 — Precisamos desenvolver o servidor em sí, na linguagem Java utilizamos a classe `ServerSocket` para fazer a implementação de um servidor TCP.

2 — Para criar um servidor basta definirmos uma porta e pronto. Ele irá começar a fazer a leitura desta porta, caso a porta já esteja aberta, uma Exeção será disparada.

3 — Como sabemos que no futuro precisaremos aceitar diversas conexões simultaneamente, podemos fazer isso de maneira simples
utilizando uma Threpool para controlar os threads extras sem se preocupar, além disso, podemos evitar que a máquina trave o dê
erro devido aos limites de recursos do sistema.

4 — Podemos utilizar a classe `Executors`, chamando o método `newFixedThreadPool` para criar uma threadpool informando a quantia de threads que desejamos.

5 — Criaremos tamém um `Logger` para informar o estado de nosso sistema, podemos criar um logger simplesmente chamando o método `Logger.getLogger('name')`, assim criando uma instância do logger com o nome que selecionamos.

6 — Agora, devemos criar uma função para iniciarmos a leitura de nosso servidor, para isso podemos criar um método simples chamado `listen`.

7 — Dentro nesta função que criamos, iremos iniciar um while, verificando se o servidor está vinculado. Para isso chamamos o método `ìsBound()`, ele retornará verdadeiro se o estado do servidor for vinculado.

8 — Dentre desse “looping” de repetição, preciamos fazer a chamada do método do servidor `accept()`, ao chamar este método o código ficará travado esperando que seja feita alguma conexão.

9 — Depois que o código continuar, teremos um objeto da classe `Socket`, contendo as informações da conexão além das 'portas' de entrada e saída da conexão, que utilizaremos posteriormente para ler e escrever dados.

// InputStream do servidor == Outputstream do cliente.
// Outputstream do servidor == Inputstream do cliente.

10 — Precisamos criar um meio de processar a conexão do cliente com o servidor. Criaremos uma classe, que servirá como um "cliente",
que precesará a conexão do lado servidor, vou chamar esta classe de `ServerClientConnection`, esta classe irá rebecer o ‘socket’ que foi aceito e representa a nossa conexão.
Além disso, esse 'cliente' implementará a classe `Runnable`, assim então podendo usar a Threadpool criada anteriormente para lidar com a execução do processamento da conexão.

11 — Para isso simplesmente criamos uma instância de `ServerClientConnection`, então chamados o método `submit()` da nossa Threadpool, para realizar a execução do nosso processamento.

### ServidorCliente

12 — Nossa classe `ServerClientConnection` receberá o objeto to tipo `Socket` em seu construtor. Precisaremos definir também a entrada e saída de dados, por isso utilizamos as classes `BufferedInputStream` e `BufferedOutputStream` essas classes que vão envolver a `InputStream` e `OutputStream` do ‘socket’, estas classes possuem uma memória temporária interno e isso irá nos ajudar na leitura dos dados.

13 — Como nossa classe implementa a interface `Runnable`, um metodo `run()` deverá ser implementado obrigatoriamente, este método é implementado pela ‘interface’, executado pela nossa threadpool.

14 — Começaremos envolvendo todo o código da função em um bloco `try-catch` para podermos lidar com excessões caso elas ocorram.

15 — Declararemos uma variavél do tipo `ArrayList<String>` para salvar todas as mensagems que forem enviadas ao servidor.

16 — Criaremos também uma instância de leitor `BufferedReader`, ele que implementa o método de leitura que utilizamos para obter os dados. Para criar a instância do leitor, precissamos passar um `InputStreamReader`, e com isso passamos nossa InputStream, além de definir uma codificação para os dados, por convensão utilizamos UTF_8, a classe `StandardCharsets` possuem intâncias das condificações padrões.

17 — Criaremos um laço de repetição que irá verificar se foi enviado algum dado, para o servidor, para ele poder começar a leitura. Utilizaremos a função `ready()` do nosso leitor. Para evitar que ele fique verificando diversas vezes em um curto intervalo de tempo, chamarei um `Thread.sleep(100)` para que a thread durma por 100 milisegundos, esse tempo pode ser aumentado ou diminuido conforme a demanda.

18 — Quando o código for liberado, a nossa conexão poderá efetuar a leitura dos dados, então iremos criar uma função para ler até um determinado ponto. Chamarei esta função de `readUpTo()`, e ela receberá 2 parâmetros, o nosso leitor, e a ‘string’ que delimita o fim, isto é importante.

19 — Dentro de nossa função, iremos chamar a função `readLine()` e verificar o conteúdo para ver se ele difere de nulo, e se ele difere de nossa ‘string’ de parada.
Normalmente quando lemos árquivos de texto com o `BufferedReader` lemos até encontrar o EOF (End of File), contudo como estamos trabalhando com uma conexão aberta, o EOF só existe, no fim do da conexão, ou seja, quando se é desconectado, e isso não é oque precisamos.

20 — Cada vez que for lido uma linha com o médoto `readLine()` ela é adicionada a uma lista, e retornada quando chegamos a nossa parada.

21 — Quando nosso método de leitura retornar com a lista das linhas lídas, deveremos criar um meio de processar os dados recebidos.

22 — Para este projeto, irei criar uma função que irá processar os dados e enviar para a saída da conexão. Para isso devemos criar um meio de escrever os dados.

23 — Iremos criar uma instância de um escritor utilizando a classe `BufferedWriter`, esta classe é bem similar com a classe `BufferedReader`, contudo a mesma escreve ao invés de ler. Para criar a instância criaremos uma instância de `OutputStreamWriter` passando a nossa OutputStream e a codificação escolhida.

24 — Para a função que irá processar os dados, vou criar a função `processData()`, esta função irá receber 2 parâmetros, o primeiro as linhas lidas recebidas, e o nosso escritor a intância do `BufferedWriter`.

25 — Para motivos de testagem, nosso servidor simplesmente irá receber a mensagem, e envia-lá de volta com todos os caracteres transformados em maísculo.

26 — Para isso, iteramos as linhas da lista, e chamamos a função `write` do escritor passando a chamada do método `toUpperCase()` da nossa linha.

27 — Depois de "processar" os dados, precisamos chamar o método `flush()` do nosso `BufferedWriter` para garantir que os dados sejam escríto para fora da memória temporária, ou seja, enviados para a outra ponta da conexão.

28 — Nesse ponto a nossa mensagem for enviada e já podemos fechar a conexão. Para isso, devemos fechar todas as instâncias declaradas, para garantir que nada seja fechado fora da hora, não foi utilizado try with resources, por isso devemos fechar cada instância individualmente.

29 — Começaremos chamando método `close()` do nosso leitor, seguido pela porta de entrada de dados, a nossa InputStream, em seguida, fechamos nosso escritor, então fechamos a nossa porta de saída de dados o OutputStream, e por fim, encerramos a conexão, também chamando o método `close()` presente em nosso ‘socket’.

30 — Apartir deste momento podemos testar o funcionamento do nosso servidor.

## Testando o projeto

### Main

1 — Vou começar criando uma classe para testar o servidor, para isso vou criar uma classe chamada de `Principal`.

2 — E nela vou declarar o método main do java o método `public static void main(String[] args)`

3 — Vou declerar uma variavel do tipo `Servidor`, passando a porta `9797` e o número de 4 threads.

4 — Por fim chamados o método `listen()`, ele será responsavél por iniciar a leitura das conexões do nosso servidor.

### TestClientServerConnection

5 — Vamos escrever um teste simples utilizando JUnit para testar o comportamente do servidor.

6 — Esse teste irá enviar ao nosso servidor uma ‘string’ contentando uma mensagem simples e irá verificar se o resultado recebido do servidor, é a mesma mensagem enviada, contudo com todos seus caracteres em maísculo.

7 — Para não alongar muito o código está disponível no github.

8 — Explicar o código e executar demonstrando que funciona.