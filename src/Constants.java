/**
 * Classe para representar as constantes utilizadas no simulador.
 * 
 * @author Tiago Ferreto
 * @author Ramon Costi Fernandes <ramon.fernandes@acad.pucrs.br>
 */
public class Constants {

	// Endereco MAC para broadcast.
	public static final String macBcast = "FF:FF:FF:FF:FF:FF";

	// Comando SENDMSG
	public static final int sendMsgCmd = 0;

	// Comando PING
	public static final int pingCmd = 1;

	// Comando TRACERT
	public static final int tracertCmd = 2;

	// Lista com os possiveis comandos a serem executados
	public static final Commands[] cmds = { Commands.SENDMSG};

	// Tempo de sleep para os prints (em milisegundos)
	public static final int sleepTime = 250;
}
