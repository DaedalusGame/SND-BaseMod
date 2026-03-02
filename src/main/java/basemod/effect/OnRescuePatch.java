package basemod.effect;

import com.bord.dice.modthedice.lib.SpirePatch2;
import com.tann.dice.gameplay.content.ent.Ent;
import com.tann.dice.gameplay.content.ent.Hero;
import com.tann.dice.gameplay.effect.eff.Eff;
import com.tann.dice.gameplay.effect.eff.keyword.Keyword;
import com.tann.dice.gameplay.fightLog.Snapshot;
import com.tann.dice.gameplay.fightLog.command.TargetableCommand;
import basemod.KeywordRegistry;
import basemod.keywords.IOnRescueEffect;

@SpirePatch2(clz = TargetableCommand.class, method = "onRescue")
public class OnRescuePatch {
    public static boolean Postfix(TargetableCommand __instance, boolean __result, Hero saved, Ent saver, Snapshot present, Snapshot prePresent) {
        boolean effect = __result;

        Eff eff = __instance.targetable.getDerivedEffects(prePresent);

        for (Keyword kw : eff.getKeywords()) {
            IOnRescueEffect onRescue = KeywordRegistry.onRescueEffects.get(kw);
            if(onRescue != null && onRescue.activate(__instance, saved, saver, present, prePresent)) {
                effect = true;
            }
        }

        return effect;
    }
}
