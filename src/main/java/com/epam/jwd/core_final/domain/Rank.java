package com.epam.jwd.core_final.domain;

import com.epam.jwd.core_final.exception.UnknownEntityException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public enum Rank implements BaseEntity {
    TRAINEE(1L),
    SECOND_OFFICER(2L),
    FIRST_OFFICER(3L),
    CAPTAIN(4L);

    private final Long id;

    Rank(Long id) {
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
     *
     * @throws UnknownEntityException if such id does not exist
     */
    public static Rank resolveRankById(int id) {
        Rank rankById;
        List<Rank> ranks = Arrays.asList(Rank.values());
        Predicate<Rank> rankPredicate = rank -> rank.getId() == id;

        if (ranks.stream().anyMatch(rankPredicate)) {
            rankById = ranks.stream()
                    .filter(rankPredicate)
                    .findFirst()
                    .get();
        } else {
            throw new UnknownEntityException(Rank.class.getName());
        }

        return rankById;
    }
}
