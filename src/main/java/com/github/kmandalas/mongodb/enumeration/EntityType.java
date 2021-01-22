package com.github.kmandalas.mongodb.enumeration;

public enum EntityType {
    type_1,
    type_2,
    type_3,
    type_4,
    type_5;
    public static EntityType get(final String type) {
        for (final EntityType e : EntityType.values()) {
            if (e.name().equalsIgnoreCase(type)) {
                return e;
            }
        }
        return null;
    }

}


