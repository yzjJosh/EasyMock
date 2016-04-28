package trading_system;

import exceptions.CustomedException;

import java.util.List;

/*
 * TradingService
 * The interface to be mocked.
 */
public interface TradingService {
	public double getPrice(Commodity commodity,String tag);
	public int getQuantity(Commodity commodity)throws CustomedException;
	public boolean serviceAvailable(int tag);
	public List<Commodity> commodityOnMarket();


}
