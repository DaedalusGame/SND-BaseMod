package basemod.content;

import com.tann.dice.gameplay.modifier.Modifier;

import java.util.List;

public interface IInitializeModifiers {
    default void initializeCurses(List<Modifier> modifiers) {
        //NOOP
    }

    default void initializeTweaks(List<Modifier> modifiers) {
        //NOOP
    }

    default void initializeBlessings(List<Modifier> modifiers) {
        //NOOP
    }
}
