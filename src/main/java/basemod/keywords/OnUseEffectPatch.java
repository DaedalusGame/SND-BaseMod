package basemod.keywords;

import com.bord.dice.modthedice.lib.SpirePatch2;
import com.tann.dice.gameplay.content.ent.Ent;
import com.tann.dice.gameplay.effect.eff.Eff;
import com.tann.dice.gameplay.effect.eff.keyword.KUtils;
import com.tann.dice.gameplay.effect.eff.keyword.Keyword;
import com.tann.dice.gameplay.effect.targetable.Targetable;
import com.tann.dice.gameplay.fightLog.EntState;
import basemod.KeywordRegistry;

import java.util.List;

@SpirePatch2(clz = EntState.class,
method = "activateOnUseKeywords")
public class OnUseEffectPatch {
    public static void Prefix(EntState __instance, Eff eff, List<Keyword> keywords, Ent source, Targetable targetable) {
        int kVal = KUtils.getValue(eff);

        for (Keyword keyword : keywords) {
            IOnUseEffect effect = KeywordRegistry.onUseEffects.get(keyword);
            if(effect != null) {
                effect.activate(__instance, kVal, eff, keywords, source, targetable);
            }
        }
    }
}
