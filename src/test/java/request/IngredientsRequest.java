package request;
import java.util.ArrayList;

public class IngredientsRequest {
    private ArrayList<String> ingredients;

    public IngredientsRequest(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }

    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }
}
