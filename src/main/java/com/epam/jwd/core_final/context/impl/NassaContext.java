package com.epam.jwd.core_final.context.impl;

import com.epam.jwd.core_final.context.ApplicationContext;
import com.epam.jwd.core_final.domain.BaseEntity;
import com.epam.jwd.core_final.domain.CrewMember;
import com.epam.jwd.core_final.domain.FlightMission;
import com.epam.jwd.core_final.domain.Spaceship;
import com.epam.jwd.core_final.exception.InvalidStateException;
import com.epam.jwd.core_final.service.impl.CrewServiceImpl;
import com.epam.jwd.core_final.service.impl.SpaceshipServiceImpl;
import com.epam.jwd.core_final.strategy.impl.OneLineReadStrategy;
import com.epam.jwd.core_final.strategy.impl.MultiLineReadStrategy;
import com.epam.jwd.core_final.util.PropertyReaderUtil;
import org.apache.log4j.Logger;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

// todo
public class NassaContext implements ApplicationContext {

    private static final Logger logger = Logger.getLogger(NassaContext.class);
    public static final NassaContext INSTANCE = new NassaContext();
    private static long spaceshipsFileLength = 0;
    private static long crewMembersFileLength = 0;

    private NassaContext(){
    }

    // no getters/setters for them
    private Collection<CrewMember> crewMembers = new ArrayList<>();
    private Collection<Spaceship> spaceships = new ArrayList<>();
    private Collection<FlightMission> missions = new ArrayList<>();
    private List<String> possibleMissions = new ArrayList<>();

    @Override
    public <T extends BaseEntity> Collection<T> retrieveBaseEntityList(Class<T> tClass) {
        if (tClass.getSimpleName().equals(CrewMember.class.getSimpleName())) {
            return (Collection<T>) crewMembers;
        } else if (tClass.getSimpleName().equals(Spaceship.class.getSimpleName())){
            return (Collection<T>) spaceships;
        } else {
            return (Collection<T>) missions;
        }
    }

    /**
     * You have to read input files, populate collections
     * @throws InvalidStateException
     */
    @Override
    public void init() throws InvalidStateException {
        File spaceshipsFile = new File("src/main/resources/" +
                PropertyReaderUtil.populateApplicationProperties().getInputRootDir() + "/" +
                PropertyReaderUtil.populateApplicationProperties().getSpaceshipsFileName());
        File crewMembersFile = new File("src/main/resources/" +
                PropertyReaderUtil.populateApplicationProperties().getInputRootDir() + "/" +
                PropertyReaderUtil.populateApplicationProperties().getCrewFileName());

        List<String> infoAboutSpaceshipsFromFile;

        try {
            infoAboutSpaceshipsFromFile = MultiLineReadStrategy.INSTANCE.readInfoFromFile(spaceshipsFile);
        } catch (FileNotFoundException e) {
            logger.fatal("File '" + spaceshipsFile.getName() + "' is inaccessible or does not exist.");
            throw new InvalidStateException(spaceshipsFile.getName());
        }

        if (spaceshipsFileLength != spaceshipsFile.length()) {
            spaceshipsFileLength = spaceshipsFile.length();

            spaceships.removeIf(ship -> ship.getReadyForNextMissions() == true);
            for (int i = 0; i < infoAboutSpaceshipsFromFile.size(); i++) {
                Spaceship spaceship = SpaceshipServiceImpl.INSTANCE.createSpaceship(
                        Arrays.asList(infoAboutSpaceshipsFromFile.get(i).split(";")));

                if (spaceships.stream().noneMatch(ship -> ship.getName().equalsIgnoreCase(spaceship.getName()))) {
                    spaceships.add(spaceship);
                }
            }
        }

        List<String> infoAboutCrewMembersFromFile;

        try {
            infoAboutCrewMembersFromFile = OneLineReadStrategy.INSTANCE.readInfoFromFile(crewMembersFile);
        } catch (FileNotFoundException e) {
            logger.fatal("File '" + crewMembersFile.getName() + "' is inaccessible or does not exist.");
            throw new InvalidStateException(crewMembersFile.getName());
        }

        if (crewMembersFileLength != crewMembersFile.length()) {
            crewMembersFileLength = crewMembersFile.length();

            crewMembers.removeIf(member -> member.getReadyForNextMissions() == true);
            for (int i = 0; i < infoAboutCrewMembersFromFile.size(); i++) {
                CrewMember crewMember = CrewServiceImpl.INSTANCE.createCrewMember(
                        Arrays.asList(infoAboutCrewMembersFromFile.get(i).split(",")));

                if (crewMembers.stream().noneMatch(member -> member.equals(crewMember))) {
                    crewMembers.add(crewMember);
                }
            }
        }
    }

    public List<String> createListOfPossibleMissions() {
        final File missionsFile = new File("src/main/resources/input/possibleMissions");

        try {
            possibleMissions = MultiLineReadStrategy.INSTANCE.readInfoFromFile(missionsFile);
        } catch (FileNotFoundException e) {
            logger.fatal("File '" + missionsFile.getPath() + "' is inaccessible or does not exist.");
            throw new InvalidStateException(missionsFile.getPath());
        }

        return possibleMissions;
    }
}
