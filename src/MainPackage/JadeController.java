package MainPackage;

import Agents.*;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mike on 04.06.2017.
 */
public class JadeController {
    private static JadeController ourInstance = new JadeController();

    public static JadeController getInstance() {
        return ourInstance;
    }

    public ContainerController myContainer;
    public ContainerController MySecondContainer;
    public List<ContainerController> containerList = new ArrayList<ContainerController>();


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

    public void CreateContainers() {
        Runtime myRuntime = Runtime.instance();
        for (int i = 1; i < ConfigFiles.NumberOfContainers()+1; i++) {
            Runtime myRuntime2 = Runtime.instance();
            Profile container = new ProfileImpl("localhost", 1099, Profile.PLATFORM_ID);
            container.setParameter(Profile.CONTAINER_NAME, ConfigFiles.GetContainerName(i));
            containerList.add(myRuntime.createAgentContainer(container));
        }
    }

    public void CreateMovableAgentsByTeams()
    {
        // Number of platfoms in other words
        for (int i = 0; i < ConfigFiles.NumberOfTeams; i++) {

            for (int j = 0; j < ConfigFiles.TeamMembers; j++)
                CreateMovableAgent(j, i);
        }
    }

    public ContainerController GetContainerById(int teamid)
    {
        if( teamid == 0)
            return myContainer;
        else return containerList.get(teamid-1);
    }

    public void CreateMovableAgent(int id, int teamid) {
        try {
            RAMovable initiator = new RAMovable();
            initiator.ID = id;
            initiator.TeamID = teamid;
            initiator.NumberOfLaps = ConfigFiles.Laps;
            initiator.NumOfTeamMembers = ConfigFiles.TeamMembers;
            initiator.CurrentContainerID = id;
            AgentController cont = GetContainerById(id).acceptNewAgent(ConfigFiles.AgentPrefix + id + ConfigFiles.AgentSuffix + teamid, initiator);

            cont.start();

        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
    }


    public void CreateMovableAgent() {
        try {
            MovableAgent master = new MovableAgent();
            AgentController cont = MySecondContainer.acceptNewAgent("Movable1", master);
            cont.start();
            master.MoveThisAgent();
        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
    }

    public void CreateAnotherMovableAgent() {
        try {
            MovableAgent master = new MovableAgent();
            AgentController cont = myContainer.acceptNewAgent("Movable2", master);
            cont.start();
            //    master.MoveThisAgent();
        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
    }


    public void CreateMasterAgent() {
        try {
            Master master = new Master();
            AgentController cont = myContainer.acceptNewAgent("Master", master);
            cont.start();
        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
    }

    public void CreateInitiatorAgent() {
        try {
            InitiatorAgent initiator = new InitiatorAgent();
            AgentController cont = myContainer.acceptNewAgent("Initiator", initiator);
            cont.start();
        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
    }

    public void CreateAgentsByTeams() {
        for (int i = 0; i < ConfigFiles.NumberOfTeams; i++) {
            for (int j = 0; j < ConfigFiles.TeamMembers; j++)
                CreateAgent(j, i);
        }
    }

    public void CreateAgent(int id, int teamid) {
        try {
            RA initiator = new RA();
            initiator.ID = id;
            initiator.TeamID = teamid;
            AgentController cont = myContainer.acceptNewAgent(ConfigFiles.AgentPrefix + id + ConfigFiles.AgentSuffix + teamid, initiator);
            cont.start();

        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
    }


}
