package basemod;

import com.bord.dice.modthedice.lib.SpirePatch2;
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
    public static void Postfix() {
        System.out.println("Patching PipeItem!!");

        for (IInitializeItemPipes heroPipe : BaseMod.itemPipes) {
            heroPipe.initialize();
        }
    }

    @SpirePatch2(
            clz= PipeItem.class,
            method="makeHiddenItems"
    )
    public static class PatchHiddenItems {
        public static List<Item> Postfix(List<Item> __result) {
            ArrayList<Item> list = new ArrayList<Item>(__result);

            for (IInitializeItemPipes heroPipe : BaseMod.itemPipes) {
                heroPipe.modifyHiddenModifiers(list);
            }

            return list;
        }
    }
}