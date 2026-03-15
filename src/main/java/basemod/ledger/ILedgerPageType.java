package basemod.ledger;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.tann.dice.screens.dungeon.panels.book.page.ledgerPage.LedgerPage;

public interface ILedgerPageType {
    String getName(LedgerPage.LedgerPageType type);

    Actor getActor(int contentWidth);
}
