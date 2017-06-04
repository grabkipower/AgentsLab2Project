package Agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

/**
 * Created by Mike on 04.06.2017.
 */
public class RA extends Agent {
    public int ID = -1;
    public int TeamID = -1;
    public int LapNumber = -1;
    public int LapID = 100;
    public int NumberOfLapsToGo = -1;
    public int CurrentLap = -1;
    public int NumOfTeamMembers = -1;
    public AID NextAgentAID;
    public boolean IsRunning = false;


    public boolean IsFirstMessageResponded = true;
    public boolean IsMessagedResponded = true;
    public boolean ShouldInformAboutNewLap = false;

    public boolean HasRaisedLap = false;

    protected void setup() {

        addBehaviour(WaitForStartEnd);
        addBehaviour(WaitForResponses);
        addBehaviour(WaitForTurn);
    }

    Behaviour WaitForTurn = new CyclicBehaviour() {
        @Override
        public void action() {
            if (IsRunning) {
                ACLMessage msg = receive(MessageTemplate.MatchOntology(ConfigFiles.PassingRelay));
                if (msg != null) {
                    String content = msg.getContent();
                    int receivedLap = ReadLapNumberFromContent(content);
                    if (receivedLap > CurrentLap || (NumOfTeamMembers == ID + 1 && receivedLap == CurrentLap)) {
                        System.out.println("Tutaj " + ID + " " + TeamID + " Właśnie otrzymałem pałeczkę na okrążeniu " + receivedLap);
                        CurrentLap = receivedLap;
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
                    msg2.addReceiver(NextAgentAID);
                    msg2.setContent(PrepareContentToSent());
                    send(msg2);
                }

                if (ShouldInformAboutNewLap) {
                    ACLMessage msg2 = new ACLMessage(ACLMessage.INFORM);
                    msg2.setOntology(ConfigFiles.PassingRelay);
                    msg2.addReceiver(NextAgentAID);
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
//                if(!IsFirstMessageResponded)
//                {
//                    ACLMessage msg2 = new ACLMessage(ACLMessage.INFORM);
//                    msg2.setOntology(ConfigFiles.PassingRelay);
//                    msg2.addReceiver(NextAgentAID);
//                    msg2.setContent(PrepareContentToSent());
//                    send(msg2);
//                }


            }
        }
    };

    Behaviour WaitForResponses = new CyclicBehaviour() {
        @Override
        public void action() {
            ACLMessage msg = receive(MessageTemplate.MatchOntology(ConfigFiles.MessageConfirmObtained));
            if (msg != null) {
                IsFirstMessageResponded = true;
                IsMessagedResponded = true;

            } else block();
        }
    };


    Behaviour WaitForStartEnd = new CyclicBehaviour() {
        @Override
        public void action() {
            // Wait for start message
            ACLMessage msg = receive(MessageTemplate.MatchOntology(ConfigFiles.StartEndOnthology));
            if (msg != null) {
                if (msg.getPerformative() == ACLMessage.AGREE) {
                    NumberOfLapsToGo = Integer.parseInt(msg.getContent());
                    StartingActions();
                    IsRunning = true;
                }
                if (msg.getPerformative() == ACLMessage.CANCEL) {
                    IsRunning = false;
                }

            } else
                block();
        }
    };

    public void StartingActions() {
        System.out.println("Zaczynam swoja prace, moje id: " + ID + " " + TeamID);
        int nextAgentID;

        // todo : to nie moze tak byc
        NumOfTeamMembers = ConfigFiles.TeamMembers;
        if (ID == NumOfTeamMembers - 1)
            nextAgentID = 0;
        else
            nextAgentID = ID + 1;

        if ((NumOfTeamMembers == ID + 1))
            CurrentLap = 0;
        NextAgentAID = new AID(ConfigFiles.GetAgentAddress(nextAgentID, TeamID), AID.ISGUID);
        // todo shall i add address???
    }

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
