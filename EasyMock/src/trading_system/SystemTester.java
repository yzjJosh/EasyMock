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
		System.out.println(tester.testMarketValue());
	}

	public boolean testMarketValue() {
		// TODO Auto-generated method stub
		List<Commodity> stocks = new ArrayList<>();
		Commodity googleStock = new Commodity("1", "Google", 10);
		Commodity microsoftStock = new Commodity("2", "Microsoft", 100);
		
		stocks.add(googleStock);
		stocks.add(microsoftStock);
		tradingSystem.setStocks(stocks);

		EasyMock.expect(tradingService.getPrice(googleStock)).setReturn(50.00);
		EasyMock.expect(tradingService.getPrice(microsoftStock)).setReturn(1000.00);
		
		EasyMock.replay(tradingService);
		double marketValue = tradingSystem.getMarketValue();
		return marketValue == 100500.0;
	}

	public void setUp() {
		// TODO Auto-generated method stub
		tradingSystem = new TradingSystem();
		tradingService = (TradingService) EasyMock.createMock(TradingService.class);
		tradingSystem.setStockService(tradingService);
		EasyMock.record(tradingService);
	}
}
