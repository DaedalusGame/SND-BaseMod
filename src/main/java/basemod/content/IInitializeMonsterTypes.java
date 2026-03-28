package basemod.content;

import com.tann.dice.gameplay.content.ent.type.MonsterType;

import java.lang.reflect.Modifier;
import java.util.List;

public interface IInitializeMonsterTypes {
    default void initialize(List<MonsterType> monsterTypes) {
        //NOOP
    }

    default void initializeNightmare(List<MonsterType> monsterTypes) {
        //NOOP
    }
}
