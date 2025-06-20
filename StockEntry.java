public class StockEntry {
    private int day;
    private int price;

    public StockEntry(int day, int price) {
        this.day = day;
        this.price = price;
    }

    public int getDay() { return day; }
    public int getPrice() { return price; }

    public void setPrice(int price) { this.price = price; }
}
