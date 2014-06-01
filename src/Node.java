import java.util.ArrayList;

import org.jcsp.lang.Alternative;
import org.jcsp.lang.AltingChannelInput;
import org.jcsp.lang.CSProcess;
import org.jcsp.lang.ChannelOutput;
import org.jcsp.lang.Guard;

/**
 * Classe que representa um Nodo (ou host) nas topologias de rede. Estes objetos
 * sao capazes de receber e enviar dados para rede.
 * 
 * @author Tiago Ferreto
 * @author Ramon Costi Fernandes <ramon.fernandes@acad.pucrs.br>
 */
public class Node implements CSProcess {
	// Nome do host/nodo;
	private String hostName;

	// Endereco MAC do nodo;
	private String ipAddr;

	// Canal de entrada de dados do nodo;
	private AltingChannelInput in;

	// Canal de entrada de comandos do nodo (via terminal);
	private AltingChannelInput consoleIn;

	// Canal de saida de dados do nodo com o MTU;
	private MTUOut out;
	
	//Buffer das mensagens;
	private ArrayList<Packet> buffer = new ArrayList<>();

	
	/**
	 * Cria um Nodo no ambiente de simulacao, atribuindo um nome
	 * <code>hostName</code>, endereco MAC <code>macAddr</code>, e canais de
	 * comunicacao com demais elementos de rede e uma console de controle
	 * <code>consoleIn</code>
	 * 
	 * @param hostName Nome do deste nodo (host).
	 * @param macAddr Endereco MAC deste nodo.
	 * @param in Porta de comunicacao para entrada de dados provenientes da
	 *            rede.
	 * @param consoleIn Porta de comunicacao para receber comandos da console.
	 * @param out Porta de comunicacao para saida de dados originados deste
	 *            nodo.
	 */
	public Node(String hostName, String ipAddr, AltingChannelInput in, AltingChannelInput consoleIn, MTUOut out) {
		super();
		this.hostName = hostName;
		this.ipAddr = ipAddr;
		this.in = in;
		this.consoleIn = consoleIn;
		this.out = out;
	}

	/**
	 * Retorna o nome deste host.
	 * 
	 * @return Retorna um objeto {@link String} com o nome deste host.
	 */
	public String getHostName() {
		return this.hostName;
	}

	/**
	 * Envia uma mensagem qualquer do tipo {@link String} para o endereco MAC
	 * especificado via parametro, encapsulando os dados em um objeto
	 * {@link Packet}.
	 * 
	 * @param dstMacAddr Endereco MAC destino da mensagem a ser enviada.
	 * @param msg Mensagem a ser encapsulada no pacote.
	 */
	private void sendMsg(String dstMacAddr, String msg) {
		Packet pkt = new Packet(dstMacAddr, this.ipAddr, msg,msg.length(),0);
		out.write(pkt);
	}

	/**
	 * Verifica se o pacote recebido esta destinado a este nodo.
	 * 
	 * @param pkt Objeto {@link Packet} recebido;
	 * @return <code>true</code> se o pacote foi destinado a este nodo;
	 *         <code>false</code> caso contrario;
	 */
	private boolean checkPkt(Packet pkt) {
		if (pkt == null)
			return false;
		if (pkt.dstAddr.equalsIgnoreCase(this.ipAddr))
			return true;
		if (pkt.dstAddr.equalsIgnoreCase(Constants.macBcast))
			return true;
		
		return false;
	}

	/**
	 * Imprime o conteudo do pacote recebido.
	 * 
	 * @param pkt Objeto {@link Packet} com a mensagem a ser impressa.
	 */
	private void printPkt(Packet pkt) {
		System.out.printf("[%s] received : %s, from [%s] \n\n0", this.hostName, pkt.data, pkt.srcAddr);
	}

	/**
	 * Interpreta o comando que este nodo deve executar, realizando as operacoes
	 * apropriadas.
	 * 
	 * @param command
	 */
	private void runConsoleCommand(CommandWrapper command) {
		switch (command.cmd) {
		case SENDMSG:
			sendMsg(command.dstMacAddr, command.param);
			break;
		case PING:
			break;
		case TRACERT:
			break;
		default:
			// NOP
			break;
		}
	}
	
	private String mergePacketBuffer(int size){
		char[] data = new char[size];
		char[] aux ;
		for (Packet packet : buffer) {
			aux = packet.data.toCharArray();
			for (int i = 0; i < aux.length; i++) {
				data[packet.getOffset()+i] = aux[i];
			}
		}
		buffer = new ArrayList<>();
		return String.valueOf(data);
	}
	

	@Override
	public void run() {
		System.out.println(this.hostName + " Online.");
		Packet pkt;
		CommandWrapper cmdw;
		final Guard[] guard = { consoleIn, in };
		final Alternative alt = new Alternative(guard);

		while (true) {
			switch (alt.fairSelect()) {
			// 0 Corresponde ao canal "consoleIn"
			case 0:
				cmdw = (CommandWrapper) consoleIn.read();
				runConsoleCommand(cmdw);
				break;
			// 1 Corresponde ao canal "in"
			case 1:
				pkt = (Packet) this.in.read();
				if (checkPkt(pkt)){
					buffer.add(pkt);
					if(pkt.lastFragment){
						String buffData = this.mergePacketBuffer(pkt.sizeFinal);
						pkt.data = buffData;
						printPkt(pkt);
					}
				}
				break;
			}
		}
	}

	@Override
	public String toString() {
		return String.format("[%s] - IP: [%s]", hostName, ipAddr);
	}

}
