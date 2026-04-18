package mys.hexvoid.recipe;

import com.google.gson.JsonObject;
import mys.hexvoid.registry.HexvoidRecipes;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Consumer;

public class WeavingMediaRecipeBuilder {
    private final Ingredient input;
    private final ItemStack output;

    private WeavingMediaRecipeBuilder(Ingredient input, ItemStack output) {
        this.input = input;
        this.output = output.copy();
    }

    public static WeavingMediaRecipeBuilder weavingMedia(Ingredient input, ItemStack output) {
        return new WeavingMediaRecipeBuilder(input, output);
    }

    public void save(Consumer<FinishedRecipe> writer, ResourceLocation id) {
        writer.accept(new Result(id, input, output));
    }

    private record Result(
            ResourceLocation id,
            Ingredient input,
            ItemStack output
    ) implements FinishedRecipe {

        @Override
        public void serializeRecipeData(JsonObject json) {
            json.add("input", input.toJson());

            JsonObject outputJson = new JsonObject();
            outputJson.addProperty("item", Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(output.getItem())).toString());

            if (output.getCount() != 1) {
                outputJson.addProperty("count", output.getCount());
            }

            CompoundTag tag = output.getTag();
            if (tag != null && !tag.isEmpty()) {
                outputJson.addProperty("nbt", tag.toString());
            }

            json.add("output", outputJson);
        }

        @Override
        public @NotNull ResourceLocation getId() {
            return id;
        }

        @Override
        public net.minecraft.world.item.crafting.@NotNull RecipeSerializer<?> getType() {
            return HexvoidRecipes.WEAVING_MEDIA_SERIALIZER.get();
        }

        @Override
        public @Nullable JsonObject serializeAdvancement() {
            return null;
        }

        @Override
        public @Nullable ResourceLocation getAdvancementId() {
            return null;
        }
    }
}