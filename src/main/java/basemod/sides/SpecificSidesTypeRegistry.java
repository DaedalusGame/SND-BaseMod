package basemod.sides;

import basemod.LazyS;
import basemod.conditionalBonus.IConditionalBonusType;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.tann.dice.gameplay.trigger.personal.affectSideModular.condition.SpecificSidesType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpecificSidesTypeRegistry {
    public static class SpecificSidesTypeRegistrar {
        public String name;
        public int[] sideIndices;
        public LazyS<TextureRegion> texture;
        public Vector2[] sidePositions;
        public String description;
        public String shortName;
    }

    public static List<SpecificSidesTypeRegistrar> registry = new ArrayList<>();

    public static void registerSpecificSidesType(String name, int[] indices, LazyS<TextureRegion> tex, Vector2[] sidePositions, String description, String shortName) {
        SpecificSidesTypeRegistrar i = new SpecificSidesTypeRegistrar();
        i.name = name;
        i.sideIndices = indices;
        i.texture = tex;
        i.sidePositions = sidePositions;
        i.description = description;
        i.shortName = shortName;
        registry.add(i);
    }
}