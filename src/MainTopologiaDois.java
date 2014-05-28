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
public class MainTopologiaDois {

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
		final One2OneChannel chanR1 = Channel.one2one();
		final One2OneChannel chanR2 = Channel.one2one();
		final One2OneChannel chanR3 = Channel.one2one();
		final One2OneChannel chanR4 = Channel.one2one();

		
		String macRouter  = "00:00:00:00:00:R1";
		String macRouter2 = "00:00:00:00:00:R2";
		String macRouter3 = "00:00:00:00:00:R3";
		String macRouter4 = "00:00:00:00:00:R4";
		
		//in() entra
		//out() saí
		
		//ROUTER 1
		// Instanciacao dos processos Nodos.
		CSProcess node0 = new Node("Node 1", "10.0.1.1", chan0A.in(), cons0.in(), new MTUOut(20,chan0B.out()));
		CSProcess node1 = new Node("Node 2", "10.0.2.1", chan1A.in(), cons1.in(), new MTUOut(10,chan1B.out()));
		CSProcess node2 = new Node("Node 3", "10.0.3.1", chan2A.in(), cons2.in(), new MTUOut(10,chan2B.out()));
		CSProcess node3 = new Node("Node 4", "10.0.4.1", chan3A.in(), cons3.in(), new MTUOut(15,chan3B.out()));

		//Lista de quem o router envia dados
		MTUOut[] routerOuts = { new MTUOut(20, chan0A.out()), new MTUOut(15, chanR2.out()),new MTUOut(5, chanR3.out()) };
		//Lista de quem o router lee dados
		AltingChannelInput[] routerIns = {chan0B.in(),chanR1.in()};
		//Lista dos IPs das portas do router
		String[] routerIPports = {"10.0.1.2","10.0.10.1","10.0.30.1"};
		//Cria tabela de roteamento
		RouterTable rt = new RouterTable();
		rt.addRoute(new Route("10.0.1.1", 0, routerIPports[0]));
		rt.addRoute(new Route("10.0.2.1", 1, routerIPports[1]));
		rt.addRoute(new Route("10.0.10.2",2 , "0.0.0.0"));
		rt.addRoute(new Route("10.0.3.1", 2, routerIPports[2]));
		rt.addRoute(new Route("10.0.4.1", 1, routerIPports[1]));
		//Instancia o router
		CSProcess router = new Router("Roter R1", macRouter, routerIns, routerOuts, rt,routerIPports);

		//ROUTER 2
		String[] routerIPports2 = {"10.0.2.2","10.0.10.2","10.0.20.1"};
		MTUOut[] routerOuts2 = {new MTUOut(10,chan1A.out()),new MTUOut(15,chanR1.out()),new MTUOut(10,chanR4.out())};
		AltingChannelInput[] routerIns2 = { chan1B.in(),chanR2.in()};

		RouterTable rt2 = new RouterTable();

		rt2.addRoute(new Route("10.0.1.1", 1 , routerIPports2[1]));
		rt2.addRoute(new Route("10.0.2.1", 0 , routerIPports2[0]));
		rt2.addRoute(new Route("10.0.3.1", 1 , routerIPports2[1]));
		rt2.addRoute(new Route("10.0.4.1", 2 , routerIPports2[2]));
		rt2.addRoute(new Route("10.0.10.1",1 , "0.0.0.0"));
		rt2.addRoute(new Route("10.0.20.1",2 , "0.0.0.0"));
		
		CSProcess router2 = new Router("Roter R2", macRouter2,routerIns2 , routerOuts2, rt2,routerIPports2);
		
				
		//ROUTER 3
		String[] routerIPports3 = {"10.0.3.2","10.0.30.2","10.0.40.2"};
		MTUOut[] routerOuts3 = {new MTUOut(10,chan2A.out()),new MTUOut(5,chanR1.out()),new MTUOut(20,chanR4.out())};
		AltingChannelInput[] routerIns3 = { chan2B.in(),chanR3.in()};

		RouterTable rt3 = new RouterTable();

		rt3.addRoute(new Route("10.0.1.1", 1 , routerIPports3[1]));
		rt3.addRoute(new Route("10.0.2.1", 2 , routerIPports3[2]));
		rt3.addRoute(new Route("10.0.3.1", 0 , routerIPports3[0]));
		rt3.addRoute(new Route("10.0.4.1", 2 , routerIPports3[2]));
		rt3.addRoute(new Route("10.0.30.1",1 , "0.0.0.0"));
		rt3.addRoute(new Route("10.0.20.1",2 , "0.0.0.0"));
				
		CSProcess router3 = new Router("Roter R3", macRouter3,routerIns3 , routerOuts3, rt3,routerIPports3);		
		
		//ROUTER 4
		String[] routerIPports4 = {"10.0.4.2","10.0.20.2","10.0.40.2"};
		MTUOut[] routerOuts4 = {new MTUOut(10,chan3A.out()),new MTUOut(10,chanR2.out()),new MTUOut(20,chanR3.out())};
		AltingChannelInput[] routerIns4 = { chan3B.in(),chanR4.in()};

		RouterTable rt4 = new RouterTable();

		rt4.addRoute(new Route("10.0.1.1", 2 , routerIPports3[2]));
		rt4.addRoute(new Route("10.0.2.1", 1 , routerIPports3[1]));
		rt4.addRoute(new Route("10.0.3.1", 2 , routerIPports3[2]));
		rt4.addRoute(new Route("10.0.4.1", 0 , routerIPports3[0]));
		rt4.addRoute(new Route("10.0.20.1",1 , "0.0.0.0"));
		rt4.addRoute(new Route("10.0.40.1",2 , "0.0.0.0"));
				
		CSProcess router4 = new Router("Roter R4", macRouter4,routerIns4 , routerOuts4, rt4,routerIPports4);
		
		// Instanciacao da console de comandos e associacao dos nodos com seus
		// canais de comunicacao.
		NodeChannelWrapper[] nodes = { new NodeChannelWrapper(node0, cons0.out()),new NodeChannelWrapper(node1, cons1.out()),new NodeChannelWrapper(node2, cons2.out()),new NodeChannelWrapper(node3, cons3.out())};
		CSProcess console = new CommandConsole(nodes);

		// Execucao dos processos em paralelo.
		CSProcess[] processes = { node0, node1, node2,node3 ,router,router2,router3,router4, console, };
		CSProcess simulator = new Parallel(processes);
		simulator.run();
	}
}
