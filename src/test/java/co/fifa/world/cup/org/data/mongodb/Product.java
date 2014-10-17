package co.fifa.world.cup.org.data.mongodb;

/**
 * Created by U342597 on 16/10/2014.
 */
public class Product {

    String id;
    String name;
    double netPrice;
    int spaceUnits;
    double discountRate;
    double taxRate;

    public Product() {}

    public Product(String id, String name, double netPrice, int spaceUnits, double discountRate, double taxRate) {
        this.id = id;
        this.name = name;
        this.netPrice = netPrice;
        this.spaceUnits = spaceUnits;
        this.discountRate = discountRate;
        this.taxRate = taxRate;
    }

}
