package trading_system;

//import easymock.EasyMock;
import static org.easymock.EasyMock.*;

import easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by wenwen on 4/27/16.
 */
public class TradingSystemTest {
    private TradingSystem tradingSystem;
    private TradingService tradingService;


    @Before
    public void setup(){
        tradingSystem = new TradingSystem();
        tradingService = (TradingService) EasyMock.createMock(TradingService.class);
        tradingSystem.setTradingService(tradingService);
        EasyMock.record(tradingService);
    }
//    @Test
//    public void testCommodityValue(){
//        Map<Commodity,Integer> commodities = new HashMap<>();
//        Commodity fish = new Commodity("1", "Fish");
//        Commodity beef = new Commodity("2", "Beef");
//        commodities.put(fish,200);
//        commodities.put(beef,300);
//        tradingSystem.setCommodities(commodities);
//
//        EasyMock.expect(tradingService.getPrice(beef,"mean")).setReturn(800.00);
//        EasyMock.expect(tradingService.getPrice(fish,"mean")).setReturn(40.00);
//        EasyMock.replay(tradingService);
//
//        assertEquals(tradingSystem.getCommodityValue(), 200 * 40.00 + 300 * 800.00, 0.00);
//    }
//
//    @Test
//    public void testSellingFunction1(){
//
//        Map<Commodity,Integer> commodities = new HashMap<>();
//        Commodity fish = new Commodity("1", "Fish");
//        Commodity beef = new Commodity("2", "Beef");
//        commodities.put(fish,200);
//        commodities.put(beef,300);
//        tradingSystem.setCommodities(commodities);
//
//        EasyMock.expect(tradingService.getPrice(beef,"selling")).setReturn(1000.00);
//        EasyMock.expect(tradingService.getPrice(fish,"selling")).setReturn(60.00);
//        EasyMock.replay(tradingService);
//
//
//        assertEquals(tradingSystem.sellCommodity(beef,100),1000.00*100,0.00);
//        assertEquals(tradingSystem.sellCommodity(fish,100),60.00*100,0.00);
//        assertEquals(300 - 100, (int) tradingSystem.getCommodities().get(beef));
//        assertEquals(200 - 100, (int) tradingSystem.getCommodities().get(fish));
//    }
//
//    @Test
//    public void testBuyingFunction1(){
//
//        Map<Commodity,Integer> commodities = new HashMap<>();
//        Commodity fish = new Commodity("1", "Fish");
//        Commodity beef = new Commodity("2", "Beef");
//        commodities.put(fish,200);
//        commodities.put(beef,300);
//        tradingSystem.setCommodities(commodities);
//
//        EasyMock.expect(tradingService.getPrice(beef,"buying")).setReturn(600.00);
//        EasyMock.expect(tradingService.getPrice(fish,"buying")).setReturn(40.00);
//        EasyMock.replay(tradingService);
//
//
//        assertEquals(tradingSystem.addCommodity(beef,100),600.00*100,0.00);
//        assertEquals(300+100,(int)tradingSystem.getCommodities().get(beef));
//
//        assertEquals(tradingSystem.addCommodity(fish,100),40.00*100,0.00);
//        assertEquals(200+100,(int)tradingSystem.getCommodities().get(fish));
//    }

    @Test
    public void testAsWhole(){
        Map<Commodity,Integer> commodities = new HashMap<>();
        Commodity fish = new Commodity("1", "Fish");
        Commodity beef = new Commodity("2", "Beef");
        commodities.put(fish,200);
        commodities.put(beef,300);
        tradingSystem.setCommodities(commodities);

        EasyMock.record(tradingService);
        EasyMock.expect(tradingService.getPrice(beef,"mean")).setReturn(800.00).setPrint("evaluate beef");
        EasyMock.expect(tradingService.getPrice(fish,"mean")).setReturn(40.00).setPrint("evaluate fish");

       // EasyMock.startBranch(tradingService);
        EasyMock.startBranch(tradingService);
            EasyMock.startBranch(tradingService);
                EasyMock.startBranch(tradingService);
                    EasyMock.expect(tradingService.getPrice(beef,"selling")).setReturn(1000.00).setPrint("sell beef");

                EasyMock.switchBranch(tradingService);
                    EasyMock.expect(tradingService.getPrice(fish,"selling")).setReturn(80.00).setPrint("sell fish");
                EasyMock.endBranch(tradingService);
            EasyMock.switchBranch(tradingService);
                EasyMock.expect(tradingService.getPrice(beef,"buying")).setReturn(600.00).setPrint("buy beef");
            EasyMock.endBranch(tradingService);
        EasyMock.switchBranch(tradingService);
                EasyMock.expect(tradingService.getPrice(fish,"buying")).setReturn(80.00).setPrint("buy fish");
        EasyMock.endBranch(tradingService);
      //  EasyMock.endBranch(tradingService);

       // EasyMock.startBranch(tradingService);
        EasyMock.startBranch(tradingService);
            EasyMock.startBranch(tradingService);
                EasyMock.startBranch(tradingService);
                    EasyMock.expect(tradingService.getPrice(beef,"selling")).setReturn(1000.00).setPrint("sell beef");
                EasyMock.switchBranch(tradingService);
                    EasyMock.expect(tradingService.getPrice(fish,"selling")).setReturn(80.00).setPrint("sell fish");
                EasyMock.endBranch(tradingService);
            EasyMock.switchBranch(tradingService);
                EasyMock.expect(tradingService.getPrice(beef,"buying")).setReturn(600.00).setPrint("buy beef");
            EasyMock.endBranch(tradingService);
        EasyMock.switchBranch(tradingService);
            EasyMock.expect(tradingService.getPrice(fish,"buying")).setReturn(80.00).setPrint("buy fish");
        EasyMock.endBranch(tradingService);
       // EasyMock.endBranch(tradingService);

        EasyMock.replay(tradingService);// start real testing!!!

        // test1
        assertEquals(tradingSystem.getCommodityValue(), 200 * 40.00 + 300 * 800.00, 0.00);
        tradingSystem.sellCommodity(fish,100);

        tradingSystem.sellCommodity(fish,100);

        EasyMock.replay(tradingService);// start real testing!!!

        // test1
        assertEquals(tradingSystem.getCommodityValue(), 300 * 800.00, 0.00);
        tradingSystem.sellCommodity(beef,100);

        tradingSystem.sellCommodity(beef,100);
        //EasyMock.replay(tradingService);


    }

}