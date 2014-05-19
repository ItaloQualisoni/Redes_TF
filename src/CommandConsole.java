import java.util.Scanner;

import org.jcsp.lang.CSProcess;
import org.jcsp.lang.ChannelOutput;

/**
 * Classe que representa uma console para interagir com os nodos do simulador,
 * possibilitando o envio de comandos aos mesmos.
 * 
 * @author Tiago Ferreto
 * @author Ramon Costi Fernandes <ramon.fernandes@acad.pucrs.br>
 */
public class CommandConsole implements CSProcess {

	// Array de objetos {@link NodeChannelWrapper}, associando os nodos com seus
	// respectivos canais de comunicacao com a console.
	private NodeChannelWrapper[] nodes;

	// Scanner para leitura dos comandos provenientes dos usuarios.
	private Scanner scan;

	// Indice correspondente ao Nodo ao qual a console esta atualmente
	// atribuida.
	private int nodeIndex;

	/**
	 * Cria uma console de comando para realizar comando sobre os objetos
	 * {@link Node} do simulador de rede.
	 * 
	 * @param nodes
	 */
	public CommandConsole(NodeChannelWrapper[] nodes) {
		super();
		this.nodes = nodes;
		this.nodeIndex = 0;
		this.scan = new Scanner(System.in);
	}

	/**
	 * Seleciona o nodo no qual a console de comandos efetuara os comandos, de
	 * acordo com o indice informado pela entrada padrao (teclado). O tratamento
	 * acerca do valor lido nao sera realizado neste metodo.
	 * 
	 * @return Um valor inteiro correspondente ao nodo selecionado.
	 */
	private int selectNode() {
		int opt;
		for (int i = 0; i < nodes.length; i++)
			System.out.println(i + ") " + nodes[i].node);
		System.out.print("Selecione um nodo para conectar a console: ");
		opt = scan.nextInt();
		scan.nextLine(); // flush do \n
		return opt;
	}

	/**
	 * Verifica se o nodo selecionado existe e pode ser escolhido para
	 * realizacao de comandos.
	 * 
	 * @return <code>true</code> se o indice do nodo escolhido for valido;
	 *         <code>false</code> se o indice do nodo escolhido nao for valido.
	 */
	private boolean checkNodeIndex() {
		if (this.nodeIndex >= 0 && this.nodeIndex < nodes.length)
			return true;
		return false;
	}

	/**
	 * Imprime todos os comandos disponiveis para selecao pelo usuario.
	 */
	private void printCommands() {
		Commands cmd;
		for (int i = 0; i < Constants.cmds.length; i++) {
			cmd = Constants.cmds[i];
			System.out.println(cmd.getValue() + ") " + cmd);
		}
	}

	/**
	 * Faz a captura do comando informado pelo usuario, retornando o valor
	 * numerico do mesmo.
	 * 
	 * @return Valor numerico do comando escolhido.
	 */
	private int selectCmd() {
		int output = -1;
		output = scan.nextInt();
		scan.nextLine(); // flush do \n
		return output;
	}

	/**
	 * Metodo para envia uma mensagem de texto qualquer para um nodo
	 * destinatario. O MAC destino e a mensagem devem ser informados para que a
	 * mensagem possa ser enviada.
	 * 
	 * @param channel Canal de comunicacao associado ao nodo que executara este
	 *            comando.
	 */
	private void sendMsg(ChannelOutput channel) {
		String line;
		String[] params;
		CommandWrapper cmdw;

		System.out.println("Comando sendMSG: <MAC DESTINO> <MENSAGEM>");
		// Requisita os parametros da mensagem
		line = scan.nextLine();
		params = line.split(" ");
		if (params.length < 2)
			System.out.println("Parametros informados de maneira incorreta.");
		else {
			System.out.println(params[0]);
			System.out.println(params[1]);
			cmdw = new CommandWrapper(Commands.SENDMSG, params[0], params[1]);
			channel.write(cmdw);
		}
	}

	private void pingCmd(ChannelOutput channel) {
		// TODO (voces nao pensaram que isso seria dado de graca para voces,
		// certo?)
		System.out.println("Este comando nao foi implementado. Noites de sono serao perdidas aqui.");
	}

	private void tracertCmd(ChannelOutput channel) {
		// TODO (muito menos isso daqui...)
		System.out.println("Este comando nao foi implementado. Muitas outras noites de sono serao perdidas aqui.");
	}

	/**
	 * Metodo para executar um comando. Todos os comandos possiveis para
	 * execucao podem ser colocados aqui.
	 * 
	 * @param cmd Codigo numero correspondente ao comando a ser executado.
	 */
	private void executeCmd(int cmd) {
		ChannelOutput outChannel = nodes[nodeIndex].out;

		switch (cmd) {
		case Constants.sendMsgCmd:
			sendMsg(outChannel);
			break;
		case Constants.pingCmd:
			pingCmd(outChannel);
			break;
		case Constants.tracertCmd:
			tracertCmd(outChannel);
			break;
		default:
			System.out.println("Comando invalido: " + cmd);
			break;
		}
	}

	@Override
	public void run() {

		try {
			while (true) {
				// Sleep tatico para nao atropelar os prints (nao resolvam
				// coisas reais dessa forma, pessoal... utilizem semafaros ou
				// outros mecanismos de sincronismo entre os processos pra
				// controlar essas coisas quando forem programar algo serio).
				Thread.sleep(Constants.sleepTime);

				// Seleciona um nodo para conectar a console
				this.nodeIndex = selectNode();
				if (checkNodeIndex()) {
					System.out.println("Nodo " + nodeIndex + " selecionado.");
					// Lista os comandos disponiveis
					printCommands();
					// Seleciona um comando
					executeCmd(selectCmd());
				} else {
					System.out.println("O nodo " + nodeIndex + "nao pode ser selecionado para efetuar comandos.");
				}
			}
		} catch (InterruptedException e) {
			System.out.println(e);
			e.printStackTrace();
		}
	}
}
