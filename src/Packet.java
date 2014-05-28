/**
 * Classe que define um pacote do trabalho de redes, utilizado para a
 * comunicacao entre processos.
 * 
 * @author Tiago Ferreto
 * @author Ramon Costi Fernandes <ramon.fernandes@acad.pucrs.br>
 */
public class Packet {

	public String dstAddr;
	public String srcAddr;
	public String data;
	public boolean lastFragment = false;
	public int offset = 0 ;
	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int sizeFinal;
	

	/**
	 * Cria um pacote com o endereco MAC destino e destino, assim como os dados
	 * que devem ser enviados.
	 * 
	 * @param dstMacAddr Endereco MAC destino a ser preenchido no pacote.
	 * @param srcMacAddr Endereco MAC origem a ser preenchido no pacote.
	 * @param data Dados que devem ser encapsulados no pacote.
	 */
	public Packet(String dstAddr, String srcAddr, String data, int size,int offset) {
		super();
		this.offset = offset;
		this.dstAddr = dstAddr;
		this.srcAddr = srcAddr;
		this.data = data;
		this.sizeFinal=size;
	}

	@Override
	public String toString() {
		return "Packet [dstMacAddr=" + dstAddr + ", srcMacAddr=" + srcAddr + ", data=" + data + "]";
	}
	public void setLastFragment(boolean lastFragment){
		this.lastFragment = lastFragment;
	}

	public boolean lastFragment() {
		return lastFragment;
	}
	

}
