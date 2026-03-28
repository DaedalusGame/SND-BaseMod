package basemod;

import basemod.content.*;
import basemod.keywords.IKeywordInitializer;
import basemod.ledger.ILedgerPageType;
import basemod.ledger.LedgerPageTypeRegistry;
import basemod.pipes.IInitializeHeroPipes;
import basemod.pipes.IInitializeItemPipes;
import basemod.pipes.IInitializeModPipes;
import basemod.pipes.IInitializeMonsterPipes;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.bord.dice.modthedice.Loader;
import com.bord.dice.modthedice.ModInfo;
import com.bord.dice.modthedice.lib.SpireInitializer;
import com.tann.dice.screens.dungeon.panels.book.page.ledgerPage.LedgerPage;
import com.tann.dice.util.Colours;
import com.tann.dice.util.Pixl;

import java.util.ArrayList;
import java.util.List;

@SpireInitializer
public class BaseMod {
    public static class ModSpecific<T> {
        public T content;
        public ModInfo modInfo;

        public ModSpecific(ModInfo modInfo, T content) {
            this.content = content;
            this.modInfo = modInfo;
        }
    }

    static ModSpecificList<IKeywordInitializer> keywordInitializers = new ModSpecificList<>();
    public static ModSpecificList<IInitializeItemPipes> itemPipes = new ModSpecificList<>();
    public static ModSpecificList<IInitializeModPipes> modPipes = new ModSpecificList<>();
    public static ModSpecificList<IInitializeHeroPipes> heroPipes = new ModSpecificList<>();
    public static ModSpecificList<IInitializeMonsterPipes> monsterPipes = new ModSpecificList<>();

    public static ModSpecificList<IInitializeAbilities> abilities = new ModSpecificList<>();
    public static ModSpecificList<IInitializeHeroTypes> heroTypes = new ModSpecificList<>();
    public static ModSpecificList<IInitializeMonsterTypes> monsterTypes = new ModSpecificList<>();
    public static ModSpecificList<IInitializeItems> items = new ModSpecificList<>();
    public static ModSpecificList<IInitializeModifiers> modifiers = new ModSpecificList<>();

    public static void initialize() {
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

    public static void register(IInitializeAbilities initializer) {
        abilities.add(initializer);
    }

    public static void register(IInitializeHeroTypes initializer) {
        heroTypes.add(initializer);
    }

    public static void register(IInitializeMonsterTypes initializer) {
        monsterTypes.add(initializer);
    }

    public static void register(IInitializeItems initializer) {
        items.add(initializer);
    }

    public static void register(IInitializeModifiers initializer) {
        modifiers.add(initializer);
    }

    public static void runKeywordInitializers() {
        for (ModSpecific<IKeywordInitializer> keywordInitializer: keywordInitializers.getAll()) {
            keywordInitializer.content.initialize();
        }
    }
}
