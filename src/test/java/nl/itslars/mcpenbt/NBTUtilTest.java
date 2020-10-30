package nl.itslars.mcpenbt;
import nl.itslars.mcpenbt.enums.HeaderType;
import nl.itslars.mcpenbt.enums.TagType;
import nl.itslars.mcpenbt.tags.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Unit test for NBT Utils
 */
public class NBTUtilTest {

    /**
     * Test the ByteTag
     */
    @Test
    public void testByteTag() {
        testTag(new ByteTag(UUID.randomUUID().toString().substring(0, 10), (byte) ThreadLocalRandom.current().nextInt()));
    }


    /**
     * Test the ShortTag
     */
    @Test
    public void testShortTag() {
        testTag(new ShortTag(UUID.randomUUID().toString().substring(0, 10), (short) ThreadLocalRandom.current().nextInt()));
    }


    /**
     * Test the IntTag
     */
    @Test
    public void testIntTag() {
        testTag(new IntTag(UUID.randomUUID().toString().substring(0, 10), ThreadLocalRandom.current().nextInt()));
    }


    /**
     * Test the LongTag
     */
    @Test
    public void testLongTag() {
        testTag(new LongTag(UUID.randomUUID().toString().substring(0, 10), ThreadLocalRandom.current().nextLong()));
    }


    /**
     * Test the FloatTag
     */
    @Test
    public void testFloatTag() {
        testTag(new FloatTag(UUID.randomUUID().toString().substring(0, 10), ThreadLocalRandom.current().nextFloat()));
    }


    /**
     * Test the DoubleTag
     */
    @Test
    public void testDoubleTag() {
        testTag(new DoubleTag(UUID.randomUUID().toString().substring(0, 10), ThreadLocalRandom.current().nextDouble()));
    }


    /**
     * Test the ByteArrayTag
     */
    @Test
    public void testByteArrayTag() {
        // Generate a random byte array
        int arrayLength = ThreadLocalRandom.current().nextInt(100);
        byte[] array = new byte[arrayLength];
        for (int i = 0; i < arrayLength; i++) {
            array[i] = (byte) ThreadLocalRandom.current().nextInt();
        }

        testTag(new ByteArrayTag(UUID.randomUUID().toString().substring(0, 10), array));
    }


    /**
     * Test the StringTag
     */
    @Test
    public void testStringTag() {
        testTag(new StringTag(UUID.randomUUID().toString().substring(0, 10), UUID.randomUUID().toString().substring(0, 10)));
    }


    /**
     * Test the ListTag
     */
    @Test
    public void testListTag() {
        // Generate a random list
        List<Tag> elements = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            elements.add(new FloatTag(null, ThreadLocalRandom.current().nextFloat()));
        }

        testTag(new ListTag<>(UUID.randomUUID().toString().substring(0, 10), TagType.TAG_FLOAT, elements));
    }


    /**
     * Test the CompoundTag
     */
    @Test
    public void testCompoundTag() {
        // Generate a random list of elements
        List<Tag> elements = new ArrayList<>();
        for (int i = 0; i < ThreadLocalRandom.current().nextInt(100); i++) {
            elements.add(new FloatTag(UUID.randomUUID().toString().substring(0, 10), ThreadLocalRandom.current().nextFloat()));
        }

        testTag(new CompoundTag(UUID.randomUUID().toString().substring(0, 10), elements));
    }


    /**
     * Test the IntArrayTag
     */
    @Test
    public void testIntArrayTag() {
        // Generate a random int array
        int arrayLength = ThreadLocalRandom.current().nextInt(100);
        int[] array = new int[arrayLength];
        for (int i = 0; i < arrayLength; i++) {
            array[i] = ThreadLocalRandom.current().nextInt();
        }

        testTag(new IntArrayTag(UUID.randomUUID().toString().substring(0, 10), array));
    }

    /**
     * Tests any tag type, by first converting it to a byte array, converting that array back to a tag,
     * followed by converting that tag back to a new byte array, and checking if the two byte arrays are equal.
     * @param tag The original tag
     */
    private void testTag(Tag tag) {
        // Convert the original tag to a byte array
        byte[] result1 = NBTUtil.write(tag);
        // Convert the byte array back to a tag
        Tag tag2 = NBTUtil.read(false, result1);
        // Convert the newly created tag back to a new byte array
        byte[] result2 = NBTUtil.write(tag2);

        // Check equality in byte arrays
        Assert.assertArrayEquals(result1, result2);
    }

    /**
     * Tests NBT tag header functionality
     */
    @Test
    public void testHeader() {
        // Generate random tags with header, and check their equality
        Tag tag1 = new IntTag(UUID.randomUUID().toString().substring(0, 10), ThreadLocalRandom.current().nextInt());
        byte[] result1 = NBTUtil.write(tag1, HeaderType.LEVEL_DAT);
        Tag tag2 =  NBTUtil.read(true, result1);
        byte[] result2 = NBTUtil.write(tag2, HeaderType.LEVEL_DAT);
        Assert.assertArrayEquals(result1, result2);
    }

    /**
     * Test an original level.dat file, retrieved from a Minecraft Bedrock world on Windows 10.
     * @throws IOException If the file could not be read
     */
    @Test
    public void testOriginalNBTFile() throws IOException {
        // Read the file into a byte array
        byte[] originalLevelDat = Files.readAllBytes(new File("./src/test/level.dat").toPath());
        // Convert the byte array back to a tag
        Tag tag = NBTUtil.read(true, originalLevelDat);
        // Convert the tag to a byte array
        byte[] newLevelDat = NBTUtil.write(tag, HeaderType.LEVEL_DAT);

        // Check byte array equality
        Assert.assertArrayEquals(newLevelDat, originalLevelDat);
    }
}