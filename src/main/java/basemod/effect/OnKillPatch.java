package basemod.effect;

import com.bord.dice.modthedice.lib.SpirePatch2;
import com.tann.dice.gameplay.content.ent.Ent;
import com.tann.dice.gameplay.effect.eff.Eff;
import com.tann.dice.gameplay.effect.eff.keyword.Keyword;
import com.tann.dice.gameplay.fightLog.Snapshot;
import com.tann.dice.gameplay.fightLog.command.TargetableCommand;
import basemod.KeywordRegistry;
import basemod.keywords.IOnKillEffect;

@SpirePatch2(clz = TargetableCommand.class, method = "onKill")
public class OnKillPatch {
    public static boolean Postfix(TargetableCommand __instance, boolean __result, Ent killer, Snapshot prePresent, Snapshot present) {
        boolean effect = __result;

        Eff eff = __instance.targetable.getDerivedEffects(prePresent);

        for (Keyword kw : eff.getKeywords()) {
            IOnKillEffect onKill = KeywordRegistry.onKillEffects.get(kw);
            if(onKill != null && onKill.activate(__instance, killer, prePresent, present)) {
                effect = true;
            }
        }

        return effect;
    }
}