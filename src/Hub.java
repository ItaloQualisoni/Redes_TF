import org.jcsp.lang.Alternative;
import org.jcsp.lang.AltingChannelInput;
import org.jcsp.lang.CSProcess;
import org.jcsp.lang.ChannelOutput;
import org.jcsp.lang.Guard;

/**
 * 
 * Classe que descreve o comportamento de um Hub de <code>n</code> portas,
 * realizando o encaminhamento de pacotes recebidos atraves de uma porta para as
 * demais.
 * 
 * @author Tiago Ferreto
 * @author Ramon Costi Fernandes <ramon.fernandes@acad.pucrs.br>
 */
public class Hub implements CSProcess {

	// Canais de entrada do Hub
	private AltingChannelInput[] in;

	// Canais de saida do Hub
	private ChannelOutput[] out;

	/**
	 * Cria um objeto Hub com um numero de portas indefido, de acordo com a
	 * cardinalidade do array de canais de entrada/saida utilizados.
	 * 
	 * @param in Array de portas de entrada do Hub.
	 * @param out Array de portas de saida do Hub.
	 */
	public Hub(AltingChannelInput[] in, ChannelOutput[] out) {
		super();
		this.in = in;
		this.out = out;
	}

	/**
	 * Realiza o encaminhamento dos pacote recebidos pela porta
	 * <code>port</code> para as demais porta do hub, controlando para que o
	 * pacote nao seja devolvido para a porta na qual o mesmo foi recebido.
	 * 
	 * @param srcPort Porta origem, de onde o pacote foi recebido;
	 * @param pkt Pacote a ser encaminhado;
	 */
	private void forwardPkts(int srcPort, Packet pkt) {
		for (int i = 0; i < in.length; i++) {
			if (i != srcPort)
				out[i].write(pkt);
		}
	}

	@Override
	public void run() {
		System.out.println("HUB Online.");
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
			forwardPkts(port, pkt);
		}
	}
}
