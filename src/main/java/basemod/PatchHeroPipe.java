package basemod;

import basemod.pipes.*;
import basemod.util.ContentSource;
import basemod.util.PipeHandler;
import com.bord.dice.modthedice.lib.SpirePatch2;
import com.tann.dice.gameplay.content.ent.type.HeroType;
import com.tann.dice.gameplay.content.gen.pipe.Pipe;
import com.tann.dice.gameplay.content.gen.pipe.PipeMaster;
import com.tann.dice.gameplay.content.gen.pipe.entity.hero.PipeHero;

import java.util.ArrayList;
import java.util.Collection;
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

        PipeHandler handler = PipeHandler.make(() -> PipeHero.pipes);

        handler.setBaseGame();
        for (BaseMod.ModSpecific<IInitializeHeroPipes> heroPipe : BaseMod.heroPipes) {
            heroPipe.content.initialize();
            handler.setMod(heroPipe.modInfo);
        }
    }

    private static List<HeroType> makeHiddenHeroes() {
        ArrayList<HeroType> lst = new ArrayList<>();

        for (BaseMod.ModSpecific<IInitializeHeroPipes> heroPipe : BaseMod.heroPipes) {
            heroPipe.content.modifyHiddenModifiers(lst);
        }

        return lst;
    }
}