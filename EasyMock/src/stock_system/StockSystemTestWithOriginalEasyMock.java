package stock_system;

import easymock.EasyMock;
import exceptions.CustomedException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.*;

/**
 * Created by wenwen on 4/27/16.
 */
public class StockSystemTestWithOriginalEasyMock {
    private StockSystem stockSystem;
    private StockMarket stockMarket;

    Map<Stock,Integer> stocks = new HashMap<>();
    Stock Apple = new Stock("1", "Apple");
    Stock Google = new Stock("2", "Google");

    List<Stock> list = new ArrayList<>();


    @Before
    public void setup(){
        stockSystem = new StockSystem();
        stockMarket = (StockMarket) createMock(StockMarket.class);
        stockSystem.setStockMarket(stockMarket);

        stocks.put(Apple,200);
        stocks.put(Google,300);
        list.add(Apple);
        list.add(Google);
        stockSystem.setStocks(stocks);
        //stockSystem.record(stockMarket);
    }
    @Test
    public void testStockValue(){
        Map<Stock,Integer> stocks = new HashMap<>();
        Stock Apple = new Stock("1", "Apple");
        Stock Google = new Stock("2", "Google");
        stocks.put(Apple,200);
        stocks.put(Google,300);
        stockSystem.setStocks(stocks);

        expect(stockMarket.getPrice(Google,"mean")).andReturn(691.09);
        expect(stockMarket.getPrice(Apple,"mean")).andReturn(94.83);
        replay(stockMarket);

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

        expect(stockMarket.getPrice(Google,"selling")).andReturn(714.17);
        expect(stockMarket.getPrice(Apple,"selling")).andReturn(97.88);
        replay(stockMarket);


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
            expect(stockMarket.getQuantity(Google)).andReturn(100);
        }catch (CustomedException e){
            System.out.print(e);
        }

        expect(stockMarket.getPrice(Google,"buying")).andReturn(689.55);
        try{
            expect(stockMarket.getQuantity(Apple)).andReturn(100);
        }catch (CustomedException e){
            System.out.print(e);
        }

        expect(stockMarket.getPrice(Apple,"buying")).andReturn(94.83);
        replay(stockMarket);


        assertEquals(stockSystem.buyStock(Google),689.55*100,0.00);
        assertEquals(300+100,(int)stockSystem.getStockList().get(Google));

