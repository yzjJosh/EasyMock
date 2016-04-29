package trading_system;

//import easymock.EasyMock;
import static org.easymock.EasyMock.*;

import easymock.EasyMock;
import exceptions.CustomedException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    @Test(expected = RuntimeException.class)
    public void testCommodityValue(){
        Map<Commodity,Integer> commodities = new HashMap<>();
        Commodity fish = new Commodity("1", "Fish");
        Commodity beef = new Commodity("2", "Beef");

        commodities.put(fish,200);
        commodities.put(beef,300);
        tradingSystem.setCommodities(commodities);

        EasyMock.expect(tradingService.getPrice(beef, "mean")).setReturn(800.00);
        EasyMock.expect(tradingService.getPrice(fish,"mean")).setReturn(40.00).setThrowable(new RuntimeException("Boo! Here comes an exception."));
        EasyMock.replay(tradingService);

        assertEquals(tradingSystem.getCommodityValue(), 200 * 40.00 + 300 * 800.00, 0.00);
    }


    @Test
    public void testAsWhole(){
        Map<Commodity,Integer> commodities = new HashMap<>();
        Commodity fish = new Commodity("1", "Fish");
        Commodity beef = new Commodity("2", "Beef");
        commodities.put(fish,200);
        commodities.put(beef,300);
        List<Commodity> list = new ArrayList<>();
        list.add(fish);
        list.add(beef);
        tradingSystem.setCommodities(commodities);

        EasyMock.record(tradingService);

        EasyMock.startBranch(tradingService);
         EasyMock.expect(tradingService.serviceAvailable(1)).setReturn(true);
        EasyMock.switchBranch(tradingService);
         EasyMock.expect(tradingService.serviceAvailable(0)).setReturn(false);
        EasyMock.endBranch(tradingService);


        EasyMock.expect(tradingService.getPrice(beef,"mean")).setReturn(800.00).setPrint("evaluate beef");
        EasyMock.expect(tradingService.getPrice(fish,"mean")).setReturn(40.00).setPrint("evaluate fish");

        EasyMock.startBranch(tradingService);
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
        EasyMock.switchBranch(tradingService);
            EasyMock.expect(tradingService.commodityOnMarket()).setReturn(list).setPrint(" commodity list");
        EasyMock.endBranch(tradingService);

       // EasyMock.startBranch(tradingService);
        EasyMock.startBranch(tradingService);
            EasyMock.startBranch(tradingService);
                EasyMock.startBranch(tradingService);
                    EasyMock.expect(tradingService.getPrice(beef,"selling")).setReturn(1000.00).setPrint("sell beef");
                EasyMock.switchBranch(tradingService);
                    EasyMock.expect(tradingService.getPrice(fish,"selling")).setReturn(80.00).setPrint("sell fish");
                EasyMock.endBranch(tradingService);
            EasyMock.switchBranch(tradingService);
            try{
               EasyMock.expect( tradingService.getQuantity(beef)).setReturn(4000).setThrowable(new CustomedException("There is no beef on the market for now")).setPrint("buy beef");
            }catch (CustomedException e){

            }
               EasyMock.expect(tradingService.getPrice(beef,"buying")).setReturn(600.00).setPrint("buy beef");
            EasyMock.endBranch(tradingService);
        EasyMock.switchBranch(tradingService);
            try{
                EasyMock.expect(tradingService.getQuantity(fish)).setReturn(4000).setPrint("buy fish1");

            }catch (CustomedException e){
                System.out.print("WENWEN2");
            }
            EasyMock.expect(tradingService.getPrice(fish,"buying")).setReturn(80.00).setPrint("buy fish2");
        EasyMock.endBranch(tradingService);
      //  EasyMock.endBranch(tradingService);

        EasyMock.replay(tradingService);// start real testing!!!

        // test1
        assertTrue(tradingSystem.sendRequest(1));
        assertEquals(tradingSystem.getCommodityValue(), 200 * 40.00 + 300 * 800.00, 0.00);
        tradingSystem.sellCommodity(fish,100);
        tradingSystem.sellCommodity(fish,100);

        EasyMock.replay(tradingService);// start real testing!!!

        // test2
        assertTrue(tradingSystem.sendRequest(1));
        assertEquals(tradingSystem.getCommodityValue(), 300 * 800.00, 0.00);
        tradingSystem.sellCommodity(beef,100);
        tradingSystem.sellCommodity(beef,100);

        //test3
        EasyMock.replay(tradingService);
        assertTrue(tradingSystem.sendRequest(1));
        assertEquals(tradingSystem.getCommodityValue(), 100 * 800.00, 0.00);
        tradingSystem.sellCommodity(beef,100);
        try{
        tradingSystem.addCommodity(beef);
        }catch (CustomedException e){
            System.out.println("Boom! " + e);
        }

        //test4
        EasyMock.replay(tradingService);
        if(!tradingSystem.sendRequest(0))
            System.out.println("Trading Service is not available");


        //test5
        EasyMock.replay(tradingService);
        assertTrue(tradingSystem.sendRequest(1));
        assertEquals(tradingSystem.getCommodityValue(), 0.0, 0.00);
        try{
            tradingSystem.addCommodity(fish);
            System.out.print("WENWEN5");
        }catch (Exception e){ e.printStackTrace(); System.out.print("WENWEN3");}
        try{
            tradingSystem.addCommodity(fish);
        }catch (Exception e){  System.out.print("WENWEN4");}

        //test6
        EasyMock.replay(tradingService);
        assertTrue(tradingSystem.sendRequest(1));
        assertEquals(tradingSystem.getCommodityValue(), 0.0, 0.00);
        List<Commodity> commodityOnMarket = tradingSystem.getList();
        try{
            tradingSystem.addCommodity(commodityOnMarket.get(0));
        }catch (Exception e){  System.out.print("WENWEN4");}
    }

}