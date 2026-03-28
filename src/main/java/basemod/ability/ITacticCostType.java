package basemod.ability;

import com.tann.dice.gameplay.effect.eff.Eff;
import com.tann.dice.gameplay.effect.targetable.ability.tactic.TacticCostType;

public interface ITacticCostType {
    String desc(TacticCostType instance);

    boolean isValid(TacticCostType instance, Eff checkEff);
}
