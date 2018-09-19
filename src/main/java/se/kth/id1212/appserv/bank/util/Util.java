package se.kth.id1212.appserv.bank.util;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * A functional library of utility methods.
 */
public class Util {
    private Util() {}

    /**
     * Creates a string representation of the specified object. The string
     * contains the class name and the names and values of all fields declared
     * in the object's class. Fields in superclasses and implemented interfaces
     * are not included.
     *
     * @param objToRepresent The object to represent as a string.
     * @return A string representation of the specified object.
     */
    public static String toString(Object objToRepresent) {
        StringBuilder builder =
            new StringBuilder(objToRepresent.getClass().getName());
        builder.append("[");
        builder.append(
            Arrays.stream(objToRepresent.getClass().getDeclaredFields())
                  .map((field) -> {
                      field.setAccessible(true);
                      try {
                          return field.getName() + ":" +
                                 field.get(objToRepresent);
                      } catch (Exception e) {
                          return field.getName() + ":" + e.getMessage();
                      }
                  }).collect(Collectors.joining(", ")));
        builder.append("]");
        return builder.toString();
    }
}
