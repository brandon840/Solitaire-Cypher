package assignment2;


public class SolitaireCipher {
	public Deck key;
	
	public SolitaireCipher (Deck key) {
		this.key = new Deck(key); // deep copy of the deck
	}
	
	/* 
	 * TODO: Generates a keystream of the given size
	 */
	public int[] getKeystream(int size) {
		int[] keyStream = new int[size];

		for (int i=0;i<size;i++){
			keyStream[i] = key.generateNextKeystreamValue();
		}

		return keyStream;
	}
		
	/* 
	 * TODO: Encodes the input message using the algorithm described in the pdf.
	 */
	public String encode(String msg) {
		String msgTemp = msg.replaceAll("[^a-zA-Z]", "");; //removes all non letters
		//msgTemp = msgTemp.replaceAll(" ",""); //remove all spaces
		msgTemp = msgTemp.toUpperCase();

		int [] keyStream = getKeystream(msgTemp.length());
		String newMessage = "";

		for (int i=0;i<keyStream.length; i++){
			newMessage+= (char) (((msgTemp.charAt(i)-'A')+keyStream[i]) % 26 + 65);
		}

		return newMessage;
	}
	
	/* 
	 * TODO: Decodes the input message using the algorithm described in the pdf.
	 */
	public String decode(String msg) {

		int [] keyStream = getKeystream(msg.length());

		for (int i=0;i<keyStream.length;i++){
			System.out.println(keyStream[i]);
		}

		String oldMessage = "";

		for (int i=0;i<keyStream.length; i++){
			oldMessage += (char) ((((msg.charAt(i)-'A')-keyStream[i]) + 52 ) % 26 + 65);
		}

		return oldMessage;
	}
	
}
