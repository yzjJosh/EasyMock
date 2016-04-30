package stock_system;



import exceptions.CustomedException;


import java.util.List;
import java.util.Map;

public class StockSystem {
    private StockMarket stockMarket;
    private Map<Stock,Integer> stockList;
    private double amountOfMoney = 100000.00;
    /*
     * Get the trading service.
     * @return the service the system provides.
     */
    public StockMarket getstockMarketService() {
        return stockMarket;
    }
    /*
     * Set the service the system provides.
     */
    public void setStockMarket(StockMarket stockMarket) {
        this.stockMarket = stockMarket;
    }

    /*
     * Get the trading commodities.
     * @return the commodities the system provides.
     */
    public Map<Stock,Integer> getStockList() {
        return stockList;
    }

    /*
     * Set the commodities.
     */
    public void setStocks(Map<Stock,Integer> stockList) {
        this.stockList = stockList;
    }

    /*
     * Get the whole value of the commodities.
     * @return the whole value.
     */
    public double getStockValue() {
        double value = 0.0;

        for (Stock stock : stockList.keySet()) {
            value += stockMarket.getPrice(stock,"mean") * stockList.get(stock);
        }
        return value;
    }

    public double buyStock(Stock stock) throws CustomedException{
        double val = 0.0;
        int quantity = stockMarket.getQuantity(stock);

        val = stockMarket.getPrice(stock,"buying")*quantity;
       if(this.amountOfMoney-val<0)
           throw new CustomedException("Money is not enough");
           else
        this.amountOfMoney = this.amountOfMoney-val;

        if(!stockList.containsKey(stock)){
            stockList.put(stock,quantity);
        }else{
            stockList.put(stock,stockList.get(stock)+quantity);

        }

        return val;
    }

    public double sellStock(Stock stock,int quantitiy ) throws CustomedException{
        double value = 0.0;
        if(quantitiy>stockList.get(stock))
            throw new CustomedException("We only have "+ stockList.get(stock)+ " "+ stock.getTicker()+" stocks. So "+quantitiy+" is out of bound.");
        else{
            value = stockMarket.getPrice(stock,"selling")*quantitiy;
            stockList.put(stock,stockList.get(stock)-quantitiy);
        }
        this.amountOfMoney = this.amountOfMoney+value;
        return value;
    }

    public boolean sendRequest(int tag){
        return stockMarket.serviceAvailable(tag);
    }

    public List<Stock> getList(){
        return stockMarket.stockOnMarket();
    }

    public double getAmountOfMoney(){return amountOfMoney;}

    public String getSummary(){return (String)"Now we have " + getAmountOfMoney()+" dollars and stocks worth "+ getStockValue()+" dollars \n \n";}
}
