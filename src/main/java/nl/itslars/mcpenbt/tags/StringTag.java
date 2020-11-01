package nl.itslars.mcpenbt.tags;

import nl.itslars.mcpenbt.enums.TagType;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Objects;

/**
 * Class for representing a String NBT Tag
 */
public class StringTag extends Tag {

    // The value that this tag represents
    private String value;

    public StringTag(String name, String value) {
        super(name);
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Converts the StringTag to a byte array.
     * 2 bytes are used to indicate the string length, followed by the actual string bytes
     * @return The resulting byte array
     */
    @Override
    protected byte[] toBytes() {
        return ByteBuffer.allocate(2 + value.length()).order(ByteOrder.LITTLE_ENDIAN).putShort((short) value.length()).put(value.getBytes()).array();
    }

    @Override
    public TagType getType() {
        return TagType.TAG_STRING;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StringTag stringTag = (StringTag) o;
        return value.equals(stringTag.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    /**
     * Converts the given input stream to a StringTag
     * @param name The name that this tag should get
     * @param stream The input stream
     * @return The resulting tag
     * @throws IOException If the InputStream threw an exception
     */
    public static Tag read(String name, InputStream stream) throws IOException {
        // Read the string length
        byte[] stringLength = new byte[]{(byte) stream.read(), (byte) stream.read()};
        short length = ByteBuffer.wrap(stringLength).order(ByteOrder.LITTLE_ENDIAN).getShort();
        byte[] stringBuffer = new byte[length];
        // Fill the string buffer with the string bytes
        for (int i = 0; i < length; i++) {
            stringBuffer[i] = (byte) stream.read();
        }
        return new StringTag(name, new String(stringBuffer));
    }
}
