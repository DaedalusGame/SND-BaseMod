package basemod.pipes;

import com.bord.dice.modthedice.lib.SpirePatch2;
import com.tann.dice.gameplay.content.gen.pipe.regex.PipeRegex;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;

@SpirePatch2(clz = PipeRegex.class, method = "groups")
public class PipeRegexPatch {
    public static int getNumGroups(PipeRegex pipe, int numGroups) {
        if(pipe instanceof IHasGroupCount) {
            return ((IHasGroupCount) pipe).getGroupCount();
        }

        return numGroups;
    }

    public static ExprEditor Instrument()
    {
        return new ExprEditor() {
            @Override
            public void edit(FieldAccess f) throws CannotCompileException {
                if(f.getFieldName().equals("numGroups")) {
                    f.replace("{" +
                            "$_ = yourmod.pipes.PipeRegexPatch.getNumGroups(this, $proceed($$));" +
                            "}");
                }
            }
        };
    }
}