package mopa;

/**
 * Consumes finished groups from outside the museum
 * 
 * @author ngeard@unimelb.edu.au
 *
 */

public class Consumer extends Thread {

	// the security check that groups exit through
	private Foyer foyer;
	
	public Consumer(Foyer foyer) {
		this.foyer = foyer;
	}
	
	public void run() {
		while (!isInterrupted()) {
			try {
				// remove a group that has left the museum
				foyer.departFromMuseum();
		
				// let some time pass before the next departure
				sleep(Params.departurePause());
			}
			catch (InterruptedException e) {
				this.interrupt();
			}
		}
	}
}
