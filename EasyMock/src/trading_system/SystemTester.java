package trading_system;

import java.util.ArrayList;
import java.util.List;
import easymock.*;
public class SystemTester {

	TradingSystem tradingSystem;
	TradingService tradingService;
	
	public static void main(String[] args) {
		SystemTester tester = new SystemTester();
		tester.setUp();
		System.out.println(tester.testCommodityValue());
	}

	/*
	 * test the whole value of the commodities.
	 * @return if the mocking value is correct.
	 */
	public boolean testCommodityValue() {
		List<Commodity> commodities = new ArrayList<>();
		Commodity fish = new Commodity("1", "Fish", 10);
		Commodity beef = new Commodity("2", "Beef", 100);
		
		commodities.add(fish);
		commodities.add(beef);
		tradingSystem.setStocks(commodities);

		EasyMock.expect(tradingService.getPrice(fish)).setReturn(50.00);
		EasyMock.expect(tradingService.getPrice(beef)).setReturn(1000.00);
		
		EasyMock.replay(tradingService);
		double marketValue = tradingSystem.getMarketValue();
		return marketValue == 100500.0;
	}

	/*
	 * Set up the mocking service.
	 */
	public void setUp() {
		tradingSystem = new TradingSystem();
		tradingService = (TradingService) EasyMock.createMock(TradingService.class);
		tradingSystem.setStockService(tradingService);
		EasyMock.record(tradingService);
	}
}
