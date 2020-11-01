package nl.itslars.mcpenbt.tags;

import nl.itslars.mcpenbt.enums.TagType;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.*;

/**
 * Class for representing a List NBT Tag
 * @param <T> The Tag type that is in this list
 */
public class ListTag<T extends Tag> extends Tag implements Iterable<T> {

    // The tag type that is in this list
    private TagType elementType;
    // The list of elements that this list represents
    private List<T> elements;

    public ListTag(String name, TagType elementType, List<T> elements) {
        super(name);
        this.elementType = elementType;
        this.elements = elements;
    }

    public List<T> getElements() {
        return elements;
    }

    @Override
    public Iterator<T> iterator() {
        return elements.iterator();
    }

    /**
     * Converts the ListTag object to a byte array.
     * 1 byte for the type of tag, 4 bytes for the list length, and additionally all list elements
     * @return The resulting byte array
     */
    @Override
    protected byte[] toBytes() {
        // Create the type and length buffer
        byte[] list = ByteBuffer.allocate(5).order(ByteOrder.LITTLE_ENDIAN).put(elementType.getId()).putInt(elements.size()).array();
        // Add all elements to the buffer
        for (T t : elements) {
            byte[] bytes = t.getBytes();
            list = Arrays.copyOf(list, list.length + bytes.length);
            System.arraycopy(bytes, 0, list, list.length - bytes.length, bytes.length);
        }
        return list;
    }

    @Override
    public TagType getType() {
        return TagType.TAG_LIST;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ListTag<?> listTag = (ListTag<?>) o;
        return elementType == listTag.elementType &&
                elements.equals(listTag.elements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(elementType, elements);
    }

    /**
     * Converts the given input stream to a ListTag
     * @param name The name that this tag should get
     * @param stream The input stream
     * @return The resulting tag
     * @throws IOException If the InputStream threw an exception
     */
    public static Tag read(String name, InputStream stream) throws IOException {
        // Read the Tag Type of this list
        int nextId = stream.read();
        TagType type = TagType.fromId((byte) nextId);

        // read the list length
        byte[] arrayLength = new byte[]{(byte) stream.read(), (byte) stream.read(), (byte) stream.read(), (byte) stream.read()};
        int length = ByteBuffer.wrap(arrayLength).order(ByteOrder.LITTLE_ENDIAN).getInt();
        // Create and fill the actual ArrayList
        List<Tag> result = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            result.add(Tag.read(stream, type.getId()));
        }

        return new ListTag<>(name, type, result);
    }
}
