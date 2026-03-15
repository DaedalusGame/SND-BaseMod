package basemod.conditionalBonus;

import basemod.EnumPatcher;
import basemod.IEnumPatch;
import com.tann.dice.gameplay.effect.eff.conditionalBonus.ConditionalBonusType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConditionalBonusTypeRegistry {
    public static class ConditionalBonusTypeRegistrar implements IEnumPatch<ConditionalBonusType> {
        public String name;
        public IConditionalBonusType func;

        @Override
        public void edit(ConditionalBonusType conditionalBonusType) {
            //NOOP
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public Class[] getTypeSignature() {
            return new Class[0];
        }

        @Override
        public Object[] getParameters() {
            return new Object[0];
        }
    }

    public static Map<String, IConditionalBonusType> getBonusMap = new HashMap<>();

    public static IConditionalBonusType getBonusFunc(com.tann.dice.gameplay.effect.eff.conditionalBonus.ConditionalBonusType type) {
        return getBonusMap.getOrDefault(type.name(),null);
    }

    public static void registerBonus(String name, IConditionalBonusType func) {
        ConditionalBonusTypeRegistrar i = new ConditionalBonusTypeRegistrar();
        i.name = name;
        i.func = func;
        EnumPatcher.registerPatch(ConditionalBonusType.class, i);

        getBonusMap.put(name, func);
    }
}
