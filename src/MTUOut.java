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
	
	public void write(Packet pkt){
		if(pkt.data.length() > this.mtu){
			String dataAux  = pkt.data.substring(0,this.mtu);
			String dataAux2 = pkt.data.substring(mtu,pkt.data.length());
			
			//Fragmentação
			System.out.println("Fragmentando mensagem: ");
			Packet aux = new Packet (pkt.dstAddr, pkt.srcAddr,dataAux ,pkt.sizeFinal,pkt.offset);
			Packet aux2 = new Packet(pkt.dstAddr, pkt.srcAddr,dataAux2,pkt.sizeFinal,pkt.offset+this.mtu);
			
			if (pkt.sizeFinal == pkt.data.length()||pkt.lastFragment == true) {
				aux2.setLastFragment(true);
			}	
			
			System.out.println("Enviando pacote 1: "+ aux.data);
			System.out.println("Empilhando pacote 2: "+ aux2.data);
			out.write(aux);
			this.write(aux2);
		}else{
			if (pkt.sizeFinal == pkt.data.length()) {
				pkt.setLastFragment(true);
			}
			System.out.println("\nEnviando pacote : " + pkt.data);
			out.write(pkt);
		}
	}
	
}
