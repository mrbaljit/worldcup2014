////////////////////////////////////////////////////////////////////////////////
//
// Copyright (c) 2014, Suncorp Metway Limited. All rights reserved.
//
// This is unpublished proprietary source code of Suncorp Metway Limited.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
////////////////////////////////////////////////////////////////////////////////
// $Id$
// $Revision$
// $Date$
// $Author$
////////////////////////////////////////////////////////////////////////////////
package co.fifa.world.cup.org.data.mongodb;

import co.fifa.world.cup.org.data.mongodb.AggregationTests.CarDescriptor.Entry;
import com.mongodb.CommandResult;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.authentication.UserCredentials;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.util.Version;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.unwind;
import static org.springframework.data.mongodb.core.query.Criteria.where;

public class AggregationTests {

    public static void main(String[] args) throws UnknownHostException {
        final String DB_NAME = "aggregationTests";
        String LOCALHOST = "127.0.0.1";
        UserCredentials userCredentials = new UserCredentials("kick", "me");
        MongoClient client = new MongoClient(LOCALHOST);
        MongoTemplate mongoTemplate = new MongoTemplate(client, DB_NAME, userCredentials);

        CommandResult commandResult = mongoTemplate.executeCommand("{ buildInfo: 1 }");
        Version mongoVersion = Version.parse(commandResult.get("version").toString());
        System.out.println("MongoDB version : " + mongoVersion);

        //drop the collection
        mongoTemplate.dropCollection(Product.class);

        Product product = new Product("P1", "A", 1.99, 3, 0.05, 0.19);
        mongoTemplate.insert(product);

        //product = new Product("P2", "B", 1.99, 3, 0.05, 0.19);
        // mongoTemplate.insert(product);

        // arithmenticOperatorsInProjectionExample
        TypedAggregation<Product> agg = newAggregation(Product.class, //
                project("name", "netPrice", "spaceUnits") //
                        .and("netPrice").plus(1).as("netPricePlus1") //
                        .and("netPrice").minus(1).as("netPriceMinus1") //
                        .and("netPrice").multiply(2).as("netPriceMul2") //
                        .and("netPrice").divide(1.19).as("netPriceDiv119") //
                        .and("spaceUnits").mod(2).as("spaceUnitsMod2") //
                        .and("spaceUnits").plus("spaceUnits").as("spaceUnitsPlusSpaceUnits") //
                        .and("spaceUnits").minus("spaceUnits").as("spaceUnitsMinusSpaceUnits") //
                        .and("spaceUnits").multiply("spaceUnits").as("spaceUnitsMultiplySpaceUnits") //
                        .and("spaceUnits").divide("spaceUnits").as("spaceUnitsDivideSpaceUnits") //
                        .and("spaceUnits").mod("spaceUnits").as("spaceUnitsModSpaceUnits") //
        );

        AggregationResults<DBObject> result = mongoTemplate.aggregate(agg, DBObject.class);
        List<DBObject> resultList = result.getMappedResults();
        for (DBObject dbObject : resultList) {
            System.out.println(dbObject.get("_id"));
            System.out.println(dbObject.get("name"));
            System.out.println(dbObject.get("netPrice") + " : " + dbObject.get("netPricePlus1"));
            System.out.println(dbObject.get("netPrice") + " : " + dbObject.get("netPriceMinus1"));
            System.out.println(dbObject.get("netPrice") + " : " + dbObject.get("netPriceMul2"));
            System.out.println(dbObject.get("netPrice") + " : " + dbObject.get("netPriceDiv119"));
            System.out.println(dbObject.get("spaceUnits") + " : " + dbObject.get("spaceUnitsMod2"));
            System.out.println(product.spaceUnits + " : " + dbObject.get("spaceUnitsPlusSpaceUnits"));
            System.out.println(product.spaceUnits + " : " + dbObject.get("spaceUnitsMinusSpaceUnits"));
            System.out.println(product.spaceUnits + " : " + dbObject.get("spaceUnitsMultiplySpaceUnits"));
            System.out.println(product.spaceUnits + " : " + dbObject.get("spaceUnitsDivideSpaceUnits"));
            System.out.println(product.spaceUnits + " : " + dbObject.get("spaceUnitsModSpaceUnits"));
        }

        // expressionsInProjectionExample
        agg = newAggregation(Product.class, //
                project("name", "netPrice", "spaceUnits") //
                        .andExpression("netPrice + 1").as("netPricePlus1") //
                        .andExpression("netPrice - 1").as("netPriceMinus1") //
                        .andExpression("netPrice / 2").as("netPriceDiv2") //
                        .andExpression("netPrice * 1.19").as("grossPrice") //
                        .andExpression("spaceUnits % 2").as("spaceUnitsMod2") //
                        .andExpression("(netPrice * 0.8  + 1.2) * 1.19").as("grossPriceIncludingDiscountAndCharge") //

        );

        result = mongoTemplate.aggregate(agg, DBObject.class);
        resultList = result.getMappedResults();
        for (DBObject dbObject : resultList) {
            System.out.println(dbObject.get("_id"));
            System.out.println(dbObject.get("name"));
            System.out.println(dbObject.get("netPrice") + " : " + dbObject.get("netPricePlus1"));
            System.out.println(dbObject.get("netPrice") + " : " + dbObject.get("netPriceMinus1"));
            System.out.println(dbObject.get("netPrice") + " : " + dbObject.get("netPriceDiv2"));
            System.out.println(dbObject.get("spaceUnits") + " : " + dbObject.get("spaceUnitsMod2"));
            System.out.println(product.spaceUnits + " : " + dbObject.get("grossPriceIncludingDiscountAndCharge"));

        }

        // expressionsInProjectionExampleShowcase
        double shippingCosts = 1.2;
        agg = newAggregation(Product.class, //
                project("name", "netPrice") //
                        .andExpression("(netPrice * (1-discountRate)  + [0]) * (1+taxRate)", shippingCosts)
                        .as("salesPrice") //
        );
        result = mongoTemplate.aggregate(agg, DBObject.class);
        resultList = result.getMappedResults();
        for (DBObject dbObject : resultList) {
            System.out.println(dbObject.get("_id"));
            System.out.println(dbObject.get("name"));
            System.out.println(dbObject.get("salesPrice"));
        }

        // stringExpressionsInProjectionExample
        agg = newAggregation(Product.class, //
                project("name", "netPrice") //
                        .andExpression("concat(name, '_bubu')").as("name_bubu") //

        );

        result = mongoTemplate.aggregate(agg, DBObject.class);
        resultList = result.getMappedResults();
        for (DBObject dbObject : resultList) {
            System.out.println(dbObject.get("_id"));
            System.out.println(dbObject.get("name"));
            System.out.println(dbObject.get("name_bubu"));
        }

        // shouldAllowGroupByIdFields
        mongoTemplate.dropCollection(User.class);

        LocalDateTime now = new LocalDateTime();

        User user1 = new User("u1", new PushMessage("1", "aaa", now.toDate()));
        User user2 = new User("u2", new PushMessage("2", "bbb", now.minusDays(2).toDate()));
        User user3 = new User("u3", new PushMessage("3", "ccc", now.minusDays(1).toDate()));

        mongoTemplate.save(user1);
        mongoTemplate.save(user2);
        mongoTemplate.save(user3);

        Aggregation agg1 = newAggregation( //
                project("id", "msgs"), //
                unwind("msgs"), //
                match(where("msgs.createDate").gt(now.minusDays(2).toDate())), //
                group("id").push("msgs").as("msgs") //
        );

        AggregationResults<DBObject> results = mongoTemplate.aggregate(agg1, User.class, DBObject.class);
        List<DBObject> mappedResults = results.getMappedResults();
        for (DBObject dbObject : mappedResults) {
            System.out.println(dbObject.get("_id"));
            System.out.println(dbObject.get("msgs"));

        }

        // shouldAggregateOrderDataToAnInvoice
        mongoTemplate.dropCollection(Order.class);

        double taxRate = 0.19;

        LineItem product1 = new LineItem("1", "p1", 1.23);
        LineItem product2 = new LineItem("2", "p2", 0.87, 2);
        LineItem product3 = new LineItem("3", "p3", 5.33);
        Order order = new Order("o4711", "c42", new Date()).addItem(product1).addItem(product2).addItem(product3);

        mongoTemplate.save(order);

        AggregationResults<Invoice> invoiceAggregationResults = mongoTemplate.aggregate(newAggregation(Order.class, //
                match(where("id").is(order.getId())), unwind("items"), //
                project("id", "customerId", "items") //
                        .andExpression("items.price * items.quantity").as("lineTotal"), //
                group("id") //
                        .sum("lineTotal").as("netAmount") //
                        .addToSet("items").as("items"), //
                project("id", "items", "netAmount") //
                        .and("orderId").previousOperation() //
                        .andExpression("netAmount * [0]", taxRate).as("taxAmount") //
                        .andExpression("netAmount * (1 + [0])", taxRate).as("totalAmount") //
        ), Invoice.class);

        Invoice invoice = invoiceAggregationResults.getUniqueMappedResult();
        System.out.println("invoice.getTotalAmount() : " + invoice.getTotalAmount());
        System.out.println("invoice.getTaxAmount() : " + invoice.getTaxAmount());
        System.out.println("invoice.getOrderId() : " + invoice.getOrderId());
        System.out.println("invoice.getNetAmount() :" + invoice.getNetAmount());

        // shouldAllowGroupingByAliasedFieldDefinedInFormerAggregationStage
        mongoTemplate.dropCollection(CarPerson.class);

        CarPerson person1 = new CarPerson("first1", "last1",
                new CarDescriptor.Entry("MAKE1", "MODEL1", 2000),
                new CarDescriptor.Entry("MAKE1", "MODEL2", 2001),
                new CarDescriptor.Entry("MAKE2", "MODEL3", 2010),
                new CarDescriptor.Entry("MAKE3", "MODEL4", 2014));

        CarPerson person2 = new CarPerson("first2", "last2", new CarDescriptor.Entry("MAKE3", "MODEL4", 2014));

        CarPerson person3 = new CarPerson("first3", "last3", new CarDescriptor.Entry("MAKE2", "MODEL5", 2011));

        mongoTemplate.save(person1);
        mongoTemplate.save(person2);
        mongoTemplate.save(person3);

        TypedAggregation<CarPerson> carPersonTypedAggregation = Aggregation.newAggregation(CarPerson.class,
                unwind("descriptors.carDescriptor.entries"), //
                project() //
                        .and("descriptors.carDescriptor.entries.make").as("make") //
                        .and("descriptors.carDescriptor.entries.model").as("model") //
                        .and("firstName").as("firstName") //
                        .and("lastName").as("lastName"), //
                group("make"));

        //must have size of 3
        result = mongoTemplate.aggregate(carPersonTypedAggregation, DBObject.class);
        mappedResults = result.getMappedResults();
        for (DBObject dbObject : mappedResults) {
            System.out.println(dbObject.get("_id"));
        }

        // shouldSupportReturningCurrentAggregationRoot
        mongoTemplate.dropCollection(Person.class);
        mongoTemplate.save(new Person("p1_first", "p1_last", 25));
        mongoTemplate.save(new Person("p2_first", "p2_last", 32));
        mongoTemplate.save(new Person("p3_first", "p3_last", 25));
        mongoTemplate.save(new Person("p4_first", "p4_last", 15));
        List<DBObject> personsWithAge25 = mongoTemplate.find(Query.query(where("age").is(25)), DBObject.class,
                mongoTemplate.getCollectionName(Person.class));
        for (DBObject dbObject : personsWithAge25) {
            System.out.println(dbObject.get("_id"));
            System.out.println(dbObject.get("firstname"));
        }
        Aggregation aggAge = newAggregation(group("age").push(Aggregation.ROOT).as("users"));
        result = mongoTemplate.aggregate(aggAge, Person.class, DBObject.class);
        System.out.println(result.getMappedResults().size());
        DBObject o = (DBObject) result.getMappedResults().get(2);
        System.out.println(o.get("_id"));

        // shouldRetrieveDateTimeFragementsCorrectly
        mongoTemplate.dropCollection(ObjectWithDate.class);

        LocalDateTime dateTime = new DateTime() //
                .withYear(2015) //
                .withMonthOfYear(2) //
                .withDayOfMonth(8) //
                .withTime(8, 4, 5, 6).toLocalDateTime();

        ObjectWithDate owd = new ObjectWithDate(dateTime.toDate());
        mongoTemplate.insert(owd);

        ProjectionOperation dateProjection = Aggregation.project() //
                .and("dateValue").extractHour().as("hour") //
                .and("dateValue").extractMinute().as("min") //
                .and("dateValue").extractSecond().as("second") //
                .and("dateValue").extractMillisecond().as("millis") //
                .and("dateValue").extractYear().as("year") //
                .and("dateValue").extractMonth().as("month") //
                .and("dateValue").extractWeek().as("week") //
                .and("dateValue").extractDayOfYear().as("dayOfYear") //
                .and("dateValue").extractDayOfMonth().as("dayOfMonth") //
                .and("dateValue").extractDayOfWeek().as("dayOfWeek") //
                .andExpression("dateValue + 86400000").extractDayOfYear().as("dayOfYearPlus1Day") //
                .andExpression("dateValue + 86400000").project("dayOfYear").as("dayOfYearPlus1DayManually") //
                ;

        Aggregation dateAggregation = newAggregation(dateProjection);
        result = mongoTemplate.aggregate(dateAggregation, ObjectWithDate.class, DBObject.class);
        DBObject dbo = result.getMappedResults().get(0);
        System.out.println(dbo.get("year"));
        System.out.println(dbo.get("hour"));
        System.out.println(dbo.get("min"));
        System.out.println(dbo.get("dayOfMonth") + " : " + dateTime.getDayOfMonth());

        // shouldPerformDateProjectionOperatorsCorrectly
        mongoTemplate.dropCollection(Data.class);
        Data data = new Data();
        data.stringValue = "ABC";
        mongoTemplate.insert(data);

        TypedAggregation<Data> dataTypedAggregation = newAggregation(Data.class, project() //
                .andExpression("concat(stringValue, 'DE')").as("concat") //
                .andExpression("strcasecmp(stringValue,'XYZ')").as("strcasecmp") //
                .andExpression("substr(stringValue,1,1)").as("substr") //
                .andExpression("toLower(stringValue)").as("toLower") //
                .andExpression("toUpper(toLower(stringValue))").as("toUpper") //
        );


        results = mongoTemplate.aggregate(dataTypedAggregation, DBObject.class);
        dbo = results.getUniqueMappedResult();
        System.out.println(dbo.get("concat"));
        System.out.println(dbo.get("strcasecmp"));
        System.out.println(dbo.get("substr"));
        System.out.println(dbo.get("toLower"));
        System.out.println(dbo.get("toUpper"));


    }

