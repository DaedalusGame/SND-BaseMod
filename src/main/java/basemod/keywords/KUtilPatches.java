package basemod.keywords;

import basemod.BaseMod;
import com.bord.dice.modthedice.lib.SpirePatch2;
import com.tann.dice.gameplay.effect.eff.keyword.KUtils;
import basemod.EnumPatcher;

public class KUtilPatches {
    @SpirePatch2(
            clz = KUtils.class,
            method = "init"
    )
    public static class Init {
        public static void Prefix() {
            BaseMod.runKeywordInitializers();

            EnumPatcher.initialize();
        }
    }
}
