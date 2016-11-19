public class Acceptor {
	
	private Messenger messenger;
	private Integer currentProposalNumber;
	private Integer acceptedId;
	private int acceptedValue;
	public Acceptor() {
		this(0);
	}
	
	public Acceptor(Integer currentProposalNumber) {
		this.currentProposalNumber = currentProposalNumber;
	}
	
	//proposal same as prepare
	//proposal coming in as a a packet
	public void receiveProposal(Packet proposal) {
		//proposal doesn't include get/put operaiton
		Integer proposalId = proposal.getProposalId();
		String proposerAddress = proposal.getProposalIpAddress();
		if(proposalId < currentProposalNumber) {
	        //still send back acccept id and value
			//TODO - use packet instead. add data to packet for acceptedId and acceptedValue
			messenger.sendRejection(proposerAddress, currentProposalNumber, acceptedId, acceptedValue);
		} else {
			if(proposalId > currentProposalNumber) {
				currentProposalNumber = proposalId;
			}
			messenger.sendPromise(proposerAddress, currentProposalNumber, acceptedId, acceptedValue);
		}
	}
	
    public void receiveAcceptRequest(Packet accept) {
    	Integer proposalId = proposal.getProposalId();
    	//this to be operation later 
    	int value = accept.getValue();
    	if(currentProposalNumber == null || 
    			currentProposalNumber <= proposalId) {
    		currentProposalNumber = proposalId;
    		acceptedValue = value;
    		acceptedId = proposalId;
    		//send to proposer
    		messenger.sendAcceptMessage(acceptedId, acceptedValue);
    		//send to learner. Learner does the operation. Sends it back to the operation
    		message.sendMessageToLearner(acceptedValue);
    	}
    }
}