import java.util.Optional;

public interface IVendingMachine {
	public int getSelectedItemPrice(ProductDetails details) throws Exception;
	public Optional<Bucket> insertCoin(ECoin ...coin) throws Exception;
	public Bucket getItemsandChange(int coin);
}
