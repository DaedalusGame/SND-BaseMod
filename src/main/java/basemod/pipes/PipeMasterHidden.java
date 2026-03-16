package basemod.pipes;

import basemod.util.ReflectionUtils;
import com.tann.dice.gameplay.content.ent.type.MonsterType;
import com.tann.dice.gameplay.content.gen.pipe.PipeMaster;
import com.tann.dice.util.lang.Words;

import java.util.List;

public class PipeMasterHidden<T> extends PipeMaster<T> {
    public PipeMasterHidden(List<T> masterList) {
        super(masterList);
    }

    @Override
    public String document() {
        List<T> master = ReflectionUtils.readField(List.class, PipeMaster.class, this, "master");

        if (master.size() > 0) {
            Object first = master.get(0);
            return "hidden " + Words.plural(first.getClass().getSimpleName().toLowerCase());
        }

        return "undocumented";
    }
}
