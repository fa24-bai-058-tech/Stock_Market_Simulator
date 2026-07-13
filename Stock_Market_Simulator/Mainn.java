package mainn;
import java.util.Scanner;
public class Mainn {
       public static void main(String[] args) {
        LinkedList market = new LinkedList();
        Queue activityQueue = new Queue();
        // Main method ke andar jahan LinkedList aur Queue banayi hai, wahan yeh likhein:
       Stack actionStack = new Stack("",null); 
        Scanner sc = new Scanner(System.in);
        int choice;
        do {
            System.out.println("\n--- Stock Market Menu ---");
            System.out.println("1. Add Stock");
            System.out.println("2. Delete Stock");
            System.out.println("3. Search Stock");
            System.out.println("4. Display All Stocks");
            System.out.println("5. View Queue Activity");
            System.out.println("6. Remove Queue Activity");
            System.out.println("7. Undo LAst action");
            System.out.println("8. Buy Stock");
            System.out.println("9. Sell Stock");
            System.out.println("0. Exit");
            System.out.print("Enter choice: ");
            choice = sc.nextInt();
            sc.nextLine(); 
            switch (choice) {
                case 1:
                    System.out.print("Enter Name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter ID: ");
                    int id = sc.nextInt();
                    System.out.print("Enter Price: ");
                    int price = sc.nextInt();
                   System.out.print("Enter Quantity: ");
                    int quantity=sc.nextInt();
                    market.addStock(name, id, price, quantity);
                    activityQueue.enque("Added stock: " + name);
                    Stock tempStock = new Stock(name, id, price, quantity);
                    actionStack.push("ADD", tempStock); 
                    break;
                case 2:
                    System.out.print("Enter Name to Delete: ");
                    String delName = sc.nextLine();
                    market.deletestock(delName);
                    activityQueue.enque("Deleted stock: " + delName);
                    break;
                case 3:
                    System.out.print("Enter Name to Search: ");
                    String srcName = sc.nextLine();
                    market.searchstock(srcName);
                    break;
                case 4:
                    market.displaystock();
                    break;
                case 5:
                    activityQueue.display();
                    break;
                case 6:
                    activityQueue.deque();
                    break;
                case 7:
                    Stack lastAction = actionStack.pop();
                    if (lastAction == null) {
                          System.out.println("Nothing to undo!");
                    } else {
                     if (lastAction.type.equals("ADD")) {
                    // Agar add hua tha, toh undo karne ke liye delete kar dein
                      market.deletestock(lastAction.stockState.sname);
                    System.out.println(" Undo: Added stock removed.");
                        }
                     else if (lastAction.type.equals("BUY")) {
    // Undo a buy by subtracting the quantity
                     market.sellStock(lastAction.stockState.sname, lastAction.stockState.quantity);
                     System.out.println(" Undo: Buying reversed.");
                        }
                    else if (lastAction.type.equals("SELL")) {
                     // Undo a sell by adding back the quantity
                     market.buyStock(lastAction.stockState.sname, lastAction.stockState.quantity);
                     System.out.println(" Undo: Selling reversed.");
                        }
                        }
                    break;
                case 8:
                     System.out.print("Enter Stock Name to Buy: ");
                     String buyName = sc.nextLine();
                     System.out.print("Enter Quantity: ");
                     int buyQty = sc.nextInt();
                     market.buyStock(buyName, buyQty);
                     activityQueue.enque("Bought " + buyQty + " shares of " + buyName);
                        // Track for undo (saving state)
                        Stock buyState = new Stock(buyName, 0, 0,0); 
                        buyState.quantity = buyQty; 
                        actionStack.push("BUY", buyState);
                        break;

                case 9:
                        System.out.print("Enter Stock Name to Sell: ");
                     String sellName = sc.nextLine();
                     System.out.print("Enter Quantity: ");
                        int sellQty = sc.nextInt();
                        market.sellStock(sellName, sellQty);
                     activityQueue.enque("Sold " + sellQty + " shares of " + sellName);
    
    // Track for undo
                     Stock sellState = new Stock(sellName, 0, 0,0);
                     sellState.quantity = sellQty;
                     actionStack.push("SELL", sellState);
                     break;


                case 0:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid Choice!");
            }
        } while (choice != 0);
        sc.close();
    }
}
    class Stock{
        String sname;
        int sid;
        int sprice;
        int quantity;
        public Stock(String sname,int sid,int sprice,int quantity){
            this.sname=sname;
            this.sid=sid;
            this.sprice=sprice;
            this.quantity=quantity;
        }
        public void stockdetails(){
            System.out.println("Stock name :"+sname +"Stock id :" +sid +"Price Rs.:"+ sprice + " Owned :" + quantity);
        }
    }   
    class Node{
        Stock stockdata;
        Node next;
        public Node(Stock stockdata){
            this.stockdata=stockdata;
            this.next=null;
        }
    } 
    class LinkedList{
        Node head=null;
        Node tail=null;
            public void addStock(String sname, int sid, int sprice,int quantity) {
            Stock newStock = new Stock(sname, sid, sprice, quantity);
            Node newNode = new Node(newStock);

            if(head==null){
                head=newNode;
                tail=newNode;
            }else{
                tail.next=newNode;
                tail=newNode;
            }
                    System.out.println(" Stock " + sname + " added successfully!");
           }
           public void buyStock(String name, int qty) {
    Node t = head;
    while (t != null) {
        if (t.stockdata.sname.equalsIgnoreCase(name)) {
            t.stockdata.quantity += qty;
            System.out.println(" Bought " + qty + " shares of " + name);
            return;
        }
        t = t.next;
    }
    System.out.println(" Stock not found in market!");
}

public void sellStock(String name, int qty) {
    Node t = head;
    while (t != null) {
        if (t.stockdata.sname.equalsIgnoreCase(name)) {
            if (t.stockdata.quantity >= qty) {
                t.stockdata.quantity -= qty;
                System.out.println(" Sold " + qty + " shares of " + name);
            } else {
                System.out.println(" Not enough shares to sell! You only own " + t.stockdata.quantity);
            }
            return;
        }
        t = t.next;
    }
    System.out.println(" Stock not found in market!");
}

        
        public void deletestock(String name){
            if(head==null){
                System.out.println("List is Empty");
                return;
            }
            if (head.stockdata.sname.equalsIgnoreCase(name)) {
            System.out.println(" Deleted: " + head.stockdata.sname);
            head = head.next;
            return;
        }

        // Case B: Baki list mein se delete karna
        Node temp = head;
        while (temp.next != null && !temp.next.stockdata.sname.equalsIgnoreCase(name)) {
            temp = temp.next;
        }

        if (temp.next == null) {
            System.out.println(" Stock not found. Deletion failed.");
        } else {
            System.out.println(" Deleted: " + temp.next.stockdata.sname);
            temp.next = temp.next.next; // Link bypass kar diya
        }
    }
    public void searchstock(String name){
        if(head==null){
            System.out.println("List is alredy empty");
            return;
        }
        else{
            Node t=head;
             while(t!=null){
                if(t.stockdata.sname.equalsIgnoreCase(name)){

                
            System.out.println("Stock  Found ");
            t.stockdata.stockdetails();
            return;}
            t=t.next;
        }
        System.out.println("Not found");
    }
           }
    public void displaystock(){
        if(head==null){
            System.out.println("No stock Avalible");
            return;
        }
        else{
            Node temp=head;
            int count =1;
            
            while(temp!=null){
                temp.stockdata.stockdetails();
                temp=temp.next;
                System.out.println(count+". ");
                count++;
            }
        }
    }   
}
    class Stack {
        String type;        
        Stock stockState;   
        Stack next;        

        public Stack top = null; 
   
public Stack(String type, Stock stockToSave) {
    this.type = type;
    
    // Yahan fix hai: Check for null
    if (stockToSave != null) {
        this.stockState = new Stock(stockToSave.sname, stockToSave.sid, stockToSave.sprice,stockToSave.quantity);
    } else {
        this.stockState = null; // ya new Stock("", 0, 0.0);
    }
    this.next = null;
}

        // 1. PUSH: Naya action history mein sab se upar rakhna
        public void push(String type, Stock stockToSave) {
            Stack newNode = new Stack(type, stockToSave);
            newNode.next = top;
            top = newNode;
        }

        // 2. POP: Sab se aakhri action ko nikalna aur return karna
        public Stack pop() {
            if (isEmpty()) {
                return null;
            }
            Stack popNode = top;
            top = top.next;
            return popNode; // Yeh batayega ke kaunsa stock aur kya action tha
        }

        // 3. IS EMPTY: Check karna ke history khali toh nahi
        public boolean isEmpty() {
            return top == null;
        }
    }
    class Queue{
        class Node{
            String activity;
            Node next;
            public Node(String activity){
                this.activity=activity;
                this.next=null;
            }
        }
        Node front;
        Node rear;

        public void enque(String activity){
            Node n=new Node(activity);

            if(front==null){
                front=rear=n;
            }
            else{
                rear.next=n;
                rear=n;
            }
            System.out.println("Activity Added "+activity);
        }

        public void deque(){
            if(front==null){
                System.out.println("Queue is empty");
                return;
            }
            else{
                front=front.next;
                if(front==null){
                    rear=null;
                }
            }
        }
        public void display(){
            if(front== null){
                System.out.println("Queue is Empty");
                return;
            }
            Node t=front;
            while(t!=null){
                System.out.println(t.activity);
                t=t.next;
              }
           }
        }
