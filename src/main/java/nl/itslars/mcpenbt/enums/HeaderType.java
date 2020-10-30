package nl.itslars.mcpenbt.enums;

/**
 * Enum that represents the possible header values.
 * As of the first commit, only LEVEL_DAT is used, but there are probably multiple header types.
 */
public enum HeaderType {

    NONE(-1),
    LEVEL_DAT(8);

    int headerTypeNumber;

    HeaderType(int headerTypeNumber) {
        this.headerTypeNumber = headerTypeNumber;
    }

    /**
     * Retrieves the header type number
     * @return The header type number
     */
    public int getHeaderTypeNumber() {
        return headerTypeNumber;
    }
}
