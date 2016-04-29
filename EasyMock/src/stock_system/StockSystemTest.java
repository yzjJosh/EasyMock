package stock_system;

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
public class StockSystemTest {
    private StockSystem stockSystem;
    private StockMarket stockMarket;


    @Before
    public void setup(){
        stockSystem = new StockSystem();
        stockMarket = (StockMarket) EasyMock.createMock(StockMarket.class);
        stockSystem.setStockMarket(stockMarket);
        EasyMock.record(stockMarket);
    }
    @Test(expected = RuntimeException.class)
    public void testStockValue(){
        Map<Stock,Integer> stocks = new HashMap<>();
        Stock Apple = new Stock("1", "Apple");
        Stock Google = new Stock("2", "Google");

        stocks.put(Apple,200);
        stocks.put(Google,300);
        stockSystem.setStocks(stocks);

        EasyMock.expect(stockMarket.getPrice(Google, "mean")).setReturn(691.09);
        EasyMock.expect(stockMarket.getPrice(Apple,"mean")).setReturn(94.83);
        EasyMock.replay(stockMarket);

        assertEquals(stockSystem.getStockValue(), 200 * 94.83 + 300 * 691.09, 0.00);
    }


    @Test
    public void testAsWhole(){
        Map<Stock,Integer> stocks = new HashMap<>();
        Stock Apple = new Stock("1", "Apple");
        Stock Google = new Stock("2", "Google");
        stocks.put(Apple,200);
        stocks.put(Google,300);
        List<Stock> list = new ArrayList<>();
        list.add(Apple);
        list.add(Google);
        stockSystem.setStocks(stocks);

        EasyMock.record(stockMarket);

        EasyMock.startBranch(stockMarket);
        EasyMock.expect(stockMarket.serviceAvailable(1)).setReturn(true);
        EasyMock.switchBranch(stockMarket);
        EasyMock.expect(stockMarket.serviceAvailable(0)).setReturn(false);
        EasyMock.endBranch(stockMarket);


        EasyMock.expect(stockMarket.getPrice(Google,"mean")).setReturn(691.09).setPrint("evaluate Google");
        EasyMock.expect(stockMarket.getPrice(Apple,"mean")).setReturn(94.83).setPrint("evaluate Apple");

        EasyMock.startBranch(stockMarket);
        EasyMock.startBranch(stockMarket);
        EasyMock.startBranch(stockMarket);
        EasyMock.startBranch(stockMarket);

        EasyMock.expect(stockMarket.getPrice(Google,"selling")).setReturn(714.17).setPrint("sell Google");

        EasyMock.switchBranch(stockMarket);
        EasyMock.expect(stockMarket.getPrice(Apple,"selling")).setReturn(97.88).setPrint("sell Apple");
        EasyMock.endBranch(stockMarket);
        EasyMock.switchBranch(stockMarket);
        EasyMock.expect(stockMarket.getPrice(Google,"buying")).setReturn(689.55).setPrint("buy Google");
        EasyMock.endBranch(stockMarket);
        EasyMock.switchBranch(stockMarket);
        EasyMock.expect(stockMarket.getPrice(Apple,"buying")).setReturn(97.88).setPrint("buy Apple");
        EasyMock.endBranch(stockMarket);
        EasyMock.switchBranch(stockMarket);
        EasyMock.expect(stockMarket.stockOnMarket()).setReturn(list).setPrint(" Stock list");
        EasyMock.endBranch(stockMarket);

        // EasyMock.startBranch(stockMarket);
        EasyMock.startBranch(stockMarket);
        EasyMock.startBranch(stockMarket);
        EasyMock.startBranch(stockMarket);
        EasyMock.expect(stockMarket.getPrice(Google,"selling")).setReturn(714.17).setPrint("sell Google");
        EasyMock.switchBranch(stockMarket);
        EasyMock.expect(stockMarket.getPrice(Apple,"selling")).setReturn(97.88).setPrint("sell Apple");
        EasyMock.endBranch(stockMarket);
        EasyMock.switchBranch(stockMarket);
        try{
            EasyMock.expect( stockMarket.getQuantity(Google)).setReturn(4000).setThrowable(new CustomedException("There is no Google on the market for now")).setPrint("buy Google");
        }catch (CustomedException e){

        }
        EasyMock.expect(stockMarket.getPrice(Google,"buying")).setReturn(689.55).setPrint("buy Google");
        EasyMock.endBranch(stockMarket);
        EasyMock.switchBranch(stockMarket);
        try{
            EasyMock.expect(stockMarket.getQuantity(Apple)).setReturn(2000).setPrint("Apple on the market");

        }catch (CustomedException e){
            System.out.print(e);
        }
        EasyMock.expect(stockMarket.getPrice(Apple,"buying")).setReturn(97.88).setPrint("buy Apple");
        EasyMock.endBranch(stockMarket);

        EasyMock.expect(stockMarket.getPrice(Google,"mean")).setReturn(691.09).setPrint("evaluate Google");
        EasyMock.expect(stockMarket.getPrice(Apple,"mean")).setReturn(94.83).setPrint("evaluate Apple");
        //  EasyMock.endBranch(stockMarket);

        EasyMock.replay(stockMarket);// start real testing!!!

        // test1
        System.out.println("test 1\n");
        assertTrue(stockSystem.sendRequest(1));
        assertEquals(stockSystem.getStockValue(), 200 * 94.83 + 300 * 691.09, 0.00);
        double moneyOriginal = stockSystem.getAmountOfMoney();
        double moneyEarned = 0;
        try{moneyEarned+=stockSystem.sellStock(Apple,100);}catch (CustomedException e){System.out.println(e);}
        try{moneyEarned+=stockSystem.sellStock(Apple,100000);}catch (CustomedException e){System.out.println(e);}
        assertEquals(stockSystem.getAmountOfMoney(),moneyEarned+moneyOriginal,0.00);
        System.out.println(stockSystem.getSummary());


        EasyMock.replay(stockMarket);// start real testing!!!

        // test2
        System.out.println("test 2\n");
        assertTrue(stockSystem.sendRequest(1));
        assertEquals(stockSystem.getStockValue(), 100*94.83+300 * 691.09, 0.00);
        moneyOriginal = stockSystem.getAmountOfMoney();
        moneyEarned = 0;
        try{moneyEarned+=stockSystem.sellStock(Google,100);}catch (CustomedException e){System.out.println(e);}
        try{moneyEarned+=stockSystem.sellStock(Google,100);}catch (CustomedException e){System.out.println(e);}
        assertEquals(stockSystem.getAmountOfMoney(),moneyEarned+moneyOriginal,0.00);
        System.out.println(stockSystem.getSummary());

        //test3
        System.out.println("test 3\n");
        EasyMock.replay(stockMarket);
        assertTrue(stockSystem.sendRequest(1));
        assertEquals(stockSystem.getStockValue(),100*94.83+100 * 691.09, 0.00);
        try{stockSystem.sellStock(Google,100);}catch (CustomedException e){}
        try{
            stockSystem.buyStock(Google);
        }catch (CustomedException e){
            System.out.println(e);
        }
        System.out.println("\n \n");

        //test4
        System.out.println("test4 \n");
        EasyMock.replay(stockMarket);
        if(!stockSystem.sendRequest(0))
            System.out.println("Stock Market Service is not available");
        System.out.println(stockSystem.getSummary());

        //test5
        System.out.println("test5 \n");
        EasyMock.replay(stockMarket);
        assertTrue(stockSystem.sendRequest(1));
        assertEquals(stockSystem.getStockValue(), 100*94.83, 0.00);
        moneyOriginal = stockSystem.getAmountOfMoney();
        double moneyUsed = 0;
        List<Stock> stockOnMarket = stockSystem.getList();
        try{
            moneyUsed = stockSystem.buyStock(stockOnMarket.get(0));
        }catch (CustomedException e){  System.out.println(e);}
        assertEquals(stockSystem.getAmountOfMoney(),moneyOriginal-moneyUsed,0.00);
        System.out.println(stockSystem.getSummary());

        //test6
        System.out.println("test6 \n");
        EasyMock.replay(stockMarket);
        assertTrue(stockSystem.sendRequest(1));
        assertEquals(stockSystem.getStockValue(), 2100*94.83, 0.00);
        moneyOriginal = stockSystem.getAmountOfMoney();
        moneyUsed = 0;
        stockOnMarket = stockSystem.getList();
        try{
            moneyUsed = stockSystem.buyStock(stockOnMarket.get(0));
        }catch (CustomedException e){  System.out.println(e);}
        assertEquals(stockSystem.getAmountOfMoney(),moneyOriginal-moneyUsed,0.00);
        System.out.println(stockSystem.getSummary());


    }

}