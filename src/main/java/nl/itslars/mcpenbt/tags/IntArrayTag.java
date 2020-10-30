package nl.itslars.mcpenbt.tags;

import nl.itslars.mcpenbt.enums.TagType;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Class for representing an Integer Array NBT Tag
 */
public class IntArrayTag extends Tag {

    // The values in this integer array
    private int[] values;

    public IntArrayTag(String name, int... values) {
        super(name);
        this.values = values;
    }

    public int[] getValues() {
        return values;
    }

    /**
     * Converts the IntArrayTag object to a byte array.
     * 4 bytes for the length of the array, followed by the integers (4 bytes each)
     * @return The resulting byte array
     */
    @Override
    protected byte[] toBytes() {
        ByteBuffer valuesBuffer = ByteBuffer.allocate(4 + 4 * values.length).order(ByteOrder.LITTLE_ENDIAN);
        // Set the length of the array
        valuesBuffer.putInt(values.length);
        // Add the integer values
        for (int value : values) {
            valuesBuffer.putInt(value);
        }
        return valuesBuffer.array();
    }

    @Override
    public TagType getType() {
        return TagType.TAG_INT_ARRAY;
    }

    /**
     * Converts the given input stream to a IntArrayTag
     * @param name The name that this tag should get
     * @param stream The input stream
     * @return The resulting tag
     * @throws IOException If the InputStream threw an exception
     */
    public static Tag read(String name, InputStream stream) throws IOException {
        // Read the length of the byte array
        byte[] arrayLength = new byte[]{(byte) stream.read(), (byte) stream.read(), (byte) stream.read(), (byte) stream.read()};
        int length = ByteBuffer.wrap(arrayLength).order(ByteOrder.LITTLE_ENDIAN).getInt();
        // Create and fill a byte array
        int[] array = new int[length];
        for (int i = 0; i < length; i++) {
            byte[] buffer = new byte[]{(byte) stream.read(), (byte) stream.read(), (byte) stream.read(), (byte) stream.read()};
            array[i] = ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN).getInt();
        }
        return new IntArrayTag(name, array);
    }
}