    static class ObjectWithDate {

        Date dateValue;

        public ObjectWithDate(Date dateValue) {
            this.dateValue = dateValue;
        }
    }

    /**
     * @see
     */
    static class User {

        @Id
        String id;
        List<PushMessage> msgs;

        public User() {}

        public User(String id, PushMessage... msgs) {
            this.id = id;
            this.msgs = Arrays.asList(msgs);
        }
    }

    /**
     * @see
     */
    static class PushMessage {

        @Id String id;
        String content;
        Date createDate;

        public PushMessage() {}

        public PushMessage(String id, String content, Date createDate) {
            this.id = id;
            this.content = content;
            this.createDate = createDate;
        }
    }

    @org.springframework.data.mongodb.core.mapping.Document
    static class CarPerson {

        @Id private String id;
        private String firstName;
        private String lastName;
        private Descriptors descriptors;

        public CarPerson(String firstname, String lastname, Entry... entries) {
            this.firstName = firstname;
            this.lastName = lastname;

            this.descriptors = new Descriptors();

            this.descriptors.carDescriptor = new CarDescriptor(entries);
        }
    }

    @SuppressWarnings("unused")
    static class Descriptors {
        private CarDescriptor carDescriptor;
    }

    static class CarDescriptor {

        private List<Entry> entries = new ArrayList<AggregationTests.CarDescriptor.Entry>();

        public CarDescriptor(Entry... entries) {

            for (Entry entry : entries) {
                this.entries.add(entry);
            }
        }

        @SuppressWarnings("unused")
        static class Entry {
            private String make;
            private String model;
            private int year;

            public Entry() {}

            public Entry(String make, String model, int year) {
                this.make = make;
                this.model = model;
                this.year = year;
            }
        }
    }

}
