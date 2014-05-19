import org.jcsp.lang.CSProcess;
import org.jcsp.lang.ChannelOutput;

/**
 * Classe para associar um nodo ao seu canal de comunicacao, utilizado pela
 * console de comandos.
 * 
 * @author Tiago Ferreto
 * @author Ramon Costi Fernandes <ramon.fernandes@acad.pucrs.br>
 */
public class NodeChannelWrapper {

	// Referencia para o objeto {@link Node} correspondente.
	public CSProcess node;

	// Canal ao qual o nodo esta associado para comunicacao com a console de
	// comando.
	public ChannelOutput out;

	/**
	 * Cria um objeto para encapsular um objeto {@link Node} com seu respectivo
	 * canal de comunicacao.
	 * 
	 * @param node0 {@link Node} associado a este objeto.
	 * @param out Canal de comunicacao utilizado para comunicacao da console com
	 *            este nodo.
	 */
	public NodeChannelWrapper(CSProcess node0, ChannelOutput out) {
		this.node = node0;
		this.out = out;
	}

}
