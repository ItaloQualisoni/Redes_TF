import java.util.ArrayList;

public class RouterTable {

	ArrayList<Route> table;
	
	public RouterTable(ArrayList<Route> tableRouter) {
		// TODO Auto-generated constructor stub
		this.table = tableRouter;
	}
	public RouterTable() {
		// TODO Auto-generated constructor stub
		table = new ArrayList<Route>();
	}
	
	/*Adiciona uma nova rota*/
	public void addRoute(Route rot){
		table.add(rot);
	}
	
	public Route getRoute(String source){
		for(Route rot:table){
			if(rot.getSource().equalsIgnoreCase(source)){
				return rot;
			}
		}
		System.out.println("Nao foi achado uma rota para "+source);
		return null;
	}
	
}




