package basemod.pipes;

import com.tann.dice.gameplay.content.ent.type.HeroType;
import com.tann.dice.gameplay.content.ent.type.MonsterType;
import com.tann.dice.gameplay.content.gen.pipe.Pipe;

import java.util.List;

public interface IInitializePipes<T> {
    void initialize();

    void modifyHiddenModifiers(List<T> items);
}
