package com.epam.jwd.core_final.factory.impl;

import com.epam.jwd.core_final.criteria.CrewMemberCriteria;
import com.epam.jwd.core_final.criteria.Criteria;
import com.epam.jwd.core_final.criteria.SpaceshipCriteria;
import com.epam.jwd.core_final.domain.*;
import com.epam.jwd.core_final.factory.EntityFactory;
import com.epam.jwd.core_final.service.impl.CrewServiceImpl;
import com.epam.jwd.core_final.service.impl.SpaceshipServiceImpl;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class FlightMissionFactory implements EntityFactory<FlightMission> {

    private static long flightMissionIdCounter = 0;
    public static final  FlightMissionFactory INSTANCE = new FlightMissionFactory();

    private FlightMissionFactory(){
    }

    @Override
    public FlightMission create(List<String> infoFromFile) {
        FlightMission flightMission = new FlightMission(++flightMissionIdCounter,
                                                        "Mission to " + infoFromFile.get(1),
                                                        LocalDateTime.now(),
                                                        LocalDateTime.now().plusDays(Long.valueOf(infoFromFile.get(3))),
                                                        Long.valueOf(infoFromFile.get(2)));

        Criteria<Spaceship> spaceshipCriteria = new SpaceshipCriteria.SpaceshipCriteriaBuilder() {{
            flightDistance(flightMission.getDistance());
            isReadyForNextMissions(true);
        }}.build();

        Optional<Spaceship> foundSpaceship = SpaceshipServiceImpl.INSTANCE.findSpaceshipByCriteria(spaceshipCriteria);
        if (!foundSpaceship.isEmpty()) {
            SpaceshipServiceImpl.INSTANCE.assignSpaceshipOnMission(flightMission, foundSpaceship.get());
        }

        for (Map.Entry<Role, Short> entry : flightMission.getAssignedSpaceship().getCrew().entrySet()) {
            int numberOfMembers = entry.getValue();
            while (numberOfMembers > 0) {
                Criteria<CrewMember> crewMemberCriteria = new CrewMemberCriteria.CrewMemberCriteriaBuilder() {{
                    role(entry.getKey());
                    isReadyForNextMissions(true);
                }}.build();

                Optional<CrewMember> foundCrewMember = CrewServiceImpl.INSTANCE.findCrewMemberByCriteria(crewMemberCriteria);
                if (!foundCrewMember.isEmpty()) {
                    CrewServiceImpl.INSTANCE.assignCrewMemberOnMission(flightMission, foundCrewMember.get());
                }

                numberOfMembers--;
            }
        }

        return flightMission;
    }
}
