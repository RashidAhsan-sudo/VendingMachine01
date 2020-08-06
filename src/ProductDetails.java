
public class ProductDetails {
	
	public String itemName;
	public int itemCost;
	
	public ProductDetails(String itemName, int itemCost) {
		super();
		this.itemName = itemName;
		this.itemCost = itemCost;
	}	
	
	public ProductDetails(String itemName) {
		super();
		this.itemName = itemName;
	}

	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public int getItemCost() {
		return itemCost;
	}
	public void setItemCost(int itemCost) {
		this.itemCost = itemCost;
	}
	
	
}
