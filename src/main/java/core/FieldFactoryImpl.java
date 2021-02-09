package core;

public class FieldFactoryImpl implements FieldFactory {
    @Override
    public Field emptyField(int size) {
        return new FieldImpl(size);
    }
}
