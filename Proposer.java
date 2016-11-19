public class Proposer{
	private final static int QUORUM_SIZE = 3;
	
	private Messenger messenger;
	private Integer currentProposerId;
	//from all the different acceptors 
	private Set<Packet> promises;
	private Packet paxosInfoPacket;
	
	private Integer lastValueReceived;
	private Integer valueForOperation;
	public Proposer() {
		promises = new HashSet<Packet>();
		
	}
	
	public void prepare(Integer operation) {
		//dont need any promises. starting fresh
		promises.clear();
		currentProposerId = currentProposerId + 1;
        //send to every acceptor
		messenger.sendProposal(currentProposalId);
		valueForOperation = operation;
	}
	
	public void receivePromise(Packet promise) {
	    String acceptorIpAddress = promise.getFromIpAddress();
	    Integer proposalId = promise.getProposalId();
	    if(promise.getValue() != null) {
	    	if(promise.getValue().equals(lastValueReceived)) {
	    		//go ahead and commit this
	    		messenger.sendCommitMessage(proposalId, lastValueReceived);
	    	} else {
	    	    //another operation is in flight
	    	    Thread.sleep(1000);
	    	    lastValueReceived = promise.getValue();
			    messenger.sendProposal(currentProposalId);
	    	}
	    }
	    if(proposalId.equals(currentProposerId) && promises.contains(promise)) {
	    	promises.add(promise);
	    	if(promises.size().equals(QUORUM_SIZE)) {
	    		messenger.sendAcceptMessage(proposalId, valueForOperation);
	    	}
	    }
	   
	}
}