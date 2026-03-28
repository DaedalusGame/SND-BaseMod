package basemod.content;

import com.tann.dice.gameplay.content.ent.type.HeroType;
import com.tann.dice.gameplay.effect.targetable.ability.Ability;

import java.util.List;

public interface IInitializeAbilities {
    default void initialize(List<Ability> abilities) {
        //NOOP
    }
}
