import java.util.LinkedList;
import java.util.Iterator;

// ====================================================
// PriceHistory.java
// Uses LinkedList (DS) to store price history
// ====================================================
public class PriceHistory {
    private String symbol;
    private LinkedList<Double> prices;
    private int maxSize;

    public PriceHistory(String symbol, int maxSize) {
        this.symbol = symbol;
        this.maxSize = maxSize;
        this.prices = new LinkedList<>();
    }

    // Add new price to history (remove oldest if full)
    public void addPrice(double price) {
        if (prices.size() >= maxSize) {
            prices.removeFirst();  // O(1) - LinkedList advantage
        }
        prices.addLast(price);
    }

    // Get moving average using LinkedList traversal
    public double getMovingAverage(int period) {
        if (prices.size() < period) return 0;

        double sum = 0;
        int count = 0;
        Iterator<Double> it = prices.descendingIterator();
        while (it.hasNext() && count < period) {
            sum += it.next();
            count++;
        }
        return sum / period;
    }

    // Get highest price in history
    public double getMaxPrice() {
        return prices.stream().mapToDouble(Double::doubleValue).max().orElse(0);
    }

    // Get lowest price in history
    public double getMinPrice() {
        return prices.stream().mapToDouble(Double::doubleValue).min().orElse(0);
    }

    public LinkedList<Double> getPrices() { return prices; }
    public String getSymbol()             { return symbol; }

    public void displayHistory() {
        System.out.println("\n📈 Price History for " + symbol + ":");
        System.out.println("  Prices (oldest → newest): " + prices);
        System.out.printf("  5-Day Moving Avg: $%.2f%n", getMovingAverage(5));
        System.out.printf("  Max: $%.2f | Min: $%.2f%n", getMaxPrice(), getMinPrice());
    }
}
