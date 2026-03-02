package basemod.keywords;

import com.tann.dice.gameplay.content.ent.Ent;
import com.tann.dice.gameplay.effect.eff.Eff;
import com.tann.dice.gameplay.effect.eff.keyword.Keyword;
import com.tann.dice.gameplay.fightLog.Snapshot;

import java.util.List;

public interface IOnAfterUseEffect {
    void activate(int kVal, Eff src, List<Keyword> keywords, Snapshot snapshot, Ent source, int sideIndex);
}
