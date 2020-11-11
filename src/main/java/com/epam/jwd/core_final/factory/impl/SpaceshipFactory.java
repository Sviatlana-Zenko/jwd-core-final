package com.epam.jwd.core_final.factory.impl;

import com.epam.jwd.core_final.domain.*;
import com.epam.jwd.core_final.factory.EntityFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpaceshipFactory implements EntityFactory<Spaceship> {

    private static long spaceshipIdCounter = 0;
    public static final SpaceshipFactory INSTANCE = new SpaceshipFactory();

    private SpaceshipFactory() {
    }

    @Override
    public Spaceship create(List<String> infoFromFile) {
        return new Spaceship(++spaceshipIdCounter,
                             infoFromFile.get(0),
                             Long.parseLong(infoFromFile.get(1)),
                             createMapFromString(infoFromFile.get(2)));
    }

    private Map<Role, Short> createMapFromString(String s) {
        Map<Role, Short> crew = new HashMap<>();
        String[] temp;

        s = s.replace('{', ' ').replace('}', ' ').trim();
        temp = s.split(",");
        for (int i = 0; i < temp.length; i++) {
            String[] temp2 = temp[i].split(":");
            crew.put(Role.resolveRoleById(Integer.parseInt(temp2[0])), Short.parseShort(temp2[1]));
        }

        return crew;
    }
}
