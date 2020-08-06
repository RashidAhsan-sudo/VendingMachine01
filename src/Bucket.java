import java.util.List;

public class Bucket {
	
	private ProductDetails details;
	private List<ECoin> coin;	
	
	public Bucket(ProductDetails details, List<ECoin> coin) {
		super();
		this.details = details;
		this.coin = coin;
	}
	public ProductDetails getDetails() {
		return details;
	}
	public void setDetails(ProductDetails details) {
		this.details = details;
	}
	public List<ECoin> getCoin() {
		return coin;
	}
	public void setCoin(List<ECoin> coin) {
		this.coin = coin;
	}
	
	

}
