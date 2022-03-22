import mopa.*;

/**
 * The top-level component of the museum simulator.
 * 
 * It is responsible for:
 *  - creating all the components of the system; 
 *  - starting all of the processes; 
 *  - supervising processes regularly to check that all are alive.
 *  
 * @author ngeard@unimelb.edu.au
 * 
 */

public class Main {

	/**
	 * The driver of the museum simulator system:
	 */

	public static void main(String [] args) {
		Museum museum = Museum.getInstance(Params.ROOMS);

		// generate the producer, the consumer and the security guard
		Producer producer = new Producer(museum.getFoyer());
		Consumer consumer = new Consumer(museum.getFoyer());

		// start the various threads
		producer.start();
		consumer.start();
		museum.getSecurityGuard().start();
		for(Guide guide : museum.getGuides()) {
			guide.start();
		}

		//regularly check on the status of threads
		while (producer.isAlive()
				&& consumer.isAlive()
				&& museum.isSecurityGuardAlive()
				&& museum.areGuidesAlive()) {
			try {
				Thread.sleep(Params.MAIN_INTERVAL);
			}
			catch (InterruptedException e) {
				System.out.println("Main was interrupted");
				break;
			}
		}

		//if some thread died, interrupt all other threads and halt
		producer.interrupt();    
		consumer.interrupt();
		museum.getSecurityGuard().interrupt();
		for(Guide guide : museum.getGuides()) {
			guide.interrupt();
		}

		System.out.println("Main terminates, all threads terminated");
		System.exit(0);
	}
}
