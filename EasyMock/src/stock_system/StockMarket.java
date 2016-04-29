package stock_system;

import exceptions.CustomedException;

import java.util.List;

/*
 * TradingService
 * The interface to be mocked.
 */
public interface StockMarket {
    public double getPrice(Stock stock,String tag);
    public int getQuantity(Stock stock)throws CustomedException;
    public boolean serviceAvailable(int tag);
    public List<Stock> stockOnMarket();


}
