package basemod;

import com.badlogic.gdx.graphics.Color;
import com.tann.dice.gameplay.effect.eff.conditionalBonus.ConditionalBonus;
import com.tann.dice.gameplay.effect.eff.conditionalBonus.conditionalRequirement.ConditionalRequirement;
import com.tann.dice.gameplay.effect.eff.keyword.Keyword;
import com.tann.dice.gameplay.effect.eff.keyword.KeywordAllowType;
import com.tann.dice.gameplay.effect.eff.keyword.KeywordCombineType;
import basemod.keywords.*;

import java.util.ArrayList;
import java.util.function.Consumer;

public class KeywordRegistry {
    public static class KeywordRegistrar {
        public String name;
        public Class[] typeSig;
        public Object[] obj;
        public Consumer<Object[]> filler;
        public Consumer<Keyword> editor;
    }

    public static ArrayList<KeywordRegistrar> registry = new ArrayList<>();
    public static EnumRegistry<Keyword, IOnUseEffect> onUseEffects = new EnumRegistry<>();
    public static EnumRegistry<Keyword, IOnKillEffect> onKillEffects = new EnumRegistry<>();
    public static EnumRegistry<Keyword, IOnRescueEffect> onRescueEffects = new EnumRegistry<>();
    public static EnumRegistry<Keyword, IOnAfterUseEffect> onAfterUseEffects = new EnumRegistry<>();
    public static EnumRegistry<Keyword, IKeywordUsable> keywordUsableRegistry = new EnumRegistry<>();
    public static EnumRegistry<Keyword, IKeywordColorTag> colorTagRegistry = new EnumRegistry<>();

    public static void registerEnumPair(String name, int num) {
        KeywordRegistrar keyword = new KeywordRegistrar();
        keyword.name = name;
        keyword.typeSig = new Class[] { int.class };
        keyword.obj = new Object[] { num };
        registry.add(keyword);
    }

    public static void registerEnumMeta(String name, LazyKeyword keywordA, LazyKeyword keywordB, KeywordCombineType combineType)
    {
        KeywordRegistrar keyword = new KeywordRegistrar();
        keyword.name = name;
        keyword.typeSig = new Class[] { Keyword.class, Keyword.class, KeywordCombineType.class };
        keyword.obj = new Object[]{null, null, combineType};
        keyword.filler = objects -> {
            objects[0] = Keyword.byName(keywordA.name);
            objects[1] = Keyword.byName(keywordB.name);
        };
        registry.add(keyword);
    }

    public static void registerGroup(String name, LazyKeyword kw, boolean side) {
        KeywordRegistrar keyword = new KeywordRegistrar();
        keyword.name = name;
        keyword.typeSig = new Class[] {
                Keyword.class,
                boolean.class
        };
        keyword.obj = new Object[]{
                kw,
                side
        };
        registry.add(keyword);
    }

    public static void registerBasic(String name, Color color, String rules, String extraRules, KeywordAllowType allowType) {
        KeywordRegistrar keyword = new KeywordRegistrar();
        keyword.name = name;
        keyword.typeSig = new Class[] {
                Color.class,
                String.class,
                String.class,
                KeywordAllowType.class
        };
        keyword.obj = new Object[]{
                color,
                rules,
                extraRules,
                allowType
        };
        registry.add(keyword);
    }

    public static void registerConditionalRequirement(String name, Color color, String rules, String extraRules, LazyS<ConditionalRequirement> targettingRequirement) {
        KeywordRegistrar keyword = new KeywordRegistrar();
        keyword.name = name;
        keyword.typeSig = new Class[] {
                Color.class,
                String.class,
                String.class,
                ConditionalRequirement.class
        };
        keyword.obj = new Object[]{
                color,
                rules,
                extraRules,
                targettingRequirement
        };
        registry.add(keyword);
    }

    public static void registerConditionalBonus(String name, Color color, String rules, String extraRules, LazyS<ConditionalBonus> bonus, Consumer<Keyword> editor) {
        KeywordRegistrar keyword = new KeywordRegistrar();
        keyword.name = name;
        keyword.typeSig = new Class[] {
                Color.class,
                String.class,
                String.class,
                ConditionalBonus.class
        };
        keyword.obj = new Object[]{
                color,
                rules,
                extraRules,
                bonus
        };
        keyword.editor = editor;
        registry.add(keyword);
    }

    public static void registerMinus(String name, LazyKeyword swapKeyword) {
        KeywordRegistrar keyword = new KeywordRegistrar();
        keyword.name = name;
        keyword.typeSig = new Class[] {
                Keyword.class,
                float.class
        };
        keyword.obj = new Object[]{
                swapKeyword,
                1.0f,
        };
        registry.add(keyword);
    }

    public static void registerSwap(String name, LazyKeyword swapKeyword) {
        KeywordRegistrar keyword = new KeywordRegistrar();
        keyword.name = name;
        keyword.typeSig = new Class[] {
                Keyword.class,
                int[].class
        };
        keyword.obj = new Object[]{
                swapKeyword,
                new int[0],
        };
        registry.add(keyword);
    }

    public static void registerSelf(String name, LazyKeyword swapKeyword) {
        KeywordRegistrar keyword = new KeywordRegistrar();
        keyword.name = name;
        keyword.typeSig = new Class[] {
                Keyword.class,
                long[].class
        };
        keyword.obj = new Object[]{
                swapKeyword,
                new long[0],
        };
        registry.add(keyword);
    }
}
