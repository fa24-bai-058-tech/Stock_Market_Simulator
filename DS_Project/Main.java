import java.util.Scanner;
import java.util.List;

// ====================================================
// Main.java - Financial Stock Analysis System
// Data Structures Used:
//   1. HashMap     - StockMarket (O(1) lookup)
//   2. LinkedList  - PriceHistory (O(1) add/remove)
//   3. Stack       - Transaction history (undo)
//   4. Queue       - Alert processing (FIFO)
//   5. ArrayList   - Portfolio holdings (ordered)
// ====================================================
public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("FINANCIAL STOCK ANALYSIS SYSTEM");
        // Initialize Market (HashMap)
        StockMarket market = new StockMarket();

        // Initialize Transaction Stack
        TransactionStack txStack = new TransactionStack();

        // Initialize Alert Queue
        AlertQueue alertQueue = new AlertQueue();

        // Load sample stocks
        loadSampleData(market, alertQueue);

        // Initialize Portfolio
        System.out.print("\nEnter your name: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) name = "Investor";

        Portfolio portfolio = new Portfolio(name, 50000.00);
        System.out.printf("💰 Welcome %s! Starting balance: $50,000.00%n", name);

        // Main Menu Loop
        boolean running = true;
        while (running) {
            printMenu();
            System.out.print("Enter choice: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    market.displayMarket();
                    break;

                case "2":
                    System.out.print("Enter stock symbol: ");
                    String sym = scanner.nextLine().trim().toUpperCase();
                    Stock found = market.getStock(sym);
                    if (found != null) {
                        System.out.println("\n📊 Stock Details:");
                        System.out.println("  " + found);
                        System.out.printf("  High: $%.2f | Low: $%.2f%n",
                                found.getHighPrice(), found.getLowPrice());

                        PriceHistory hist = market.getHistory(sym);
                        if (hist != null) hist.displayHistory();
                    } else {
                        System.out.println("❌ Stock not found.");
                    }
                    break;

                case "3": // Buy Stock
                    System.out.print("Enter symbol to BUY: ");
                    String buySym = scanner.nextLine().trim().toUpperCase();
                    Stock buyStock = market.getStock(buySym);
                    if (buyStock == null) { System.out.println("❌ Not found."); break; }
                    System.out.print("Enter quantity: ");
                    try {
                        int qty = Integer.parseInt(scanner.nextLine().trim());
                        if (portfolio.buyStock(buyStock, qty)) {
                            txStack.push(new Transaction(buySym, Transaction.Type.BUY,
                                    buyStock.getCurrentPrice(), qty));
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("❌ Invalid quantity.");
                    }
                    break;

                case "4": // Sell Stock
                    System.out.print("Enter symbol to SELL: ");
                    String sellSym = scanner.nextLine().trim().toUpperCase();
                    Stock sellStock = market.getStock(sellSym);
                    if (sellStock == null) { System.out.println("❌ Not found."); break; }
                    System.out.print("Enter quantity: ");
                    try {
                        int qty = Integer.parseInt(scanner.nextLine().trim());
                        if (portfolio.sellStock(sellStock, qty)) {
                            txStack.push(new Transaction(sellSym, Transaction.Type.SELL,
                                    sellStock.getCurrentPrice(), qty));
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("❌ Invalid quantity.");
                    }
                    break;

                case "5":
                    portfolio.displayPortfolio(market);
                    break;

                case "6":
                    txStack.displayAll();
                    break;

                case "7":
                    txStack.undo();
                    break;

                case "8":
                    alertQueue.displayAll();
                    System.out.print("\nProcess next alert? (y/n): ");
                    if (scanner.nextLine().trim().equalsIgnoreCase("y")) {
                        alertQueue.processNext();
                    }
                    break;

                case "9":
                    System.out.print("Enter symbol to update: ");
                    String updSym = scanner.nextLine().trim().toUpperCase();
                    System.out.print("Enter new price: $");
                    try {
                        double newPrice = Double.parseDouble(scanner.nextLine().trim());
                        market.updatePrice(updSym, newPrice);
                    } catch (NumberFormatException e) {
                        System.out.println("❌ Invalid price.");
                    }
                    break;

                case "10":
                    System.out.println("\n🏆 Top 3 Gainers:");
                    List<Stock> gainers = market.getTopGainers(3);
                    for (int i = 0; i < gainers.size(); i++) {
                        System.out.printf("  %d. %s%n", i + 1, gainers.get(i));
                    }
                    System.out.println("\n📉 Top 3 Losers:");
                    List<Stock> losers = market.getTopLosers(3);
                    for (int i = 0; i < losers.size(); i++) {
                        System.out.printf("  %d. %s%n", i + 1, losers.get(i));
                    }
                    break;

                case "0":
                    System.out.println("\n👋 Thank you for using Financial Stock Analysis System!");
                    System.out.println("   Developed with ❤️  using Java Data Structures");
                    running = false;
                    break;

                default:
                    System.out.println("❌ Invalid option. Try again.");
            }
        }

        scanner.close();
    }

    // Load sample market data
    private static void loadSampleData(StockMarket market, AlertQueue alertQueue) {
        System.out.println("\n📥 Loading market data...");

        market.addStock(new Stock("AAPL", "Apple Inc.",          178.50, 175.00, 180.00, 173.00, 52_000_000));
        market.addStock(new Stock("GOOGL","Alphabet Inc.",        141.20, 138.50, 143.00, 137.50, 21_000_000));
        market.addStock(new Stock("MSFT", "Microsoft Corp.",      378.90, 380.00, 382.00, 376.00, 18_000_000));
        market.addStock(new Stock("AMZN", "Amazon.com Inc.",      184.30, 182.00, 186.00, 181.50, 29_000_000));
        market.addStock(new Stock("TSLA", "Tesla Inc.",           245.60, 252.00, 255.00, 240.00, 85_000_000));
        market.addStock(new Stock("NVDA", "NVIDIA Corporation",   875.40, 860.00, 880.00, 858.00, 43_000_000));
        market.addStock(new Stock("META", "Meta Platforms Inc.",  505.20, 500.00, 510.00, 498.00, 16_000_000));
        market.addStock(new Stock("NFLX", "Netflix Inc.",         632.10, 628.00, 637.00, 625.00, 8_000_000));

        // Add historical prices (LinkedList)
        double[] aaplHistory = {168, 170, 172, 174, 173, 175, 176, 178, 177, 178.50};
        for (double p : aaplHistory) market.getHistory("AAPL").addPrice(p);

        double[] tslaHistory = {260, 258, 255, 250, 248, 252, 249, 247, 251, 245.60};
        for (double p : tslaHistory) market.getHistory("TSLA").addPrice(p);

        // Add sample alerts (Queue)
        alertQueue.enqueue(new StockAlert("AAPL", StockAlert.AlertType.PRICE_HIGH,
                185.00, "AAPL approaching resistance level"));
        alertQueue.enqueue(new StockAlert("TSLA", StockAlert.AlertType.PRICE_LOW,
                240.00, "TSLA at support level - watch for bounce"));
        alertQueue.enqueue(new StockAlert("NVDA", StockAlert.AlertType.VOLUME_SPIKE,
                900.00, "NVDA high volume activity detected"));

        System.out.println("✅ Market data loaded successfully!\n");
    }

    private static void printMenu() {
        System.out.println("\n┌─────────────────────────────────────────┐");
        System.out.println("│         📈  MAIN MENU  📈               │");
        System.out.println("├─────────────────────────────────────────┤");
        System.out.println("│  1. View Market (All Stocks)            │");
        System.out.println("│  2. Search Stock + Price History        │");
        System.out.println("│  3. Buy Stock                           │");
        System.out.println("│  4. Sell Stock                          │");
        System.out.println("│  5. View My Portfolio                   │");
        System.out.println("│  6. View Transactions (Stack)           │");
        System.out.println("│  7. Undo Last Transaction (Stack)       │");
        System.out.println("│  8. View/Process Alerts (Queue)         │");
        System.out.println("│  9. Update Stock Price (LinkedList)     │");
        System.out.println("│ 10. Top Gainers & Losers                │");
        System.out.println("│  0. Exit                                │");
        System.out.println("└─────────────────────────────────────────┘");
    }
}
