package me.fjerreiro.sentineleconomy.objects;

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

    public SellOffer(String uuid, String player, String material, String nbt, Boolean custom, double price, int totalqty, int soldQty, double money, String date) {
        this.uuid = uuid;
        this.player = player;
        this.material = material;
        this.nbt = nbt;
        this.custom = custom;
        this.price = price;
        this.totalqty = totalqty;
        this.soldQty = soldQty;
        this.money = money;
        this.date = date;
    }

    //Get and set methods
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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

    public void setSoldQty(int soldQty) {
        this.soldQty = soldQty;
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

    //Change object
    public void changeSellOffer(String uuid, String player, String material, String nbt, boolean custom, double price, int totalqty, int soldQty, double money, String date) {
            if (uuid != null) {
                this.uuid = uuid;
            }
            if (player != null) {
                this.player = player;
            }
            if (material != null) {
                this.material = material;
            }
            if (nbt != null) {
                this.nbt = nbt;
            }
            this.custom = custom;
            if (price >= 0) {
                this.price = price;
            }
            if (totalqty >= 0) {
                this.totalqty = totalqty;
            }
            if (soldQty >= 0) {
                this.soldQty = soldQty;
            }
            if (money >= 0) {
                this.money = money;
            }
            if (date != null) {
                this.date = date;
            }
    }
}
