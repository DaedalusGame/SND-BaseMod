package basemod;

import basemod.keywords.IKeywordInitializer;
import basemod.pipes.IInitializeHeroPipes;
import basemod.pipes.IInitializeItemPipes;
import basemod.pipes.IInitializeModPipes;
import basemod.pipes.IInitializeMonsterPipes;
import com.bord.dice.modthedice.lib.SpireInitializer;

import java.util.ArrayList;
import java.util.List;

@SpireInitializer
public class BaseMod {
    static List<IKeywordInitializer> keywordInitializers = new ArrayList<>();
    public static List<IInitializeItemPipes> itemPipes = new ArrayList<>();
    public static List<IInitializeModPipes> modPipes = new ArrayList<>();
    public static List<IInitializeHeroPipes> heroPipes = new ArrayList<>();
    public static List<IInitializeMonsterPipes> monsterPipes = new ArrayList<>();

    public static void initialize() {
        System.out.println("Hello world!");
    }

    public static void register(IKeywordInitializer initializer) {
        keywordInitializers.add(initializer);
    }

    public static void register(IInitializeItemPipes initializer) {
        itemPipes.add(initializer);
    }

    public static void register(IInitializeModPipes initializer) {
        modPipes.add(initializer);
    }

    public static void register(IInitializeHeroPipes initializer) {
        heroPipes.add(initializer);
    }

    public static void register(IInitializeMonsterPipes initializer) {
        monsterPipes.add(initializer);
    }

    public static void runKeywordInitializers() {
        for (IKeywordInitializer keywordInitializer: keywordInitializers) {
            keywordInitializer.initialize();
        }
    }
}
