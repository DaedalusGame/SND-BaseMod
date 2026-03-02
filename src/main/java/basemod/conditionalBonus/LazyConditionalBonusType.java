package basemod.conditionalBonus;

import com.tann.dice.gameplay.effect.eff.conditionalBonus.ConditionalBonusType;
import basemod.ILazy;

public class LazyConditionalBonusType implements ILazy {
    public String name;

    public LazyConditionalBonusType(String name) {
        this.name = name;
    }

    @Override
    public Object get() {
        return ConditionalBonusType.valueOf(name);
    }
}
