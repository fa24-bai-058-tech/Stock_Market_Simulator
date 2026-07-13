import java.util.Stack;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// ====================================================
// Transaction.java - Represents a Buy/Sell transaction
// ====================================================
class Transaction {
    public enum Type { BUY, SELL }

    private String symbol;
    private Type type;
    private double price;
    private int quantity;
    private LocalDateTime timestamp;

    public Transaction(String symbol, Type type, double price, int quantity) {
        this.symbol    = symbol;
        this.type      = type;
        this.price     = price;
        this.quantity  = quantity;
        this.timestamp = LocalDateTime.now();
    }

    public String getSymbol()   { return symbol; }
    public Type   getType()     { return type; }
    public double getPrice()    { return price; }
    public int    getQuantity() { return quantity; }
    public double getTotalValue(){ return price * quantity; }

    @Override
    public String toString() {
        String fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                                      .format(timestamp);
        return String.format("[%s] %s %s | Qty: %d | Price: $%.2f | Total: $%.2f",
                fmt, type, symbol, quantity, price, getTotalValue());
    }
}

// ====================================================
// TransactionStack.java
// Uses Stack (DS) - LIFO for undo-last-trade feature
// ====================================================
class TransactionStack {
    private Stack<Transaction> stack;

    public TransactionStack() {
        stack = new Stack<>();
    }

    // Push new transaction
    public void push(Transaction t) {
        stack.push(t);
        System.out.println("✅ Transaction recorded: " + t);
    }

    // Undo last transaction (LIFO)
    public Transaction undo() {
        if (stack.isEmpty()) {
            System.out.println("❌ No transactions to undo.");
            return null;
        }
        Transaction t = stack.pop();
        System.out.println("↩️  Undid transaction: " + t);
        return t;
    }

    // Peek last transaction
    public Transaction peek() {
        if (stack.isEmpty()) return null;
        return stack.peek();
    }

    public boolean isEmpty() { return stack.isEmpty(); }
    public int size()        { return stack.size(); }

    public void displayAll() {
        System.out.println("\n📋 All Transactions (most recent first):");
        if (stack.isEmpty()) {
            System.out.println("  No transactions yet.");
            return;
        }
        Stack<Transaction> temp = new Stack<>();
        temp.addAll(stack);
        while (!temp.isEmpty()) {
            System.out.println("  " + temp.pop());
        }
    }
}
