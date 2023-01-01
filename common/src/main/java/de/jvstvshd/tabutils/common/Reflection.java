package de.jvstvshd.tabutils.common;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class Reflection {

    private Reflection() {
    }

    public static void modifyFinalField(Field field, Object object, Object newValue) throws Exception {
        field.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        field.set(object, newValue);
    }

    public static void modifyFinalFieldUnsafe(Field field, Object object, Object newValue) throws ReflectiveOperationException {
        final Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
        unsafeField.setAccessible(true);
        final Unsafe unsafe = (Unsafe) unsafeField.get(null);
        unsafe.putObject(object, unsafe.objectFieldOffset(field), newValue);
        /*field.setAccessible(true);

        final Object staticFieldBase = unsafe.staticFieldBase(field);
        final long staticFieldOffset = unsafe.staticFieldOffset(field);
        unsafe.putObject(staticFieldBase, staticFieldOffset, newValue);*/
    }

}
