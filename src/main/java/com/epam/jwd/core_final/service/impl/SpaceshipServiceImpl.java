package com.epam.jwd.core_final.service.impl;

import com.epam.jwd.core_final.context.impl.NassaContext;
import com.epam.jwd.core_final.criteria.Criteria;
import com.epam.jwd.core_final.criteria.SpaceshipCriteria;
import com.epam.jwd.core_final.domain.FlightMission;
import com.epam.jwd.core_final.domain.Spaceship;
import com.epam.jwd.core_final.exception.AssignationException;
import com.epam.jwd.core_final.exception.EntityDuplicateException;
import com.epam.jwd.core_final.factory.impl.SpaceshipFactory;
import com.epam.jwd.core_final.service.SpaceshipService;
import com.epam.jwd.core_final.util.EntityCheckUtil;
import org.apache.log4j.Logger;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SpaceshipServiceImpl implements SpaceshipService {

    private static final Logger logger = Logger.getLogger(SpaceshipServiceImpl.class);
    public static final SpaceshipServiceImpl INSTANCE = new SpaceshipServiceImpl();

    private SpaceshipServiceImpl() {
    }

    @Override
    public List<Spaceship> findAllSpaceships() {
        return (List<Spaceship>) NassaContext.INSTANCE.retrieveBaseEntityList(Spaceship.class);
    }

    @Override
    public List<Spaceship> findAllSpaceshipsByCriteria(Criteria<? extends Spaceship> criteria) {
        List<Spaceship> spaceshipsFoundByCriteria;
        SpaceshipCriteria spaceshipCriteria = (SpaceshipCriteria) criteria;

        spaceshipsFoundByCriteria = NassaContext.INSTANCE.retrieveBaseEntityList(Spaceship.class).stream()
                .filter(spaceship -> spaceshipCriteria.getId() == null ||
                                     spaceship.getId().equals(spaceshipCriteria.getId()))
                .filter(spaceship -> spaceshipCriteria.getName() == null ||
                                     spaceship.getName().equalsIgnoreCase(spaceshipCriteria.getName()))
                .filter(spaceship -> spaceshipCriteria.getFlightDistance() == null
                                     || spaceship.getFlightDistance().compareTo(spaceshipCriteria.getFlightDistance()) > 0)
                .filter(spaceship -> spaceshipCriteria.getCrew() == null ||
                                     spaceship.getCrew().equals(spaceshipCriteria.getCrew()))
                .filter(spaceship -> spaceshipCriteria.getReadyForNextMissions() == null ||
                                     spaceship.getReadyForNextMissions().equals(spaceshipCriteria.getReadyForNextMissions()))
                .collect(Collectors.toList());

        return spaceshipsFoundByCriteria;
    }

    @Override
    public Optional<Spaceship> findSpaceshipByCriteria(Criteria<? extends Spaceship> criteria) {
        return findAllSpaceshipsByCriteria(criteria).stream().findFirst();
    }

    @Override
    public Spaceship updateSpaceshipDetails(Spaceship spaceship, Criteria<? extends Spaceship> criteria) {
        SpaceshipCriteria spaceshipCriteria = (SpaceshipCriteria) criteria;

        if (Optional.ofNullable(spaceship).isPresent()) {
            if (spaceshipCriteria.getName() != null) {
                updateSpaceshipName(spaceship, spaceshipCriteria.getName());
            } else {
                updateSpaceshipDistance(spaceship, spaceshipCriteria.getFlightDistance());
            }
        }

        return spaceship;
    }

    @Override
    public void assignSpaceshipOnMission(FlightMission flightMission,
                                         Spaceship spaceship) throws AssignationException {
        if (Optional.ofNullable(spaceship).isPresent()) {
            if (spaceship.getReadyForNextMissions()) {
                flightMission.setAssignedSpaceship(spaceship);
                spaceship.setReadyForNextMissions(false);
            } else {
                logger.fatal("There is no a " + Spaceship.class.getName() +
                             "with requested parameters. New mission cannot be created.");
                throw new AssignationException(Spaceship.class.getName());
            }
        }
    }

    @Override
    public Spaceship createSpaceship(List<String> infoFromFile) throws EntityDuplicateException{
        Spaceship spaceship;

        if (EntityCheckUtil.checkFullNameMatch(NassaContext.INSTANCE.retrieveBaseEntityList(Spaceship.class),
                infoFromFile.get(0))) {
            logger.fatal("Attempt to create entity duplicate (entity with name '" +
                         infoFromFile.get(0) + "' already exists.");
            throw new EntityDuplicateException(infoFromFile.get(0));
        } else {
            spaceship = SpaceshipFactory.INSTANCE.create(infoFromFile);
            logger.info("New created object: " + spaceship.toString());
        }

        return spaceship;
    }

    private void updateSpaceshipDistance(Spaceship spaceship, Long newDistance) {
        if (newDistance.compareTo(spaceship.getFlightDistance()) < 0) {
            spaceship.setFlightDistance(newDistance);
            System.out.println("You have changed spaceship distance. " +
                               "New distance - " + newDistance + ".");
        } else {
            System.out.println("You can't set that distance. New distance should be less " +
                               "than current distance (" + spaceship.getFlightDistance() + ").");
        }
    }

    private void updateSpaceshipName(Spaceship spaceship, String newName) {
        if (NassaContext.INSTANCE.retrieveBaseEntityList(Spaceship.class).stream()
                .noneMatch(ship -> ship.getName().equalsIgnoreCase(newName))) {
            spaceship.setName(newName);
            System.out.println("You have changed spaceship name. " +
                               "New name - " + newName + ".");
        } else {
            System.out.println("You can't set that name. " +
                               "A spaceship with the same name already exists.");
        }
    }
}
