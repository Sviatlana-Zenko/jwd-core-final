package com.epam.jwd.core_final.service.impl;

import com.epam.jwd.core_final.context.impl.NassaContext;
import com.epam.jwd.core_final.criteria.Criteria;
import com.epam.jwd.core_final.criteria.FlightMissionCriteria;
import com.epam.jwd.core_final.domain.FlightMission;
import com.epam.jwd.core_final.domain.MissionResult;
import com.epam.jwd.core_final.factory.impl.FlightMissionFactory;
import com.epam.jwd.core_final.service.MissionService;
import com.epam.jwd.core_final.util.DateTimeUtil;
import com.epam.jwd.core_final.util.PropertyReaderUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.TimerTask;
import java.util.stream.Collectors;
import java.util.Timer;

public class MissionServiceImpl implements MissionService {

    private static final Logger logger = Logger.getLogger(MissionServiceImpl.class);
    public static final MissionServiceImpl INSTANCE = new MissionServiceImpl();

    private MissionServiceImpl() {
    }

    @Override
    public List<FlightMission> findAllMissions() {
        return (List<FlightMission>) NassaContext.INSTANCE.retrieveBaseEntityList(FlightMission.class);
    }

    @Override
    public List<FlightMission> findAllMissionsByCriteria(Criteria<? extends FlightMission> criteria) {
        List<FlightMission> flightMissionsFoundByCriteria;
        FlightMissionCriteria missionCriteria = (FlightMissionCriteria) criteria;

        flightMissionsFoundByCriteria = NassaContext.INSTANCE.retrieveBaseEntityList(FlightMission.class).stream()
                .filter(flightMission -> missionCriteria.getId() == null ||
                                         flightMission.getId().equals(missionCriteria.getId()))
                .filter(flightMission -> missionCriteria.getName() == null ||
                                         flightMission.getName().equalsIgnoreCase(missionCriteria.getName()))
                .filter(flightMission -> missionCriteria.getStartDate() == null ||
                                         flightMission.getStartDate().equals(missionCriteria.getStartDate()))
                .filter(flightMission -> missionCriteria.getEndDate() == null ||
                                         flightMission.getEndDate().equals(missionCriteria.getEndDate()))
                .filter(flightMission -> missionCriteria.getDistance() == null ||
                                         flightMission.getDistance().compareTo(missionCriteria.getDistance()) > 0)
                .filter(flightMission -> missionCriteria.getAssignedSpaceship() == null ||
                                         flightMission.getAssignedSpaceship().equals(missionCriteria.getAssignedSpaceship()))
                .filter(flightMission -> missionCriteria.getAssignedCrew() == null ||
                                         flightMission.getAssignedCrew().equals(missionCriteria.getAssignedCrew()))
                .filter(flightMission -> missionCriteria.getMissionResult() == null ||
                                         flightMission.getMissionResult() == missionCriteria.getMissionResult())
                .collect(Collectors.toList());

        return flightMissionsFoundByCriteria;
    }

    @Override
    public Optional<FlightMission> findMissionByCriteria(Criteria<? extends FlightMission> criteria) {
        return findAllMissionsByCriteria(criteria).stream().findFirst();
    }

    @Override
    public FlightMission updateMissionDetails(FlightMission flightMission,
                                                Criteria<? extends FlightMission> criteria) {
        FlightMissionCriteria missionCriteria = (FlightMissionCriteria) criteria;

        if (Optional.ofNullable(flightMission).isPresent()) {
            if (missionCriteria.getName() != null) {
                updateFlightMissionName(flightMission, missionCriteria.getName());
            } else {
                updateFlightMissionEndDate(flightMission, missionCriteria.getEndDate());
            }
        }

        return flightMission;
    }

    @Override
    public FlightMission createMission(List<String> infoFromFile) {
        FlightMission flightMission = FlightMissionFactory.INSTANCE.create(infoFromFile);
        NassaContext.INSTANCE.retrieveBaseEntityList(FlightMission.class).add(flightMission);
        logger.info("New created object: " + flightMission.toString());

        return flightMission;
    }

    @Override
    public void writeMissionsToFile() {
        ObjectMapper mapper = new ObjectMapper();
        File file = new File("src/main/resources/" +
                             PropertyReaderUtil.populateApplicationProperties().getOutputRootDir() +
                             "/missions");
        try {
            mapper.writeValue(file, NassaContext.INSTANCE.retrieveBaseEntityList(FlightMission.class));
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public void showRealTimeStatus(FlightMission flightMission) {
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                MissionResult current = flightMission.getMissionResult();
                Random random = new Random();
                int temp = random.nextInt(2);

                if (current == MissionResult.PLANNED) {
                    if (temp == 0) {
                        flightMission.setMissionResult(MissionResult.CANCELLED);
                    } else {
                        flightMission.setMissionResult(MissionResult.IN_PROGRESS);
                    }
                } else if (current == MissionResult.IN_PROGRESS) {
                    if (temp == 0) {
                        flightMission.setMissionResult(MissionResult.FAILED);
                    } else {
                        flightMission.setMissionResult(MissionResult.COMPLETED);
                    }
                } else if (current == MissionResult.COMPLETED || current == MissionResult.CANCELLED) {
                    flightMission.getAssignedSpaceship().setReadyForNextMissions(true);
                    for (int i = 0; i < flightMission.getAssignedCrew().size(); i++) {
                        flightMission.getAssignedCrew().get(i).setReadyForNextMissions(true);
                    }
                }
                System.out.println("Real-time status -> " + flightMission.getMissionResult());
            }
        };

        Timer timer = new Timer(true);
        System.out.println("Please, stand by.");
        timer.schedule(timerTask, 10000L);

        try {
            Thread.sleep(12000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        timer.cancel();
    }

    private void updateFlightMissionName(FlightMission flightMission, String newName) {
        if (NassaContext.INSTANCE.retrieveBaseEntityList(FlightMission.class).stream()
                .noneMatch(mission -> mission.getName().equalsIgnoreCase(newName))) {
            flightMission.setName(newName);
            System.out.println("You have changed flight mission name. " +
                               "New name - " + newName + ".");
        } else {
            System.out.println("You can't set that name. " +
                               "A flight mission with the same name already exists.");
        }
    }

    private void updateFlightMissionEndDate(FlightMission flightMission, LocalDateTime newEndDate) {
        Long maxNumberOfDaysToAdd = 50L;

        if (newEndDate.isBefore(flightMission.getEndDate().plusDays(maxNumberOfDaysToAdd))) {
            flightMission.setEndDate(newEndDate);
            System.out.println("You have changed flight mission end date. New end date - " +
                               DateTimeUtil.getFormattedDateAndTime(flightMission.getEndDate()) + ".");
        } else {
            System.out.println("You can't add more than " + maxNumberOfDaysToAdd + ". " +
                               "Instead consider ability of cancelling the mission.");
        }
    }
}
