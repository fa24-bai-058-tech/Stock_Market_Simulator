import java.util.LinkedList;
import java.util.Queue;

// ====================================================
// StockAlert.java - Price Alert Model
// ====================================================
class StockAlert {
    public enum AlertType { PRICE_HIGH, PRICE_LOW, VOLUME_SPIKE }

    private String    symbol;
    private AlertType alertType;
    private double    triggerValue;
    private String    message;

    public StockAlert(String symbol, AlertType alertType, double triggerValue, String message) {
        this.symbol       = symbol;
        this.alertType    = alertType;
        this.triggerValue = triggerValue;
        this.message      = message;
    }

    @Override
    public String toString() {
        return String.format("🔔 ALERT [%s] %s - %s (Trigger: $%.2f)",
                alertType, symbol, message, triggerValue);
    }
}

// ====================================================
// AlertQueue.java
// Uses Queue (DS) - FIFO for processing alerts in order
// ====================================================
public class AlertQueue {
    private Queue<StockAlert> queue;

    public AlertQueue() {
        queue = new LinkedList<>();
    }

    // Add alert to queue
    public void enqueue(StockAlert alert) {
        queue.offer(alert);
        System.out.println("⚠️  New alert queued: " + alert);
    }

    // Process next alert (FIFO)
    public StockAlert processNext() {
        if (queue.isEmpty()) {
            System.out.println("✅ No pending alerts.");
            return null;
        }
        StockAlert alert = queue.poll();
        System.out.println("🔄 Processing: " + alert);
        return alert;
    }

    // Check alerts against current stock price
    public void checkAlerts(Stock stock) {
        for (StockAlert alert : queue) {
            System.out.println("  Checking: " + alert + " for " + stock.getSymbol());
        }
    }

    public boolean isEmpty() { return queue.isEmpty(); }
    public int size()        { return queue.size(); }

    public void displayAll() {
        System.out.println("\n🔔 Pending Alerts (" + queue.size() + "):");
        if (queue.isEmpty()) {
            System.out.println("  No pending alerts.");
            return;
        }
        for (StockAlert a : queue) {
            System.out.println("  " + a);
        }
    }
}
