package response;

public class ResponseMessage {
    private  boolean messageSuccessCreateCustomer  = true;
    private  boolean messageUnsuccessfulCreateCustomer  = false;

    private  String messageError403DoubleCustomer  = "User already exists";
    private  String messageError403CustomerWithEmptyField  = "Email, password and name are required fields";

    private  String messageError403CustomerWithRepeatPassword  = "User with such email already exists";
    private  String messageError401IncorrectEmailOrPassword  = "email or password are incorrect";
    private  String messageError401Unauthorized  = "You should be authorised";
    private  String messageError400ShouldBeIngredients  = "Ingredient ids must be provided";

    public boolean isMessageSuccessCreateCustomer() {
        return messageSuccessCreateCustomer;
    }

    public String getMessageError403DoubleCustomer() {
        return messageError403DoubleCustomer;
    }

    public String getMessageError403CustomerWithEmptyField() {
        return messageError403CustomerWithEmptyField;
    }

    public String getMessageError401IncorrectEmailOrPassword() {
        return messageError401IncorrectEmailOrPassword;
    }

    public String getMessageError401Unauthorized() {
        return messageError401Unauthorized;
    }

    public boolean isMessageUnsuccessfulCreateCustomer() {
        return messageUnsuccessfulCreateCustomer;
    }

    public String getMessageError403CustomerWithRepeatPassword() {
        return messageError403CustomerWithRepeatPassword;
    }

    public String getMessageError400ShouldBeIngredients() {
        return messageError400ShouldBeIngredients;
    }
}
