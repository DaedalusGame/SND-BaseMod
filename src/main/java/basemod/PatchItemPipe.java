package basemod;

import basemod.util.PipeHandler;
import basemod.util.ReflectionUtils;
import com.bord.dice.modthedice.lib.SpirePatch2;
import com.tann.dice.gameplay.content.gen.pipe.Pipe;
import com.tann.dice.gameplay.content.gen.pipe.PipeMaster;
import com.tann.dice.gameplay.content.gen.pipe.entity.hero.PipeHero;
import com.tann.dice.gameplay.content.gen.pipe.item.PipeItem;
import com.tann.dice.gameplay.content.item.Item;
import basemod.pipes.*;

import java.util.ArrayList;
import java.util.List;

@SpirePatch2(
        clz= PipeItem.class,
        method="init"
)
public class PatchItemPipe {
    public static <T> void removeLastMasterPipe(List<Pipe<T>> pipes){
        Pipe<T> lastPipe = null;
        for (Pipe<T> pipe : pipes) {
            if(pipe instanceof PipeMaster) {
                lastPipe = pipe;
            }
        }

        if(lastPipe != null) {
            pipes.remove(lastPipe);
        }
    }

    public static void Postfix() {
        System.out.println("Patching PipeItem!!");

        ReflectionUtils.CallableMethod makeHiddenItems = ReflectionUtils.getMethod(PipeItem.class, "makeHiddenItems");

        PipeHandler handler = PipeHandler.make(() -> PipeItem.pipes);
        removeLastMasterPipe(PipeItem.pipes);
        PipeItem.pipes.add(new PipeMasterHidden<>((List<Item>)makeHiddenItems.invoke(null)));

        handler.setBaseGame();
        for (BaseMod.ModSpecific<IInitializeItemPipes> heroPipe : BaseMod.itemPipes) {
            heroPipe.content.initialize();
            handler.setMod(heroPipe.modInfo);
        }
    }

    @SpirePatch2(
            clz= PipeItem.class,
            method="makeHiddenItems"
    )
    public static class PatchHiddenItems {
        public static List<Item> Postfix(List<Item> __result) {
            ArrayList<Item> list = new ArrayList<Item>(__result);

            for (BaseMod.ModSpecific<IInitializeItemPipes> heroPipe : BaseMod.itemPipes) {
                heroPipe.content.modifyHiddenModifiers(list);
            }

            return list;
        }
    }
}