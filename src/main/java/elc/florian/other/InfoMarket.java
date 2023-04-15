package elc.florian.other;

public class InfoMarket {
    private String material;
    private String type;
    private float price;
    private float taxe;
    private int product;
    private int coin;

    public String getMaterial() {
        return material;
    }

    public float getPrice() {
        return price;
    }

    public float getTaxe() {
        return taxe;
    }

    public int getProduct() {
        return product;
    }

    public int getCoin() {
        return coin;
    }

    public String getType() {
        return type;
    }

    public InfoMarket(String material, String type, float price, float taxe, int product, int coin) {
        this.material = material;
        this.type = type;
        this.price = price;
        this.taxe = taxe;
        this.product = product;
        this.coin = coin;
    }
}
