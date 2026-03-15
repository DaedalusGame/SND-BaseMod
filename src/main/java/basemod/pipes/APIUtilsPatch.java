package basemod.pipes;

import basemod.util.ContentSource;
import basemod.util.ReflectionUtils;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.bord.dice.modthedice.lib.*;
import com.bord.dice.modthedice.patcher.PatchingException;
import com.tann.dice.gameplay.content.gen.pipe.Pipe;
import com.tann.dice.gameplay.content.gen.pipe.entity.hero.PipeHero;
import com.tann.dice.gameplay.content.gen.pipe.entity.monster.PipeMonster;
import com.tann.dice.gameplay.content.gen.pipe.item.PipeItem;
import com.tann.dice.gameplay.content.gen.pipe.mod.PipeMod;
import com.tann.dice.screens.dungeon.panels.book.page.BookPage;
import com.tann.dice.screens.dungeon.panels.book.page.stuffPage.APIUtils;
import com.tann.dice.screens.dungeon.panels.book.page.stuffPage.PipeType;
import com.tann.dice.statics.sound.Sounds;
import com.tann.dice.util.Colours;
import com.tann.dice.util.Pixl;
import com.tann.dice.util.Rectactor;
import com.tann.dice.util.Tann;
import com.tann.dice.util.lang.Words;
import com.tann.dice.util.listener.TannListener;
import com.tann.dice.util.ui.standardButton.StandardButton;
import com.tann.dice.util.ui.standardButton.StandardButtonStyle;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

import java.util.*;

public class APIUtilsPatch {
    public static String getAlphabet() {
        return Words.DOUBLE_ALPHABET;
    }

    static ReflectionUtils.CallableMethod shouldSkip;
    static ReflectionUtils.CallableMethod makeDocumentation;
    static ReflectionUtils.CallableMethod sortedPipesToView;

    static {
        shouldSkip = ReflectionUtils.getMethod(APIUtils.class, "shouldSkip", Pipe.class);
        makeDocumentation = ReflectionUtils.getMethod(APIUtils.class, "makeDocumentation", String.class, Color.class, List.class, BookPage.class, int.class);
        sortedPipesToView = ReflectionUtils.getMethod(APIUtils.class, "sortedPipesToView", List.class);
    }

    static String toBigIdentifier(int i) {
        String alphabet = getAlphabet();
        int e = i / alphabet.length();
        i = i % alphabet.length();

        if(e > alphabet.length()) {
            return "??";
        }

        if(e <= 0) {
            return "" + alphabet.charAt(i);
        } else {
            return "" + alphabet.charAt(e - 1) + alphabet.charAt(i);
        }
    }

    private static Collection<PipeGroup> groupPipesByContent(Collection<Pipe> pipes) {
        Map<String, PipeGroup> groups = new HashMap<>();

        for (Pipe pipe : pipes) {
            ContentSource source = PipePatch.contentSource.get(pipe);
            String id = null;
            if (source != null && source.isModded()) {
                id = source.modInfo.ID;
            }

            PipeGroup group = groups.computeIfAbsent(id, x -> new PipeGroup(source));
            group.pipes.add(pipe);
        }

        return groups.values();
    }

