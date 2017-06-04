package Agents;

/**
 * Created by Mike on 04.06.2017.
 */
public  class ConfigFiles {

public static int TeamNumber = 2;
public static int Laps = 3;
public static int TeamMembers = 4;



public static String AgentPrefix = "RA_";
public static String AgentSuffix ="KKK";

    public static String TeamOnthology = "Team";
    public static String StartEndOnthology = "StartEnd";
    public static String PassingRelay ="PassRelay";
    public static String MessageConfirmObtained = "Obtained";

    public static String GetAgentAddress(int id, int teamid)
    {
        return AgentPrefix + id + AgentSuffix + teamid + "@MyMainPlatform";
    }
}
