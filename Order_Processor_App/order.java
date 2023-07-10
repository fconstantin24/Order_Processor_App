public class order {
    private final String id;
    private final Integer number_of_products;

    public order(String id, Integer number_of_products) {
        this.id = id;
        this.number_of_products = number_of_products;
    }

    public String getId() {
        return id;
    }

    public Integer getNumber_of_products() {
        return number_of_products;
    }
}