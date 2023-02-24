package fr.irit.elipse.keyboardsimulator;
import java.util.ArrayList;
import java.util.List;

public class TextUtils {
	public static Character getLetter(Character c) {
		int cread = c.charValue();
		if(Character.isLetter((char)cread))
		{
			cread = Character.toLowerCase((char)cread);
			if(cread == 'à') cread = 'a';
			if(cread == 'â') cread = 'a';
			if(cread == 'ä') cread = 'a';
			if(cread == 225) cread = 'a';
			if(cread == 227) cread = 'a';
			if(cread == 230) cread = 'a';
			if(cread == 170) cread = ' ';
			
			if(cread == 'â') cread = 'a';
			if(cread == 'ç') cread = 'c';
			if(cread == 'î') cread = 'i';
			if(cread == 'ï') cread = 'i';
			if(cread == 237) cread = 'i';
			if(cread == 'è') cread = 'e';
			if(cread == 'ê') cread = 'e';
			if(cread == 'é') cread = 'e';
			if(cread == 'ë') cread = 'e';
			if(cread == 'ô') cread = 'o';
			if(cread == 243) cread = 'o';
			if(cread == 246) cread = 'o';
			if(cread == 339) cread = 'o'; // e dans le o
			if(cread == 'û') cread = 'u';
			if(cread == 'ù') cread = 'u';
			if(cread == 252) cread = 'u';
		}else
			cread = ' ';
		return new Character((char)cread);
	}
	
	public static List<Character> getCharList(List<Character> list){
		ArrayList<Character> newList = new ArrayList<Character>();
		
		for(Character c:list) {
			Character newChar = getLetter(c);
			if(!newList.contains(newChar))
				newList.add(newChar);
		}
		return newList;
	}
}
