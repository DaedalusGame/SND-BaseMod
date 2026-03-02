package basemod.conditionalBonus;

import com.bord.dice.modthedice.lib.SpirePatch2;
import com.bord.dice.modthedice.lib.SpireReturn;
import com.tann.dice.gameplay.effect.eff.Eff;
import com.tann.dice.gameplay.effect.eff.conditionalBonus.ConditionalBonusType;
import com.tann.dice.gameplay.fightLog.EntState;
import com.tann.dice.gameplay.fightLog.Snapshot;

@SpirePatch2(
        clz = ConditionalBonusType.class,
        method = "getBonus"
)
public class ConditionalBonusTypePatch {
    public static SpireReturn<Integer> Prefix(ConditionalBonusType __instance, Snapshot s, EntState sourceState, EntState targetState, int bonusAmount, int value, Eff eff) {
        IConditionalBonusType bonusType = ConditionalBonusTypeRegistry.getBonusFunc(__instance);
        if(bonusType != null) {
            return SpireReturn.Return(bonusType.getBonus(s, sourceState, targetState, bonusAmount, value, eff));
        }

        return SpireReturn.Continue();
    }

   // public static int Postfix(ConditionalBonusType __instance, int __result) {
    //    return __result;
    //}
}
