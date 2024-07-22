package me.fjerreiro.sentineleconomy.objects;

public class BuyOffer {

    private String uuid;
    private String player;
    private String material;
    private String nbt;
    private boolean custom;
    private double price;
    private int totalqty;
    private int boughtqty;
    private double money;
    private String date;

    public BuyOffer(String uuid, String player, String material, String nbt, boolean custom, double price, int totalqty, int boughtqty, double money, String date) {
        this.uuid = uuid;
        this.player = player;
        this.material = material;
        this.nbt = nbt;
        this.custom = custom;
        this.price = price;
        this.totalqty = totalqty;
        this.boughtqty = boughtqty;
        this.money = money;
        this.date = date;
    }

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

    public int getBoughtqty() {
        return boughtqty;
    }

    public void setBoughtqty(int boughtqty) {
        this.boughtqty = boughtqty;
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

    public void changeBuyOffer(String uuid, String player, String material, String nbt, boolean custom, double price, int totalqty, int boughtqty, double money, String date) {
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
        if (boughtqty >= 0) {
            this.boughtqty = boughtqty;
        }
        if (money >= 0) {
            this.money = money;
        }
        if (date != null) {
            this.date = date;
        }
    }
}
