import org.jcsp.lang.ChannelOutput;


public class MTUOut {
	private int mtu;
	// Canais de saida do Hub
	private ChannelOutput out;
	public MTUOut(int mtu, ChannelOutput out) {
		super();
		this.mtu = mtu;
		this.out = out;
	}
	public int getMtu() {
		return mtu;
	}
	public ChannelOutput getOut() {
		return out;
	}
	
	public void write(Packet pkt, int sizeMsgFinal){
		if(pkt.data.length() > mtu){
			
			String dataAux=pkt.data.substring(0,mtu);
			String dataAux2 = pkt.data.substring(mtu,pkt.data.length());
			
			//Seta como fragmentado
			
			Packet aux = new Packet (pkt.dstAddr, pkt.srcAddr,dataAux ,sizeMsgFinal);
			Packet aux2 = new Packet(pkt.dstAddr, pkt.srcAddr,dataAux2,sizeMsgFinal);
			
			aux.setMoreFragment(true);
			aux2.setMoreFragment(true);
			
			System.out.println("Fragmentando mensagem: ");
			System.out.println("pacote 1 "+ aux.data);
			System.out.println("pacote 2 "+ aux2.data);
			System.out.println("Enviando pacotes");
			out.write(aux);
			this.write(aux2,sizeMsgFinal);
		}else{
			pkt.setMoreFragment(false);
			System.out.println("Enviando pacote");
			out.write(pkt);
		}
	}
	
}
