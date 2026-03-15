package basemod;

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

    static List<ModSpecific<IKeywordInitializer>> keywordInitializers = new ArrayList<>();
    public static List<ModSpecific<IInitializeItemPipes>> itemPipes = new ArrayList<>();
    public static List<ModSpecific<IInitializeModPipes>> modPipes = new ArrayList<>();
    public static List<ModSpecific<IInitializeHeroPipes>> heroPipes = new ArrayList<>();
    public static List<ModSpecific<IInitializeMonsterPipes>> monsterPipes = new ArrayList<>();

    public static void initialize() {
        System.out.println("Hello world!");
    }

    public static void register(IKeywordInitializer initializer) {
        keywordInitializers.add(new ModSpecific<>(Loader.getCurrentMod(), initializer));
    }

    public static void register(IInitializeItemPipes initializer) {
        itemPipes.add(new ModSpecific<>(Loader.getCurrentMod(), initializer));
    }

    public static void register(IInitializeModPipes initializer) {
        modPipes.add(new ModSpecific<>(Loader.getCurrentMod(), initializer));
    }

    public static void register(IInitializeHeroPipes initializer) {
        heroPipes.add(new ModSpecific<>(Loader.getCurrentMod(), initializer));
    }

    public static void register(IInitializeMonsterPipes initializer) {
        monsterPipes.add(new ModSpecific<>(Loader.getCurrentMod(), initializer));
    }

    public static void runKeywordInitializers() {
        for (ModSpecific<IKeywordInitializer> keywordInitializer: keywordInitializers) {
            keywordInitializer.content.initialize();
        }
    }
}
