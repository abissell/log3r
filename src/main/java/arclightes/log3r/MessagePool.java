package main.java.arclightes.log3r;

interface MessagePool<T extends LogMessage> {
	T getNextMessage();
}
