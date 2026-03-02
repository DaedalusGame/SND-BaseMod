package basemod;

import basemod.pipes.*;
import com.bord.dice.modthedice.lib.SpirePatch2;
import com.tann.dice.gameplay.content.ent.type.HeroType;
import com.tann.dice.gameplay.content.gen.pipe.PipeMaster;
import com.tann.dice.gameplay.content.gen.pipe.entity.hero.PipeHero;

import java.util.ArrayList;
import java.util.List;

@SpirePatch2(
        clz= PipeHero.class,
        method="init"
)
public class PatchHeroPipe {
    public static void Postfix() {
        System.out.println("Patching PipeHero!!");
        List<HeroType> heroList = makeHiddenHeroes();
        if(heroList.size() > 0) {
            PipeHero.pipes.add(new PipeMaster<>(heroList));
        }

        for (IInitializeHeroPipes heroPipe : BaseMod.heroPipes) {
            heroPipe.initialize();
        }
    }

    private static List<HeroType> makeHiddenHeroes() {
        ArrayList<HeroType> lst = new ArrayList<>();

        for (IInitializeHeroPipes heroPipe : BaseMod.heroPipes) {
            heroPipe.modifyHiddenModifiers(lst);
        }

        return lst;
    }
}