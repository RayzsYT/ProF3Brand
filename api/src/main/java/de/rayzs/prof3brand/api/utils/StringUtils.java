package de.rayzs.prof3brand.api.utils;

public class StringUtils {


    private StringUtils() {}


    /**
     * Looks for the String which is being searched inside the source
     * String. If it finds a match, it will return the index of the
     * last letter of the first found match.
     *
     * @param searching String to be searched.
     * @param source Source String where to search.
     *
     * @return Index of the last letter of the first found match. -1 if nothing is found.
     */
    public static int searchIndex(final String searching, final String source) {
        final char[] sourceChars = source.toCharArray();
        final int searchLength = searching.length();

        int s = 0;
        for (int i = 0; i < sourceChars.length; i++) {
            if (s == searchLength) {
                return i - searchLength;
            }

            if (sourceChars[i] != searching.charAt(s)) {
                s = 0;
                continue;
            }

            s++;
        }

        return -1;
    }
}
