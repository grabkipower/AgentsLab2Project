package Agents;

import com.sun.xml.internal.bind.v2.model.core.ID;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import sun.security.provider.ConfigFile;

/**
 * Created by Mike on 04.06.2017.
 */
public class Master extends Agent {


    protected void setup() {
        addBehaviour(StartSimulation);
    }

        Behaviour StartSimulation = new OneShotBehaviour() {
        @Override
        public void action() {

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("Wysyłam start");
            ACLMessage msg = new ACLMessage(ACLMessage.AGREE);
            msg.setContent(PrepareStartString());
            msg.setOntology(ConfigFiles.StartEndOnthology);
            AddReceivers(msg);
            send(msg);

        }
    };

    Behaviour EndSimulation = new OneShotBehaviour() {
        @Override
        public void action() {
            ACLMessage msg = new ACLMessage(ACLMessage.CANCEL);
            msg.setContent(PrepareStartString());
            msg.setOntology(ConfigFiles.StartEndOnthology);
            AddReceivers(msg);
            send(msg);
        }
    };


    // todo parse!!!
    private String PrepareStartString() {
        String txt = "";
        txt += ConfigFiles.Laps;
        return txt;
    }

    private void AddReceivers(ACLMessage msg)
    {
        for( int i = 0; i < ConfigFiles.TeamNumber;i++)
        {
            for( int j =0; j < ConfigFiles.TeamMembers; j++)
            {
                AID agent = new AID(ConfigFiles.GetAgentAddress(j,i));
                msg.addReceiver(agent);
            }
        }
    }
}
