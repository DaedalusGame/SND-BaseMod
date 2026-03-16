package basemod;

import basemod.util.PipeHandler;
import basemod.util.ReflectionUtils;
import com.bord.dice.modthedice.lib.SpirePatch2;
import com.tann.dice.gameplay.content.gen.pipe.entity.hero.PipeHero;
import com.tann.dice.gameplay.content.gen.pipe.entity.monster.PipeMonster;
import com.tann.dice.gameplay.content.gen.pipe.item.PipeItem;
import com.tann.dice.gameplay.content.gen.pipe.mod.PipeMod;
import basemod.pipes.*;
import com.tann.dice.gameplay.content.item.Item;
import com.tann.dice.gameplay.modifier.Modifier;

import java.util.ArrayList;
import java.util.List;

import static basemod.PatchItemPipe.removeLastMasterPipe;

@SpirePatch2(
        clz= PipeMod.class,
        method="init"
)
public class PatchModifierPipe {
    public static void Postfix() {
        System.out.println("Patching PipeMod!!");

        ReflectionUtils.CallableMethod makeHiddenModifiers = ReflectionUtils.getMethod(PipeMod.class, "makeHiddenModifiers");

        PipeHandler handler = PipeHandler.make(() -> PipeMod.pipes);
        removeLastMasterPipe(PipeMod.pipes);
        PipeMod.pipes.add(new PipeMasterHidden<>((List<Modifier>)makeHiddenModifiers.invoke(null)));

        handler.setBaseGame();
        for (BaseMod.ModSpecific<IInitializeModPipes> heroPipe : BaseMod.modPipes) {
            heroPipe.content.initialize();
            handler.setMod(heroPipe.modInfo);
        }
    }
    @SpirePatch2(
            clz= PipeMod.class,
            method="makeHiddenModifiers"
    )

    public static class PatchHiddenModifiers {
        public static List<Modifier> Postfix(List<Modifier> __result) {
            ArrayList<Modifier> list = new ArrayList<>(__result);

            for (BaseMod.ModSpecific<IInitializeModPipes> heroPipe : BaseMod.modPipes) {
                heroPipe.content.modifyHiddenModifiers(list);
            }

            return list;
        }
    }
}
