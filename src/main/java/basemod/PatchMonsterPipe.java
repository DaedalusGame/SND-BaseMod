package basemod;

import basemod.pipes.*;
import com.bord.dice.modthedice.lib.SpirePatch2;
import com.tann.dice.gameplay.content.ent.type.MonsterType;
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

        for (IInitializeMonsterPipes heroPipe : BaseMod.monsterPipes) {
            heroPipe.initialize();
        }
    }

    @SpirePatch2(
            clz= PipeMonster.class,
            method="makeSecretMonsters"
    )
    public static class PatchHiddenMonsters {
        public static List<MonsterType> Postfix(List<MonsterType> __result) {
            ArrayList<MonsterType> list = new ArrayList<MonsterType>(__result);

            for (IInitializeMonsterPipes heroPipe : BaseMod.monsterPipes) {
                heroPipe.modifyHiddenModifiers(list);
            }

            return list;
        }
    }
}