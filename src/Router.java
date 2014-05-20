import org.jcsp.lang.Alternative;
import org.jcsp.lang.AltingChannelInput;
import org.jcsp.lang.CSProcess;
import org.jcsp.lang.ChannelOutput;
import org.jcsp.lang.Guard;


public class Router implements CSProcess {
	// Nome do host/nodo;
	private String hostName;

	// Endereco MAC do nodo;
	private String macAddr;
	
	// Canais de entrada do Hub
	private AltingChannelInput[] in;

	// Canais de saida do Hub
	private ChannelOutput[] out;
	//Tabela de roteamento
	private RouterTable table;
	
	public Router(String hostName,String macAddr,AltingChannelInput[] in, ChannelOutput[] out,RouterTable table) {
		// TODO Auto-generated constructor stub
		super();
		this.hostName = hostName;
		this.macAddr = macAddr;
		this.in = in;
		this.out = out;
		this.table = table;
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
	
	
	private void forwardPkts(Packet pkt) {
		System.out.println("Roteador "+this.hostName);
		System.out.println("Inicio do roteamento do pacote de "+pkt.data);
		Route aux = table.getRoute(pkt.dstMacAddr);		
		
		if(aux==null){
			return;
		}
		System.out.println("Roteando para porta " + aux.getPort()+" com MAC "+aux.getDestination());
		out[aux.getPort()].write(pkt);
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
			forwardPkts(pkt);
		}
	}
	
}
