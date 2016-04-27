package trading_system;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.*;

/**
 * Created by wenwen on 4/27/16.
 */
public class TradingSystemTest0 {
    private TradingSystem tradingSystem;
    private TradingService tradingService;


    @Before
    public void setup(){
        tradingSystem = new TradingSystem();
        tradingService = (TradingService) createMock(TradingService.class);
        tradingSystem.setTradingService(tradingService);
        //  tradingSystem.record(tradingService);
    }
    @Test
    public void testCommodityValue(){
        Map<Commodity,Integer> commodities = new HashMap<>();
        Commodity fish = new Commodity("1", "Fish");
        Commodity beef = new Commodity("2", "Beef");
        commodities.put(fish,200);
        commodities.put(beef,300);
        tradingSystem.setCommodities(commodities);

        expect(tradingService.getPrice(beef,"mean")).andReturn(800.00);
        expect(tradingService.getPrice(fish,"mean")).andReturn(40.00);
        replay(tradingService);

        assertEquals(tradingSystem.getCommodityValue(), 200 * 40.00 + 300 * 800.00, 0.00);
    }

    @Test
    public void testSellingFunction(){

        Map<Commodity,Integer> commodities = new HashMap<>();
        Commodity fish = new Commodity("1", "Fish");
        Commodity beef = new Commodity("2", "Beef");
        commodities.put(fish,200);
        commodities.put(beef,300);
        tradingSystem.setCommodities(commodities);

        expect(tradingService.getPrice(beef,"selling")).andReturn(1000.00);
        expect(tradingService.getPrice(fish,"selling")).andReturn(60.00);
        replay(tradingService);


        assertEquals(tradingSystem.sellCommodity(beef,100),1000.00*100,0.00);
        assertEquals(tradingSystem.sellCommodity(fish,100),60.00*100,0.00);
        assertEquals(300 - 100, (int) tradingSystem.getCommodities().get(beef));
        assertEquals(200 - 100, (int) tradingSystem.getCommodities().get(fish));
    }

    @Test
    public void testBuyingFunction(){

        Map<Commodity,Integer> commodities = new HashMap<>();
        Commodity fish = new Commodity("1", "Fish");
        Commodity beef = new Commodity("2", "Beef");
        commodities.put(fish,200);
        commodities.put(beef,300);
        tradingSystem.setCommodities(commodities);

        expect(tradingService.getPrice(beef,"buying")).andReturn(600.00);
        expect(tradingService.getPrice(fish,"buying")).andReturn(40.00);
        replay(tradingService);


        assertEquals(tradingSystem.addCommodity(beef,100),600.00*100,0.00);
        assertEquals(300+100,(int)tradingSystem.getCommodities().get(beef));

        assertEquals(tradingSystem.addCommodity(fish,100),40.00*100,0.00);
        assertEquals(200+100,(int)tradingSystem.getCommodities().get(fish));
    }
}