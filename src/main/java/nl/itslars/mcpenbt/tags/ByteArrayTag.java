package nl.itslars.mcpenbt.tags;

import nl.itslars.mcpenbt.enums.TagType;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

/**
 * Class for representing a Byte Array NBT Tag
 */
public class ByteArrayTag extends Tag {

    // The values in this byte array
    private byte[] values;

    public ByteArrayTag(String name, byte... values) {
        super(name);
        this.values = values == null ? new byte[]{} : values;
    }

    public byte[] getValues() {
        return values;
    }

    public void setValues(byte[] values) {
        this.values = values;
    }

    public void setValue(int i, byte value) {
        values[i] = value;
    }

    /**
     * Converts the ByteArrayTag object to a byte array.
     * 4 bytes for the length of the array, followed by the bytes
     * @return The resulting byte array
     */
    @Override
    protected byte[] toBytes() {
        return ByteBuffer.allocate(4 + values.length).order(ByteOrder.LITTLE_ENDIAN).putInt(values.length).put(values).array();
    }

    @Override
    public TagType getType() {
        return TagType.TAG_BYTE_ARRAY;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ByteArrayTag that = (ByteArrayTag) o;
        return Arrays.equals(values, that.values) && ((getName() == null && that.getName() == null) || (getName().equals(that.getName())));
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(values);
    }

    /**
     * Converts the given input stream to a ByteArrayTag
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
        byte[] array = new byte[length];
        for (int i = 0; i < length; i++) {
            array[i] = (byte) stream.read();
        }
        return new ByteArrayTag(name, array);
    }
}
