package basemod.conditionalBonus;

import com.tann.dice.gameplay.effect.eff.Eff;
import com.tann.dice.gameplay.fightLog.EntState;
import com.tann.dice.gameplay.fightLog.Snapshot;

public interface IConditionalBonusType {
    int getBonus(Snapshot s, EntState sourceState, EntState targetState, int bonusAmount, int value, Eff eff);
}