    @SpirePatch2(clz = APIUtils.class, method = "makeDocumentation")
    public static class MakeDocumentation {
        public static ExprEditor Instrument()
        {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    if(m.getMethodName().equals("size")) {
                        m.replace("{$_ = 0;}");
                    }
                }
            };
        }

        @SpireInsertPatch(
                locator= Locator.class,
                localvars={"p","textWidth","texturey"}
        )
        public static void Insert(String title, Color col, List<Pipe> pipes, final BookPage page, final int contentWidth, Pixl p, int textWidth, boolean texturey) {
            int actualIndex = 0;

            for (Pipe pipe: pipes) {
                if (!(boolean)shouldSkip.invoke(null, pipe) && pipe.isTexturey() == texturey && (!texturey || !pipe.document().contains("onster") && !pipe.document().contains("item"))) {
                    Group a = (new Pixl()).text("[notranslate]" + (pipe.isComplexAPI() ? "[purple]" : "") + toBigIdentifier(actualIndex) + ")").pix();
                    a.setWidth(15.0F);

                    String doc = pipe.document();
                    if (texturey) {
                        doc = doc.replaceAll("hero", "[yellow]a[cu][orange]n[cu][grey]y[cu]");
                    }

                    Group b = (new Pixl()).actor(a).text("[notranslate][grey]" + doc.replace(".", ".[p][p][q]"), textWidth).pix();
                    a = b;

                    b.addListener(new TannListener() {
                        public boolean action(int button, int pointer, float x, float y) {
                            page.showThing(APIUtils.makeTMPageAPI2(contentWidth, page, pipe));
                            return true;
                        }
                    });

                    if (b.getHeight() > 8.0F) {
                        Color c = Colours.withAlpha(Colours.randomHashed(pipe.getClass().getSimpleName().hashCode() + pipe.document().hashCode()), 0.09F).cpy();
                        Rectactor ra = new Rectactor((Actor)a, c);
                        ((Group)a).addActor(ra);
                        ra.toBack();
                    }

                    p.actor(b).row();
                    ++actualIndex;
                }
            }
        }

        private static class Locator extends SpireInsertLocator {
            // This is the abstract method from SpireInsertLocator that will be used to find the line
            // numbers you want this patch inserted at
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(APIUtils.class, "sortedPipesToView");

                return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<Matcher>(), finalMatcher);
            }
        }
    }

    public static class PipeGroup {
        public ContentSource source;
        public List<Pipe> pipes = new ArrayList<>();

        public PipeGroup(ContentSource source) {
            this.source = source;
        }

        public String getModName() {
            if(source.modInfo != null) {
                return source.modInfo.Name;
            } else {
                return "";
            }
        }
    }

    @SpirePatch2(clz = APIUtils.class, method = "makeTMPageAPI1")
    public static class MakeTMPageAPI1 {
        @SpireInsertPatch(
                locator=Locator.class,
                localvars={"actors"}
        )
        public static void Insert(int contentWidth, BookPage page, @ByRef List<Actor>[] actors) {
            List<Actor> actorRepl = actors[0] = new ArrayList<>(actors[0]);
            actorRepl.clear();

            for (PipeGroup group : groupPipesByContent((Collection<Pipe>)(Collection) PipeMod.pipes)) {
                actorRepl.add((Actor)makeDocumentation.invoke(null, "modifier [b]"+group.getModName(), Colours.purple, sortedPipesToView.invoke(null,group.pipes), page, contentWidth));
            }
            for (PipeGroup group : groupPipesByContent((Collection<Pipe>)(Collection) PipeHero.pipes)) {
                actorRepl.add((Actor)makeDocumentation.invoke(null, "hero [b]"+group.getModName(), Colours.yellow, sortedPipesToView.invoke(null,group.pipes), page, contentWidth));
            }
            for (PipeGroup group : groupPipesByContent((Collection<Pipe>)(Collection) PipeMonster.pipes)) {
                actorRepl.add((Actor)makeDocumentation.invoke(null, "monster [b]"+group.getModName(), Colours.orange, sortedPipesToView.invoke(null,group.pipes), page, contentWidth));
            }
            for (PipeGroup group : groupPipesByContent((Collection<Pipe>)(Collection) PipeItem.pipes)) {
                actorRepl.add((Actor)makeDocumentation.invoke(null, "item [b]"+group.getModName(), Colours.grey, sortedPipesToView.invoke(null,group.pipes), page, contentWidth));
            }
            actorRepl.add((Actor)makeDocumentation.invoke(null, "texture", Colours.blue, Pipe.makeAllPipes(), page, contentWidth));
        }

        private static class Locator extends SpireInsertLocator {
            // This is the abstract method from SpireInsertLocator that will be used to find the line
            // numbers you want this patch inserted at
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(Tann.class, "makeGroup");

                return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<Matcher>(), finalMatcher);
            }
        }
    }

    @SpirePatch2(clz = APIUtils.class, method = "makeTMPageAPI2")
    public static class MakeTMPageAPI2 {
        public static ExprEditor Instrument()
        {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    if(m.getMethodName().equals("size")) {
                        m.replace("{$_ = 0;}");
                    }
                }
            };
        }

        @SpireInsertPatch(
                locator= MakeDocumentation.Locator.class,
                localvars={"type","maxWidth","ptmp"}
        )
        public static void Insert(final int contentWidth, final BookPage almanacPage, Pipe selectedPipe, Pixl ptmp, int maxWidth, PipeType type) {
            int actualIndex = 0;

            List<Pipe> contents = (List<Pipe>)sortedPipesToView.invoke(null, type.contents);

            for(int i = 0; i < contents.size(); ++i) {
                final Pipe pipe = contents.get(i);
                if (!(boolean)shouldSkip.invoke(null,pipe)) {
                    String preTag = "[grey]";
                    String text = (pipe == selectedPipe ? "[light]" : (pipe.isComplexAPI() ? "[purple]" : "")) + toBigIdentifier(actualIndex);
                    Color col = ReflectionUtils.readField(Color.class, PipeType.class, type, "col");
                    StandardButton tb = (new StandardButton(preTag + text, col)).setStyle(StandardButtonStyle.SimpleSquare);
                    tb.makeTiny();
                    tb.setRunnable(new Runnable() {
                        public void run() {
                            Sounds.playSound(Sounds.pipSmall);
                            almanacPage.showThing(APIUtils.makeTMPageAPI2(contentWidth, almanacPage, pipe));
                        }
                    });
                    ptmp.actor(tb, (float)maxWidth);
                    ++actualIndex;
                }
            }
        }

        private static class Locator extends SpireInsertLocator {
            // This is the abstract method from SpireInsertLocator that will be used to find the line
            // numbers you want this patch inserted at
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(APIUtils.class, "sortedPipesToView");

                return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<Matcher>(), finalMatcher);
            }
        }
    }
}