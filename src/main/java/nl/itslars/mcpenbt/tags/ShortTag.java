package nl.itslars.mcpenbt.tags;

import nl.itslars.mcpenbt.enums.TagType;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Class for representing a Short NBT Tag
 */
public class ShortTag extends Tag {

    // The value that this tag represents
    private short value;

    public ShortTag(String name, short value) {
        super(name);
        this.value = value;
    }

    public short getValue() {
        return value;
    }

    /**
     * Converts the ShortTag to a byte array.
     * 2 bytes are used, for the short that is represented.
     * @return The resulting byte array
     */
    @Override
    protected byte[] toBytes() {
        return ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN).putShort(value).array();
    }

    @Override
    public TagType getType() {
        return TagType.TAG_SHORT;
    }

    /**
     * Converts the given input stream to a ShortTag
     * @param name The name that this tag should get
     * @param stream The input stream
     * @return The resulting tag
     * @throws IOException If the InputStream threw an exception
     */
    public static Tag read(String name, InputStream stream) throws IOException {
        // Read the input stream bytes into a byte array
        byte[] buffer = new byte[2];
        for (int i = 0; i < 2; i++) {
            buffer[i] = (byte) stream.read();
        }
        // Convert the byte array to a short
        short result = ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN).getShort();
        return new ShortTag(name, result);
    }
}