package mopa;

/**
 * Produces new tour groups arriving at the museum.
 *
 * @author ngeard@unimelb.edu.au
 *
 */

public class Producer extends Thread {

	// the foyer of the museum
	private Foyer foyer;

	// create a new producer
	public Producer(Foyer foyer) {
		this.foyer = foyer;
	}

	// groups arrive at the museum at random intervals
	public void run() {
		while(!isInterrupted()) {
			try {
				// send a new group to museum
				Group group = Group.getNewGroup();
				foyer.arriveAtMuseum(group);

				// wait for the security guard to escort the group inside
				sleep(Params.WALKING_TIME + Params.SECURITY_TIME);

				// let some time pass before the next group arrives
				sleep(Params.arrivalPause());
			}
			catch (InterruptedException e) {
				this.interrupt();
			}
		}
	}
}
