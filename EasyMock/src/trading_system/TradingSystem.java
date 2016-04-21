package trading_system;

import java.util.List;

public class TradingSystem {
	private TradingService tradingService;
	private List<Commodity> commodities;
	
	public TradingService getStockService() {
		return tradingService;
	}
	
	public void setStockService(TradingService tradingService) {
		this.tradingService = tradingService;
	}
	
	public List getStocks() {
		return commodities;
	}
	
	public void setStocks(List commodities) {
		this.commodities = commodities;
	}
	
	public double getMarketValue() {
		double marketValue = 0.0;
		
		for (Commodity commodity : commodities) {
			marketValue += tradingService.getPrice(commodity) * commodity.getQuantity();
		}
		return marketValue;
	}
}
