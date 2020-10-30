package nl.itslars.mcpenbt.tags;

import nl.itslars.mcpenbt.enums.TagType;

import java.io.IOException;
import java.io.InputStream;

/**
 * Class for representing a Byte NBT Tag
 */
public class ByteTag extends Tag {

    // The Byte value
    private byte value;

    public ByteTag(String name, byte value) {
        super(name);
        this.value = value;
    }

    public byte getValue() {
        return value;
    }

    /**
     * Converts the ByteTag to a byte array.
     * 1 byte is used, the actual byte that is represented by this class.
     * @return The resulting byte array
     */
    @Override
    protected byte[] toBytes() {
        return new byte[]{value};
    }

    @Override
    public TagType getType() {
        return TagType.TAG_BYTE;
    }

    /**
     * Converts the given input stream to a ByteTag
     * @param name The name that this tag should get
     * @param stream The input stream
     * @return The resulting tag
     * @throws IOException If the InputStream threw an exception
     */
    public static Tag read(String name, InputStream stream) throws IOException {
        return new ByteTag(name, (byte) stream.read());
    }
}
