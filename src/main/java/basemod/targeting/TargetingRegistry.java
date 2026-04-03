package basemod.targeting;

import basemod.EnumRegistry;
import com.tann.dice.gameplay.effect.eff.keyword.Keyword;

import java.util.ArrayList;
import java.util.List;

public class TargetingRegistry {

    static List<ICanTargetBackrow> canTargetBackrowHandlers = new ArrayList<>();

    public static void register(ICanTargetBackrow canTargetBackrowHandler) {
        canTargetBackrowHandlers.add(canTargetBackrowHandler);
    }
}
