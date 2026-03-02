package basemod;

import com.bord.dice.modthedice.lib.SpirePatch2;
import com.tann.dice.gameplay.content.gen.pipe.mod.PipeMod;
import basemod.pipes.*;
import com.tann.dice.gameplay.modifier.Modifier;

import java.util.ArrayList;
import java.util.List;

@SpirePatch2(
        clz= PipeMod.class,
        method="init"
)
public class PatchModifierPipe {
    public static void Postfix() {
        System.out.println("Patching PipeMod!!");

        for (IInitializeModPipes heroPipe : BaseMod.modPipes) {
            heroPipe.initialize();
        }
    }
    @SpirePatch2(
            clz= PipeMod.class,
            method="makeHiddenModifiers"
    )

    public static class PatchHiddenModifiers {
        public static List<Modifier> Postfix(List<Modifier> __result) {
            ArrayList<Modifier> list = new ArrayList<>(__result);

            for (IInitializeModPipes heroPipe : BaseMod.modPipes) {
                heroPipe.modifyHiddenModifiers(list);
            }

            return list;
        }
    }
}
