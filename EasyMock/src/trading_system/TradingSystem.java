package trading_system;

import java.util.List;

public class TradingSystem {
	private TradingService tradingService;
	private List<Commodity> commodities;
	
	/*
	 * Get the trading service.
	 * @return the service the system provides.
	 */
	public TradingService getTradingService() {
		return tradingService;
	}
	/*
	 * Set the service the system provides.
	 */
	public void setTradingService(TradingService tradingService) {
		this.tradingService = tradingService;
	}
	
	/*
	 * Get the trading commodities.
	 * @return the commodities the system provides.
	 */
	public List<Commodity> getCommodities() {
		return commodities;
	}
	
	/*
	 * Set the commodities.
	 */
	public void setCommodities(List<Commodity> commodities) {
		this.commodities = commodities;
	}
	
	/*
	 * Get the whole value of the commodities.
	 * @return the whole value.
	 */
	public double getCommodityValue() {
		double value = 0.0;
		
		for (Commodity commodity : commodities) {
			value += tradingService.getPrice(commodity) * commodity.getQuantity();
		}
		return value;
	}
}
