
public enum ECoin {
	FIVE(5), TEN(10), TWENTY(20);

	private int coinValue;

	ECoin(int i) {
		this.coinValue = i;
	}

	public int getCoinValue() {
		return this.coinValue;
	}
}
