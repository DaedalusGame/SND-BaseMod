package basemod;

import basemod.pipes.*;
import basemod.util.PipeHandler;
import basemod.util.ReflectionUtils;
import com.bord.dice.modthedice.lib.SpirePatch2;
import com.tann.dice.gameplay.content.ent.type.MonsterType;
import com.tann.dice.gameplay.content.gen.pipe.entity.hero.PipeHero;
import com.tann.dice.gameplay.content.gen.pipe.entity.monster.PipeMonster;
import com.tann.dice.gameplay.content.gen.pipe.item.PipeItem;
import com.tann.dice.gameplay.content.gen.pipe.mod.PipeMod;
import com.tann.dice.gameplay.modifier.Modifier;

import java.util.ArrayList;
import java.util.List;

import static basemod.PatchItemPipe.removeLastMasterPipe;

@SpirePatch2(
        clz= PipeMonster.class,
        method="init"
)
public class PatchMonsterPipe {
    public static void Postfix() {
        System.out.println("Patching PipeMonster!!");

        ReflectionUtils.CallableMethod makeSecretMonsters = ReflectionUtils.getMethod(PipeMonster.class, "makeSecretMonsters");

        PipeHandler handler = PipeHandler.make(() -> PipeMonster.pipes);
        removeLastMasterPipe(PipeMonster.pipes);
        PipeMonster.pipes.add(new PipeMasterHidden<>((List<MonsterType>)makeSecretMonsters.invoke(null)));

        handler.setBaseGame();
        for (BaseMod.ModSpecific<IInitializeMonsterPipes> heroPipe : BaseMod.monsterPipes) {
            heroPipe.content.initialize();
            handler.setMod(heroPipe.modInfo);
        }
    }

    @SpirePatch2(
            clz= PipeMonster.class,
            method="makeSecretMonsters"
    )
    public static class PatchHiddenMonsters {
        public static List<MonsterType> Postfix(List<MonsterType> __result) {
            ArrayList<MonsterType> list = new ArrayList<MonsterType>(__result);

            for (BaseMod.ModSpecific<IInitializeMonsterPipes> heroPipe : BaseMod.monsterPipes) {
                heroPipe.content.modifyHiddenModifiers(list);
            }

            return list;
        }
    }
}