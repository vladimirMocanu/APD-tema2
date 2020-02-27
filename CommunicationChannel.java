import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Class that implements the channel used by headquarters and space explorers to
 * communicate.
 */
public class CommunicationChannel {

	/**
	 * Creates a {@code CommunicationChannel} object.
	 */

	LinkedBlockingQueue<Message> headQuarter;
	LinkedBlockingQueue<Message> spaceExplorer;
	ReentrantLock get;
	ReentrantLock put;

	public CommunicationChannel() {

		headQuarter = new LinkedBlockingQueue<Message>();
		spaceExplorer = new LinkedBlockingQueue<Message>();
		put = new ReentrantLock();
		get = new ReentrantLock();
	}

	/**
	 * Puts a message on the space explorer channel (i.e., where space explorers
	 * write to and headquarters read from).
	 * 
	 * @param message message to be put on the channel
	 */
	public void putMessageSpaceExplorerChannel(Message message) {

		try {
			spaceExplorer.put(message);

		} catch (InterruptedException e) {

			throw new RuntimeException(e);
		}
	}

	/**
	 * Gets a message from the space explorer channel (i.e., where space explorers
	 * write to and headquarters read from).
	 * 
	 * @return message from the space explorer channel
	 */
	public Message getMessageSpaceExplorerChannel() {

		Message returnMessage = null;

		try {
			returnMessage = spaceExplorer.take();

		} catch (InterruptedException e) {
			throw new RuntimeException(e);

		}

		return returnMessage;
	}

	/**
	 * Puts a message on the headquarters channel (i.e., where headquarters write to
	 * and space explorers read from).
	 * 
	 * @param message message to be put on the channel
	 */

	public void putMessageHeadQuarterChannel(Message message) {

		put.lock();

		try {
			headQuarter.put(message);

		} catch (InterruptedException e) {
			throw new RuntimeException(e);

		} finally {
			if (message.getData() == "EXIT" || message.getData() == "END") {
				put.unlock();

			} else if (put.getHoldCount() == 2) {
				put.unlock();
				put.unlock();
			}
		}
	}

	/**
	 * Gets a message from the headquarters channel (i.e., where headquarters write
	 * to and space explorer read from).
	 * 
	 * @return message from the header quarter channel
	 */

	public Message getMessageHeadQuarterChannel() {

		get.lock();

		Message returnMessage = null;
		try {
			returnMessage = headQuarter.take();

		} catch (InterruptedException e) {
			throw new RuntimeException(e);

		} finally {
			if (returnMessage.getData() == "EXIT" || returnMessage.getData() == "END") {
				get.unlock();

			} else if (get.getHoldCount() == 2) {
				get.unlock();
				get.unlock();
			}
		}

		return returnMessage;
	}
}
