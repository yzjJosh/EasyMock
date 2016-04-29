package stock_system;

public class Stock {
    private String StockId;
    private String name;
    //private int quantity;

    public Stock(String StockId, String name) {
        this.StockId = StockId;
        this.name = name;
    }

    public String getStockId() {
        return StockId;
    }

    public void setStockId(String StockId) {
        this.StockId = StockId;
    }

    public String getTicker() {
        return name;
    }

}
