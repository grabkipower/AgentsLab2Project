package Agents;

import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.FIPANames;
import jade.domain.JADEAgentManagement.WhereIsAgentAction;
import jade.domain.mobility.MobilityOntology;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Mike on 04.06.2017.
 */
public class Master extends Agent {

    long StartSimulationTime = 0;
    long EndSimulationTime = 0;
    List<Integer> WhoHasEndedSoFar = new ArrayList<Integer>();
    List<Long> TimeOfEnd = new ArrayList<Long>();

    Behaviour receiveMsg = new CyclicBehaviour() {
        @Override
        public void action() {
            ACLMessage msg = receive();
            if( msg!= null) {
                int a = 2;
            }
            else
                block();
        }
    };


    protected void setup() {
        addBehaviour(StartSimulation);
        addBehaviour(receiveMsg);
    }

        Behaviour StartSimulation = new OneShotBehaviour() {
        @Override
        public void action() {

            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

//            ACLMessage msg2 = prepareRequestToAMS(new AID(ConfigFiles.GetAgentAddressByContainer(0,0,0)));
//            send(msg2);



            System.out.println("Wysyłam start");

            addBehaviour(WaitForEnd);
            StartSimulationTime = System.currentTimeMillis();
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

    Behaviour WaitForEnd = new CyclicBehaviour() {
        @Override
        public void action() {
            ACLMessage msg = receive(MessageTemplate.MatchOntology(ConfigFiles.LapsDoneInfo));
            if( msg != null) {
                int teamid = GetTeamFromEndMessage(msg.getContent());
                AddToEndListIfNotPresent(teamid);
                if( WhoHasEndedSoFar.size() == ConfigFiles.NumberOfTeams)
                {
                    EndSimulationTime = System.currentTimeMillis();

                    System.out.println("#####################################");
                    System.out.println("The simulation has ended");
                    System.out.println(ConfigFiles.TeamMembers);
                    System.out.println(ConfigFiles.NumberOfTeams);
                    System.out.println(ConfigFiles.Laps);
                    System.out.println(StartSimulationTime);

                    for(int i =0; i < WhoHasEndedSoFar.size();i++)
                    {
                        System.out.println(WhoHasEndedSoFar.get(i));
                        System.out.println(TimeOfEnd.get(i));
                    }
                    System.out.println("#####################################");
                    String domek ="";
                }

            }
            else
            {
                block();
            }

        }
    };

    private void AddToEndListIfNotPresent(int id)
    {
        boolean doesExist = false;
        for (int x : WhoHasEndedSoFar)
        {
            if( x == id) {
                doesExist = true;
                break;
            }
        }
        if(!doesExist) {
            WhoHasEndedSoFar.add(id);
            TimeOfEnd.add(System.currentTimeMillis());
        }
    }

    private int GetTeamFromEndMessage(String msg)
    {
        int teamid = Integer.parseInt(msg);
        return teamid;
    }


    private String PrepareStartString() {
        String txt = "";
        txt += ConfigFiles.Laps;
        return txt;
    }

    private ACLMessage prepareRequestToAMS(AID agent) {
        ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
        request.addReceiver(getAMS());
        request.setLanguage(FIPANames.ContentLanguage.FIPA_SL0);
        request.setOntology(MobilityOntology.NAME);
        request.setProtocol(FIPANames.InteractionProtocol
                .FIPA_REQUEST);
        // creates the content of the ACLMessage
        Action act = new Action();
        act.setActor(getAMS());
        WhereIsAgentAction action = new WhereIsAgentAction();
        action.setAgentIdentifier(agent);
        act.setAction(action);
        try {
            getContentManager().fillContent(request, act);
        } catch (Codec.CodecException ignore) {
        } catch (OntologyException ignore) {}
        return request;
    }


    private void AddReceivers(ACLMessage msg)
    {
        for(int i = 0; i < ConfigFiles.NumberOfTeams; i++)
        {
            AID agent = new AID(ConfigFiles.GetAgentAddressByContainer(0,0,i));
            msg.addReceiver(agent);
        }



      //  AID agent = new AID("Movable1@MyMainPlatform");
     //   msg.addReceiver(agent);
    }
}
