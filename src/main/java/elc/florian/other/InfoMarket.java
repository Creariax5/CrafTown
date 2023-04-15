package elc.florian.other;

public class InfoMarket {
    private String material;
    private String type;
    private float price;
    private float taxe;
    private int buy;
    private int sell;

    public String getMaterial() {
        return material;
    }

    public float getPrice() {
        return price;
    }

    public float getTaxe() {
        return taxe;
    }

    public int getBuy() {
        return buy;
    }

    public int getSell() {
        return sell;
    }

    public String getType() {
        return type;
    }

    public InfoMarket(String material, String type, float price, float taxe, int buy, int sell) {
        this.material = material;
        this.type = type;
        this.price = price;
        this.taxe = taxe;
        this.buy = buy;
        this.sell = sell;
    }
}
