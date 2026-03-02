package basemod.keywords;

import basemod.KeywordRegistry;
import com.bord.dice.modthedice.lib.SpirePatch2;
import com.bord.dice.modthedice.lib.SpireReturn;
import com.tann.dice.gameplay.effect.eff.keyword.Keyword;

@SpirePatch2(clz = Keyword.class,
method = "getColourTaggedString")
public class KeywordColorTagPatch {
    public static SpireReturn<String> Prefix(Keyword __instance) {
        IKeywordColorTag keywordColorTag = KeywordRegistry.colorTagRegistry.get(__instance);
        if(keywordColorTag != null) {
            return SpireReturn.Return(keywordColorTag.getColorTag(__instance));
        }

        return SpireReturn.Continue();
    }
}
