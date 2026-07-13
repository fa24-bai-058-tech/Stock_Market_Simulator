import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Comparator;

// ====================================================
// StockMarket.java
// Uses HashMap (DS) - O(1) stock lookup by symbol
// ====================================================
public class StockMarket {
    // HashMap: symbol -> Stock  (O(1) search)
    private HashMap<String, Stock> stockMap;

    // HashMap: symbol -> PriceHistory
    private HashMap<String, PriceHistory> historyMap;

    public StockMarket() {
        stockMap   = new HashMap<>();
        historyMap = new HashMap<>();
    }

    // Add stock to market
    public void addStock(Stock stock) {
        stockMap.put(stock.getSymbol(), stock);
        historyMap.put(stock.getSymbol(), new PriceHistory(stock.getSymbol(), 30));
        historyMap.get(stock.getSymbol()).addPrice(stock.getCurrentPrice());
        System.out.println("📊 Added stock: " + stock.getSymbol());
    }

    // Get stock by symbol - O(1) HashMap lookup
    public Stock getStock(String symbol) {
        return stockMap.getOrDefault(symbol.toUpperCase(), null);
    }

    // Update stock price
    public void updatePrice(String symbol, double newPrice) {
        Stock s = getStock(symbol);
        if (s == null) {
            System.out.println("❌ Stock not found: " + symbol);
            return;
        }
        double oldPrice = s.getCurrentPrice();
        s.setCurrentPrice(newPrice);
        if (newPrice > s.getHighPrice()) s.setHighPrice(newPrice);
        if (newPrice < s.getLowPrice())  s.setLowPrice(newPrice);

        historyMap.get(symbol).addPrice(newPrice);

        System.out.printf("🔄 %s price updated: $%.2f → $%.2f (%+.2f%%)%n",
                symbol, oldPrice, newPrice,
                ((newPrice - oldPrice) / oldPrice) * 100);
    }

    // Display all stocks sorted by change %
    public void displayMarket() {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("           📈  FINANCIAL STOCK MARKET  📉");
        System.out.println("=".repeat(80));

        List<Stock> sorted = new ArrayList<>(stockMap.values());
        sorted.sort(Comparator.comparingDouble(Stock::getChangePercent).reversed());

        System.out.printf("%-6s | %-20s | %-10s | %-10s | %s%n",
                "SYM", "COMPANY", "PRICE", "CHANGE%", "VOLUME");
        System.out.println("-".repeat(80));

        for (Stock s : sorted) {
            String arrow = s.getChangePercent() >= 0 ? "▲" : "▼";
            System.out.printf("%-6s | %-20s | $%-9.2f | %s%+7.2f%% | %,d%n",
                    s.getSymbol(), s.getCompanyName(),
                    s.getCurrentPrice(), arrow,
                    s.getChangePercent(), s.getVolume());
        }
        System.out.println("=".repeat(80));
    }

    // Get top N gainers using sorting on ArrayList
    public List<Stock> getTopGainers(int n) {
        List<Stock> list = new ArrayList<>(stockMap.values());
        list.sort(Comparator.comparingDouble(Stock::getChangePercent).reversed());
        return list.subList(0, Math.min(n, list.size()));
    }

    // Get top N losers
    public List<Stock> getTopLosers(int n) {
        List<Stock> list = new ArrayList<>(stockMap.values());
        list.sort(Comparator.comparingDouble(Stock::getChangePercent));
        return list.subList(0, Math.min(n, list.size()));
    }

    public PriceHistory getHistory(String symbol) {
        return historyMap.getOrDefault(symbol.toUpperCase(), null);
    }

    public HashMap<String, Stock> getAllStocks() { return stockMap; }
}
