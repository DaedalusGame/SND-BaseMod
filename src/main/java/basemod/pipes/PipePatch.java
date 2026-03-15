package basemod.pipes;

import basemod.util.ContentSource;
import com.bord.dice.modthedice.lib.SpireField;
import com.bord.dice.modthedice.lib.SpirePatch;
import com.tann.dice.gameplay.content.gen.pipe.Pipe;

@SpirePatch(
        clz= Pipe.class,
        method=SpirePatch.CLASS
)
public class PipePatch {
    public static SpireField<ContentSource> contentSource = new SpireField<>(() -> null);
}

