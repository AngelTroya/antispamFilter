import java.util.HashMap;
import java.util.Map;

public class Vocabulario {
	private Map<String, TerminoVocabulario> vocabulario;
	
	public Vocabulario(){
		vocabulario = new HashMap<String, TerminoVocabulario>();
	}
	
	public Map<String, TerminoVocabulario> getVocabulario(){
		return vocabulario;
	}
}
