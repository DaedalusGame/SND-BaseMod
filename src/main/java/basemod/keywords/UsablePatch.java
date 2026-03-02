package basemod.keywords;

import com.bord.dice.modthedice.lib.SpirePatch2;
import com.bord.dice.modthedice.lib.SpireReturn;
import com.tann.dice.gameplay.content.ent.Ent;
import com.tann.dice.gameplay.effect.eff.Eff;
import com.tann.dice.gameplay.effect.eff.keyword.Keyword;
import com.tann.dice.gameplay.effect.targetable.Targetable;
import com.tann.dice.gameplay.fightLog.FightLog;
import com.tann.dice.gameplay.fightLog.Snapshot;
import com.tann.dice.screens.dungeon.TargetingManager;
import basemod.KeywordRegistry;


public class UsablePatch {
    @SpirePatch2(clz = TargetingManager.class,
            method = "isUsable",
            paramtypez = {Targetable.class, boolean.class, boolean.class})
    public static class IsUsable {
        public static boolean Postfix(FightLog ___fightLog, TargetingManager __instance, boolean __result, Targetable t, boolean onlyRecommended, boolean cantrip) {
            Snapshot present = ___fightLog.getSnapshot(FightLog.Temporality.Present);
            Eff first = t.getDerivedEffects();

            if(!__result) return false;

            for (Keyword keyword : first.getKeywords()) {
                IKeywordUsable usable = KeywordRegistry.keywordUsableRegistry.get(keyword);

                if(usable != null && !usable.isUsable(__instance, first, present, t, onlyRecommended, cantrip)) {
                    return false;
                }
            }

            return true;
        }
    }

    @SpirePatch2(clz = TargetingManager.class,
            method = "getInvalidTargetReason",
            paramtypez = {Ent.class, Targetable.class, boolean.class})
    public static class GetInvalidTargetReason {
        public static SpireReturn<String> Prefix(FightLog ___fightLog, TargetingManager __instance, Ent target, Targetable targetable, boolean allowBadTargets) {
            Snapshot ss = ___fightLog.getSnapshot(FightLog.Temporality.Present);
            Eff firstEffect = targetable.getDerivedEffects();
            for (Keyword keyword : firstEffect.getKeywords()) {
                IKeywordUsable usable = KeywordRegistry.keywordUsableRegistry.get(keyword);

                if(usable != null) {
                    String reason = usable.getInvalidTargetReason(__instance, firstEffect, ss, target, targetable, allowBadTargets);
                    if(reason != null) {
                        return SpireReturn.Return(reason);
                    }
                }
            }

            return SpireReturn.Continue();
        }
    }
}
