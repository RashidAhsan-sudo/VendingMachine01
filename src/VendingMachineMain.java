import java.util.Optional;
import java.util.Scanner;

public class VendingMachineMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		VendingMachineImp vendingMachine = new VendingMachineImp();
		

		try {
			Scanner sc= new Scanner(System.in);
			System.out.print("Select the product among BISCUITS , CHOCOLATE, CHIPS  ");  
			String inputProduct = sc.nextLine();
			ProductDetails product = new ProductDetails(inputProduct);
			int itemPrice = vendingMachine.getSelectedItemPrice(product);
			System.out.println("Selected Item:" + product.getItemName());
			System.out.println("Selected Item Price:" + itemPrice);
			System.out.print("Enter the amount in 5, 10, 20 coins");
			int moneyPaid = sc.nextInt();
			if (itemPrice != 0) {
				ECoin insertedCoin[] = new ECoin[1];
				switch (moneyPaid) {
				case 5:
					insertedCoin[0] = ECoin.FIVE;
					break;
				case 10:
					insertedCoin[0] = ECoin.TEN;
					break;
				case 20:
					insertedCoin[0] = ECoin.TWENTY;
					break;
				default:
					break;
				}				
				Optional<Bucket> bucket = vendingMachine.insertCoin(insertedCoin);
				vendingMachine.displayInsertedCoinValue(insertedCoin);
				if (bucket.isPresent()) {
					Bucket itemBucket = bucket.get();
					if (itemBucket.getDetails() != null) {
						System.out.println("Return Item: " + itemBucket.getDetails().getItemName());
						System.out.println("Item Price: " + itemBucket.getDetails().getItemCost());

					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
