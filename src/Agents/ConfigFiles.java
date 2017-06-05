package Agents;

/**
 * Created by Mike on 04.06.2017.
 */
public class ConfigFiles {
    public static int NumberOfTeams = 25;
    public static int Laps = 300;
    public static int TeamMembers = 5;


    public static String AgentPrefix = "RA_";
    public static String AgentSuffix = "N";

    public static String StartEndOnthology = "StartEnd";
    public static String PassingRelay = "PassRelay";
    public static String MessageConfirmObtained = "Obtained";
    public static String LapsDoneInfo = "LapsDone";

    public static String GetAgentAddress(int id, int teamid) {
        return AgentPrefix + id + AgentSuffix + teamid + "@MyMainPlatform";
    }

    public static String GetMasterAddress() {
        return "Master@MyMainPlatform";
    }
}
