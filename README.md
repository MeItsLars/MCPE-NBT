# MCPE-NBT
Using this library, you can parse NBT data used by Minecraft PE/Windows 10/Bedrock Edition into Java objects.
This NBT data can then be changed, removed, and you can add new NBT data.
These Java objects can then also be converted back to a byte array, and written to a file.

# Formatting
A little bit of background information on how MCPE stores NBT:
This is how MCPE represents NBT tags:
```
id    name             length in bytes, or format
0     TAG_End          1 byte: 0x00
1     TAG_Byte         1
2     TAG_Short        2
3     TAG_Int          4
4     TAG_Long         8
5     TAG_Float        4
6     TAG_Double       8
7     TAG_Byte_Array   [NUM_ELEMENTS (int)][ELEMENTS]
8     TAG_String       [LENGTH (short)][VALUE]
9     TAG_List         [TYPE OF ELEMENT IN LIST (byte)][NUM_ELEMENTS (int)][ELEMENTS]
10    TAG_Compound     [COMPOUND][TAG_End]
11    TAG_Int_Array    [NUM_ELEMENTS (int][ELEMENTS]

[COMPOUND] = [COMPOUND][COMPOUND] | -
[COMPOUND] = [TYPE OF TAG (1 byte)][TAG_String (Tag name)][TAG]
```

Each file begins with an 8 byte header. The first 4 bytes represent the type of file, and the second 4 bytes represent the length of the NBT data in the file.
Next, is the parent compound tag. Thus, each file looks like this:

[FILE_TYPE (4 bytes)][FILE_LENGTH (4 bytes)][COMPOUND]

# Usage
You can use this library by cloning it, and building it with maven.
Then, add the following artifact to your ``pom.xml``:
```xml
<dependency>
    <groupId>nl.itslars</groupId>
    <artifactId>mcpe-nbt</artifactId>
    <version>1.0</version>
</dependency>
```
