package trading_system;

import java.util.ArrayList;
import java.util.List;
import easymock.*;
/*
 * SystemTester
 * This class is intended to mock the trading system service 
 * and test the easymock function modules.
 */
public class SystemTester {
	
	TradingSystem tradingSystem;
	//The trading service is to be mocked.
	TradingService tradingService;

	/*
	 * Test the whole value of the commodities in the trading system.
	 * @return if the mocking value is correct.
	 */
	public boolean testCommodityValue() {
		List<Commodity> commodities = new ArrayList<>();
		Commodity fish = new Commodity("1", "Fish", 10);
		Commodity beef = new Commodity("2", "Beef", 100);
		
		commodities.add(fish);
		commodities.add(beef);
		tradingSystem.setCommodities(commodities);

		EasyMock.expect(tradingService.getPrice(fish)).setReturn(50.00);
		EasyMock.expect(tradingService.getPrice(beef)).setReturn(1000.00);
		
		EasyMock.replay(tradingService);
		double marketValue = tradingSystem.getCommodityValue();
		return marketValue == 100500.0;
	}

	/*
	 * Set up the mocking service.
	 */
	public void setUp() {
		tradingSystem = new TradingSystem();
		tradingService = (TradingService) EasyMock.createMock(TradingService.class);
		tradingSystem.setTradingService(tradingService);
		EasyMock.record(tradingService);
	}
	
	
	public static void main(String[] args) {
		SystemTester tester = new SystemTester();
		tester.setUp();
		System.out.println(tester.testCommodityValue());
	}
}
