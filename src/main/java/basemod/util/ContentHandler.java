package basemod.util;

import com.bord.dice.modthedice.ModInfo;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class ContentHandler<T> {
    Set<T> handled = new HashSet<>();

    public abstract Collection<T> getPipes();

    public abstract void setContentSource(T t, ContentSource contentSource);

    public void setBaseGame() {
        for (T t : getPipes()) {
            if(handled.contains(t)) continue;
            setContentSource(t, ContentSource.BASEGAME);
            handled.add(t);
        }
    }

    public void setMod(ModInfo modInfo) {
        for (T t : getPipes()) {
            if(handled.contains(t)) continue;
            setContentSource(t, new ContentSource(modInfo));
            handled.add(t);
        }
    }
}
