package objects;

public class SellOffer {

    private String uuid;
    private String player;
    private String material;
    private String nbt;
    private boolean custom;
    private double price;
    private int totalqty;
    private int soldQty;
    private double money;
    private String date;

    public SellOffer() {
        uuid = null;
        player = null;
        material = null;
        nbt = null;
        custom = false;
        price = 0.0;
        totalqty = 0;
        soldQty = 0;
        money = 0;
        date = null;
    }

    public String getUuid() {
        return uuid;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getNbt() {
        return nbt;
    }

    public void setNbt(String nbt) {
        this.nbt = nbt;
    }

    public boolean isCustom() {
        return custom;
    }

    public void setCustom(boolean custom) {
        this.custom = custom;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getTotalqty() {
        return totalqty;
    }

    public void setTotalqty(int totalqty) {
        this.totalqty = totalqty;
    }

    public int getSoldQty() {
        return soldQty;
    }

    public void setSoldQty(int soldqty) {
        this.soldQty = soldqty;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
