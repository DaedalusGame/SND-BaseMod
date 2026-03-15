package basemod.atlas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TextureUnpacker;
import com.bord.dice.modthedice.Loader;
import com.bord.dice.modthedice.ModInfo;
import com.bord.dice.modthedice.lib.ByRef;
import com.bord.dice.modthedice.lib.SpirePatch2;
import com.tann.dice.Main;
import org.scannotation.archiveiterator.Filter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.*;
import java.util.*;
import java.util.function.Predicate;

@SpirePatch2(clz = Main.class,
        method = "loadAtli")
public class AtlasPatch {
    private static HashMap<String, byte[]> RESOURCES = new HashMap<>();

    private static byte[] bufferStream(InputStream is) throws IOException
    {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        int nextValue = is.read();
        while (nextValue != -1) {
            byteStream.write(nextValue);
            nextValue = is.read();
        }
        return byteStream.toByteArray();
    }

    private static void writeToFile(String path, byte[] data) {
        try {
            Path outPath = Paths.get(".temp/" + path);
            Files.createDirectories(outPath.getParent());
            Files.write(outPath, data, StandardOpenOption.CREATE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean testHash(String path, int hash) {
        Path outPath = Paths.get(path);

        try {
            int compareHash = Integer.parseInt(Files.readAllLines(outPath).get(0));
            if(compareHash == hash) return true;
        } catch (Exception ignored) {
            //NOOP
        }

        try {
            Files.write(outPath, Arrays.asList(Integer.toString(hash)));
        } catch (Exception ignored) {
            //NOOP
        }

        return false;
    }

    public static void Postfix(@ByRef(type="com.badlogic.gdx.graphics.g2d.TextureAtlas") Object[] ___atlas, @ByRef(type="com.badlogic.gdx.graphics.g2d.TextureAtlas") Object[] ___atlas_3d) {

        extractAssets();

        int hash2d = 0;
        int hash3d = 0;

        boolean any2d = false;
        boolean any3d = false;

        for (String path : RESOURCES.keySet()) {
            String b = path.split(":")[1];
            if (b.endsWith(".png") && !b.endsWith("atlas_image.png")) {
                if (b.startsWith("assets/2d")) {
                    hash2d += path.hashCode();
                    any2d = true;
                }

                if(b.startsWith("assets/3d")) {
                    hash3d += path.hashCode();
                    any3d = true;
                }
            }
        }

        if(any2d) {
            unpack("2d");
        }
        if(any3d) {
            unpack("3d");
        }

        FileHandle atlasResult2D = Gdx.files.local(".temp/packed/2d/atlas_image.atlas");
        FileHandle atlasResult3D = Gdx.files.local(".temp/packed/3d/atlas_image.atlas");

        boolean isDirty2d = any2d && (!testHash(".temp/pack_hash_2d", hash2d) || !atlasResult2D.exists());
        boolean isDirty3d = any3d && (!testHash(".temp/pack_hash_3d", hash3d) || !atlasResult3D.exists());

        for (String path : RESOURCES.keySet()) {
            String b = path.split(":")[1];
            if (b.endsWith(".png") && !b.endsWith("atlas_image.png")) {
                if (b.startsWith("assets/2d") && isDirty2d) {
                    writeToFile(b.replaceFirst("assets/", ""), RESOURCES.get(path));
                }

                if(b.startsWith("assets/3d") && isDirty3d) {
                    writeToFile(b.replaceFirst("assets/", ""), RESOURCES.get(path));
                }
            }
        }

        if(isDirty2d) {
            repack((short) 1024, false, ".temp/2d", ".temp/packed/2d");
        }
        if(isDirty3d) {
            repack((short) 2048, true, ".temp/3d", ".temp/packed/3d");
        }

        ___atlas[0] = new TextureAtlas(atlasResult2D);
        ___atlas_3d[0] = new TextureAtlas(atlasResult3D);
    }

    private static void extractAssets() {
        for (ModInfo modInfo: Loader.MODINFOS) {
            URL url = modInfo.jarURL;

            Filter filter = new Filter() {
                public boolean accepts(String filename) {
                    if (filename.startsWith("assets/")) {
                        return true;
                    }

                    return false;
                }
            };
            try {
                ResourceIterator it = new ResourceIterator(url.openStream(), filter);

                InputStream stream;
                while((stream = it.next()) != null) {
                    RESOURCES.put(modInfo.ID + ":" + it.getPath(), bufferStream(stream));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static int hash(FileHandle f, Predicate<FileHandle> filter) {
        int total = 0;

        if(!filter.test(f)) {
            return total;
        }

        if (f.isDirectory()) {
            FileHandle[] children = f.list();

            for (FileHandle child : children) {
                total += hash(child, filter);
            }
        }

        return (int)((long)(total + f.name().hashCode()) + f.lastModified());
    }

    private static void unpack(String base) {


        FileHandle atlas3d_handle = Gdx.files.internal(base+"/atlas_image.atlas");
        FileHandle atlas3d_image = Gdx.files.internal(base+"/atlas_image.png");

        int hash = hash(atlas3d_image, (file) -> true);
        if(testHash(".temp/extract_hash_"+base, hash)) {
            return;
        }

        System.out.println("runtime unpacking "+base+" atlas...");
        long start = System.currentTimeMillis();

        TextureUnpacker unpacker = new TextureUnpacker();
        TextureAtlas.TextureAtlasData data = new TextureAtlas.TextureAtlasData();
        data.load(atlas3d_handle,Gdx.files.internal(base), false);
        data.getPages().get(0).textureFile = Gdx.files.local(".temp/atlas_image_"+base+".png");
        atlas3d_image.copyTo(Gdx.files.local(".temp/atlas_image_"+base+".png"));
        unpacker.setQuiet(true);
        unpacker.splitAtlas(data, ".temp/"+base);
        System.out.println("done, took " + (System.currentTimeMillis() - start) + "ms");
    }

    private static void repack(short max, boolean threedee, String dir, String outputDir) {
        long start = System.currentTimeMillis();
        System.out.println("packing " + outputDir + "...");
        TexturePacker.Settings settings = new TexturePacker.Settings();
        settings.silent = true;
        settings.fast = true;
        if (threedee) {
            settings.minWidth = max;
            settings.minHeight = max;
            settings.maxWidth = max;
            settings.maxHeight = max;
            settings.paddingX = 1;
            settings.paddingY = 1;
            settings.combineSubdirectories = true;
            settings.filterMag = Texture.TextureFilter.MipMapLinearLinear;
            settings.filterMin = Texture.TextureFilter.MipMapLinearLinear;
            TexturePacker.process(settings, dir, outputDir, "atlas_image");
        } else {
            settings.combineSubdirectories = true;
            settings.maxWidth = max;
            settings.maxHeight = max;
            settings.filterMag = Texture.TextureFilter.Nearest;
            settings.filterMin = Texture.TextureFilter.Nearest;
            TexturePacker.process(settings, dir, outputDir, "atlas_image");
        }

        System.out.println("done, took " + (System.currentTimeMillis() - start) + "ms");
    }
}
