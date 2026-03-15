package basemod.util;

import com.bord.dice.modthedice.ModInfo;

public class ContentSource {
    public static ContentSource BASEGAME = new ContentSource(null);

    public ContentSource(ModInfo modInfo) {
        this.modInfo = modInfo;
    }

    public ModInfo modInfo;

    public boolean isModded() {
        return modInfo != null;
    }

    @Override
    public String toString() {
        if(modInfo != null) {
            return modInfo.ID;
        } else {
            return "basegame";
        }
    }
}
