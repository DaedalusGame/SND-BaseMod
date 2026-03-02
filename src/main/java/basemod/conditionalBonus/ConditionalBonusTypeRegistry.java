package basemod.conditionalBonus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConditionalBonusTypeRegistry {
    public static class ConditionalBonusTypeRegistrar {
        public String name;
        public IConditionalBonusType func;
    }

    public static List<ConditionalBonusTypeRegistrar> registry = new ArrayList<>();
    public static Map<String, IConditionalBonusType> getBonusMap = new HashMap<>();

    public static IConditionalBonusType getBonusFunc(com.tann.dice.gameplay.effect.eff.conditionalBonus.ConditionalBonusType type) {
        return getBonusMap.getOrDefault(type.name(),null);
    }

    public static void registerBonus(String name, IConditionalBonusType func) {
        ConditionalBonusTypeRegistrar i = new ConditionalBonusTypeRegistrar();
        i.name = name;
        i.func = func;
        registry.add(i);

        getBonusMap.put(name, func);
    }
}
