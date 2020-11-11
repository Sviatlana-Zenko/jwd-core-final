package com.epam.jwd.core_final.factory.impl;

import com.epam.jwd.core_final.domain.CrewMember;
import com.epam.jwd.core_final.domain.Rank;
import com.epam.jwd.core_final.domain.Role;
import com.epam.jwd.core_final.factory.EntityFactory;
import java.util.List;

// do the same for other entities
public class CrewMemberFactory implements EntityFactory<CrewMember> {

    private static long crewMemberIdCounter = 0;
    public static final CrewMemberFactory INSTANCE = new CrewMemberFactory();

    private CrewMemberFactory() {
    }

    @Override
    public CrewMember create(List<String> infoFromFile) {
        return new CrewMember(++crewMemberIdCounter,
                              infoFromFile.get(1),
                              Role.resolveRoleById(Integer.parseInt(infoFromFile.get(0))),
                              Rank.resolveRankById(Integer.parseInt(infoFromFile.get(2))));
    }
}
