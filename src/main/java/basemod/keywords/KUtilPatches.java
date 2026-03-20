package basemod.keywords;

import basemod.BaseMod;
import basemod.KeywordRegistry;
import com.bord.dice.modthedice.lib.SpirePatch2;
import com.bord.dice.modthedice.lib.SpireReturn;
import com.tann.dice.gameplay.content.ent.type.EntType;
import com.tann.dice.gameplay.content.ent.type.HeroType;
import com.tann.dice.gameplay.effect.eff.Eff;
import com.tann.dice.gameplay.effect.eff.keyword.KUtils;
import basemod.EnumPatcher;
import com.tann.dice.gameplay.effect.eff.keyword.Keyword;
import com.tann.dice.gameplay.effect.targetable.ability.spell.SpellUtils;

public class KUtilPatches {
    @SpirePatch2(
            clz = KUtils.class,
            method = "init"
    )
    public static class Init {
        public static void Prefix() {
            BaseMod.runKeywordInitializers();

            EnumPatcher.initialize();
        }
    }

    @SpirePatch2(clz = KUtils.class, method = "affectBaseValue")
    public static class AffectBaseValue {
        public static SpireReturn<Float> Prefix(Keyword k, Eff e, int tier, float pips, boolean player) {
            IKeywordValueCalculator calc = KeywordRegistry.valueCalculatorRegistry.get(k);
            if(calc != null) {
                return SpireReturn.Return(calc.affectBaseValue(k, e, tier, pips, player));
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch2(clz = KUtils.class, method = "getValueMultiplier")
    public static class GetValueMultiplier {
        public static SpireReturn<Float> Prefix(Keyword k, Eff e, boolean player, int tier) {
            IKeywordValueCalculator calc = KeywordRegistry.valueCalculatorRegistry.get(k);
            if(calc != null) {
                return SpireReturn.Return(calc.getValueMultiplier(k, e, player, tier));
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch2(clz = KUtils.class, method = "getFinalEffectTierAdjustment")
    public static class GetFinalEffectTierAdjustment {
        public static SpireReturn<Float> Prefix(Keyword k, Eff e, float val, int tier, EntType type) {
            IKeywordValueCalculator calc = KeywordRegistry.valueCalculatorRegistry.get(k);
            if(calc != null) {
                boolean isPlayer = type instanceof HeroType;
                return SpireReturn.Return(calc.getFinalEffectTierAdjustment(k, e, val, tier, type, isPlayer));
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch2(clz = KUtils.class, method = "getModTierAllMonster")
    public static class GetModTierAllMonster {
        public static SpireReturn<Float> Prefix(Keyword k) {
            IKeywordValueCalculator calc = KeywordRegistry.valueCalculatorRegistry.get(k);
            if(calc != null) {
                return SpireReturn.Return(calc.getModTier(k, false));
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch2(clz = KUtils.class, method = "getModTierAllHero")
    public static class GetModTierAllHero {
        public static SpireReturn<Float> Prefix(Keyword k) {
            IKeywordValueCalculator calc = KeywordRegistry.valueCalculatorRegistry.get(k);
            if(calc != null) {
                return SpireReturn.Return(calc.getModTier(k, true));
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch2(clz = KUtils.class, method = "allowAutoskip")
    public static class AllowAutoskip {
        public static SpireReturn<Boolean> Prefix(Keyword k) {
            IKeywordValueCalculator calc = KeywordRegistry.valueCalculatorRegistry.get(k);
            if(calc != null) {
                return SpireReturn.Return(calc.allowAutoskip(k));
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch2(clz = SpellUtils.class, method = "allowAddingKeyword")
    public static class SpellAllowAddingKeyword {
        public static SpireReturn<Boolean> Prefix(Keyword k) {
            IKeywordValueCalculator calc = KeywordRegistry.valueCalculatorRegistry.get(k);
            if(calc != null) {
                Boolean res = calc.allowForSpell(k);
                if(res != null) {
                    return SpireReturn.Return(res);
                }
            }
            return SpireReturn.Continue();
        }
    }
}