        assertEquals(stockSystem.buyStock(Apple),94.83*100,0.00);
        assertEquals(200+100,(int)stockSystem.getStockList().get(Apple));
    }

    @Test
    public void test1(){
        expect(stockMarket.serviceAvailable(1)).andReturn(true);
        expect(stockMarket.getPrice(Google,"mean")).andReturn(691.09);
        expect(stockMarket.getPrice(Apple,"mean")).andReturn(94.83);
        expect(stockMarket.getPrice(Apple,"selling")).andReturn(97.88);
        expect(stockMarket.getPrice(Apple, "selling")).andReturn(97.88);

        replay(stockMarket);// start real testing!!!

        System.out.println("test 1\n");
        assertTrue(stockSystem.sendRequest(1));
        assertEquals(stockSystem.getStockValue(), 200 * 94.83 + 300 * 691.09, 0.00);
        double moneyOriginal = stockSystem.getAmountOfMoney();
        double moneyEarned = 0;
        try{moneyEarned+=stockSystem.sellStock(Apple,100);}catch (CustomedException e){System.out.println(e);}
        try{moneyEarned+=stockSystem.sellStock(Apple,100000);}catch (CustomedException e){System.out.println(e);}
        assertEquals(stockSystem.getAmountOfMoney(),moneyEarned+moneyOriginal,0.00);
        System.out.println("\n \n");

    }
    @Test
    public void test1Summary(){
        expect(stockMarket.getPrice(Google,"mean")).andReturn(691.09);
        expect(stockMarket.getPrice(Apple,"mean")).andReturn(94.83);
        replay(stockMarket);
        System.out.println(stockSystem.getSummary());
    }
    @Test
    public void test2(){
        expect(stockMarket.serviceAvailable(1)).andReturn(true);
        expect(stockMarket.getPrice(Google,"mean")).andReturn(691.09);
        expect(stockMarket.getPrice(Apple,"mean")).andReturn(94.83);
        expect(stockMarket.getPrice(Google,"selling")).andReturn(714.17);
        expect(stockMarket.getPrice(Google, "selling")).andReturn(714.17);

        replay(stockMarket);
        // test2
        System.out.println("test 2\n");
        assertTrue(stockSystem.sendRequest(1));
        assertEquals(stockSystem.getStockValue(), 200*94.83+300 * 691.09, 0.00);
        double moneyOriginal = stockSystem.getAmountOfMoney();
        double moneyEarned = 0;
        try{moneyEarned+=stockSystem.sellStock(Google,100);}catch (CustomedException e){System.out.println(e);}
        try{moneyEarned+=stockSystem.sellStock(Google,100);}catch (CustomedException e){System.out.println(e);}
        assertEquals(stockSystem.getAmountOfMoney(),moneyEarned+moneyOriginal,0.00);
        System.out.println("\n \n");
    }
    @Test
    public void test3(){
        expect(stockMarket.serviceAvailable(1)).andReturn(true);
        expect(stockMarket.getPrice(Google,"mean")).andReturn(691.09);
        expect(stockMarket.getPrice(Apple,"mean")).andReturn(94.83);
        expect(stockMarket.getPrice(Google,"selling")).andReturn(714.17);
        try{
            expect(stockMarket.getQuantity(Google)).andReturn(4000).andThrow(new CustomedException("There is no Google on the market for now"));
        }catch (CustomedException e){

        }
        expect(stockMarket.getPrice(Google,"buying")).andReturn(689.55);
        replay(stockMarket);
        // test3
        System.out.println("test 3\n");
        assertTrue(stockSystem.sendRequest(1));
        assertEquals(stockSystem.getStockValue(),200*94.83+300 * 691.09, 0.00);
        try{stockSystem.sellStock(Google,100);}catch (CustomedException e){}
        try{
            stockSystem.buyStock(Google);
        }catch (CustomedException e){
            System.out.println(e);
        }
        System.out.println("\n \n");

    }
    @Test
    public void test4(){
        expect(stockMarket.serviceAvailable(0)).andReturn(false);
        replay(stockMarket);

        //test4
        System.out.println("test4 \n");
        if(!stockSystem.sendRequest(0))
            System.out.println("Stock Market Service is not available");
        System.out.println("\n \n");

    }
    @Test
    public void test5(){
        expect(stockMarket.serviceAvailable(1)).andReturn(true);
        expect(stockMarket.getPrice(Google,"mean")).andReturn(691.09);
        expect(stockMarket.getPrice(Apple, "mean")).andReturn(94.83);
        expect(stockMarket.stockOnMarket()).andReturn(list);
        try{
            expect(stockMarket.getQuantity(Apple)).andReturn(200);

        }catch (CustomedException e){
            System.out.print(e);
        }
        expect(stockMarket.getPrice(Apple,"buying")).andReturn(97.88);
        replay(stockMarket);

        //test5
        System.out.println("test5 \n");
        assertTrue(stockSystem.sendRequest(1));
        assertEquals(stockSystem.getStockValue(), 200*94.83+300 * 691.09, 0.00);
        double moneyOriginal = stockSystem.getAmountOfMoney();
        double moneyUsed = 0;
        List<Stock> stockOnMarket = stockSystem.getList();
        try{
            moneyUsed = stockSystem.buyStock(stockOnMarket.get(0));
        }catch (CustomedException e){  System.out.println(e);}
        assertEquals(stockSystem.getAmountOfMoney(),moneyOriginal-moneyUsed,0.00);
        System.out.println("\n \n");
    }
    @Test
    public void test6(){
        expect(stockMarket.serviceAvailable(1)).andReturn(true);
        expect(stockMarket.getPrice(Google,"mean")).andReturn(691.09);
        expect(stockMarket.getPrice(Apple, "mean")).andReturn(94.83);
        expect(stockMarket.stockOnMarket()).andReturn(list);
        try{
            expect(stockMarket.getQuantity(Apple)).andReturn(200000);

        }catch (CustomedException e){
            System.out.print(e);
        }
        expect(stockMarket.getPrice(Apple,"buying")).andReturn(97.88);
        replay(stockMarket);

        //test6
        System.out.println("test6 \n");
        assertTrue(stockSystem.sendRequest(1));
        assertEquals(stockSystem.getStockValue(), 200*94.83+300 * 691.09, 0.00);
        double moneyOriginal = stockSystem.getAmountOfMoney();
        double moneyUsed = 0;
        List<Stock> stockOnMarket = stockSystem.getList();
        try{
            moneyUsed = stockSystem.buyStock(stockOnMarket.get(0));
        }catch (CustomedException e){  System.out.println(e);}
        assertEquals(stockSystem.getAmountOfMoney(),moneyOriginal-moneyUsed,0.00);
        System.out.println("\n \n");
    }

}