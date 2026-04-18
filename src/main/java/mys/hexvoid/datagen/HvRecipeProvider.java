package mys.hexvoid.datagen;

import mys.hexvoid.item.tags.HexvoidTags;
import mys.hexvoid.items.EnlighteningAppleItem;
import mys.hexvoid.items.HexvoidItems;
import mys.hexvoid.items.MindStaffItem;
import mys.hexvoid.recipe.WeavingMediaRecipeBuilder;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class HvRecipeProvider extends RecipeProvider {
    public HvRecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(@NotNull Consumer<FinishedRecipe> writer) {
        var output = ItemStack.EMPTY;

        // mind staff
        output = HexvoidItems.mind_staff.get().getDefaultInstance();
        MindStaffItem.setCanStore(output);

        WeavingMediaRecipeBuilder.weavingMedia(
                Ingredient.of(HexvoidItems.mind_staff.get()),
                output
        ).save(writer, HexvoidTags.RECIPE_WEVING_MIND_STAFF);

        // enlightening apple
        output = HexvoidItems.enlightening_apple.get().getDefaultInstance();
        EnlighteningAppleItem.madeWeaved(output);

        WeavingMediaRecipeBuilder.weavingMedia(
                Ingredient.of(HexvoidItems.enlightening_apple.get()),
                output
        ).save(writer, HexvoidTags.RECIPE_WEVING_ENLIGHTENING_APPLE);
    }
}
