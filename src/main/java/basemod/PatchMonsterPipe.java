package basemod;

import basemod.pipes.*;
import basemod.util.PipeHandler;
import com.bord.dice.modthedice.lib.SpirePatch2;
import com.tann.dice.gameplay.content.ent.type.MonsterType;
import com.tann.dice.gameplay.content.gen.pipe.entity.hero.PipeHero;
import com.tann.dice.gameplay.content.gen.pipe.entity.monster.PipeMonster;

import java.util.ArrayList;
import java.util.List;

@SpirePatch2(
        clz= PipeMonster.class,
        method="init"
)
public class PatchMonsterPipe {
    public static void Postfix() {
        System.out.println("Patching PipeMonster!!");

        PipeHandler handler = PipeHandler.make(() -> PipeMonster.pipes);

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