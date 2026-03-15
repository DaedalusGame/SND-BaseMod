package basemod;

public interface IEnumPatch<T> {
    void edit(T t);

    String getName();

    Class[] getTypeSignature();

    Object[] getParameters();
}
