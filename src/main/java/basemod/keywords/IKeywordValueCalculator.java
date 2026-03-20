package basemod.keywords;

import com.tann.dice.gameplay.content.ent.type.EntType;
import com.tann.dice.gameplay.effect.eff.Eff;
import com.tann.dice.gameplay.effect.eff.keyword.Keyword;

public interface IKeywordValueCalculator {

    default float affectBaseValue(Keyword k, Eff e, int tier, float pips, boolean isPlayer) {
        return pips;
    }

    default float getValueMultiplier(Keyword k, Eff e, boolean player, int tier) {
        return Float.NaN;
    }

    default float getFinalEffectTierAdjustment(Keyword k, Eff e, float val, int tier, EntType type, boolean isPlayer) {
        return val;
    }

    default float getModTier(Keyword k, boolean hero) {
        return 0;
    }

    default boolean allowAutoskip(Keyword k) {
        return false;
    }

    default Boolean allowForSpell(Keyword k) {
        return null;
    }
}
