package com.epam.jwd.core_final.criteria;

import com.epam.jwd.core_final.domain.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Should be a builder for {@link com.epam.jwd.core_final.domain.FlightMission} fields
 */
public class FlightMissionCriteria extends Criteria<FlightMission> {

    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Long distance;
    private Spaceship assignedSpaceship;
    private List<CrewMember> assignedCrew;
    private MissionResult missionResult;

    public FlightMissionCriteria(Long id, String name,
                                 LocalDateTime startDate, LocalDateTime endDate,
                                 Long distance, Spaceship assignedSpaceship,
                                 List<CrewMember> assignedCrew, MissionResult missionResult) {
        super(id, name);
        this.startDate = startDate;
        this.endDate = endDate;
        this.distance = distance;
        this.assignedSpaceship = assignedSpaceship;
        this.assignedCrew = assignedCrew;
        this.missionResult = missionResult;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public Long getDistance() {
        return distance;
    }

    public Spaceship getAssignedSpaceship() {
        return assignedSpaceship;
    }

    public List<CrewMember> getAssignedCrew() {
        return assignedCrew;
    }

    public MissionResult getMissionResult() {
        return missionResult;
    }

    public static class FlightMissionCriteriaBuilder extends Criteria.CriteriaBuilder {

        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private Long distance;
        private Spaceship assignedSpaceship;
        private List<CrewMember> assignedCrew;
        private MissionResult missionResult;

        public void startDate(LocalDateTime startDate) {
            this.startDate = startDate;
        }

        public void endDate(LocalDateTime endDate) {
            this.endDate = endDate;
        }

        public void distance(Long distance) {
            this.distance = distance;
        }

        public void assignedSpaceship(Long distance) {
            this.assignedSpaceship = assignedSpaceship;
        }

        public void assignedCrew(List<CrewMember> assignedCrew) {
            this.assignedCrew = assignedCrew;
        }

        public void missionResult(MissionResult missionResult) {
            this.missionResult = missionResult;
        }

        public FlightMissionCriteria build() {
            return new FlightMissionCriteria(id, name, startDate, endDate, distance,
                                             assignedSpaceship, assignedCrew, missionResult);
        }
    }
}
