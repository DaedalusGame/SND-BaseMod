package basemod.targeting;

import com.tann.dice.gameplay.effect.eff.Eff;
import com.tann.dice.gameplay.fightLog.EntState;

public interface ICanTargetBackrow {
    boolean canTarget(Eff eff, EntState source, EntState target);
}
