package request;

public class CustomerCredentials {
    private String email;
    private String password;
    private String name;

    public CustomerCredentials(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public static CustomerCredentials from(Customer customer) {
        return new CustomerCredentials(customer.getEmail(), customer.getPassword(), customer.getName());
    }

    public static CustomerCredentials getWrongInFieldPassword(Customer customer) {
        return new CustomerCredentials(customer.getEmail(), customer.getwrongPassword(), customer.getName());
    }

    public static CustomerCredentials getWrongInFieldEmail(Customer customer) {
        return new CustomerCredentials(customer.getwrongEmail(), customer.getPassword(), customer.getName());
    }

}
