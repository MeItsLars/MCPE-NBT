# MCPE-NBT
Using this library, you can parse NBT data used by Minecraft PE/Windows 10/Bedrock Edition into Java objects.
This NBT data can then be changed, removed, and you can add new NBT data.
These Java objects can then also be converted back to a byte array, and written to a file.

# Usage
You can use this library by cloning it, and building it with maven. The maven artifact is hosted on Jitpack.
So, add the following repository & dependency to your ``pom.xml``:
```xml
	<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>

	<dependency>
	    <groupId>com.github.MeItsLars</groupId>
	    <artifactId>MCPE-NBT</artifactId>
	    <version>1.0</version>
	</dependency>
```
All interaction goes via the main (static) ``NBTUtil`` class. This class can read files, byte arrays and input streams into tags. Also, it can write tags to byte arrays. For more information, check the class. All methods are documented.

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
10    TAG_Compound     [TYPE OF TAG (1 byte)][TAG_String (Tag name)][TAG][TAG_End]
11    TAG_Int_Array    [NUM_ELEMENTS (int][ELEMENTS]
```

This is a bit inconsistent and abstract. Therefore I have created a context-free grammar.
The CFG for the NBT compounds is as follows:
```
[TAG] -> [TAG_Byte] | [TAG_Short] | [TAG_Int] | [TAG_Long] | [TAG_Float] | [TAG_Double] | [TAG_Byte_Array] | [TAG_String] | [TAG_List] | [TAG_Compound] | [TAG_Int_Array]
[TAG_Byte] -> [byte]
[TAG_Short] -> [short]
[TAG_Int] -> [int]
[TAG_Long] -> [long]
[TAG_Float] -> [float]
[TAG_Double] -> [double]
[TAG_Byte_Array] -> [int][Byte_Array]
[TAG_String] -> [int][string]
[TAG_List] -> [byte][int][List<T>]
[TAG_Compound] -> [Compound_List]0x00
[TAG_Int_Array] -> [int][Int_Array]

[Byte_Array] -> [byte][Byte_Array] | ε
[List<T>] -> [TAG_T][List] | ε
[Int_Array] -> [int][Int_Array] | ε

[Compound_List] = [Compound][Compound_List] | ε
[Compound] = [byte][TAG_String][TAG]
```

Each file begins with an 8 byte header. The first 4 bytes represent the type of file, and the second 4 bytes represent the length of the NBT data in the file.
Next, is the parent compound tag. Thus, the CFG for a file is:
```
S -> [int][int][TAG_Compound]
```
