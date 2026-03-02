package basemod.keywords;

import com.tann.dice.gameplay.content.ent.Ent;
import com.tann.dice.gameplay.content.ent.Hero;
import com.tann.dice.gameplay.fightLog.Snapshot;
import com.tann.dice.gameplay.fightLog.command.TargetableCommand;

public interface IOnRescueEffect {
    boolean activate(TargetableCommand command, Hero saved, Ent saver, Snapshot present, Snapshot prePresent);
}
