import java.util.HashMap;

public class Inventory<T1, T2> {
	private HashMap<T1,T2> inventory = new HashMap<T1,T2>();
	
	public boolean hasItem(ProductDetails product) {		  
		  return inventory.containsKey(product) ;
		 
		 }
		public HashMap<T1,T2> getInvetory() {
		  return inventory;
		 }
		 
		 public void putInventory(T1 t1, T2 t2){
		  this.inventory.put(t1, t2);
		 }
}
