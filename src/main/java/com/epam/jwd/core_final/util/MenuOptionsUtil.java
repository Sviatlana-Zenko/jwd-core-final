package com.epam.jwd.core_final.util;

public final class MenuOptionsUtil {

    private MenuOptionsUtil(){
    }

    public static String getMainMenuOptions() {
        return "\nChoose an entity you want to work with:\n" +
                "1 to work with crew members;\n" +
                "2 to work with spaceships;\n" +
                "3 to work with missions;\n" +
                "0 to stop working with the application.\n";
    }

    public static String getCrewMemberMenuOptions() {
        return "\nYou are working with crew members.\n" +
                "Choose an option:\n" +
                "1 to get information about all crew members;\n" +
                "2 to update name of a crew member;\n" +
                "3 to update rank of a crew member.\n";
    }

    public static String getSpaceshipMenuOptions() {
        return "\nYou are working with spaceships.\n" +
                "Choose an option:\n" +
                "1 to get information about all spaceships;\n" +
                "2 to update name of a spaceship;\n" +
                "3 to update (сut) distance of a spaceship.\n";
    }

    public static String getMissionMenuOptions() {
        return "\nYou are working with missions.\n" +
                "Choose an option:\n" +
                "1 to create new mission;\n" +
                "2 to update name of a mission;\n" +
                "3 to update end date of a mission;\n" +
                "4 to see real-time flight-status for chosen mission;\n" +
                "5 to write information about all missions in output file.\n";
    }
}
