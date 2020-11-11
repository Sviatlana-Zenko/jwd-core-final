package com.epam.jwd.core_final.util;

import com.epam.jwd.core_final.domain.AbstractBaseEntity;
import java.util.Collection;

public final class EntityCheckUtil {

    private EntityCheckUtil() {
    }

    public static Boolean checkFullNameMatch(Collection<? extends AbstractBaseEntity> entities,
                                             String nameToCheck){
        return entities.stream()
                       .anyMatch(entity -> entity.getName().equalsIgnoreCase(nameToCheck));
    }
}
