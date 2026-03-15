package basemod.ledger;

import basemod.EnumPatcher;
import basemod.IEnumPatch;
import basemod.conditionalBonus.IConditionalBonusType;
import com.badlogic.gdx.graphics.Color;
import com.tann.dice.gameplay.effect.eff.conditionalBonus.ConditionalBonusType;
import com.tann.dice.screens.dungeon.panels.book.page.ledgerPage.LedgerPage;

import java.util.HashMap;
import java.util.Map;

public class LedgerPageTypeRegistry {
    public static class LedgerPageTypeRegistrar implements IEnumPatch<LedgerPage.LedgerPageType> {
        public String name;
        public Color color;

        @Override
        public void edit(LedgerPage.LedgerPageType conditionalBonusType) {
            //NOOP
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public Class[] getTypeSignature() {
            return new Class[] { Color.class };
        }

        @Override
        public Object[] getParameters() {
            return new Object[] { color };
        }
    }

    public static Map<String, ILedgerPageType> getLedgerPageTypeMap = new HashMap<>();

    public static ILedgerPageType getLedgerPageType(LedgerPage.LedgerPageType type) {
        return getLedgerPageTypeMap.getOrDefault(type.name(), null);
    }

    public static void registerLedgerPage(String name, Color color, ILedgerPageType ledgerPageType) {
        LedgerPageTypeRegistrar registrar = new LedgerPageTypeRegistrar();
        registrar.name = name;
        registrar.color = color;
        getLedgerPageTypeMap.put(name, ledgerPageType);
        EnumPatcher.registerPatch(LedgerPage.LedgerPageType.class, registrar);
    }
}
