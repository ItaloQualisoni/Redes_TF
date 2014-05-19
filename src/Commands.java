/**
 * Enum para representar os possiveis comandos executados nos nodos do
 * simulador.
 * 
 * @author Tiago Ferreto
 * @author Ramon Costi Fernandes <ramon.fernandes@acad.pucrs.br>
 */
public enum Commands {

	SENDMSG(Constants.sendMsgCmd), PING(Constants.pingCmd), TRACERT(Constants.tracertCmd);

	private int value;

	private Commands(int value) {
		this.value = value;
	}

	/**
	 * Retorna o value numerico correspondendo ao comando.
	 * 
	 * @return Retorna um inteiro com o valor associado ao comando.
	 */
	public int getValue() {
		return value;
	}

}
