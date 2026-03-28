package basemod.ability;

import basemod.EnumPatcher;
import basemod.EnumRegistry;
import basemod.IEnumPatch;
import com.tann.dice.gameplay.effect.eff.Eff;
import com.tann.dice.gameplay.effect.targetable.ability.Ability;
import com.tann.dice.gameplay.effect.targetable.ability.tactic.TacticCostType;
import com.tann.dice.gameplay.trigger.personal.spell.learn.LearnAbility;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class AbilityRegistry {
    public static class Registrar implements IEnumPatch<TacticCostType> {
        public String name;
        public Class[] typeSig;
        public Object[] obj;

        @Override
        public void edit(TacticCostType keyword) {

        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public Class[] getTypeSignature() {
            return typeSig;
        }

        @Override
        public Object[] getParameters() {
            return obj;
        }
    }

    public static EnumRegistry<TacticCostType, ITacticCostType> abilityCostTypes = new EnumRegistry<>();
    public static List<Function<Eff,TacticCostType>> textModFuncs = new ArrayList<>();
    public static List<Function<Ability, LearnAbility>> classToLearnAbilityMapper = new ArrayList<>();

    public static <T extends Ability, TConstruct extends LearnAbility> void registerAbilityClass(Class<T> clz,Function<T, TConstruct> learnSpellSupplier) {
        classToLearnAbilityMapper.add(ability -> {
            if(clz.isInstance(ability)){
                return learnSpellSupplier.apply((T)ability);
            }
            return null;
        });
    }

    public static void registerTacticCostType(String name, String tex, boolean pippy, ITacticCostType costType)
    {
        Registrar registrar = new Registrar();
        registrar.name = name;
        registrar.typeSig = new Class[] { String.class, boolean.class };
        registrar.obj = new Object[]{tex, pippy};
        EnumPatcher.registerPatch(TacticCostType.class, registrar);
        abilityCostTypes.register(name, costType);
    }
}
