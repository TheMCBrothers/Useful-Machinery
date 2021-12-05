package themcbros.usefulmachinery.client.gui.widget;

import java.awt.*;

public class EnergyBar {
    public final Rectangle rect;

    public EnergyBar(int x, int y, int width, int height) {
        this.rect = new Rectangle(x, y, width, height);
    }

    public boolean isHovered(int mouseX, int mouseY) {
        return rect.contains(mouseX, mouseY);
    }
}
