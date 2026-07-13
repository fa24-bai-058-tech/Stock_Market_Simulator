// ====================================================
// Stock.java - Stock Data Model
// ====================================================
public class Stock {
    private String symbol;
    private String companyName;
    private double currentPrice;
    private double openPrice;
    private double highPrice;
    private double lowPrice;
    private long volume;

    public Stock(String symbol, String companyName, double currentPrice,
                 double openPrice, double highPrice, double lowPrice, long volume) {
        this.symbol = symbol;
        this.companyName = companyName;
        this.currentPrice = currentPrice;
        this.openPrice = openPrice;
        this.highPrice = highPrice;
        this.lowPrice = lowPrice;
        this.volume = volume;
    }

    // Getters
    public String getSymbol()      { return symbol; }
    public String getCompanyName() { return companyName; }
    public double getCurrentPrice(){ return currentPrice; }
    public double getOpenPrice()   { return openPrice; }
    public double getHighPrice()   { return highPrice; }
    public double getLowPrice()    { return lowPrice; }
    public long   getVolume()      { return volume; }

    // Setters
    public void setCurrentPrice(double price) { this.currentPrice = price; }
    public void setHighPrice(double price)    { this.highPrice = price; }
    public void setLowPrice(double price)     { this.lowPrice = price; }
    public void setVolume(long volume)        { this.volume = volume; }

    public double getChangePercent() {
        return ((currentPrice - openPrice) / openPrice) * 100;
    }

    @Override
    public String toString() {
        return String.format("%-6s | %-20s | Price: $%-8.2f | Change: %+.2f%% | Vol: %,d",
                symbol, companyName, currentPrice, getChangePercent(), volume);
    }
}
