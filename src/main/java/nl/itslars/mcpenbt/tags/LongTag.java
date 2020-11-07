package nl.itslars.mcpenbt.tags;

import nl.itslars.mcpenbt.enums.TagType;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Objects;

/**
 * Class for representing a Long NBT Tag
 */
public class LongTag extends Tag {

    // The value that this tag represents
    private long value;

    public LongTag(String name, long value) {
        super(name);
        this.value = value;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    /**
     * Converts the LongTag to a byte array.
     * 8 bytes are used, for the long that is represented.
     * @return The resulting byte array
     */
    @Override
    protected byte[] toBytes() {
        return ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN).putLong(value).array();
    }

    @Override
    public TagType getType() {
        return TagType.TAG_LONG;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LongTag that = (LongTag) o;
        return value == that.value && ((getName() == null && that.getName() == null) || (getName().equals(that.getName())));
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    /**
     * Converts the given input stream to a LongTag
     * @param name The name that this tag should get
     * @param stream The input stream
     * @return The resulting tag
     * @throws IOException If the InputStream threw an exception
     */
    public static Tag read(String name, InputStream stream) throws IOException {
        // Read the input stream bytes into a byte array
        byte[] buffer = new byte[8];
        for (int i = 0; i < 8; i++) {
            buffer[i] = (byte) stream.read();
        }
        // Convert the byte array to a long
        long result = ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN).getLong();
        return new LongTag(name, result);
    }
}
