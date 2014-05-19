/**
 * Classe que define um pacote do trabalho de redes, utilizado para a
 * comunicacao entre processos.
 * 
 * @author Tiago Ferreto
 * @author Ramon Costi Fernandes <ramon.fernandes@acad.pucrs.br>
 */
public class Packet {

	public String dstMacAddr;
	public String srcMacAddr;
	public String data;

	/**
	 * Cria um pacote com o endereco MAC destino e destino, assim como os dados
	 * que devem ser enviados.
	 * 
	 * @param dstMacAddr Endereco MAC destino a ser preenchido no pacote.
	 * @param srcMacAddr Endereco MAC origem a ser preenchido no pacote.
	 * @param data Dados que devem ser encapsulados no pacote.
	 */
	public Packet(String dstMacAddr, String srcMacAddr, String data) {
		super();
		this.dstMacAddr = dstMacAddr;
		this.srcMacAddr = srcMacAddr;
		this.data = data;
	}

	@Override
	public String toString() {
		return "Packet [dstMacAddr=" + dstMacAddr + ", srcMacAddr=" + srcMacAddr + ", data=" + data + "]";
	}

}
