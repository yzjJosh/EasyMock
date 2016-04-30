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

    @Test
    public void testStockValue(){
        Map<Stock,Integer> stocks = new HashMap<>();
        Stock Apple = new Stock("1", "Apple");
        Stock Google = new Stock("2", "Google");
        stocks.put(Apple,200);
        stocks.put(Google,300);
        stockSystem.setStocks(stocks);

        EasyMock.expect(stockMarket.getPrice(Google, "mean")).setReturn(691.09);
        EasyMock.expect(stockMarket.getPrice(Apple, "mean")).setReturn(94.83);
        EasyMock.replay(stockMarket);

        assertEquals(stockSystem.getStockValue(), 200 * 94.83 + 300 * 691.09, 0.00);
    }

    @Test
    public void testSellingFunction(){

        Map<Stock,Integer> stocks = new HashMap<>();
        Stock Apple = new Stock("1", "Apple");
        Stock Google = new Stock("2", "Google");
        stocks.put(Apple,200);
        stocks.put(Google,300);
        stockSystem.setStocks(stocks);

        EasyMock.expect(stockMarket.getPrice(Google, "selling")).setReturn(714.17);
        EasyMock.expect(stockMarket.getPrice(Apple, "selling")).setReturn(97.88);
        EasyMock.replay(stockMarket);


        try{assertEquals(stockSystem.sellStock(Google,100),714.17*100,0.00);}
        catch(CustomedException e){System.out.println(e);}
        try{
            assertEquals(stockSystem.sellStock(Apple,100),97.88*100,0.00);}
        catch (CustomedException e){System.out.println(e);}
        assertEquals(300 - 100, (int) stockSystem.getStockList().get(Google));
        assertEquals(200 - 100, (int) stockSystem.getStockList().get(Apple));
    }

    @Test
    public void testBuyingFunction() throws CustomedException{

        Map<Stock,Integer> stocks = new HashMap<>();
        Stock Apple = new Stock("1", "Apple");
        Stock Google = new Stock("2", "Google");
        stocks.put(Apple,200);
        stocks.put(Google,300);
        stockSystem.setStocks(stocks);
        try{
            EasyMock.expect(stockMarket.getQuantity(Google)).setReturn(100);
        }catch (CustomedException e){
            System.out.print(e);
        }

        EasyMock.expect(stockMarket.getPrice(Google, "buying")).setReturn(689.55);
        try{
            EasyMock.expect(stockMarket.getQuantity(Apple)).setReturn(100);
        }catch (CustomedException e){
            System.out.print(e);
        }

        EasyMock.expect(stockMarket.getPrice(Apple, "buying")).setReturn(94.83);
        EasyMock.replay(stockMarket);


        assertEquals(stockSystem.buyStock(Google),689.55*100,0.00);
        assertEquals(300+100,(int)stockSystem.getStockList().get(Google));

        assertEquals(stockSystem.buyStock(Apple),94.83*100,0.00);
        assertEquals(200+100,(int)stockSystem.getStockList().get(Apple));
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
        try{
            EasyMock.expect(stockMarket.getQuantity(Google)).setReturn(50).setPrint("Google on the market");

        }catch (CustomedException e){
            System.out.print(e);
        }
        EasyMock.expect(stockMarket.getPrice(Google,"buying")).setReturn(689.55).setPrint("buy Google");
        EasyMock.endBranch(stockMarket);
        EasyMock.switchBranch(stockMarket);
        try{
            EasyMock.expect(stockMarket.getQuantity(Apple)).setReturn(100).setPrint("Apple on the market");

        }catch (CustomedException e){
            System.out.print(e);
        }
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
        EasyMock.replay(stockMarket);
        assertTrue(stockSystem.sendRequest(1));
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

        EasyMock.replay(stockMarket);
        assertTrue(stockSystem.sendRequest(1));
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

        EasyMock.replay(stockMarket);
        assertTrue(stockSystem.sendRequest(1));
        System.out.println(stockSystem.getSummary());

        //test4
        System.out.println("test4 \n");
        EasyMock.replay(stockMarket);
        if(!stockSystem.sendRequest(0))
            System.out.println("Stock Market Service is not available");

        EasyMock.replay(stockMarket);
        assertTrue(stockSystem.sendRequest(1));
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

        EasyMock.replay(stockMarket);
        assertTrue(stockSystem.sendRequest(1));
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

        EasyMock.replay(stockMarket);
        assertTrue(stockSystem.sendRequest(1));
        System.out.println(stockSystem.getSummary());

        //test7
        System.out.println("test7 \n");
        EasyMock.replay(stockMarket);
        assertTrue(stockSystem.sendRequest(1));
        assertEquals(stockSystem.getStockValue(), 2100*94.83, 0.00);
        moneyOriginal = stockSystem.getAmountOfMoney();
        moneyUsed = 0;
        stockOnMarket = stockSystem.getList();
        try{
            moneyUsed = stockSystem.buyStock(stockOnMarket.get(1));
        }catch (CustomedException e){  System.out.println(e);}
        assertEquals(stockSystem.getAmountOfMoney(),moneyOriginal-moneyUsed,0.00);

        EasyMock.replay(stockMarket);
        assertTrue(stockSystem.sendRequest(1));
        System.out.println(stockSystem.getSummary());

        //test8
        System.out.println("test8 \n");
        EasyMock.replay(stockMarket);
        assertTrue(stockSystem.sendRequest(1));
        assertEquals(stockSystem.getStockValue(), 2100*94.83, 0.00);
        moneyOriginal = stockSystem.getAmountOfMoney();
        moneyEarned = 0;
        try{moneyEarned+=stockSystem.sellStock(Apple,100);}catch (CustomedException e){System.out.println(e);}
        try{moneyEarned+=stockSystem.sellStock(Apple, 100);}catch (CustomedException e){System.out.println(e);}

        EasyMock.replay(stockMarket);
        assertTrue(stockSystem.sendRequest(1));
        System.out.println(stockSystem.getSummary());

        //test9
        System.out.println("test9 \n");
        EasyMock.replay(stockMarket);
        assertTrue(stockSystem.sendRequest(1));
        assertEquals(stockSystem.getStockValue(), 1900*94.83, 0.00);
        moneyOriginal = stockSystem.getAmountOfMoney();
        moneyEarned = 0;
        try{moneyEarned+=stockSystem.sellStock(Apple,200);}catch (CustomedException e){System.out.println(e);}
        try{moneyEarned+=stockSystem.sellStock(Apple, 100);}catch (CustomedException e){System.out.println(e);}

        EasyMock.replay(stockMarket);
        assertTrue(stockSystem.sendRequest(1));
        System.out.println(stockSystem.getSummary());

        //test10
        System.out.println("test10 \n");
        EasyMock.replay(stockMarket);
        assertTrue(stockSystem.sendRequest(1));
        assertEquals(stockSystem.getStockValue(), 1600*94.83, 0.00);
        moneyOriginal = stockSystem.getAmountOfMoney();
        moneyUsed = 0;
        try{moneyUsed+=stockSystem.buyStock(Apple);}catch (CustomedException e){System.out.println(e);}
        try{moneyUsed+=stockSystem.buyStock(Google);}catch (CustomedException e){System.out.println(e);}

        EasyMock.replay(stockMarket);
        assertTrue(stockSystem.sendRequest(1));
        System.out.println(stockSystem.getSummary());

        //test11
        System.out.println("test11 \n");
        EasyMock.replay(stockMarket);
        assertTrue(stockSystem.sendRequest(1));
        assertEquals(stockSystem.getStockValue(), 1700*94.83, 0.00);
        moneyOriginal = stockSystem.getAmountOfMoney();
        moneyUsed = 0;
        try{moneyUsed+=stockSystem.buyStock(Google);}catch (CustomedException e){System.out.println(e);}
        try{moneyUsed+=stockSystem.buyStock(Apple);}catch (CustomedException e){System.out.println(e);}

        EasyMock.replay(stockMarket);
        assertTrue(stockSystem.sendRequest(1));
        System.out.println(stockSystem.getSummary());

        //test12
        System.out.println("test12 \n");
        EasyMock.replay(stockMarket);
        assertTrue(stockSystem.sendRequest(1));
        assertEquals(stockSystem.getStockValue(), 1700*94.83+50*691.09, 0.00);
        moneyOriginal = stockSystem.getAmountOfMoney();
        moneyUsed = 0;
        moneyEarned = 0;
        try{moneyUsed+=stockSystem.buyStock(Google);}catch (CustomedException e){System.out.println(e);}
        try{moneyEarned+=stockSystem.sellStock(Apple,500);}catch (CustomedException e){System.out.println(e);}

        EasyMock.replay(stockMarket);
        assertTrue(stockSystem.sendRequest(1));
        System.out.println(stockSystem.getSummary());

        //test13
        System.out.println("test13 \n");
        EasyMock.replay(stockMarket);
        assertTrue(stockSystem.sendRequest(1));
        assertEquals(stockSystem.getStockValue(), 1200*94.83+100*691.09, 0.00);
        moneyOriginal = stockSystem.getAmountOfMoney();
        moneyUsed = 0;
        moneyEarned = 0;
        try{moneyUsed+=stockSystem.buyStock(Google);}catch (CustomedException e){System.out.println(e);}
        try{moneyUsed+=stockSystem.buyStock(Google);}catch (CustomedException e){System.out.println(e);}

        EasyMock.replay(stockMarket);
        assertTrue(stockSystem.sendRequest(1));
        System.out.println(stockSystem.getSummary());

        //test14
        System.out.println("test14 \n");
        EasyMock.replay(stockMarket);
        assertTrue(stockSystem.sendRequest(1));
        assertEquals(stockSystem.getStockValue(), 1200*94.83+150*691.09, 0.00);
        moneyOriginal = stockSystem.getAmountOfMoney();
        moneyUsed = 0;
        moneyEarned = 0;
        try{moneyEarned+=stockSystem.sellStock(Apple,200);}catch (CustomedException e){System.out.println(e);}
        try{moneyUsed+=stockSystem.buyStock(Apple);}catch (CustomedException e){System.out.println(e);}

        EasyMock.replay(stockMarket);
        assertTrue(stockSystem.sendRequest(1));
        System.out.println(stockSystem.getSummary());

        //test15
        System.out.println("test15 \n");
        EasyMock.replay(stockMarket);
        assertTrue(stockSystem.sendRequest(1));
        assertEquals(stockSystem.getStockValue(), 1000*94.83+150*691.09, 0.00);
        moneyOriginal = stockSystem.getAmountOfMoney();
        moneyUsed = 0;
        moneyEarned = 0;
        try{moneyEarned+=stockSystem.sellStock(Google,100);}catch (CustomedException e){System.out.println(e);}
        try{moneyUsed+=stockSystem.buyStock(Google);}catch (CustomedException e){System.out.println(e);}

        EasyMock.replay(stockMarket);
        assertTrue(stockSystem.sendRequest(1));
        System.out.println(stockSystem.getSummary());
    }

}