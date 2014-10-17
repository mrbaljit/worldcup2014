package co.fifa.world.cup.org.data.mongodb;

import org.bson.types.ObjectId;

/**
 * Created by U342597 on 17/10/2014.
 */
public class People {

    private final ObjectId id;

    private String firstName;

    private int age;

    private People friend;

    private boolean active = true;

    public People() {
        this.id = new ObjectId();
    }

    @Override
    public String toString() {
        return "People [id=" + id + ", firstName=" + firstName + ", age=" + age + ", friend=" + friend + "]";
    }

    public People(ObjectId id, String firstname) {
        this.id = id;
        this.firstName = firstname;
    }

    public People(String firstname, int age) {
        this();
        this.firstName = firstname;
        this.age = age;
    }

    public People(String firstname) {
        this();
        this.firstName = firstname;
    }

    public ObjectId getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public People getFriend() {
        return friend;
    }

    public void setFriend(People friend) {
        this.friend = friend;
    }

    /**
     * @return the active
     */
    public boolean isActive() {
        return active;
    }

    /*
      * (non-Javadoc)
      *
      * @see java.lang.Object#equals(java.lang.Object)
      */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (!(getClass().equals(obj.getClass()))) {
            return false;
        }

        People that = (People) obj;

        return this.id == null ? false : this.id.equals(that.id);
    }

    /* (non-Javadoc)
      * @see java.lang.Object#hashCode()
      */
    @Override
    public int hashCode() {
        return id.hashCode();
    }

}
