package com.epam.jwd.core_final.domain;

import com.epam.jwd.core_final.exception.UnknownEntityException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public enum Role implements BaseEntity {
    MISSION_SPECIALIST(1L),
    FLIGHT_ENGINEER(2L),
    PILOT(3L),
    COMMANDER(4L);

    private final Long id;

    Role(Long id) {
        this.id = id;
    }

    @Override
    public Long getId() {
        return id;
    }

    /**
     * todo via java.lang.enum methods!
     */
    @Override
    public String getName() {
        return name().toLowerCase().replace("_", " ");
    }

    /**
     * todo via java.lang.enum methods!
     * @throws UnknownEntityException if such id does not exist
     */
    public static Role resolveRoleById(int id) {
        Role roleById;
        List<Role> ranks = Arrays.asList(Role.values());
        Predicate<Role> rolePredicate = role -> role.getId() == id;

        if (ranks.stream().anyMatch(rolePredicate)) {
            roleById = ranks.stream()
                    .filter(rolePredicate)
                    .findFirst()
                    .get();
        } else {
            throw new UnknownEntityException(Role.class.getName());
        }

        return roleById;
    }
}
