import org.jcsp.lang.Alternative;
import org.jcsp.lang.AltingChannelInput;
import org.jcsp.lang.CSProcess;
import org.jcsp.lang.Guard;


public class Router implements CSProcess {
	// Nome do host/nodo;
	private String hostName;

	// Endereco MAC do nodo;
	private String macAddr;
	
	// Endereco MAC do nodo;
	private String[] IPports;
	
	// Canais de entrada do Hub
	private AltingChannelInput[] in;

	// Canais de saida do Hub
	private MTUOut[] out;
	
	//Tabela de roteamento
	private RouterTable table;
	
	public Router(String hostName,String macAddr,AltingChannelInput[] in, MTUOut[] out,RouterTable table,String[] IPports) {
		// TODO Auto-generated constructor stub
		super();
		this.hostName = hostName;
		this.macAddr = macAddr;
		this.in = in;
		this.out = out;
		this.table = table;
		this.IPports = IPports;
	}
	
	public String getHostName() {
		return hostName;
	}

	public String getMacAddr() {
		return macAddr;
	}

	/**
	 * Realiza o encaminhamento dos pacote recebidos pela porta
	 * <code>port</code> para as demais porta do hub, controlando para que o
	 * pacote nao seja devolvido para a porta na qual o mesmo foi recebido.
	 * 
	 * @param srcPort Porta origem, de onde o pacote foi recebido;
	 * @param pkt Pacote a ser encaminhado;
	 */
	
	
	private void forwardPkts(Packet pkt,int port) {
		System.out.println("Roteador "+this.hostName+ " recebeu o pacote com dados \""+ pkt.data + "\" atraves da porta " + (port+1));
		//System.out.println("Inicio do roteamento do dado "+pkt.data + " recebido na porta "+(port+1) + " de IP: "+ IPports[port]);
		Route aux = table.getRoute(pkt.dstAddr);
		System.out.println("Roteando para porta " + (aux.getPort()+1)+" com IP "+IPports[aux.getPort()]);
		out[aux.getPort()].write(pkt,pkt.sizeFinal);
	}
	
	
	@Override
	public void run() {
		System.out.println(this.getHostName()+" Online.");
		int port;
		Packet pkt;
		final Guard[] altChans = (Guard[]) in;
		final Alternative alt = new Alternative(altChans);

		// Loop para o recebimento e encaminhamento dos pacotes pelo Hub.
		while (true) {
			// "fairselect" realiza uma selecao dos canais com niveis de
			// prioridade, favorecendo canais prontos mas que ainda nao foram
			// atendidos. Mesma ideia utilizada num RoundRobin.
			port = alt.fairSelect();
			pkt = (Packet) in[port].read();
			forwardPkts(pkt,port);
		}
	}
	
}
