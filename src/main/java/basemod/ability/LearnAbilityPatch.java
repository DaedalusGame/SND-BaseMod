package basemod.ability;

import com.bord.dice.modthedice.lib.SpirePatch2;
import com.bord.dice.modthedice.lib.SpireReturn;
import com.tann.dice.gameplay.effect.targetable.ability.Ability;
import com.tann.dice.gameplay.trigger.personal.Personal;
import com.tann.dice.gameplay.trigger.personal.spell.learn.LearnAbility;

import java.util.function.Function;

@SpirePatch2(clz = LearnAbility.class, method = "make")
public class LearnAbilityPatch {
    public static SpireReturn<Personal> Prefix(Ability ab) {
        for (Function<Ability, LearnAbility> mapper : AbilityRegistry.classToLearnAbilityMapper) {
            LearnAbility learn = mapper.apply(ab);
            if(learn != null) {
                return SpireReturn.Return(learn);
            }
        }

        return SpireReturn.Continue();
    }
}
