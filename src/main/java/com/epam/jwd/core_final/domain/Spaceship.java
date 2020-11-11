package com.epam.jwd.core_final.domain;

import java.util.Map;

/**
 * crew {@link java.util.Map<Role, Short>}
 * flightDistance {@link Long} - total available flight distance
 * isReadyForNextMissions {@link Boolean} - true by default. Set to false, after first failed mission
 */
public class Spaceship extends AbstractBaseEntity {
    //todo
    private Long flightDistance;
    private Map<Role, Short> crew;
    private Boolean isReadyForNextMissions;

    public Spaceship(Long id, String name,
                     Long flightDistance, Map<Role, Short> crew) {
        super(id, name);
        this.flightDistance = flightDistance;
        this.crew = crew;
        isReadyForNextMissions = true;
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

    public void setFlightDistance(Long flightDistance) {
        this.flightDistance = flightDistance;
    }

    public void setReadyForNextMissions(Boolean readyForNextMissions) {
        isReadyForNextMissions = readyForNextMissions;
    }

    public void setCrew(Map<Role, Short> crew) {
        this.crew = crew;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "id: " + super.getId() +
                ", name: " + super.getName() +
                ", flightDistance: " + flightDistance +
                ", crew: " + crew +
                ", isReadyForNextMissions: " + isReadyForNextMissions +
                "}";
    }
}
