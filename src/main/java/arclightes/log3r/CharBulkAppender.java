package main.java.arclightes.log3r;

interface CharBulkAppender {
	CharBulkAppender appendChar(char c);
	CharBulkAppender appendChars(char[] srcArray);
	CharBulkAppender appendChars(char[] srcArray, int srcPos, int lengthOfCopy);
}
