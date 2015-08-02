package nz.flatline.flatline.api.model;


public class Flat {

    public final int pk;
    public final String name;
    public final int numberOfMembers;

    public Flat(int pk, String name, int numberOfMembers) {
        this.pk = pk;
        this.name = name;
        this.numberOfMembers = numberOfMembers;
    }
}
