package basemod.keywords;

import com.tann.dice.gameplay.content.ent.Ent;
import com.tann.dice.gameplay.effect.eff.Eff;
import com.tann.dice.gameplay.effect.eff.keyword.Keyword;
import com.tann.dice.gameplay.effect.targetable.Targetable;
import com.tann.dice.gameplay.fightLog.EntState;

import java.util.List;

public interface IOnUseEffect {
    void activate(EntState ent, int kVal, Eff eff, List<Keyword> keywords, Ent source, Targetable targetable);
}
