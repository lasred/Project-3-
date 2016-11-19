public class Proposer{
	private final static int QUORUM_SIZE = 3;
	
	private Messenger messenger;
	private Integer currentProposerId;
	private Set<Packet> promises;
	private Packet paxosInfoPacket;
	public Proposer() {
		promises = new HashSet<Packet>();
		
	}
	
	public void prepare() {
        
	}
}