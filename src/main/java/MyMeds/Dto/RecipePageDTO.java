package MyMeds.Dto;

import MyMeds.Services.RecipeService;

import java.util.List;

public class RecipePageDTO {
    private List<RecipeService.recipeDTO> recipes;
    private int totalPages;

    public RecipePageDTO(List<RecipeService.recipeDTO> recipes, int totalPages) {
        this.recipes = recipes;
        this.totalPages = totalPages;
    }

    public List<RecipeService.recipeDTO> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<RecipeService.recipeDTO> recipes) {
        this.recipes = recipes;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
