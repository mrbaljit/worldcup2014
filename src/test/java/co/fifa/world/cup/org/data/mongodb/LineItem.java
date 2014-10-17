package co.fifa.world.cup.org.data.mongodb;

/**
 * Created by U342597 on 16/10/2014.
 */
public class LineItem {

    final String id;

    final String caption;

    final double price;

    int quantity = 1;

    @SuppressWarnings("unused")
    private LineItem() {
        this(null, null, 0.0, 0);
    }

    public LineItem(String id, String caption, double price) {
        this.id = id;
        this.caption = caption;
        this.price = price;
    }

    public LineItem(String id, String caption, double price, int quantity) {
        this(id, caption, price);
        this.quantity = quantity;
    }

}
