package basemod.util;

import com.bord.dice.modthedice.ModInfo;
import com.tann.dice.gameplay.content.gen.pipe.Pipe;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

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

    public static <T> ContentHandler<T> make(Supplier<List<T>> pipes, BiConsumer<T, ContentSource> setSrc) {
        return new ContentHandler<T>() {
            @Override
            public Collection<T> getPipes() {
                return pipes.get();
            }

            @Override
            public void setContentSource(T t, ContentSource contentSource) {
                setSrc.accept(t, contentSource);
            }
        };
    }
}
