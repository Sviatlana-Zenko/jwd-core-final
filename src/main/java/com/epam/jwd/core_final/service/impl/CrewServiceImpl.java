package com.epam.jwd.core_final.service.impl;

import com.epam.jwd.core_final.context.impl.NassaContext;
import com.epam.jwd.core_final.criteria.CrewMemberCriteria;
import com.epam.jwd.core_final.criteria.Criteria;
import com.epam.jwd.core_final.domain.CrewMember;
import com.epam.jwd.core_final.domain.FlightMission;
import com.epam.jwd.core_final.domain.Rank;
import com.epam.jwd.core_final.exception.AssignationException;
import com.epam.jwd.core_final.exception.EntityDuplicateException;
import com.epam.jwd.core_final.factory.impl.CrewMemberFactory;
import com.epam.jwd.core_final.service.CrewService;
import com.epam.jwd.core_final.util.EntityCheckUtil;
import org.apache.log4j.Logger;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CrewServiceImpl implements CrewService {

    private static final Logger logger = Logger.getLogger(CrewServiceImpl.class);
    public static final CrewServiceImpl INSTANCE = new CrewServiceImpl();

    private CrewServiceImpl() {
    }

    @Override
    public List<CrewMember> findAllCrewMembers() {
        return (List<CrewMember>) NassaContext.INSTANCE.retrieveBaseEntityList(CrewMember.class);
    }

    @Override
    public List<CrewMember> findAllCrewMembersByCriteria(Criteria<? extends CrewMember> criteria) {
        List<CrewMember> crewMembersFoundByCriteria;
        CrewMemberCriteria crewMemberCriteria = (CrewMemberCriteria) criteria;

        crewMembersFoundByCriteria = NassaContext.INSTANCE.retrieveBaseEntityList(CrewMember.class).stream()
                .filter(crewMember -> crewMemberCriteria.getId() == null ||
                                      crewMember.getId().equals(crewMemberCriteria.getId()))
                .filter(crewMember -> crewMemberCriteria.getName() == null ||
                                      crewMember.getName().equalsIgnoreCase(crewMember.getName()))
                .filter(crewMember -> crewMemberCriteria.getRole() == null ||
                                      crewMember.getRole() == crewMemberCriteria.getRole())
                .filter(crewMember -> crewMemberCriteria.getRank() == null ||
                                      crewMember.getRank() == crewMemberCriteria.getRank())
                .filter(crewMember -> crewMemberCriteria.getReadyForNextMissions() == null ||
                                      crewMember.getReadyForNextMissions().equals(crewMemberCriteria.getReadyForNextMissions()))
                .collect(Collectors.toList());

        return crewMembersFoundByCriteria;
    }

    @Override
    public Optional<CrewMember> findCrewMemberByCriteria(Criteria<? extends CrewMember> criteria) {
        return findAllCrewMembersByCriteria(criteria).stream().findFirst();
    }

    @Override
    public CrewMember updateCrewMemberDetails(CrewMember crewMember, Criteria<? extends CrewMember> criteria) {
        CrewMemberCriteria crewMemberCriteria = (CrewMemberCriteria) criteria;

        if (Optional.ofNullable(crewMember).isPresent()) {
            if (crewMemberCriteria.getRank() != null) {
                updateCrewMemberRank(crewMember, crewMemberCriteria.getRank());
            } else {
                updateCrewMemberName(crewMember, crewMemberCriteria.getName());
            }
        }

        return crewMember;
    }

    @Override
    public void assignCrewMemberOnMission(FlightMission flightMission,
                                          CrewMember crewMember) throws AssignationException {
        if (Optional.ofNullable(crewMember).isPresent()) {
            if (crewMember.getReadyForNextMissions()) {
                flightMission.getAssignedCrew().add(crewMember);
                crewMember.setReadyForNextMissions(false);
            } else {
                logger.fatal("There is no a " + CrewMember.class.getName() +
                             "with requested parameters. New mission cannot be created.");
                throw new AssignationException(CrewMember.class.getName());
            }
        }
    }

    @Override
    public CrewMember createCrewMember(List<String> infoFromFile) throws EntityDuplicateException {
        CrewMember crewMember;

        if (EntityCheckUtil.checkFullNameMatch(
                NassaContext.INSTANCE.retrieveBaseEntityList(CrewMember.class), infoFromFile.get(1))) {
            throw new EntityDuplicateException(infoFromFile.get(1));
        } else {
            crewMember = CrewMemberFactory.INSTANCE.create(infoFromFile);
            logger.info("New created object: " + crewMember.toString());
        }

        return crewMember;
    }

    private void updateCrewMemberName(CrewMember crewMember, String newName) {
        if (NassaContext.INSTANCE.retrieveBaseEntityList(CrewMember.class).stream()
                .noneMatch(member -> member.getName().equalsIgnoreCase(newName))) {
            crewMember.setName(newName);
            System.out.println("You have changed crew member's name. " +
                               "New name - " + newName + ".");
        } else {
            System.out.println("You can't set that name. " +
                               "Crew member with the same name already exists.");
        }
    }

    private void updateCrewMemberRank(CrewMember crewMember, Rank newRank) {
        if (crewMember.getRank() != newRank) {
            crewMember.setRank(newRank);
            System.out.println("You have changed crew member's rank. " +
                               "New rank - " + newRank + ".");
        } else {
            System.out.println("Crew member already has that rank.");
        }
    }
}
