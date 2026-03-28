package basemod.effect;

import com.tann.dice.gameplay.content.ent.Ent;
import com.tann.dice.gameplay.effect.eff.Eff;
import com.tann.dice.gameplay.effect.eff.EffType;
import com.tann.dice.gameplay.effect.targetable.Targetable;
import com.tann.dice.gameplay.fightLog.EntState;
import com.tann.dice.gameplay.fightLog.Snapshot;

public interface IEffType {
    default void hit(EntState state, Eff eff, Ent source, Targetable targetable) {}

    default void untargetedUse(Snapshot snapshot, Eff eff, Ent source) {}

    String describe(EffType type, Eff eff);
}
