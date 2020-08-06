import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

public class VendingMachineImp implements IVendingMachine {

	private Inventory<ProductDetails, Integer> itemInventory = new Inventory<ProductDetails, Integer>();
	private Inventory<ECoin, Integer> cashInventory = new Inventory<ECoin, Integer>();
	private ProductDetails currentItem;
	private int currentBalance;
	
	public VendingMachineImp() {
		initialize();
	}

	private void initialize() {
		itemInventory.putInventory(new ProductDetails("CHOCOLATE", 50), new Integer(10));
		itemInventory.putInventory(new ProductDetails("BISCUITS", 20), 10);
		itemInventory.putInventory(new ProductDetails("CHIPS", 10), 10);
		this.setCurrentBalance();
	}

	@Override
	public int getSelectedItemPrice(ProductDetails details) throws Exception {
		List<Entry<ProductDetails, Integer>> productPrice = this.itemInventory.getInvetory().entrySet().stream()
				.filter(e -> e.getKey().getItemName().equals(details.getItemName())).collect(Collectors.toList());
		if (!productPrice.isEmpty()) {
			ProductDetails selectedProduct = productPrice.get(0).getKey();
			this.currentItem = selectedProduct;
			return (int) selectedProduct.getItemCost();
		} else {
			throw new Exception("Product Not available");
		}
	}

	public void displayInsertedCoinValue(ECoin... coins) {
		Optional<Integer> insertedCoinValue = Arrays.asList(coins).stream().map(e -> e.getCoinValue())
				.collect(Collectors.toList()).stream().reduce(Integer::sum);
		int insertedValue = insertedCoinValue.get().intValue();
		System.out.println("Inserted coin value is " + insertedValue);
	}

	@Override
	public Optional<Bucket> insertCoin(ECoin... coins) throws Exception {
		Bucket bucket = null;
		if (currentItem != null) {
			Optional<Integer> insertedCoinValue = Arrays.asList(coins).stream().map(e -> e.getCoinValue())
					.collect(Collectors.toList()).stream().reduce(Integer::sum);
			int insertedValue = insertedCoinValue.get().intValue();
			if (insertedValue < this.currentItem.getItemCost()) {
				bucket = new Bucket(new ProductDetails("Amount is not full paid"), Arrays.asList(coins));
			} else {
				try {
					bucket = this.getItemsandChange(insertedValue);
				} catch (RuntimeException e1) {
					throw new RuntimeException("Change is not present, Please provide change or cancel the product");
					
				}
			}
		} else {
			throw new Exception("Item is not selected");
		}

		Bucket returnBucket = bucket == null ? new Bucket(new ProductDetails("Item is not found"), Arrays.asList(coins))
				: bucket;
		Optional<Bucket> opt = Optional.ofNullable(returnBucket);
		return opt;
	}

	@Override
	public Bucket getItemsandChange(int coin) {
		 // TODO Auto-generated method stub
		 this.addToCashInventory(coin);
		 this.setCurrentBalance();
		 int changedValue = this.getChange(coin, (int) this.currentItem.getItemCost());
		 this.substractChangedFromInventory(changedValue);
		 this.currentBalance = this.currentBalance-changedValue;
		 this.removedItemFromInventory();
		 ArrayList<ECoin> coins = new ArrayList<ECoin>();		 
		 return new Bucket(this.currentItem, this.convertToCoin(new ArrayList<ECoin>(),changedValue));
	}
	
	private void addToCashInventory(int insertedCoinValue) {
		if (insertedCoinValue >= ECoin.TWENTY.getCoinValue()) {
			int balance = this.putCoinAndIncreament(ECoin.TWENTY, insertedCoinValue);
			if (balance != 0) {
				addToCashInventory(balance);
			}
		}
	}
		 
	private int putCoinAndIncreament(ECoin coin, int insertedCoinValue) {
		int remainder = insertedCoinValue / coin.getCoinValue();
		int numberOfCoin = this.cashInventory.getInvetory().get(coin);
		numberOfCoin = remainder + numberOfCoin;
		this.cashInventory.getInvetory().put(coin, numberOfCoin);
		int balance = insertedCoinValue - (remainder * coin.getCoinValue());
		return balance;
	}
	
