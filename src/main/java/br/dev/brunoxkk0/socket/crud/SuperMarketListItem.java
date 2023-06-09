package br.dev.brunoxkk0.socket.crud;

public class SuperMarketListItem {

    private final int id;
    private String name;
    private int quantity;
    private boolean checked;

    public SuperMarketListItem(int id, String name, int quantity, boolean checked) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.checked = checked;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
