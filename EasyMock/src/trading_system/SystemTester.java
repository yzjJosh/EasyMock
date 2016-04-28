package trading_system;

//import easymock.EasyMock;
import static org.easymock.EasyMock.*;

import easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import exceptions.CustomedException;
import exceptions.IllegalTypeException;
import easymock.*;

/**
 * Created by wenwen on 4/27/16.
 */
public class SystemTester{


//	@Test
//	public void testAsWhole(){
//		Map<Commodity,Integer> commodities = new HashMap<>();
//		Commodity fish = new Commodity("1", "Fish");
//		Commodity beef = new Commodity("2", "Beef");
//		commodities.put(fish,200);
//		commodities.put(beef,300);
//		tradingSystem.setCommodities(commodities);
//
//
//		EasyMock.expect(tradingService.getPrice(beef,"mean")).setReturn(800.00);
//		EasyMock.expect(tradingService.getPrice(fish,"mean")).setReturn(40.00);
//
//		//EasyMock.startBranch(tradingService);
//		EasyMock.startBranch(tradingService);
//		EasyMock.startBranch(tradingService);
//		EasyMock.startBranch(tradingService);
//		EasyMock.expect(tradingService.getPrice(beef,"selling")).setReturn(1000.00);
//
//		EasyMock.switchBranch(tradingService);
//		EasyMock.expect(tradingService.getPrice(fish,"selling")).setReturn(80.00);
//		EasyMock.endBranch(tradingService);
//		EasyMock.switchBranch(tradingService);
//		EasyMock.expect(tradingService.getPrice(beef,"buying")).setReturn(600.00);
//		EasyMock.endBranch(tradingService);
//		EasyMock.switchBranch(tradingService);
//		EasyMock.expect(tradingService.getPrice(fish,"buying")).setReturn(80.00);
//		EasyMock.endBranch(tradingService);
//		//EasyMock.endBranch(tradingService);
//
//		//EasyMock.startBranch(tradingService);
//		EasyMock.startBranch(tradingService);
//		EasyMock.startBranch(tradingService);
//		EasyMock.startBranch(tradingService);
//		EasyMock.expect(tradingService.getPrice(beef,"selling")).setReturn(1000.00);
//		EasyMock.switchBranch(tradingService);
//		EasyMock.expect(tradingService.getPrice(fish,"selling")).setReturn(80.00);
//		EasyMock.endBranch(tradingService);
//		EasyMock.switchBranch(tradingService);
//		EasyMock.expect(tradingService.getPrice(beef,"buying")).setReturn(600.00);
//		EasyMock.endBranch(tradingService);
//		EasyMock.switchBranch(tradingService);
//		EasyMock.expect(tradingService.getPrice(fish,"buying")).setReturn(80.00);
//		EasyMock.endBranch(tradingService);
//		//EasyMock.endBranch(tradingService);
//
//		EasyMock.replay(tradingService);// start real testing!!!
//
//		// test1
//		assertEquals(tradingSystem.getCommodityValue(), 200 * 40.00 + 300 * 800.00, 0.00);
//		tradingSystem.sellCommodity(fish,100);
//		tradingSystem.addCommodity(beef,100);
//		//EasyMock.replay(tradingService);
//
//
//	}


}