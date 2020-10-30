package nl.itslars.mcpenbt.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents the type of an NBT Tag
 */
public enum TagType {

    // Note: the END type is not actually used in this project
    TAG_END((byte) 0x00, 1),
    TAG_BYTE((byte) 0x01, 1),
    TAG_SHORT((byte) 0x02, 2),
    TAG_INT((byte) 0x03, 4),
    TAG_LONG((byte) 0x04, 8),
    TAG_FLOAT((byte) 0x05, 4),
    TAG_DOUBLE((byte) 0x06, 8),
    TAG_BYTE_ARRAY((byte) 0x07, -1),
    TAG_STRING((byte) 0x08, -1),
    TAG_LIST((byte) 0x09, -1),
    TAG_COMPOUND((byte) 0x0A, -1),
    TAG_INT_ARRAY((byte) 0x0B, -1),
    ;

    // The ID of the tag
    private byte id;
    // The size of the tag, in bytes. If it is dependent on elements inside the tag, the value is -1
    private int size;

    TagType(byte id, int size) {
        this.id = id;
        this.size = size;
    }

    /**
     * Retrieves the ID of a tag
     * @return The id
     */
    public byte getId() {
        return id;
    }

    /**
     * Retrieves the size of a tag
     * @return The size (in bytes)
     */
    public int getSize() {
        return size;
    }

    // A map, mapping the tag ID's to the tags
    private static Map<Byte, TagType> typeIds = new HashMap<>();

    static {
        // Initialize the typeIds map
        for (TagType type : TagType.values()) {
            typeIds.put(type.getId(), type);
        }
    }

    /**
     * Finds a TagType from a given ID
     * @param id The ID
     * @return The TagType
     */
    public static TagType fromId(byte id) {
        return typeIds.get(id);
    }
}
