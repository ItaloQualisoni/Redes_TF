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
public class MainTopologiaUm {

	public static void main(String[] args) {
		//TODO Implementar o MTU !
		//Quem sabe criar uma classe que tenha o MTU e englobe os Channels
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
		CSProcess node0 = new Node("Node 1", "10.0.1.1", chan0A.in(), cons0.in(), new MTUOut(20,chan0B.out()));
		CSProcess node1 = new Node("Node 2", "10.0.2.1", chan1A.in(), cons1.in(), new MTUOut(10,chan1B.out()));
		CSProcess node2 = new Node("Node 3", "10.0.3.1", chan2A.in(), cons2.in(), new MTUOut(15,chan2B.out()));
		CSProcess node3 = new Node("Node 4", "10.0.4.1", chan3A.in(), cons3.in(), new MTUOut(5,chan3B.out()));

		//Tabela de roteamento
		

		MTUOut[] routerOuts = { new MTUOut(20, chan0A.out()) ,new MTUOut(10,  chan1A.out()),new MTUOut(15, chanR1.out())};
		AltingChannelInput[] routerIns = {chan0B.in(), chan1B.in(),chanR0.in()};
		String[] routerIPports = {"10.0.1.2","10.0.2.2","10.0.10.1"};
		
		RouterTable rt = new RouterTable();
		rt.addRoute(new Route("10.0.1.1", 0, "0.0.0.0"));
		rt.addRoute(new Route("10.0.2.1", 1, "0.0.0.0"));
		rt.addRoute(new Route("10.0.10.2",2 , "0.0.0.0"));
		rt.addRoute(new Route("10.0.3.1", 2, routerIPports[2]));
		rt.addRoute(new Route("10.0.4.1", 2, routerIPports[2]));
		
		CSProcess router = new Router("Roter R0", macRouter0, routerIns, routerOuts, rt,routerIPports);

		RouterTable rt1 = new RouterTable();

		String[] routerIPports1 = {"10.0.3.2","10.0.4.2","10.0.10.2"};
		

		rt1.addRoute(new Route("10.0.3.1", 0 , "0.0.0.0"));
		rt1.addRoute(new Route("10.0.4.1", 1 , "0.0.0.0"));
		rt1.addRoute(new Route("10.0.10.1",2 , "0.0.0.0"));
		rt1.addRoute(new Route("10.0.1.1", 2 , routerIPports1[2]));
		rt1.addRoute(new Route("10.0.2.1", 2 , routerIPports1[2]));
		
		MTUOut[] routerOuts1 = {new MTUOut(15,chan2A.out()),new MTUOut(5,chan3A.out()),new MTUOut(15,chanR0.out())};
		AltingChannelInput[] routerIns1 = {chan3B.in(), chan2B.in(),chanR1.in()};

		
		CSProcess router1 = new Router("Roter R1", macRouter1,routerIns1 , routerOuts1, rt1,routerIPports1);
		
		
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
		CSProcess[] processes = { node0, node1, node2,node3 ,router,router1, console, };
		CSProcess simulator = new Parallel(processes);
		simulator.run();
	}
}
