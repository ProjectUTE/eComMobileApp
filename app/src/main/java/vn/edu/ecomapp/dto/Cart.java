package vn.edu.ecomapp.dto;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    List<LineItem> lineItems = new ArrayList<>();

    public int getCartItemsCount() {
        if(lineItems == null) return 0;
        return  lineItems.size();
    }

    public int getDelivery() {
        return 0;
    }

    public int getTotal() {
        return getTotalItems() + getDelivery();
    }

    public List<LineItem> getLineItems() {
        return  lineItems;
    }

    public int getTotalItems() {
        int totalItems = 0;
        for (LineItem item : lineItems) {
            totalItems += item.getAmount();
        }
        return totalItems;
    }

    public void addLineItem(LineItem item) {
        int incomingItemTotal = item.getQuantity() * item.getPrice();
        if(incomingItemTotal != item.getAmount()) {
            item.setAmount(incomingItemTotal);
        }

        for(int i = 0; i < lineItems.size(); i++)  {
           if(lineItems.get(i).getProductId().equals(item.getProductId())) {
               int quantity = lineItems.get(i).getQuantity() + item.getQuantity();
               int itemTotal = lineItems.get(i).getPrice() * quantity;
               lineItems.get(i).setQuantity(quantity);
               lineItems.get(i).setAmount(itemTotal);
                return;
           }
       }
       lineItems.add(item);
    }

    public void removeLineItem(String id) {
        int positionExist = -1;
        for(int i = 0; i < lineItems.size(); i++) {
            if (lineItems.get(i).getId().equals(id)) {
                positionExist = i;
                break;
            }
        }
        if(positionExist == -1) return;
        int currentQuantity = lineItems.get(positionExist).getQuantity();
        int price = lineItems.get(positionExist).getPrice();
        if(currentQuantity > 1) {
            int incomingQuantity = currentQuantity - 1;
            lineItems.get(positionExist).setQuantity(incomingQuantity);
            lineItems.get(positionExist).setAmount(incomingQuantity * price);
        }
    }
}
