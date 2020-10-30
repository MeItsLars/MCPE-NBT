package nl.itslars.mcpenbt.tags;

import nl.itslars.mcpenbt.enums.TagType;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Class for representing a Int NBT Tag
 */
public class IntTag extends Tag {

    // The value that this tag represents
    private int value;

    public IntTag(String name, int value) {
        super(name);
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    /**
     * Converts the IntTag to a byte array.
     * 4 bytes are used, for the int that is represented.
     * @return The resulting byte array
     */
    @Override
    protected byte[] toBytes() {
        return ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(value).array();
    }

    @Override
    public TagType getType() {
        return TagType.TAG_INT;
    }

    /**
     * Converts the given input stream to a IntTag
     * @param name The name that this tag should get
     * @param stream The input stream
     * @return The resulting tag
     * @throws IOException If the InputStream threw an exception
     */
    public static Tag read(String name, InputStream stream) throws IOException {
        // Read the input stream bytes into a byte array
        byte[] buffer = new byte[4];
        for (int i = 0; i < 4; i++) {
            buffer[i] = (byte) stream.read();
        }
        // Convert the byte array to an int
        int result = ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN).getInt();
        return new IntTag(name, result);
    }
}
