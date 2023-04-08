package vn.edu.ecomapp.dto;

import java.util.ArrayList;
import java.util.List;

import vn.edu.ecomapp.model.LineItem;

public class Cart {
    List<LineItem> lineItems;
    int itemsTotal;


    int delivery;

    int total;

    public Cart() {
        lineItems = new ArrayList<>();
        itemsTotal = 0;
        total = 0;
        delivery = 0;
    }

    public int getDelivery() {
        return delivery;
    }

    public void setDelivery(int delivery) {
        this.delivery = delivery;
    }

    public List<LineItem> getLineItems() {
        return lineItems;
    }

    public void setLineItems(List<LineItem> lineItems) {
        this.lineItems = lineItems;
    }

    public int getItemsTotal() {
        return itemsTotal;
    }

    public void setItemsTotal(int itemsTotal) {
        this.itemsTotal = itemsTotal;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

}
