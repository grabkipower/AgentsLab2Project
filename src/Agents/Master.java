package Agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
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


    // todo parse!!!
    private String PrepareStartString() {
        String txt = "";
        txt += ConfigFiles.Laps;
        return txt;
    }

    private void AddReceivers(ACLMessage msg)
    {
        for(int i = 0; i < ConfigFiles.NumberOfTeams; i++)
        {
            for( int j =0; j < ConfigFiles.TeamMembers; j++)
            {
                AID agent = new AID(ConfigFiles.GetAgentAddress(j,i));
                msg.addReceiver(agent);
            }
        }
    }
}
