package basemod.content;

import basemod.BaseMod;
import basemod.ModSpecificList;
import basemod.util.ContentHandler;
import basemod.util.ContentSource;
import com.bord.dice.modthedice.lib.SpirePatch2;
import com.tann.dice.gameplay.content.ent.type.HeroType;
import com.tann.dice.gameplay.content.ent.type.MonsterType;
import com.tann.dice.gameplay.content.ent.type.blob.heroblobs.HeroTypeBlob;
import com.tann.dice.gameplay.content.ent.type.blob.monster.MonsterTypeBlob;
import com.tann.dice.gameplay.content.ent.type.blob.monster.MonsterTypeBlobNightmare;
import com.tann.dice.gameplay.content.ent.type.lib.HeroTypeLib;
import com.tann.dice.gameplay.content.item.ItBill;
import com.tann.dice.gameplay.content.item.blob.ItemBlob;
import com.tann.dice.gameplay.effect.targetable.ability.Ability;
import com.tann.dice.gameplay.effect.targetable.ability.AbilityUtils;
import com.tann.dice.gameplay.modifier.Modifier;
import com.tann.dice.gameplay.modifier.bless.BlessingLib;
import com.tann.dice.gameplay.modifier.generation.CurseLib;
import com.tann.dice.gameplay.modifier.tweak.TweakLib;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;

public class ContentPatch {
    private static <T, TInit> void initialize(List<T> contentList, ContentHandler<T> handler, ModSpecificList<TInit> initList, BiConsumer<TInit, List<T>> init) {
        handler.setBaseGame();
        for (BaseMod.ModSpecific<TInit> initializer : initList) {
            init.accept(initializer.content, contentList);
            handler.setMod(initializer.modInfo);
        }
    }

    @SpirePatch2(clz = TweakLib.class, method = "makeAll")
    public static class Tweaks {
        public static List<Modifier> Postfix(List<Modifier> __result) {
            __result = new ArrayList<>(__result);

            List<Modifier> result = __result;
            ContentHandler<Modifier> handler = ContentHandler.make(() -> result, (modifier, contentSource) -> {});

            initialize(__result, handler, BaseMod.modifiers, IInitializeModifiers::initializeTweaks);

            return __result;
        }
    }

    @SpirePatch2(clz = CurseLib.class, method = "makeAll")
    public static class Curses {
        public static List<Modifier> Postfix(List<Modifier> __result) {
            __result = new ArrayList<>(__result);

            List<Modifier> result = __result;
            ContentHandler<Modifier> handler = ContentHandler.make(() -> result, (modifier, contentSource) -> {});

            initialize(__result, handler, BaseMod.modifiers, IInitializeModifiers::initializeCurses);

            return __result;
        }
    }

    @SpirePatch2(clz = BlessingLib.class, method = "makeAll")
    public static class Blessings {
        public static List<Modifier> Postfix(List<Modifier> __result) {
            __result = new ArrayList<>(__result);

            List<Modifier> result = __result;
            ContentHandler<Modifier> handler = ContentHandler.make(() -> result, (modifier, contentSource) -> {});

            initialize(__result, handler, BaseMod.modifiers, IInitializeModifiers::initializeBlessings);

            return __result;
        }
    }

    @SpirePatch2(clz = HeroTypeBlob.class, method = "makeDesigned")
    public static class HeroTypes {
        public static List<HeroType> Postfix(List<HeroType> __result) {
            __result = new ArrayList<>(__result);

            List<HeroType> result = __result;
            ContentHandler<HeroType> handler = ContentHandler.make(() -> result, (modifier, contentSource) -> {});

            initialize(__result, handler, BaseMod.heroTypes, IInitializeHeroTypes::initialize);

            return __result;
        }
    }

    @SpirePatch2(clz = MonsterTypeBlob.class, method = "makeAll")
    public static class MonsterTypes {
        public static List<MonsterType> Postfix(List<MonsterType> __result) {
            __result = new ArrayList<>(__result);

            List<MonsterType> result = __result;
            ContentHandler<MonsterType> handler = ContentHandler.make(() -> result, (modifier, contentSource) -> {});

            initialize(__result, handler, BaseMod.monsterTypes, IInitializeMonsterTypes::initialize);

            return __result;
        }
    }

    @SpirePatch2(clz = MonsterTypeBlobNightmare.class, method = "make")
    public static class MonsterTypesNightmare {
        public static List<MonsterType> Postfix(List<MonsterType> __result) {
            __result = new ArrayList<>(__result);

            List<MonsterType> result = __result;
            ContentHandler<MonsterType> handler = ContentHandler.make(() -> result, (modifier, contentSource) -> {});

            initialize(__result, handler, BaseMod.monsterTypes, IInitializeMonsterTypes::initializeNightmare);

            return __result;
        }
    }

    @SpirePatch2(clz = ItemBlob.class, method = "makeAll")
    public static class Items {
        public static List<ItBill> Postfix(List<ItBill> __result) {
            __result = new ArrayList<>(__result);

            List<ItBill> result = __result;
            ContentHandler<ItBill> handler = ContentHandler.make(() -> result, (modifier, contentSource) -> {});

            initialize(__result, handler, BaseMod.items, IInitializeItems::initialize);

            return __result;
        }
    }

    @SpirePatch2(clz = AbilityUtils.class, method = "getAll")
    public static class Abilities {
        public static List<Ability> Postfix(List<Ability> __result) {
            __result = new ArrayList<>(__result);

            List<Ability> result = __result;
            ContentHandler<Ability> handler = ContentHandler.make(() -> result, (modifier, contentSource) -> {});

            initialize(__result, handler, BaseMod.abilities, IInitializeAbilities::initialize);

            return __result;
        }
    }
}
