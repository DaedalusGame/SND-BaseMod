package basemod.targeting;

import basemod.KeywordRegistry;
import com.bord.dice.modthedice.lib.SpirePatch2;
import com.tann.dice.gameplay.effect.eff.Eff;
import com.tann.dice.gameplay.effect.eff.keyword.Keyword;
import com.tann.dice.gameplay.fightLog.EntState;
import com.tann.dice.screens.dungeon.TargetingManager;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

public class TargetingManagerPatch {
    public static boolean canTargetBackrow(Eff eff, EntState sourcePresent, EntState targetPresent) {
        for (ICanTargetBackrow handler : TargetingRegistry.canTargetBackrowHandlers) {
            if(handler.canTarget(eff, sourcePresent, targetPresent)) {
                return true;
            }
        }

        return false;
    }

    @SpirePatch2(clz = TargetingManager.class, method = "isLegalTarget")
    public static class IsLegalTarget{
        public static boolean hasKeyword(Eff eff, EntState sourcePresent, EntState targetPresent, Keyword keyword, boolean result) {
            if(keyword == Keyword.ranged && !result) {
                return canTargetBackrow(eff, sourcePresent, targetPresent);
            }

            return result;
        }

        public static ExprEditor Instrument()
        {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    if(m.getMethodName().equals("hasKeyword")) {
                        m.replace("{" +
                                "$_ = basemod.targeting.TargetingManagerPatch.IsLegalTarget.hasKeyword($0,sourcePresent,targetPresent,$1,$proceed($$));" +
                                "}");
                    }
                }
            };
        }
    }
}
