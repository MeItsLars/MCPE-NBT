package nl.itslars.mcpenbt.tags;

import nl.itslars.mcpenbt.enums.TagType;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.*;

/**
 * Class for representing a Compound NBT Tag
 */
public class CompoundTag extends Tag implements Iterable<Tag> {

    // The list of elements that this tag encapsulates
    private List<Tag> elements;

    public CompoundTag(String name, List<Tag> elements) {
        super(name);
        this.elements = elements;
    }

    public List<Tag> getElements() {
        return elements;
    }

    /**
     * Retrieves the tag with the given name value
     * @param name The name value
     * @return An optional containing the tag if found, and empty otherwise
     */
    public Optional<Tag> getByName(String name) {
        return elements.stream().filter(tag -> tag.getName().equals(name)).findFirst();
    }

    @Override
    public Iterator<Tag> iterator() {
        return elements.iterator();
    }

    /**
     * Converts the CompoundTag object to a byte array.
     * It converts the elements of this tag to their byte arrays, appends them, and adds a 0x00 byte at the end
     * @return The resulting byte array
     */
    @Override
    protected byte[] toBytes() {
        byte[] result = new byte[0];

        // Append all element bytes
        for (Tag element : elements) {
            byte[] bytes = element.getBytes();
            result = Arrays.copyOf(result, result.length + bytes.length);
            System.arraycopy(bytes, 0, result, result.length - bytes.length, bytes.length);
        }

        // Add a 0x00 byte, and return
        return ByteBuffer.allocate(result.length + 1).put(result).put((byte) 0x00).array();
    }

    @Override
    public TagType getType() {
        return TagType.TAG_COMPOUND;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompoundTag that = (CompoundTag) o;
        return elements.equals(that.elements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(elements);
    }

    /**
     * Converts the given input stream to a CompoundTag
     * @param name The name that this tag should get
     * @param stream The input stream
     * @return The resulting tag
     * @throws IOException If the InputStream threw an exception
     */
    public static Tag read(String name, InputStream stream) throws IOException {
        List<Tag> result = new ArrayList<>();
        // Read and add tags, until we reach an end byte
        Tag nextTag;
        do {
            nextTag = Tag.read(stream);
            if (nextTag != null) {
                result.add(nextTag);
            }
        } while (nextTag != null);
        return new CompoundTag(name, result);
    }
}
