package trading_system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.lang.reflect.Proxy;

import exceptions.CustomedException;
import exceptions.IllegalTypeException;
import easymock.*;
import sun.jvm.hotspot.utilities.Assert;

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
		Map<Commodity,Integer> commodities = new HashMap<>();
		Commodity fish = new Commodity("1", "Fish");
		Commodity beef = new Commodity("2", "Beef");
		commodities.put(fish,200);
		commodities.put(beef,300);
		tradingSystem.setCommodities(commodities);



		EasyMock.expect(tradingService.getPrice(beef,"mean")).setReturn(800.00);
		EasyMock.expect(tradingService.getPrice(fish,"mean")).setReturn(40.00);
		EasyMock.expect(tradingService.getPrice(fish,"buying")).setReturn(30.00);
		EasyMock.expect(tradingService.getPrice(beef,"buying")).setReturn(600.00);
		EasyMock.expect(tradingService.getPrice(fish,"selling")).setReturn(50.00);
		EasyMock.expect(tradingService.getPrice(beef,"selling")).setReturn(1000.00);
//		EasyMock.expect(tradingService.getPrice(fish)).setReturn(50.00);
//		EasyMock.startBranch(tradingService);
//			EasyMock.startBranch(tradingService);
//				EasyMock.startBranch(tradingService);
//					EasyMock.expect(tradingService.getPrice())
////		EasyMock.expect(tradingService.getPrice(fish)).setReturn(40.00);
////		EasyMock.expect(tradingService.getPrice(fish)).setReturn(30.00);
////		EasyMock.expect(tradingService.getPrice(beef)).setReturn(1000.00);
////		EasyMock.expect(tradingService.getPrice(beef)).setReturn(800.00);
//		EasyMock.expect(tradingService.getPrice(beef)).setReturn(600.00);



		EasyMock.replay(tradingService);

		double marketValue = tradingSystem.getCommodityValue();
		if(marketValue!= (200*40.00+300*800.00) )return false;
		double val1 = tradingSystem.addCommodity(fish,400);
		if(val1!=400*30.00) return false;
		double val2 = tradingSystem.addCommodity(beef,400);
		if(val2!=400*600.0) return false;
		if(tradingSystem.getCommodities().get(fish)!=600) return false;
		if(tradingSystem.getCommodities().get(beef)!=700) return false;
		double val3 = tradingSystem.sellCommodity(fish,500);
		if(val3!=500*50.00) return false;

		return true;
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
