package basemod.keywords;

import com.tann.dice.gameplay.content.ent.Ent;
import com.tann.dice.gameplay.effect.eff.Eff;
import com.tann.dice.gameplay.effect.targetable.Targetable;
import com.tann.dice.gameplay.fightLog.Snapshot;
import com.tann.dice.screens.dungeon.TargetingManager;

public interface IKeywordUsable {
    boolean isUsable(TargetingManager manager, Eff first, Snapshot snapshot, Targetable t, boolean onlyRecommended, boolean cantrip);

    String getInvalidTargetReason(TargetingManager manager, Eff first, Snapshot snapshot, Ent target, Targetable targetable, boolean allowBadTargets);
}
