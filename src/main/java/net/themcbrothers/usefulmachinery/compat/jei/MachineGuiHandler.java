package net.themcbrothers.usefulmachinery.compat.jei;

import mezz.jei.api.gui.handlers.IGuiClickableArea;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.client.renderer.Rect2i;
import net.themcbrothers.usefulmachinery.client.screen.AbstractMachineScreen;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MachineGuiHandler implements IGuiContainerHandler<AbstractMachineScreen<?>> {
    private final int xPos;
    private final int yPos;
    private final int width;
    private final int height;
    private final RecipeType<?>[] recipeTypes;

    public MachineGuiHandler(int xPos, int yPos, int width, int height, RecipeType<?>... recipeTypes) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.width = width;
        this.height = height;
        this.recipeTypes = Objects.requireNonNull(recipeTypes);
    }

    @Override
    public List<Rect2i> getGuiExtraAreas(AbstractMachineScreen<?> menuScreen) {
        int slots = Objects.requireNonNull(menuScreen).getMenu().getUpgradeSlotSize();

        if (slots > 0) {
            int height = 16 + slots * 18;
            int width = 34;

            return List.of(new Rect2i(menuScreen.getGuiLeft() + 179, menuScreen.getGuiTop(), width, height));

        }

        return Collections.emptyList();
    }

    @Override
    public Collection<IGuiClickableArea> getGuiClickableAreas(AbstractMachineScreen<?> menuScreen, double guiMouseX, double guiMouseY) {
        IGuiClickableArea clickableArea = IGuiClickableArea.createBasic(this.xPos, this.yPos, this.width, this.height, this.recipeTypes);

        return List.of(clickableArea);
    }
}
