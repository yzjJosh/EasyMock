package trading_system;

public class Commodity {
	private String commodityId;
	private String name;
	private int quantity;
	
	public Commodity(String commodityId, String name, int quantity) {
		this.commodityId = commodityId;
		this.name = name;
		this.quantity = quantity;
	}
	
	public String getCommodityId() {
		return commodityId;
	}
	
	public void setCommodityId(String commodityId) {
		this.commodityId = commodityId;
	}
	
	public int getQuantity() {
		return quantity;
	}
	
	public String getTicker() {
		return name;
	}
}
