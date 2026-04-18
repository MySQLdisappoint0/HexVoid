package mys.hexvoid.recipe;

import com.google.gson.JsonObject;
import mys.hexvoid.registry.HexvoidRecipes;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.crafting.CraftingHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record WeavingMediaRecipe(
        ResourceLocation id,
        Ingredient input,
        ItemStack output
) implements Recipe<SimpleContainer> {

    @Override
    public boolean matches(@NotNull SimpleContainer container, @NotNull Level level) {
        return false;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull SimpleContainer container, @NotNull RegistryAccess registryAccess) {
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return false;
    }

    @Override
    public @NotNull ItemStack getResultItem(@NotNull RegistryAccess registryAccess) {
        return output.copy();
    }

    @Override
    public @NotNull ResourceLocation getId() {
        return id;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return HexvoidRecipes.WEAVING_MEDIA_SERIALIZER.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return HexvoidRecipes.WEAVING_MEDIA_TYPE.get();
    }


    public static class Serializer implements RecipeSerializer<WeavingMediaRecipe> {
        @Override
        public @NotNull WeavingMediaRecipe fromJson(@NotNull ResourceLocation id, @NotNull JsonObject json) {
            var input = Ingredient.fromJson(json.get("input"));
            var output = CraftingHelper.getItemStack(GsonHelper.getAsJsonObject(json, "output"), true);
            return new WeavingMediaRecipe(id, input, output);
        }

        @Override
        public @Nullable WeavingMediaRecipe fromNetwork(@NotNull ResourceLocation id, @NotNull FriendlyByteBuf buf) {
            var input = Ingredient.fromNetwork(buf);
            var output = buf.readItem();
            return new WeavingMediaRecipe(id, input, output);
        }

        @Override
        public void toNetwork(@NotNull FriendlyByteBuf buf, @NotNull WeavingMediaRecipe recipe) {
            recipe.input().toNetwork(buf);
            buf.writeItem(recipe.output());
        }
    }
}
