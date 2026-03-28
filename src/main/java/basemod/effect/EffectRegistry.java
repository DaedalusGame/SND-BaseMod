package basemod.effect;

import basemod.EnumPatcher;
import basemod.EnumRegistry;
import basemod.IEnumPatch;
import com.tann.dice.gameplay.effect.eff.EffType;

public class EffectRegistry {
    public static class Registrar implements IEnumPatch<EffType> {
        public String name;
        public Class[] typeSig;
        public Object[] obj;

        @Override
        public void edit(EffType effType) {

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

    public static EnumRegistry<EffType, IEffType> effTypes = new EnumRegistry<>();

    public static void register(String name, boolean allowBadTargets, IEffType effType)
    {
        Registrar registrar = new Registrar();
        registrar.name = name;
        registrar.typeSig = new Class[] { boolean.class };
        registrar.obj = new Object[]{allowBadTargets};
        EnumPatcher.registerPatch(EffType.class, registrar);
        effTypes.register(name, effType);
    }
}