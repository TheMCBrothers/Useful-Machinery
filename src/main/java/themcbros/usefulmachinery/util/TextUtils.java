package themcbros.usefulmachinery.util;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import themcbros.usefulmachinery.UsefulMachinery;

public class TextUtils {

    private static final String ENERGY_FORMAT = "%,d";

    public static ITextComponent translate(String prefix, String suffix, Object... params) {
        return new TranslationTextComponent(prefix + "." + UsefulMachinery.MOD_ID + "." + suffix, params);
    }

    public static ITextComponent energy(int amount) {
        String s1 = String.format(ENERGY_FORMAT, amount);
        return translate("misc", "energy", s1);
    }

    public static ITextComponent energyWithMax(int amount, int max) {
        String s1 = String.format(ENERGY_FORMAT, amount);
        String s2 = String.format(ENERGY_FORMAT, max);
        return translate("misc", "energyWithMax", s1, s2);
    }

}
