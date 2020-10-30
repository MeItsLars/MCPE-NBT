package nl.itslars.mcpenbt.tags;

import nl.itslars.mcpenbt.enums.TagType;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Abstract class for representing any NBT Tag
 */
public abstract class Tag {

    // The name (key) of this tag. Can be set to null, when no name is used (in (byte/int) list NBT)
    private String name;

    public Tag(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /**
     * Converts this NBT Tag to a byte array
     * @return The byte array
     */
    public byte[] getBytes() {
        // If there is no name, we return only the actual value of the NBT tag
        if (name == null) return toBytes();

        // Convert the name to bytes
        byte[] tagName = name.getBytes();
        // Retrieve the NBT tag value
        byte[] tagValue = toBytes();
        // Create the array. It consists of:
        // 1 byte, indicating the ID. Followed by a short that indicates the name length.
        // This is followed by the tag name, which is followed by the tag value.
        return ByteBuffer.allocate(tagName.length + tagValue.length + 3)
                .order(ByteOrder.LITTLE_ENDIAN)
                .put(getType().getId())
                .putShort((short) tagName.length)
                .put(tagName)
                .put(tagValue)
                .array();
    }

    /**
     * Abstract method for converting each NBT Tag to the associated value byte array
     * @return The byte array
     */
    protected abstract byte[] toBytes();

    /**
     * Abstract method that returns the TagType of the current NBT tag
     * @return The TagType
     */
    public abstract TagType getType();

    /**
     * Attempts to retrieve this NBT Tag as a ByteTag
     * @return The resulting tag
     */
    public ByteTag getAsByte() {
        return getSpecificInstance(ByteTag.class);
    }

    /**
     * Attempts to retrieve this NBT Tag as a ShortTag
     * @return The resulting tag
     */
    public ShortTag getAsShort() {
        return getSpecificInstance(ShortTag.class);
    }

    /**
     * Attempts to retrieve this NBT Tag as a IntTag
     * @return The resulting tag
     */
    public IntTag getAsInt() {
        return getSpecificInstance(IntTag.class);
    }

    /**
     * Attempts to retrieve this NBT Tag as a LongTag
     * @return The resulting tag
     */
    public LongTag getAsLong() {
        return getSpecificInstance(LongTag.class);
    }

    /**
     * Attempts to retrieve this NBT Tag as a FloatTag
     * @return The resulting tag
     */
    public FloatTag getAsFloat() {
        return getSpecificInstance(FloatTag.class);
    }

    /**
     * Attempts to retrieve this NBT Tag as a DoubleTag
     * @return The resulting tag
     */
    public DoubleTag getAsDouble() {
        return getSpecificInstance(DoubleTag.class);
    }

    /**
     * Attempts to retrieve this NBT Tag as a ByteArrayTag
     * @return The resulting tag
     */
    public ByteArrayTag getAsByteArray() {
        return getSpecificInstance(ByteArrayTag.class);
    }

    /**
     * Attempts to retrieve this NBT Tag as a StringTag
     * @return The resulting tag
     */
    public StringTag getAsString() {
        return getSpecificInstance(StringTag.class);
    }

    /**
     * Attempts to retrieve this NBT Tag as a ListTag
     * @return The resulting tag
     */
    @SuppressWarnings("unchecked")
    public <T extends Tag> ListTag<T> getAsList() {
        return getSpecificInstance(ListTag.class);
    }

    /**
     * Attempts to retrieve this NBT Tag as a CompoundTag
     * @return The resulting tag
     */
    public CompoundTag getAsCompound() {
        return getSpecificInstance(CompoundTag.class);
    }

    /**
     * Attempts to retrieve this NBT Tag as a IntArrayTag
     * @return The resulting tag
     */
    public IntArrayTag getAsIntArray() {
        return getSpecificInstance(IntArrayTag.class);
    }

    /**
     * Attempts to retrieve this NBT Tag as the given class
     * @param clazz The class that this NBT tag (possibly) has
     * @param <T> The resulting instance type
     * @return The actual instance
     */
    @SuppressWarnings("unchecked")
    private <T extends Tag> T getSpecificInstance(Class<T> clazz) {
        // Check if the instance is correct. If not, throw an exception
        if (!clazz.isAssignableFrom(getClass())) {
            throw new IllegalArgumentException("Expected class " + clazz.getSimpleName() + ", but is " + getClass().getSimpleName());
        }
        // Cast and return
        return (T) this;
    }

    /**
     * Converts the given stream to a Tag
     * @param stream The input stream
     * @return The resulting Tag
     * @throws IOException When the input stream throws an error
     */
    public static Tag read(InputStream stream) throws IOException {
        return read(stream, -1);
    }

    /**
     * Converts the given stream to a Tag
     * @param stream The input stream
     * @param nextId The expected type of the next tag. If set to -1, this indicates that the next ID has to be read
     * @return The resulting Tag
     * @throws IOException When the input stream throws an error
     */
    public static Tag read(InputStream stream, int nextId) throws IOException {
        // Checks if the stream has available data, and if not, throws an exception
        if (stream.available() == 0) throw new IllegalStateException("Invalid NBT formatting.");

        String nextName = null;
        // Check if the next ID is -1
        if (nextId == -1) {
            nextId = stream.read();
            // If the next ID is 0, it indicates the end of a compound tag, and we return
            if (nextId == 0) return null;

            // Read the length of the tag name
            byte[] stringLength = new byte[]{(byte) stream.read(), (byte) stream.read()};
            short length = ByteBuffer.wrap(stringLength).order(ByteOrder.LITTLE_ENDIAN).getShort();
            // Read the tag name
            byte[] stringBuffer = new byte[length];
            for (int i = 0; i < length; i++) {
                stringBuffer[i] = (byte) stream.read();
            }
            nextName = new String(stringBuffer);
        }

        // Switch based on the ID. Every different value activates a different parser
        switch (nextId) {
            case 1:
                return ByteTag.read(nextName, stream);
            case 2:
                return ShortTag.read(nextName, stream);
            case 3:
                return IntTag.read(nextName, stream);
            case 4:
                return LongTag.read(nextName, stream);
            case 5:
                return FloatTag.read(nextName, stream);
            case 6:
                return DoubleTag.read(nextName, stream);
            case 7:
                return ByteArrayTag.read(nextName, stream);
            case 8:
                return StringTag.read(nextName, stream);
            case 9:
                return ListTag.read(nextName, stream);
            case 10:
                return CompoundTag.read(nextName, stream);
            case 11:
                return IntArrayTag.read(nextName, stream);
            default:
                // If a wrong ID was given, the NBT formatting was wrong.
                throw new IllegalStateException("Invalid NBT formatting.");
        }
    }
}
