package basemod;

import basemod.sides.SpecificSidesTypeRegistry;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.bord.dice.modthedice.*;
import com.bord.dice.modthedice.lib.SpireEnum;
import com.tann.dice.gameplay.effect.eff.conditionalBonus.ConditionalBonusType;
import com.tann.dice.gameplay.effect.eff.keyword.Keyword;
import com.tann.dice.gameplay.trigger.personal.affectSideModular.condition.SpecificSidesType;
import javassist.ClassPool;
import basemod.conditionalBonus.ConditionalBonusTypeRegistry;

import java.lang.reflect.Field;
import java.util.Map;

public class EnumPatcher {
    public enum DUMMY {
        AMOGUS,
        SUS
    }
    @SpireEnum
    public static DUMMY IMPOSTER;

    private static Field enumBusterMapField;
    private static Field classLoaderField;

    static {
        try {
            enumBusterMapField = Patcher.class.getDeclaredField("enumBusterMap");
            enumBusterMapField.setAccessible(true);

            classLoaderField = MTSClassPool.class.getDeclaredField("classLoader");
            classLoaderField.setAccessible(true);

            //generate((ClassPool) poolField.get(null));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void initialize() {
        try {
            Field poolField = Loader.class.getDeclaredField("POOL");
            poolField.setAccessible(true);

            generate((ClassPool) poolField.get(null));
        } catch (NoSuchFieldException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void flattenLazies(Object[] obj) {
        for (int i = 0; i < obj.length; i++) {
            if(obj[i] instanceof ILazy) {
                obj[i] = ((ILazy)obj[i]).get();
            }
        }
    }

    private static void a(ClassLoader loader, Map<Class<?>, EnumBusterReflect> enumBusterMap, ConditionalBonusTypeRegistry.ConditionalBonusTypeRegistrar keyword)
            throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException
    {
        Class type = ConditionalBonusType.class;//field.getType();
        EnumBusterReflect buster;
        if (enumBusterMap.containsKey(type)) {
            buster = enumBusterMap.get(type);
        } else {
            buster = new EnumBusterReflect(loader, type);
            enumBusterMap.put(type, buster);
        }
        Enum<?> enumValue = buster.make(keyword.name, Keyword.values().length);
        buster.addByValue(enumValue);
        try {
            Field constantField = type.getField(keyword.name);
            ReflectionHelper.setStaticFinalField(constantField, enumValue);
        } catch (NoSuchFieldException ignored) {
            System.out.println("\t\t- Failed to initialize enum field " + keyword.name);
        }

        //field.setAccessible(true);
        //field.set(null, enumValue);
    }

    private static void a(ClassLoader loader, Map<Class<?>, EnumBusterReflect> enumBusterMap, SpecificSidesTypeRegistry.SpecificSidesTypeRegistrar keyword)
            throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException
    {
        Class[] sig = new Class[] {
            int[].class, TextureRegion.class, Vector2[].class, String.class, String.class
        };

        Class type = SpecificSidesType.class;//field.getType();
        EnumBusterReflect buster;
        if (enumBusterMap.containsKey(type)) {
            buster = enumBusterMap.get(type);
        } else {
            buster = new EnumBusterReflect(loader, type);
            enumBusterMap.put(type, buster);
        }
        Object[] obj = new Object[]{
                keyword.sideIndices, keyword.texture, keyword.sidePositions, keyword.description, keyword.shortName
        };
        flattenLazies(obj);
        Enum<?> enumValue = buster.make(keyword.name, Keyword.values().length, sig, obj);
        buster.addByValue(enumValue);
        try {
            Field constantField = type.getField(keyword.name);
            ReflectionHelper.setStaticFinalField(constantField, enumValue);
        } catch (NoSuchFieldException ignored) {
            System.out.println("\t\t- Failed to initialize enum field " + keyword.name);
        }

        //field.setAccessible(true);
        //field.set(null, enumValue);
    }


    private static void a(ClassLoader loader, Map<Class<?>, EnumBusterReflect> enumBusterMap, KeywordRegistry.KeywordRegistrar keyword)
            throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException
    {
        Class type = Keyword.class;//field.getType();
        EnumBusterReflect buster;
        if (enumBusterMap.containsKey(type)) {
            buster = enumBusterMap.get(type);
        } else {
            buster = new EnumBusterReflect(loader, type);
            enumBusterMap.put(type, buster);
        }
        if(keyword.filler != null) {
            keyword.filler.accept(keyword.obj);
        }
        flattenLazies(keyword.obj);
        Enum<?> enumValue = buster.make(keyword.name, Keyword.values().length, keyword.typeSig, keyword.obj);
        if(keyword.editor != null) {
            keyword.editor.accept((Keyword) enumValue);
        }
        buster.addByValue(enumValue);
        try {
            Field constantField = type.getField(keyword.name);
            ReflectionHelper.setStaticFinalField(constantField, enumValue);
        } catch (NoSuchFieldException ignored) {
            System.out.println("\t\t- Failed to initialize enum field " + keyword.name);
        }

        //field.setAccessible(true);
        //field.set(null, enumValue);
    }

    @SuppressWarnings("unchecked")
    public static void generate(ClassPool pool) throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException {
        ClassLoader loader;
        if (pool instanceof MTSClassPool) {
            loader = (ClassLoader) classLoaderField.get(pool);
        }
        else {
            System.out.println("Unable to obtain temporary patching class loader.");
            return;
        }

        Map<Class<?>, EnumBusterReflect> enumBusterMap = (Map<Class<?>, EnumBusterReflect>) enumBusterMapField.get(null);
        //Class<?> enumStorage = duospire.patches.gen.EnumPlace.class; //loader.loadClass(EnumPlace.class.getName());

        for (ConditionalBonusTypeRegistry.ConditionalBonusTypeRegistrar conditionalBonusType : ConditionalBonusTypeRegistry.registry) {
            a(loader, enumBusterMap, conditionalBonusType);
        }

        for (KeywordRegistry.KeywordRegistrar keyword : KeywordRegistry.registry) {
            a(loader, enumBusterMap, keyword);
        }

        for (SpecificSidesTypeRegistry.SpecificSidesTypeRegistrar keyword : SpecificSidesTypeRegistry.registry) {
            a(loader, enumBusterMap, keyword);
        }

        /*for (Field field : enumStorage.getDeclaredFields()) {
            if (Modifier.isStatic(field.getModifiers())) {
                DynamicEnum dynamicEnum = field.getDeclaredAnnotation(DynamicEnum.class);
                if (dynamicEnum != null) {
                    String enumName = field.getName();
                    if (dynamicEnum.origin().isEmpty())
                        continue;
                    String origin = dynamicEnum.origin();
                    if (!dynamicEnum.name().isEmpty())
                        enumName = dynamicEnum.name();

                    EnumBusterReflect buster;
                    if (enumBusterMap.containsKey(field.getType())) {
                        buster = enumBusterMap.get(field.getType());
                    } else {
                        buster = new EnumBusterReflect(loader, field.getType());
                        enumBusterMap.put(field.getType(), buster);
                    }
                    Enum<?> enumValue = buster.make(enumName);
                    buster.addByValue(enumValue);
                    try {
                        Field constantField = field.getType().getField(enumName);
                        ReflectionHelper.setStaticFinalField(constantField, enumValue);
                    } catch (NoSuchFieldException ignored) {
                        System.out.println("\t\t- Failed to initialize enum field " + enumName);
                    }

                    field.setAccessible(true);
                    field.set(null, enumValue);

                    PostStandardInit.registerEnumLater(origin, enumValue);
                }
            }
        }*/
    }
}
