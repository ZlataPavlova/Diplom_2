package request;

import java.util.ArrayList;

public class OrderGeneration {
    public static IngredientsRequest getDefault() {
        ArrayList<String> ingredients = new ArrayList<>();
        ingredients.add("61c0c5a71d1f82001bdaaa70");
        ingredients.add("61c0c5a71d1f82001bdaaa74");
        ingredients.add("61c0c5a71d1f82001bdaaa6d");
        return new IngredientsRequest(ingredients);
    }

    public static IngredientsRequest getOrderWithoutIngredients() {
        ArrayList<String> ingredients = new ArrayList<>();
        return new IngredientsRequest(ingredients);
    }

    public static IngredientsRequest getOrderWithIncorrectIngredients() {
        ArrayList<String> ingredients = new ArrayList<>();
        ingredients.add("111111111111111111111");
        ingredients.add("0000000000000000000");
        return new IngredientsRequest(ingredients);
    }
}
