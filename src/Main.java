import java.util.ArrayList;
import org.jcsp.lang.AltingChannelInput;
import org.jcsp.lang.CSProcess;
import org.jcsp.lang.Channel;
import org.jcsp.lang.ChannelOutput;
import org.jcsp.lang.One2OneChannel;
import org.jcsp.lang.Parallel;

/**
 * 
 * @author Tiago Ferreto
 * @author Ramon Costi Fernandes <ramon.fernandes@acad.pucrs.br>
 */
public class Main {

	public static void main(String[] args) {

		// Canais para comunicacao da console de comando com os nodos.
		final One2OneChannel cons0 = Channel.one2one();
		final One2OneChannel cons1 = Channel.one2one();
		final One2OneChannel cons2 = Channel.one2one();
		final One2OneChannel cons3 = Channel.one2one();

		// Canais para comunicacao dos nodos com o hub.
		final One2OneChannel chan0A = Channel.one2one();
		final One2OneChannel chan1A = Channel.one2one();
		final One2OneChannel chan2A = Channel.one2one();
		final One2OneChannel chan3A = Channel.one2one();

		// Canais para comunicacao do hub com os nodos.
		final One2OneChannel chan0B = Channel.one2one();
		final One2OneChannel chan1B = Channel.one2one();
		final One2OneChannel chan2B = Channel.one2one();
		final One2OneChannel chan3B = Channel.one2one();
		
		// Canais para comunicacao dos router para router.
		final One2OneChannel chanR0 = Channel.one2one();
		
		final One2OneChannel chanR1 = Channel.one2one();

		
		String macRouter0 = "00:00:00:00:00:R0";
		String macRouter1 = "00:00:00:00:00:R1";
		
		//in() entra
		//out() saí
		
		// Instanciacao dos processos Nodos.
		CSProcess node0 = new Node("Node 0", "00:00:00:00:00:A1", chan0A.in(), cons0.in(), chan0B.out());
		CSProcess node1 = new Node("Node 1", "00:00:00:00:00:B2", chan1A.in(), cons1.in(), chan1B.out());
		CSProcess node2 = new Node("Node 2", "00:00:00:00:00:C3", chan2A.in(), cons2.in(), chan2B.out());
		CSProcess node3 = new Node("Node 3", "00:00:00:00:00:D4", chan3A.in(), cons3.in(), chan3B.out());

		//Tabela de roteamento
		
		RouterTable rt = new RouterTable();
		rt.addRoute(new Route("00:00:00:00:00:A1", 0, "00:00:00:00:00:A1"));
		rt.addRoute(new Route("00:00:00:00:00:B2", 1, "00:00:00:00:00:B2"));
		rt.addRoute(new Route("00:00:00:00:00:C3", 2, macRouter1));
		rt.addRoute(new Route("00:00:00:00:00:D4", 2, macRouter1));
		
		ChannelOutput[] routerOuts = { chan0A.out(), chan1A.out(), chanR1.out()};
		AltingChannelInput[] routerIns = { chan0B.in(), chan1B.in(),chanR0.in()};
		
		CSProcess router = new Router("Roter R0", macRouter0, routerIns, routerOuts, rt);

		RouterTable rt1 = new RouterTable();
		rt1.addRoute(new Route("00:00:00:00:00:A1", 0, macRouter0));
		rt1.addRoute(new Route("00:00:00:00:00:B2", 0, macRouter0));
		rt1.addRoute(new Route("00:00:00:00:00:C3", 1, "00:00:00:00:00:C3"));
		rt1.addRoute(new Route("00:00:00:00:00:D4", 2, "00:00:00:00:00:D4"));
		
		ChannelOutput[] routerOuts1 = { chanR0.out(), chan2A.out(),chan3A.out()};
		AltingChannelInput[] routerIns1 = { chan3B.in(),chan2B.in(),chanR1.in()};
		
		CSProcess router1 = new Router("Roter R1", macRouter1,routerIns1 , routerOuts1, rt1);
		
		
		// Instanciacao do processo Hub os canais devidamente mapeados em
		// arrays.
		ChannelOutput[] hubOuts = { chan0A.out(), chan1A.out()};
		AltingChannelInput[] hubIns = { chan0B.in(), chan1B.in()};
		
		CSProcess hub = new Hub(hubIns, hubOuts);

		// Instanciacao da console de comandos e associacao dos nodos com seus
		// canais de comunicacao.
		NodeChannelWrapper[] nodes = { new NodeChannelWrapper(node0, cons0.out()),new NodeChannelWrapper(node1, cons1.out()),new NodeChannelWrapper(node2, cons2.out()),new NodeChannelWrapper(node3, cons3.out())};
		CSProcess console = new CommandConsole(nodes);

		// Execucao dos processos em paralelo.
		CSProcess[] processes = { node0, node1, node2,node3 ,router,router1, console };
		CSProcess simulator = new Parallel(processes);
		simulator.run();
	}
}
