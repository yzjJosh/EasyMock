package trading_system;

import exceptions.CustomedException;

/*
 * TradingService
 * The interface to be mocked.
 */
public interface TradingService {
	public double getPrice(Commodity commodity,String tag);
}
