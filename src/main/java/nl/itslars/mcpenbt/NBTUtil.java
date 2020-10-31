package nl.itslars.mcpenbt;

import nl.itslars.mcpenbt.enums.TagType;
import nl.itslars.mcpenbt.tags.Tag;
import nl.itslars.mcpenbt.enums.HeaderType;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Main NBT Utility class. Can be accessed for reading and writing NBT data
 */
public class NBTUtil {

    private NBTUtil() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Reads the NBT in the given file
     * @param header Whether the NBT contains a header
     * @param path The file path
     * @return The resulting NBT Tag
     */
    public static Tag read(boolean header, Path path) {
        return read(null, header, path);
    }

    /**
     * Reads the NBT from the given byte array
     * @param header Whether the NBT contains a header
     * @param bytes The bytes
     * @return The resulting NBT Tag
     */
    public static Tag read(boolean header, byte... bytes) {
        return read(null, header, new ByteArrayInputStream(bytes));
    }

    /**
     * Reads the NBT from the given input stream
     * @param header Whether the NBT contains a header
     * @param stream The input stream
     * @return The resulting NBT Tag
     */
    public static Tag read(boolean header, InputStream stream) {
        return read(null, header, stream);
    }

    /**
     * Reads the NBT in the given file
     * @param expectedType The expected resulting tag type
     * @param header Whether the NBT contains a header
     * @param path The file path
     * @return The resulting NBT Tag
     */
    public static Tag read(TagType expectedType, boolean header, Path path) {
        try {
            return read(expectedType, header, Files.readAllBytes(path));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Reads the NBT from the given byte array
     * @param expectedType The expected resulting tag type
     * @param header Whether the NBT contains a header
     * @param bytes The bytes
     * @return The resulting NBT Tag
     */
    public static Tag read(TagType expectedType, boolean header, byte... bytes) {
        return read(expectedType, header, new ByteArrayInputStream(bytes));
    }

    /**
     * Reads the NBT from the given input stream
     * @param expectedType The expected resulting tag type
     * @param header Whether the NBT contains a header
     * @param stream The input stream
     * @return The resulting NBT Tag
     */
    public static Tag read(TagType expectedType, boolean header, InputStream stream) {
        try {
            // Ignore the first 8 header bytes
            if (header && stream.skip(8) != 8) {
                throw new IllegalStateException("No header found.");
            }

            return expectedType == null ? Tag.read(stream) : Tag.read(stream, expectedType.getId());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Converts the given NBT Tag to a byte array
     * @param tag The NBT Tag
     * @return The byte array
     */
    public static byte[] write(Tag tag) {
        return write(tag, HeaderType.NONE);
    }

    /**
     * Converts the given NBT Tag to a byte array
     * @param tag The NBT Tag
     * @param headerType The header type that should be added to the NBT
     * @return The byte array
     */
    public static byte[] write(Tag tag, HeaderType headerType) {
        byte[] result = tag.getBytes();

        // If there is a header, add it
        if (headerType != HeaderType.NONE) {
            result = ByteBuffer.allocate(8 + result.length)
                    .order(ByteOrder.LITTLE_ENDIAN)
                    .putInt(headerType.getHeaderTypeNumber())
                    .putInt(result.length)
                    .put(result)
                    .array();
        }

        return result;
    }
}
