package utils;

/**
 * Interface for a Serailizer
 * 
 * @author Oleksandr Kononov
 *
 */

public interface Serializer {
	
	/**
	 * Push an object onto the stack
	 * @param o
	 */
	void push(Object o);
	
	/**
	 * Pop an object off the stack and return it
	 * @return object
	 */
	Object pop();
	
	/**
	 * Write the stack to a file
	 * @throws Exception
	 */
	void write() throws Exception;
	
	/**
	 * Read a stack from a file
	 * @throws Exception
	 */
	void read() throws Exception;
}
