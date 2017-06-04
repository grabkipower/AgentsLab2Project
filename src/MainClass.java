/**
 * Created by Mike on 04.06.2017.
 */
public class MainClass {

    public static void main(String[] args) {
        System.out.println("System init");
        System.out.println("Trying to create Jade Platform");
        JadeController jade = JadeController.getInstance();
        System.out.println("Agent Platform created");

        System.out.println("Trying to create Master Agent");
        jade.CreateMasterAgent();
        System.out.println("Master Agent created");



        jade.CreateInitiatorAgent();


        jade.CreateAgentsByTeams();
    }
}
