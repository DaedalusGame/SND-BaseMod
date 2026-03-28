package basemod.ability;

import com.bord.dice.modthedice.lib.SpirePatch2;
import com.bord.dice.modthedice.lib.SpireReturn;
import com.tann.dice.gameplay.effect.eff.Eff;
import com.tann.dice.gameplay.effect.targetable.ability.tactic.TacticCostType;

import java.util.function.Function;

public class TacticCostTypePatch {
    @SpirePatch2(clz = TacticCostType.class, method = "getValidTypeTextmod")
    public static class GetValidTypeTextmod {
        public static SpireReturn<TacticCostType> Prefix(Eff calcEff) {
            /*ITacticCostType type = AbilityRegistry.abilityCostTypes.get(__instance);
            if(type != null) {
                TacticCostType result = type.getValidTypeTextmod(__instance, calcEff);
                if(result != null) {
                    return SpireReturn.Return(result);
                }
            }*/
            for (Function<Eff, TacticCostType> textModFunc : AbilityRegistry.textModFuncs) {
                TacticCostType result = textModFunc.apply(calcEff);
                if(result != null) {
                    return SpireReturn.Return(result);
                }
            }

            return SpireReturn.Continue();
        }
    }

    @SpirePatch2(clz = TacticCostType.class, method = "isValid")
    public static class IsValid {
        public static SpireReturn<Boolean> Prefix(TacticCostType __instance, Eff e) {
            ITacticCostType type = AbilityRegistry.abilityCostTypes.get(__instance);
            if(type != null) {
                return SpireReturn.Return(type.isValid(__instance, e));
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch2(clz = TacticCostType.class, method = "desc")
    public static class Desc {
        public static SpireReturn<String> Prefix(TacticCostType __instance) {
            ITacticCostType type = AbilityRegistry.abilityCostTypes.get(__instance);
            if(type != null) {
                return SpireReturn.Return(type.desc(__instance));
            }
            return SpireReturn.Continue();
        }
    }
}