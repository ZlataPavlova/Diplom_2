package request;

public class CustomerGeneration {
    public static Customer getDefault() {
        return new Customer("федя@test.ru", "2222", "федя");
    }
    public static Customer getRepeatCredentials() {
        return new Customer("федор@test.ru", "0000", "федор");
    }
    public static Customer getNewCredentials() {
        return new Customer("федор@test.ru", "1111", "федян");
    }
    public static Customer getFieldEmailEmpty() {
        return new Customer("", "1111", "сема");
    }
    public static Customer getFieldPasswordEmpty() {
        return new Customer("сема@test.ru", "", "сема");
    }
    public static Customer getFieldNameEmpty() {
        return new Customer("сема@test.ru", "0000", "");
    }
}
