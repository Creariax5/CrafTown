package elc.florian.other;

public class InfoCity {
    private int habs_nb;
    private String habs;
    private String maire;
    private int lv;

    public int getHabs_nb() {
        return habs_nb;
    }

    public String getHabs() {
        return habs;
    }

    public String getMaire() {
        return maire;
    }

    public int getLv() {
        return lv;
    }

    public InfoCity(int habs_nb, String habs, String maire, int lv) {
        this.habs_nb = habs_nb;
        this.habs = habs;
        this.maire = maire;
        this.lv = lv;
    }
}
