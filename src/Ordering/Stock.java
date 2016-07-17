package Ordering;

public class Stock {
    
    private String stockID;
    private String name;
    private int quantity;
    
    public Stock(String name, int quantity, String stockID){
        this.name = name;
        this.quantity = quantity;
        this.stockID = stockID;
    }

    public String getStockID() {
        return stockID;
    }

    public void setStockID(String stockID) {
        this.stockID = stockID;
    }

    
    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