	void setCurrentBalance() {
		if (this.cashInventory.getInvetory().size() > 0) {
			List<Integer> cashCoinList = this.cashInventory.getInvetory().entrySet().stream()
					.map(e -> e.getKey().getCoinValue() * e.getValue()).collect(Collectors.toList());
			Optional<Integer> currentBalance = cashCoinList.stream().reduce(Integer::sum);
			this.currentBalance = currentBalance.get().intValue();
		}
	}
	
	private int getChange(int insertedValue, int itemPrice) {
		if (insertedValue > itemPrice) {
			return insertedValue - itemPrice;
		} else {
			return itemPrice - insertedValue;
		}
	}
	
	private void substractChangedFromInventory(int changedValue) {
		int reminder = 0;
		if (changedValue >= ECoin.TWENTY.getCoinValue()) {
			int balance = this.putCoinAndDecrement(ECoin.TWENTY, changedValue);
			if (balance != 0) {
				substractChangedFromInventory(balance);
			}
		} else if (changedValue >= ECoin.TEN.getCoinValue()) {
			int balance = this.putCoinAndDecrement(ECoin.TEN, changedValue);
			if (balance != 0) {
				substractChangedFromInventory(balance);
			}
		} else if (changedValue >= ECoin.FIVE.getCoinValue()) {
			int balance = this.putCoinAndDecrement(ECoin.FIVE, changedValue);
			if (balance != 0) {
				substractChangedFromInventory(balance);
			}
		}
	}
	
	private int putCoinAndDecrement(ECoin coin, int changedValue) {
		int reminder = changedValue / coin.getCoinValue();
		int numberOfCoin = this.cashInventory.getInvetory().get(coin);
		if (numberOfCoin > reminder)
			numberOfCoin = numberOfCoin - reminder;
		this.cashInventory.getInvetory().put(coin, numberOfCoin);
		int balance = changedValue - (reminder * coin.getCoinValue());
		return balance;
	}
	
	private void removedItemFromInventory() {
		int itemCount = this.itemInventory.getInvetory().get(currentItem);
		this.itemInventory.getInvetory().put(currentItem, itemCount - 1);
	}
	
	private List<ECoin> convertToCoin(List<ECoin> returnCoinsArray, int changedValue) {
		int reminder = 0;
		if (changedValue >= ECoin.TWENTY.getCoinValue()) {
			reminder = changedValue / ECoin.TWENTY.getCoinValue();
			if (reminder > 0) {
				for (int i = 0; i <= reminder - 1; i++) {
					returnCoinsArray.add(ECoin.TWENTY);
				}
			}
			int balance = changedValue - (reminder * ECoin.TWENTY.getCoinValue());
			if (balance != 0) {
				convertToCoin(returnCoinsArray, balance);
			}
		} else if (changedValue >= ECoin.TEN.getCoinValue()) {
			reminder = changedValue / ECoin.TEN.getCoinValue();
			if (reminder > 0) {
				for (int i = 0; i <= reminder - 1; i++) {
					returnCoinsArray.add(ECoin.TEN);
				}
			}
			int balance = changedValue - (reminder * ECoin.TEN.getCoinValue());
			if (balance != 0) {
				convertToCoin(returnCoinsArray, balance);
			}
		} else if (changedValue >= ECoin.FIVE.getCoinValue()) {
			reminder = changedValue / ECoin.FIVE.getCoinValue();
			if (reminder > 0) {
				for (int i = 0; i <= reminder - 1; i++) {
					returnCoinsArray.add(ECoin.FIVE);
				}
			}
			int test = changedValue - (reminder * ECoin.FIVE.getCoinValue());
			if (test != 0) {
				convertToCoin(returnCoinsArray, test);
			}
		}
		return returnCoinsArray;

	}
	
	
	
	
	

	

}
