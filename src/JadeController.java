import Agents.ConfigFiles;
import Agents.InitiatorAgent;
import Agents.Master;
import Agents.RA;
import jade.core.Location;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.core.messaging.TopicManagementHelper;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;
import jade.wrapper.State;

/**
 * Created by Mike on 04.06.2017.
 */
public class JadeController {
    private static JadeController ourInstance = new JadeController();

    public static JadeController getInstance() {
        return ourInstance;
    }

    ContainerController myContainer;


    public JadeController() {
        Runtime myRuntime = Runtime.instance();
// prepare the settings for the platform that we're going to start
        Profile myProfile = new ProfileImpl("localhost", 1099, Profile.PLATFORM_ID);
        myProfile.setParameter(Profile.PLATFORM_ID, "MyMainPlatform");
        myProfile.setParameter("gui", "true");
        myProfile.setParameter("services", "jade.core.messaging.TopicManagementService;jade.core.event.NotificationService;jade.core.mobility.AgentMobilityService");
// create the main container
        myContainer = myRuntime.createMainContainer(myProfile);

    }

    public void CreateMasterAgent()
    {
        try {
            Master master = new Master();
            AgentController cont = myContainer.acceptNewAgent("Master", master);
            cont.start();
        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
    }

    public void CreateInitiatorAgent()
    {
        try {
            InitiatorAgent initiator = new InitiatorAgent();
            AgentController cont = myContainer.acceptNewAgent("Initiator", initiator);
            cont.start();
        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
    }

    public void CreateAgentsByTeams()
    {
        for( int i = 0; i < ConfigFiles.TeamNumber;i++)
        {
            for( int j =0; j < ConfigFiles.TeamMembers; j++)
                CreateAgent(j,i);
        }
    }

    public void CreateAgent(int id, int teamid)
    {
        try {
            RA initiator = new RA();
            initiator.ID = id;
            initiator.TeamID = teamid;
            AgentController cont = myContainer.acceptNewAgent(ConfigFiles.AgentPrefix+id+ConfigFiles.AgentSuffix+teamid, initiator);
            cont.start();

        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
    }




    public void DeleteAgents()
    {


    }
}
