package basemod.keywords;

import com.tann.dice.gameplay.content.ent.Ent;
import com.tann.dice.gameplay.fightLog.Snapshot;
import com.tann.dice.gameplay.fightLog.command.TargetableCommand;

public interface IOnKillEffect {
    boolean activate(TargetableCommand command, Ent killer, Snapshot prePresent, Snapshot present);
}
