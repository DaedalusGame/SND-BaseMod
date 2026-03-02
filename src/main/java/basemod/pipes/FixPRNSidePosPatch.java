package basemod.pipes;

import com.bord.dice.modthedice.lib.SpirePatch2;
import com.bord.dice.modthedice.lib.SpireReturn;
import com.tann.dice.gameplay.content.gen.pipe.regex.prnPart.PRNSidePos;
import com.tann.dice.gameplay.trigger.personal.affectSideModular.condition.SpecificSidesType;

import java.util.List;

@SpirePatch2(clz = PRNSidePos.class,
method = "regex")
public class FixPRNSidePosPatch {
    public static SpireReturn<String> Prefix() {
        String firstGroup = "(";
        List<SpecificSidesType> vals = PRNSidePos.makeValids();

        for(int i = 0; i < vals.size(); ++i) {
            SpecificSidesType sst = vals.get(i);
            firstGroup = firstGroup + sst.getShortName();
            if (i < vals.size() - 1) {
                firstGroup = firstGroup + "|";
            }
        }

        if (firstGroup.endsWith("|")) {
            firstGroup = firstGroup.substring(0, firstGroup.length() - 1);
        }

        firstGroup = firstGroup + ")";
        return SpireReturn.Return(firstGroup);
    }
}
