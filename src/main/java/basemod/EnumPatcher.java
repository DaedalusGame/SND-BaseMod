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
import java.util.*;
import java.util.stream.Collectors;

public class EnumPatcher {
    public static class PatchList<T> {
        public Class<T> type;
        public List<IEnumPatch<T>> patches = new ArrayList<>();

        public PatchList(Class<T> type) {
            this.type = type;
        }

        public void add(IEnumPatch<T> patch) {
            patches.add(patch);
        }

        public void apply(ClassLoader loader, Map<Class<?>, EnumBusterReflect> enumBusterMap) {
            System.out.println("Patching "+type);

            for (IEnumPatch<T> patch : patches) {
                try {
                    ab(type, loader, enumBusterMap, patch);
                } catch (IllegalAccessException | NoSuchFieldException | ClassNotFoundException e) {
                    //NOOP
                }
            }
        }
    }

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

    private static Map<Class, PatchList> patchLists = new HashMap<>();

    private static Map<Class, Float> sortOrder = new HashMap<>();

    public static <T extends Enum<?>> void registerSortOrder(Class<T> type, float order) {
        sortOrder.put(type, order);
    }

    public static <T extends Enum<?>> void registerPatch(Class<T> type, IEnumPatch<T> patch) {
        PatchList<T> patchList = patchLists.getOrDefault(type, null);
        if(patchList == null){
            patchList = new PatchList<>(type);
            patchLists.put(type,patchList);
        }
        patchList.add(patch);
    }

    public static void initialize() {
        try {
            Field poolField = Loader.class.getDeclaredField("POOL");
            poolField.setAccessible(true);

            registerSortOrder(ConditionalBonusType.class, 50f);
            registerSortOrder(Keyword.class, 100f);

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

    private static <T> void ab(Class<T> type, ClassLoader loader, Map<Class<?>, EnumBusterReflect> enumBusterMap, IEnumPatch<T> enumPatch)
            throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException
    {
        String name = enumPatch.getName();
        System.out.println("Add enum: "+name);

        EnumBusterReflect buster;
        if (enumBusterMap.containsKey(type)) {
            buster = enumBusterMap.get(type);
        } else {
            buster = new EnumBusterReflect(loader, type);
            enumBusterMap.put(type, buster);
        }
        Object[] parameters = enumPatch.getParameters();
        flattenLazies(parameters);
        Enum<?> enumValue = buster.make(name, Keyword.values().length, enumPatch.getTypeSignature(), parameters);
        enumPatch.edit((T)enumValue);
        buster.addByValue(enumValue);
        try {
            Field constantField = type.getField(name);
            ReflectionHelper.setStaticFinalField(constantField, enumValue);
        } catch (NoSuchFieldException ignored) {
            System.out.println("\t\t- Failed to initialize enum field " + name);
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

        List<PatchList> sortedPatches = patchLists.values()
                .stream()
                .sorted(new Comparator<PatchList>() {
                    @Override
                    public int compare(PatchList o1, PatchList o2) {
                        return Float.compare(
                                sortOrder.getOrDefault(o1.type, 0f),
                                sortOrder.getOrDefault(o2.type, 0f)
                        );
                    }
                })
                .collect(Collectors.toList());
        for (PatchList patchList : sortedPatches) {
            patchList.apply(loader, enumBusterMap);
        }

        /*for (ConditionalBonusTypeRegistry.ConditionalBonusTypeRegistrar conditionalBonusType : ConditionalBonusTypeRegistry.registry) {
            a(loader, enumBusterMap, conditionalBonusType);
        }

        for (KeywordRegistry.KeywordRegistrar keyword : KeywordRegistry.registry) {
            a(loader, enumBusterMap, keyword);
        }

        for (SpecificSidesTypeRegistry.SpecificSidesTypeRegistrar keyword : SpecificSidesTypeRegistry.registry) {
            a(loader, enumBusterMap, keyword);
        }*/

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
