package stock_system;

import exceptions.CustomedException;
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
public class StockSystemTestWithOriginalEasyMock {
    private StockSystem stockSystem;
    private StockMarket stockMarket;


    @Before
    public void setup(){
        stockSystem = new StockSystem();
        stockMarket = (StockMarket) createMock(StockMarket.class);
        stockSystem.setStockMarket(stockMarket);
        //  stockSystem.record(stockMarket);
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
}