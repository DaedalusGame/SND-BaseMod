package basemod.render;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.bord.dice.modthedice.lib.ByRef;
import com.bord.dice.modthedice.lib.SpirePatch2;
import com.bord.dice.modthedice.lib.SpireReturn;
import com.tann.dice.gameplay.content.ent.EntSize;
import com.tann.dice.gameplay.content.ent.die.EntDie;
import com.tann.dice.gameplay.effect.eff.keyword.Keyword;
import com.tann.dice.gameplay.fightLog.EntState;
import com.tann.dice.gameplay.fightLog.FightLog;

import java.util.List;

@SpirePatch2(clz = EntDie.class, method = "getKeywordLocs")
public class EntDiePatch {
    public static SpireReturn<float[]> Prefix(EntDie __instance, @ByRef float[][] ___keywordLocs) {
        float[] keywordLocs = ___keywordLocs[0];
        if (keywordLocs != null) {
            return SpireReturn.Return(keywordLocs);
        } else {
            ___keywordLocs[0] = keywordLocs = new float[96];
            EntState es = __instance.ent.getState(FightLog.Temporality.Visual);

            for(int sideIndex = 0; sideIndex < 6; ++sideIndex) {
                List<Keyword> bonuses = es.getSideState(sideIndex).getBonusKeywords();

                for(int keywordIndex = 0; keywordIndex < 4 && keywordIndex < bonuses.size(); ++keywordIndex) {
                    Keyword k = bonuses.get(keywordIndex);
                    int mainIndex = sideIndex * 16 + keywordIndex * 4;
                    TextureRegion image = k.getImage(__instance.ent.getSize());
                    keywordLocs[mainIndex + 0] = (float)image.getRegionX() / 2048f;
                    keywordLocs[mainIndex + 1] = (float)image.getRegionY() / 2048f;
                    keywordLocs[mainIndex + 2] = __instance.ent.getSize() != EntSize.small && !k.isFlipCorner() ? 0.0F : 1.0F;
                    keywordLocs[mainIndex + 3] = (float)(image.getRegionHeight() * 20 + image.getRegionWidth());
                }
            }

            return SpireReturn.Return(keywordLocs);
        }
    }
}
