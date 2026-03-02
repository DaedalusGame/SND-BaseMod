package basemod.keywords;

import com.bord.dice.modthedice.lib.SpirePatch2;
import com.tann.dice.gameplay.content.ent.Ent;
import com.tann.dice.gameplay.effect.eff.Eff;
import com.tann.dice.gameplay.effect.eff.keyword.KUtils;
import com.tann.dice.gameplay.effect.eff.keyword.Keyword;
import com.tann.dice.gameplay.effect.targetable.DieTargetable;
import com.tann.dice.gameplay.fightLog.Snapshot;
import basemod.KeywordRegistry;

import java.util.List;

@SpirePatch2(clz = DieTargetable.class,
        method = "afterUseKeywords")
public class OnAfterUseEffectPatch {
    public static void Prefix(Eff src, List<Keyword> keywords, Snapshot snapshot, Ent source, int sideIndex) {
        int kVal = KUtils.getValue(src);

        for (Keyword keyword : keywords) {
            IOnAfterUseEffect effect = KeywordRegistry.onAfterUseEffects.get(keyword);
            if(effect != null) {
                effect.activate(kVal, src, keywords, snapshot, source, sideIndex);
            }
        }
    }
}

