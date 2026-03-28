package basemod.content;

import com.tann.dice.gameplay.content.ent.type.HeroType;
import com.tann.dice.gameplay.content.item.ItBill;

import java.util.List;

public interface IInitializeHeroTypes {
    default void initialize(List<HeroType> heroTypes) {
        //NOOP
    }
}
