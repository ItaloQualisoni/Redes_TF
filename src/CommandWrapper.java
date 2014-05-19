/**
 * Classe para encapsular um comando e seus parametros.
 * 
 * @author Tiago Ferreto
 * @author Ramon Costi Fernandes <ramon.fernandes@acad.pucrs.br>
 */
public class CommandWrapper {

	// Enum {@link ConsoleCommands} com o comando desejado.
	public Commands cmd;

	// Endereco MAC destino (parametro comum a todos os comandos).
	public String dstMacAddr;

	// Parametros quaisquer do comando.
	public String param;

	/**
	 * Cria um objeto para enpcasular comandos interpretaveis pelos nodos.
	 * 
	 * @param cmd {@link Commands} Objeto com o comando a ser atribuido neste
	 *            objeto.
	 * @param macDst Endereco MAC destino para onde este comando deve ser
	 *            direcionado.
	 * @param param Parametros diversos adicionais aos comandos.
	 */
	public CommandWrapper(Commands cmd, String macDst, String param) {
		super();
		this.cmd = cmd;
		this.dstMacAddr = macDst;
		this.param = param;
	}

}
