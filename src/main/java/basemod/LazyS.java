package basemod;

import java.util.function.Supplier;

public class LazyS<T> implements ILazy {
    Supplier<T> supplier;

    public LazyS(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    @Override
    public Object get() {
        return supplier.get();
    }
}
