package basemod.ability;

import com.bord.dice.modthedice.lib.SpirePatch2;
import com.tann.dice.gameplay.effect.targetable.ability.generation.SpellGeneration;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;

import java.util.Objects;

public class SpellGenerationPatch {
    @SpirePatch2(clz = SpellGeneration.class, method = "okForAbilityMaybe")
    public static class OkForAbilityMaybe{
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(FieldAccess f) throws CannotCompileException {
                    if(Objects.equals(f.getFieldName(), "Mana") || Objects.equals(f.getFieldName(), "Recharge")) {
                        f.replace("{$_ = null;}");
                    }
                }
            };
        }
    }
}
