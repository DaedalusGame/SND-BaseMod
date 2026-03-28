package basemod.content;

import com.tann.dice.gameplay.content.ent.type.MonsterType;
import com.tann.dice.gameplay.content.item.ItBill;

import java.util.List;

public interface IInitializeItems {
    default void initialize(List<ItBill> items) {
        //NOOP
    }
}
