package basemod.effect;

import com.bord.dice.modthedice.lib.SpirePatch2;
import com.bord.dice.modthedice.lib.SpireReturn;
import com.tann.dice.gameplay.content.ent.Ent;
import com.tann.dice.gameplay.effect.eff.Eff;
import com.tann.dice.gameplay.effect.eff.EffType;
import com.tann.dice.gameplay.effect.targetable.Targetable;
import com.tann.dice.gameplay.fightLog.EntState;
import com.tann.dice.gameplay.fightLog.Snapshot;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

public class EffectPatch {
    public static void hit(EntState state, Eff eff, Ent source, Targetable targetable) {
        IEffType effType = EffectRegistry.effTypes.get(eff.getType());
        if(effType != null) {
            effType.hit(state, eff, source, targetable);
        }
    }

    public static void untargetedUse(Snapshot snapshot, Eff eff, Ent source) {
        IEffType effType = EffectRegistry.effTypes.get(eff.getType());
        if(effType != null) {
            effType.untargetedUse(snapshot, eff, source);
        }
    }

    public static String describe(EffType type, Eff eff) {
        IEffType effType = EffectRegistry.effTypes.get(eff.getType());
        if(effType != null) {
            return effType.describe(type, eff);
        }
        return null;
    }

    @SpirePatch2(clz = EffType.class, method = "describe")
    public static class EffTypeDescribe {
        public static SpireReturn<String> Prefix(EffType __instance, Eff source) {
            String desc = describe(__instance, source);
            if(desc != null) {
                return SpireReturn.Return(desc);
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch2(clz = Snapshot.class, method = "untargetedUse")
    public static class SnapshotUntargetedUse {
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    if(m.getMethodName().equals("getType")) {
                        m.replace("{" +
                                "basemod.effect.EffectPatch.untargetedUse(this,e,source);" +
                                "$_ = $proceed($$);" +
                                "}");
                    }
                }
            };
        }
    }

    @SpirePatch2(clz = EntState.class, method = "hit", paramtypez = {Eff.class, Ent.class, Targetable.class})
    public static class EntStateHit {
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    if(m.getMethodName().equals("activateOnUseKeywords")) {
                        m.replace("{" +
                                "basemod.effect.EffectPatch.hit(this,eff,source,targetable);" +
                                "$_ = $proceed($$);" +
                                "}");
                    }
                }
            };
        }
    }
}
