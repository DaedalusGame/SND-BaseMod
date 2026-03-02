package basemod.keywords;

import com.tann.dice.gameplay.effect.eff.keyword.Keyword;
import basemod.ILazy;

public class LazyKeyword implements ILazy {
    public String name;

    public LazyKeyword(String name) {
        this.name = name;
    }

    @Override
    public Object get() {
        return Keyword.byName(name);
    }
}
