package basemod.render;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tann.dice.statics.Images;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TextureUtil {
    public static TextureRegion loadTex(String path) {
        try {
            Method loadMethod = Images.class.getDeclaredMethod("load", String.class);
            loadMethod.setAccessible(true);
            return (TextureRegion)loadMethod.invoke(null, path);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
