package Agents;

import MainPackage.JadeController;
import jade.content.Concept;
import jade.content.ContentElement;
import jade.content.onto.basic.Action;
import jade.core.Agent;
import jade.core.ContainerID;
import jade.core.Location;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.wrapper.ControllerException;

/**
 * Created by Mike on 06.06.2017.
 */
public class MovableAgent extends Agent {

    protected void setup()
    {
        addBehaviour(WaitForStartEnd);
    }

    Behaviour WaitForStartEnd = new CyclicBehaviour() {
        @Override
        public void action() {
            // Wait for start message
            ACLMessage msg = receive(MessageTemplate.MatchOntology(ConfigFiles.StartEndOnthology));
            if (msg != null) {
                if (msg.getPerformative() == ACLMessage.AGREE) {

                }
                if (msg.getPerformative() == ACLMessage.CANCEL) {

                }

            } else
                block();
        }
    };

    public void MoveThisAgent()
    {
        String name= this.getAID().getName().toString();
        if( name.equals( "Movable1@MyMainPlatform")) {
            ContainerID destination = new ContainerID();
            destination.setName("Main-Container");
            doMove(destination);
        }else {
            ContainerID destination = new ContainerID();
            destination.setName("Container-1");
            doMove(destination);
        }
    }
}
