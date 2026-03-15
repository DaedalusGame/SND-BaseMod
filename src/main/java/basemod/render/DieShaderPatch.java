package basemod.render;

import com.bord.dice.modthedice.lib.ByRef;
import com.bord.dice.modthedice.lib.SpireInsertPatch;
import com.bord.dice.modthedice.lib.SpirePatch2;
import com.tann.dice.statics.bullet.DieShader;

@SpirePatch2(clz = DieShader.class,
        method = "init")
public class DieShaderPatch {
    @SpireInsertPatch(
            rloc=2,
            localvars={"frag"}
    )
    public static void Insert(@ByRef() String[] frag)
    {
        String fragProgram = frag[0];
        frag[0] = fragProgram
                .replace("const float TX_SZ = 1024.;", "const float TX_SZ = 2048.;")
        ;
    }
}
