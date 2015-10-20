package integration.shop.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class OrderItem implements Serializable {

    private int count;
    private BigDecimal priceAfterDiscount;
    private Item item;

    public BigDecimal totalPriceAfterDiscount() {
        if(priceAfterDiscount == null) {
            throw new IllegalArgumentException("Property priceAfterDiscount can no be null");
        }
        return  priceAfterDiscount.multiply(BigDecimal.valueOf(count));
    }

    public String getItemName() {
        return item.getName();
    }

    public BigDecimal getItemPrice() {
        return item.getPrice();
    }

    public  boolean isSimCard(){
        return item instanceof SimCard;
    }

    public boolean isSmartphone() {
        return  item instanceof Smartphone;
    }

    public  boolean isTablet() {
        return  item instanceof Tablet;
    }
}
