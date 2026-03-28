package basemod;

import basemod.BaseMod.ModSpecific;
import com.bord.dice.modthedice.Loader;
import com.tann.dice.util.Tann;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ModSpecificList<T> implements Iterable<ModSpecific<T>> {
    private final List<ModSpecific<T>> list = new ArrayList<>();

    public void add(T t) {
        list.add(new ModSpecific<>(Loader.getCurrentMod(), t));
    }

    public List<ModSpecific<T>> getAll() {
        return list;
    }

    @Override
    public Iterator<ModSpecific<T>> iterator() {
        return list.iterator();
    }
}
