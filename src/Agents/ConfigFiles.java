package Agents;

/**
 * Created by Mike on 04.06.2017.
 */
public class ConfigFiles {
    public static int NumberOfTeams = 10;
    public static int Laps = 2;
    // Containers - 1 in other words
    public static int TeamMembers = 10;
    public static int NumberOfContainers(){
            return TeamMembers - 1;
    }

    public static String GetContainerNameAddress(int id)
    {
        return "MyMainPlatform";
    }

    public static String GetContainerName(int id)
    {
        if( id == 0)
            return "Main-Container";
        else
            return  "MyContainer" + Integer.toString(id);
    }


    public static String AgentPrefix = "RA_";
    public static String AgentSuffix = "N";

    public static String StartEndOnthology = "StartEnd";
    public static String PassingRelay = "PassRelay";
    public static String MessageConfirmObtained = "Obtained";
    public static String LapsDoneInfo = "LapsDone";





    public static String GetCurrentTeamOnthology(int teamid)
    {
        String ret = "Team" +Integer.toString(teamid);
        return  ret;
    }

    public static String GetAgentAddressByContainer(int ContID, int AgentID, int Teamid)
    {
        return AgentPrefix + AgentID + AgentSuffix + Teamid + "@"+GetContainerNameAddress(ContID);
    }

    public static String GetAgentAddress(int id, int teamid) {
        return AgentPrefix + id + AgentSuffix + teamid + "@MyMainPlatform";
    }

    public static String GetMasterAddress() {
        return "Master@MyMainPlatform";
    }
}
