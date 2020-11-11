package com.epam.jwd.core_final.context;

import com.epam.jwd.core_final.context.impl.NassaContext;
import com.epam.jwd.core_final.criteria.CrewMemberCriteria;
import com.epam.jwd.core_final.criteria.Criteria;
import com.epam.jwd.core_final.criteria.FlightMissionCriteria;
import com.epam.jwd.core_final.criteria.SpaceshipCriteria;
import com.epam.jwd.core_final.domain.CrewMember;
import com.epam.jwd.core_final.domain.FlightMission;
import com.epam.jwd.core_final.domain.Rank;
import com.epam.jwd.core_final.domain.Spaceship;
import com.epam.jwd.core_final.service.impl.CrewServiceImpl;
import com.epam.jwd.core_final.service.impl.MissionServiceImpl;
import com.epam.jwd.core_final.service.impl.SpaceshipServiceImpl;
import com.epam.jwd.core_final.util.MenuOptionsUtil;
import com.epam.jwd.core_final.util.PropertyReaderUtil;
import com.epam.jwd.core_final.util.UserInputUtil;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


// todo replace Object with your own types
@FunctionalInterface
public interface ApplicationMenu {

    ApplicationContext getApplicationContext();

    default void printAvailableOptions(String menuOptionsToPrint) {
        System.out.print(menuOptionsToPrint);
    }

    default void handleUserInput() {
        LocalDateTime lastInitTime = LocalDateTime.now();
        Boolean isCorrect = false;

        while (!isCorrect) {
            if (lastInitTime.plusMinutes(PropertyReaderUtil.populateApplicationProperties().getFileRefreshRate())
                            .isBefore(LocalDateTime.now() )) {
                lastInitTime = LocalDateTime.now();
                NassaContext.INSTANCE.init();
            }

            printAvailableOptions(MenuOptionsUtil.getMainMenuOptions());

            Integer mainMenuUserChoice = UserInputUtil.getUserNumberInput("Enter number -> ");
            switch (mainMenuUserChoice) {
                case 1:
                    handleCrewMemberMenuUserInput();
                    break;
                case 2:
                    handleSpaceshipMenuUserInput();
                    break;
                case 3:
                    handleMissionMenuUserInput();
                    break;
                case 0:
                    isCorrect = true;
                    break;
                default:
                    System.out.println("There is no such option. Try again -> ");
            }
        }
    }

    default void handleCrewMemberMenuUserInput() {
        Boolean isCorrectInput = false;

        while (!isCorrectInput) {
            printAvailableOptions(MenuOptionsUtil.getCrewMemberMenuOptions());

            int crewMemberMenuChoice = UserInputUtil.getUserNumberInput("Enter number -> ");
            isCorrectInput = true;
            switch (crewMemberMenuChoice) {
                case 1:
                    System.out.println(CrewServiceImpl.INSTANCE.findAllCrewMembers());
                    break;
                case 2:
                case 3:
                    Criteria<CrewMember> updateCriteria;
                    Criteria<CrewMember> idCriteria = new CrewMemberCriteria.CrewMemberCriteriaBuilder() {{
                        id(Long.valueOf(UserInputUtil.getUserNumberInput("\nEnter id to find crew member -> ")));
                    }}.build();

                    Optional<CrewMember> foundById = CrewServiceImpl.INSTANCE.findCrewMemberByCriteria(idCriteria);
                    if (!foundById.isEmpty()) {
                        updateCriteria = getCriteriaToUpdateCrewMember(crewMemberMenuChoice);
                        CrewServiceImpl.INSTANCE.updateCrewMemberDetails(foundById.get(), updateCriteria);
                    } else {
                        System.out.println("There is no a crew member with such id.");
                    }
                    break;
                default:
                    isCorrectInput = false;
                    System.out.println("There is no such option. Try again -> ");
            }
        }
    }

    default void handleSpaceshipMenuUserInput() {
        Boolean isCorrectInput = false;

        while (!isCorrectInput) {
            printAvailableOptions(MenuOptionsUtil.getSpaceshipMenuOptions());

            int spaceshipMenuChoice = UserInputUtil.getUserNumberInput("Enter number -> ");
            isCorrectInput = true;
            switch (spaceshipMenuChoice) {
                case 1:
                    System.out.println(SpaceshipServiceImpl.INSTANCE.findAllSpaceships());
                    break;
                case 2:
                case 3:
                    Criteria<Spaceship> updateCriteria;
                    Criteria<Spaceship> idCriteria = new SpaceshipCriteria.SpaceshipCriteriaBuilder(){{
                        id(Long.valueOf(UserInputUtil.getUserNumberInput("\nEnter id to find spaceship ->")));
                    }}.build();

                    Optional<Spaceship> foundById = SpaceshipServiceImpl.INSTANCE.findSpaceshipByCriteria(idCriteria);
                    if (!foundById.isEmpty()) {
                        updateCriteria = getCriteriaToUpdateSpaceship(spaceshipMenuChoice);
                        SpaceshipServiceImpl.INSTANCE.updateSpaceshipDetails(foundById.get(), updateCriteria);
                    } else {
                        System.out.println("There is no a spaceship with such id.");
                    }
                    break;
                default:
                    isCorrectInput = false;
                    System.out.println("There is no such option. Try again -> ");
            }
        }
    }

