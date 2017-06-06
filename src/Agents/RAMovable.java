package Agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.ContainerID;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

/**
 * Created by Mike on 06.06.2017.
 */
public class RAMovable extends Agent {
    public int ID = -1;
    public int TeamID = -1;
    public int CurrentContainerID = -1;

    public int NumberOfLaps = -1;
    public int CurrentLap = -1;
    public int NumOfTeamMembers = -1;
    public int NextAgentID = -1;
    public AID NextAgentAID;
    public AID MasterAID;
    public boolean IsRunning = true;


    public boolean StartSignalArrived = false;
    public boolean StartSignalHandled = true;

    boolean ShouldInformAboutNewLap = false;
    boolean IsMessagedResponded = true;

    protected void setup() {

        addBehaviour(WaitForStartEnd);
     //   addBehaviour(WaitForResponses);
        addBehaviour(WaitForTurn);
    }

    private void MoveToNexxtContainer()
    {
   //     System.out.println(ID + " " + TeamID +" Poruszam się z: "+ Integer.toString(CurrentContainerID));
        // Move to the next container
        ContainerID destination = new ContainerID();
        int NextContainer = CurrentContainerID+1;
        if(NextContainer >= ConfigFiles.TeamMembers)
            NextContainer = 0;

        destination.setName(ConfigFiles.GetContainerName(NextContainer));
        doMove(destination);
        CurrentContainerID = NextContainer;
    }

    Behaviour WaitForTurn = new CyclicBehaviour() {
        @Override
        public void action() {
            if (IsRunning) {
                if (!StartSignalHandled && StartSignalArrived && ID == 0) {
                    MoveToNexxtContainer();
                    ShouldInformAboutNewLap = true;
                    StartSignalHandled = true;
                }
                ACLMessage msg = receive(MessageTemplate.MatchOntology(ConfigFiles.PassingRelay));
                if (msg != null) {
                    String content = msg.getContent();
                    int receivedLap = ReadLapNumberFromContent(content);
                    if (receivedLap > CurrentLap || (NumOfTeamMembers == ID + 1 && receivedLap == CurrentLap)) {
                        System.out.println("Tutaj " + ID + " " + TeamID + " Właśnie otrzymałem pałeczkę na okrążeniu " + receivedLap + " moje położenie: " + CurrentContainerID);
                        CurrentLap = receivedLap;
                        MoveToNexxtContainer();
                        ShouldInformAboutNewLap = true;

                        if (NumOfTeamMembers == ID + 1) {
                            CurrentLap++;
                        }

                    }
                    ACLMessage reply = msg.createReply();
                    reply.setOntology(ConfigFiles.MessageConfirmObtained);
                    send(reply);

                } else block();

                if (!IsMessagedResponded) {
                    ACLMessage msg2 = new ACLMessage(ACLMessage.INFORM);
                    msg2.setOntology(ConfigFiles.PassingRelay);
                    msg2.addReceiver(CreateReceiverForCurrentContainer());
                    msg2.setContent(PrepareContentToSent());
                    send(msg2);
                }

                if (ShouldInformAboutNewLap) {
                    ACLMessage msg2 = new ACLMessage(ACLMessage.INFORM);
                    msg2.setOntology(ConfigFiles.PassingRelay);
                    msg2.addReceiver(CreateReceiverForCurrentContainer());
                    msg2.setContent(PrepareContentToSent());
                    send(msg2);
                    IsMessagedResponded = false;
                    ShouldInformAboutNewLap = false;
                }


                if (CurrentLap == -1 && ID == 0) {
                    CurrentLap = 0;
                    ACLMessage msg2 = new ACLMessage(ACLMessage.INFORM);
                    msg2.setOntology(ConfigFiles.PassingRelay);
                    msg2.addReceiver(NextAgentAID);
                    msg2.setContent(PrepareContentToSent());
                    send(msg2);
                    IsMessagedResponded = false;

                }
                if (CurrentLap == NumberOfLaps) {
                    IsRunning = false;
                    addBehaviour(SendInfoAboutEnd);
                }
            }
        }

    };
    Behaviour SendInfoAboutEnd = new OneShotBehaviour() {
        @Override
        public void action() {
            ACLMessage msg = new ACLMessage(ACLMessage.CONFIRM);
            msg.setOntology(ConfigFiles.LapsDoneInfo);
            msg.addReceiver(new AID(ConfigFiles.GetMasterAddress()));
            msg.setContent(Integer.toString(TeamID));
            send(msg);
        }
    };


    private AID CreateReceiverForCurrentContainer()
    {
        if(NextAgentID == -1)
        {
            if( ID == ConfigFiles.TeamMembers -1)
                NextAgentID= 0;
            else NextAgentID = ID +1;
        }
        AID Ret = new AID(ConfigFiles.GetAgentAddressByContainer(CurrentContainerID,NextAgentID, TeamID));
        return Ret;
    }



    Behaviour WaitForStartEnd = new CyclicBehaviour() {
        @Override
        public void action() {
            // Wait for start message
            ACLMessage msg = receive(MessageTemplate.MatchOntology(ConfigFiles.StartEndOnthology));
            if (msg != null) {
                if (msg.getPerformative() == ACLMessage.AGREE) {
                    System.out.println("Dostalem polecenie startu " + ID+ " druzyna:" + TeamID);
                    StartSignalArrived = true;
                    StartSignalHandled = false;
                }
                if (msg.getPerformative() == ACLMessage.CANCEL) {
                    IsRunning = false;
                }
            } else
                block();
        }
    };
    private String PrepareContentToSent() {
        String ret = "";
        ret += CurrentLap;
        return ret;
    }

    private int ReadLapNumberFromContent(String content) {
        int lap = 0;
        try {
            lap = Integer.parseInt(content);

        } catch (Exception e) {
            String domek = "";
        }
        return lap;
    }
}
