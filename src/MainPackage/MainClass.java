package MainPackage;

/**
 * Created by Mike on 04.06.2017.
 */
public class MainClass {

    public static void main(String[] args) {
        JadeController jade = JadeController.getInstance();
        jade.CreateMasterAgent();
        jade.CreateContainers();

        jade.CreateMovableAgentsByTeams();

//        jade.CreateMovableAgent();
//        jade.CreateAnotherMovableAgent();
//        JadeController2 jade2 = JadeController2.getInstance();




//
//        jade.CreateMasterAgent();
//        jade.CreateInitiatorAgent();
//        jade.CreateAgentsByTeams();
    }
}
