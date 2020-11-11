package com.epam.jwd.core_final.criteria;

import com.epam.jwd.core_final.domain.Role;
import com.epam.jwd.core_final.domain.Spaceship;
import java.util.Map;

/**
 * Should be a builder for {@link Spaceship} fields
 */
public class SpaceshipCriteria extends Criteria<Spaceship> {

    private Long flightDistance;
    private Map<Role, Short> crew;
    private Boolean isReadyForNextMissions;

    public SpaceshipCriteria(Long id, String name, Long flightDistance,
                             Map<Role, Short> crew, Boolean isReadyForNextMissions) {
        super(id, name);
        this.flightDistance = flightDistance;
        this.crew = crew;
        this.isReadyForNextMissions = isReadyForNextMissions;
    }

    public Long getFlightDistance() {
        return flightDistance;
    }

    public Map<Role, Short> getCrew() {
        return crew;
    }

    public Boolean getReadyForNextMissions() {
        return isReadyForNextMissions;
    }

    public static class SpaceshipCriteriaBuilder extends Criteria.CriteriaBuilder {

        private Long flightDistance;
        private Map<Role, Short> crew;
        private Boolean isReadyForNextMissions;

        public void flightDistance(Long flightDistance) {
            this.flightDistance = flightDistance;
        }

        public void crew(Map<Role, Short> crew) {
            this.crew = crew;
        }

        public void isReadyForNextMissions(Boolean isReadyForNextMissions) {
            this.isReadyForNextMissions = isReadyForNextMissions;
        }

        public SpaceshipCriteria build() {
            return new SpaceshipCriteria(id, name, flightDistance, crew, isReadyForNextMissions);
        }
    }
}
