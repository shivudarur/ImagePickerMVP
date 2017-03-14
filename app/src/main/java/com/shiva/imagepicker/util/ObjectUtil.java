package com.shiva.imagepicker.util;

import java.util.Collection;

/**
 * Created by shivananda.darura on 23/02/17.
 */

public class ObjectUtil {
    public static boolean isEmpty(String text) {
        return text == null || text.trim().length() <= 0;
    }

    public static boolean isNull(Object object) {
        return object == null;
    }

    public static boolean isEmptyCollection(Collection collection) {
        return isNull(collection) || collection.isEmpty();
    }
}
