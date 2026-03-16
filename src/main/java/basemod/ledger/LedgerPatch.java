package basemod.ledger;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.bord.dice.modthedice.lib.SpirePatch2;
import com.bord.dice.modthedice.lib.SpireReturn;
import com.tann.dice.screens.dungeon.panels.book.page.ledgerPage.LedgerPage;

public class LedgerPatch {
    @SpirePatch2(clz = LedgerPage.class, method = "getContentActorFromSidebar")
    public static class GetContentActor {
        public static SpireReturn<Actor> Prefix(LedgerPage __instance, Object type, int contentWidth) {
            if(type instanceof LedgerPage.LedgerPageType) {
                ILedgerPageType func = LedgerPageTypeRegistry.getLedgerPageType((LedgerPage.LedgerPageType) type);
                if (func != null) {
                    return SpireReturn.Return(func.getActor(contentWidth, __instance));
                }
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch2(clz = LedgerPage.LedgerPageType.class, method = "getPagesInOrder")
    public static class GetPagesInOrder {
        public static LedgerPage.LedgerPageType[] Postfix(LedgerPage.LedgerPageType[] __result) {
            return __result;
        }
    }

    @SpirePatch2(clz = LedgerPage.LedgerPageType.class, method = "getName")
    public static class GetName {
        public static SpireReturn<String> Prefix(LedgerPage.LedgerPageType __instance) {
            ILedgerPageType func = LedgerPageTypeRegistry.getLedgerPageType(__instance);

            if(func != null) {
                return SpireReturn.Return(func.getName(__instance));
            }

            return SpireReturn.Continue();
        }
    }
}
