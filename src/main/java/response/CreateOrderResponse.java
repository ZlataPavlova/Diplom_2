package response;

public class CreateOrderResponse {
    private boolean success;
    private Order order;
    private String name;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Order getOrderResponse() {
        return order;
    }

    public void setOrder(Order orderResponse) {
        this.order = order;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