    default void handleMissionMenuUserInput() {
        Boolean isCorrectInput = false;

        while (!isCorrectInput) {
            printAvailableOptions(MenuOptionsUtil.getMissionMenuOptions());

            int missionMenuChoice = UserInputUtil.getUserNumberInput("Enter number -> ");
            isCorrectInput = true;
            switch (missionMenuChoice) {
                case 1:
                    List<String> possibleMissions = NassaContext.INSTANCE.createListOfPossibleMissions();

                    int planetNumber = 0;
                    while (planetNumber < 1 || planetNumber > 10) {
                        planetNumber = UserInputUtil.getUserNumberInput("\nEnter planet number (from 1 to 10) -> ");
                    }

                    List<String> chosenPlanet = new ArrayList<>();
                    for (int i = 0; i < possibleMissions.size(); i++) {
                        String[] temp = possibleMissions.get(i).split(";");
                        if (planetNumber == Integer.parseInt(temp[0])) {
                            chosenPlanet = Arrays.asList(temp);
                        }
                    }
                    System.out.println(MissionServiceImpl.INSTANCE.createMission(chosenPlanet));
                    break;
                case 2:
                case 3:
                    Criteria<FlightMission> updateCriteria;
                    Criteria<FlightMission> idCriteria1 = new FlightMissionCriteria.FlightMissionCriteriaBuilder() {{
                        id(Long.valueOf(UserInputUtil.getUserNumberInput("\nEnter id to find mission -> ")));
                    }}.build();

                    Optional<FlightMission> foundById = MissionServiceImpl.INSTANCE.findMissionByCriteria(idCriteria1);

                    if (!foundById.isEmpty()) {
                        updateCriteria = getCriteriaToUpdateMission(missionMenuChoice, foundById.get());
                        MissionServiceImpl.INSTANCE.updateMissionDetails(foundById.get(), updateCriteria);
                    } else {
                        System.out.println("There is no mission with such id.");
                    }
                    break;
                case 4:
                    Criteria<FlightMission> idCriteria2 = new FlightMissionCriteria.FlightMissionCriteriaBuilder() {{
                        id(Long.valueOf(UserInputUtil.getUserNumberInput("\nEnter id to find mission -> ")));
                    }}.build();

                    Optional<FlightMission> foundById1 = MissionServiceImpl.INSTANCE.findMissionByCriteria(idCriteria2);
                    if (!foundById1.isEmpty()) {
                        MissionServiceImpl.INSTANCE.showRealTimeStatus(foundById1.get());
                    } else {
                        System.out.println("There is no mission with such id.");
                    }
                    break;
                case 5:
                    MissionServiceImpl.INSTANCE.writeMissionsToFile();
                    System.out.println("Information about missions was written in json file.");
                    break;
                default:
                    isCorrectInput = false;
                    System.out.println("There is no such option. Try again -> ");
            }
        }
    }

    private static Criteria<CrewMember> getCriteriaToUpdateCrewMember(int crewMemberMenuChoice) {
        Criteria<CrewMember> updateCriteria;
        if (crewMemberMenuChoice == 2) {
            updateCriteria = new CrewMemberCriteria.CrewMemberCriteriaBuilder() {{
                name(UserInputUtil.getUserStringInput("Enter new name -> "));
            }}.build();
        } else {
            int rankID = 0;
            while (rankID < 1 || rankID > 4) {
                rankID = UserInputUtil.getUserNumberInput("Choose new rank:\n" +
                                                          "1 - trainee;\n" +
                                                          "2 - second officer;\n" +
                                                          "3 - first officer;\n" +
                                                          "4 - captain.\n" +
                                                          "Enter number - > ");
            }
            int finalRankID = rankID;
            updateCriteria = new CrewMemberCriteria.CrewMemberCriteriaBuilder() {{
                rank(Rank.resolveRankById(finalRankID));
            }}.build();
        }

        return updateCriteria;
    }

    private static Criteria<Spaceship> getCriteriaToUpdateSpaceship(int spaceshipMenuChoice) {
        Criteria<Spaceship> updateCriteria;

        if (spaceshipMenuChoice == 2) {
            updateCriteria = new SpaceshipCriteria.SpaceshipCriteriaBuilder() {{
                name(UserInputUtil.getUserStringInput("Enter new name -> "));
            }}.build();
        } else {
            updateCriteria = new SpaceshipCriteria.SpaceshipCriteriaBuilder() {{
                flightDistance(Long.valueOf(UserInputUtil.getUserNumberInput("Enter new distance -> ")));
            }}.build();
        }

        return updateCriteria;
    }

    private static Criteria<FlightMission> getCriteriaToUpdateMission(int missionMenuChoice,
                                                                      FlightMission flightMission) {
        Criteria<FlightMission> updateCriteria;

        if (missionMenuChoice == 2) {
            updateCriteria = new FlightMissionCriteria.FlightMissionCriteriaBuilder() {{
                name(UserInputUtil.getUserStringInput("Enter new name -> "));
            }}.build();
        } else {
            updateCriteria = new FlightMissionCriteria.FlightMissionCriteriaBuilder() {{
                endDate(flightMission.getEndDate().plusDays(
                        UserInputUtil.getUserNumberInput("How many days you want to add? Enter number -> ")));
            }}.build();
        }

        return updateCriteria;
    }
}
