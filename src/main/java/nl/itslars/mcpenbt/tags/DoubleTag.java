package nl.itslars.mcpenbt.tags;

import nl.itslars.mcpenbt.enums.TagType;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Class for representing a Double NBT Tag
 */
public class DoubleTag extends Tag {

    // The value that this tag represents
    private double value;

    public DoubleTag(String name, double value) {
        super(name);
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    /**
     * Converts the DoubleTag to a byte array.
     * 8 bytes are used, for the double that is represented.
     * @return The resulting byte array
     */
    @Override
    protected byte[] toBytes() {
        return ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN).putDouble(value).array();
    }

    @Override
    public TagType getType() {
        return TagType.TAG_DOUBLE;
    }

    /**
     * Converts the given input stream to a DoubleTag
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
        // Convert the byte array to a double
        double result = ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN).getDouble();
        return new DoubleTag(name, result);
    }
}
