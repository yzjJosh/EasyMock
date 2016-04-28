package trading_system;



import exceptions.CustomedException;


import java.util.Map;

public class TradingSystem {
	private TradingService tradingService;
	private Map<Commodity,Integer> inventory;
	
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
	public Map<Commodity,Integer> getCommodities() {
		return inventory;
	}
	
	/*
	 * Set the commodities.
	 */
	public void setCommodities(Map<Commodity,Integer> commodities) {
		this.inventory = commodities;
	}
	
	/*
	 * Get the whole value of the commodities.
	 * @return the whole value.
	 */
	public double getCommodityValue() {
		double value = 0.0;

			for (Commodity commodity : inventory.keySet()) {
				value += tradingService.getPrice(commodity,"mean") * inventory.get(commodity);
			}


		return value;
	}

	public double addCommodity(Commodity commodity,int quantity){
		double val = 0.0;
		if(!inventory.containsKey(commodity)){
			inventory.put(commodity,quantity);
		}else{
			inventory.put(commodity,inventory.get(commodity)+quantity);

		}
		val = tradingService.getPrice(commodity,"buying")*quantity;


		return val;
	}

	public double sellCommodity(Commodity commodity,int quantitiy ){
		double value = 0.0;
		if(quantitiy>inventory.get(commodity))
			throw new IllegalArgumentException("We only have"+inventory.get(commodity)+ " "+commodity.getTicker());
		else{
			//try{
				value = tradingService.getPrice(commodity,"selling")*quantitiy;
					inventory.put(commodity,inventory.get(commodity)-quantitiy);
			//}catch (CustomedException e){

			//	System.out.println(commodity.getTicker()+" is not on the market for now.");
			//}

		}
		return value;
	}
}
