package basemod.util;

import basemod.pipes.PipePatch;
import com.tann.dice.gameplay.content.gen.pipe.Pipe;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

public abstract class PipeHandler extends ContentHandler<Pipe> {
    @Override
    public void setContentSource(Pipe t, ContentSource contentSource) {
        PipePatch.contentSource.set(t, contentSource);
    }

    public static <T> PipeHandler make(Supplier<List<T>> pipes) {
        return new PipeHandler() {
            @Override
            public Collection<Pipe> getPipes() {
                return (Collection<Pipe>) pipes.get();
            }
        };
    }
}
