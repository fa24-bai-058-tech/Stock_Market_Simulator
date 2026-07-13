import java.util.ArrayList;
import java.util.HashMap;

// ====================================================
// PortfolioItem.java - Holding in portfolio
// ====================================================
class PortfolioItem {
    private String symbol;
    private int    quantity;
    private double avgBuyPrice;

    public PortfolioItem(String symbol, int quantity, double avgBuyPrice) {
        this.symbol      = symbol;
        this.quantity    = quantity;
        this.avgBuyPrice = avgBuyPrice;
    }

    public String getSymbol()       { return symbol; }
    public int    getQuantity()     { return quantity; }
    public double getAvgBuyPrice()  { return avgBuyPrice; }

    public void addShares(int qty, double price) {
        double totalCost = (avgBuyPrice * quantity) + (price * qty);
        quantity    += qty;
        avgBuyPrice  = totalCost / quantity;
    }

    public void removeShares(int qty) {
        quantity = Math.max(0, quantity - qty);
    }

    public double getProfitLoss(double currentPrice) {
        return (currentPrice - avgBuyPrice) * quantity;
    }

    public double getProfitLossPercent(double currentPrice) {
        return ((currentPrice - avgBuyPrice) / avgBuyPrice) * 100;
    }
}

// ====================================================
// Portfolio.java
// Uses ArrayList (DS) + HashMap for fast lookup
// ====================================================
public class Portfolio {
    private String ownerName;

    // ArrayList to maintain ordered holdings
    private ArrayList<PortfolioItem> holdings;

    // HashMap for O(1) lookup by symbol
    private HashMap<String, PortfolioItem> holdingMap;

    private double cashBalance;

    public Portfolio(String ownerName, double initialCash) {
        this.ownerName   = ownerName;
        this.cashBalance = initialCash;
        holdings         = new ArrayList<>();
        holdingMap       = new HashMap<>();
    }

    // Buy stock
    public boolean buyStock(Stock stock, int quantity) {
        double totalCost = stock.getCurrentPrice() * quantity;
        if (totalCost > cashBalance) {
            System.out.printf("❌ Insufficient funds. Need: $%.2f | Available: $%.2f%n",
                    totalCost, cashBalance);
            return false;
        }

        cashBalance -= totalCost;

        if (holdingMap.containsKey(stock.getSymbol())) {
            holdingMap.get(stock.getSymbol()).addShares(quantity, stock.getCurrentPrice());
        } else {
            PortfolioItem item = new PortfolioItem(stock.getSymbol(), quantity, stock.getCurrentPrice());
            holdings.add(item);
            holdingMap.put(stock.getSymbol(), item);
        }

        System.out.printf("✅ Bought %d shares of %s at $%.2f | Cash left: $%.2f%n",
                quantity, stock.getSymbol(), stock.getCurrentPrice(), cashBalance);
        return true;
    }

    // Sell stock
    public boolean sellStock(Stock stock, int quantity) {
        if (!holdingMap.containsKey(stock.getSymbol())) {
            System.out.println("❌ You don't own " + stock.getSymbol());
            return false;
        }

        PortfolioItem item = holdingMap.get(stock.getSymbol());
        if (item.getQuantity() < quantity) {
            System.out.printf("❌ Not enough shares. Own: %d | Requested: %d%n",
                    item.getQuantity(), quantity);
            return false;
        }

        double revenue = stock.getCurrentPrice() * quantity;
        cashBalance += revenue;
        item.removeShares(quantity);

        if (item.getQuantity() == 0) {
            holdings.remove(item);
            holdingMap.remove(stock.getSymbol());
        }

        System.out.printf("✅ Sold %d shares of %s at $%.2f | Cash now: $%.2f%n",
                quantity, stock.getSymbol(), stock.getCurrentPrice(), cashBalance);
        return true;
    }

    // Display portfolio
    public void displayPortfolio(StockMarket market) {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("       💼  PORTFOLIO: " + ownerName.toUpperCase());
        System.out.println("=".repeat(80));
        System.out.printf("%-6s | %-6s | %-10s | %-10s | %-12s | %s%n",
                "SYM", "QTY", "AVG BUY", "CUR PRICE", "VALUE", "P&L");
        System.out.println("-".repeat(80));

        double totalValue = cashBalance;

        // Iterate ArrayList - maintains insertion order
        for (PortfolioItem item : holdings) {
            Stock stock = market.getStock(item.getSymbol());
            if (stock == null) continue;

            double curPrice   = stock.getCurrentPrice();
            double value      = curPrice * item.getQuantity();
            double pl         = item.getProfitLoss(curPrice);
            double plPercent  = item.getProfitLossPercent(curPrice);
            totalValue       += value;

            String plSign = pl >= 0 ? "+" : "";
            System.out.printf("%-6s | %-6d | $%-9.2f | $%-9.2f | $%-11.2f | %s%.2f (%.1f%%)%n",
                    item.getSymbol(), item.getQuantity(),
                    item.getAvgBuyPrice(), curPrice, value,
                    plSign, pl, plPercent);
        }

        System.out.println("-".repeat(80));
        System.out.printf("Cash Balance: $%.2f%n", cashBalance);
        System.out.printf("Total Portfolio Value: $%.2f%n", totalValue);
        System.out.println("=".repeat(80));
    }

    public double getCashBalance() { return cashBalance; }
    public String getOwnerName()   { return ownerName; }
}
