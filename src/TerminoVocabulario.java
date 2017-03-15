
public class TerminoVocabulario {
	private int[] numOcurrencias;
	private double[] condprob;
	//Categoria.values().length es el tamaño del array
	
	public TerminoVocabulario(){
		numOcurrencias = new int[Categoria.values().length];
		condprob = new double[Categoria.values().length];
	}
	//métodos GET
	
	public int getNumOcurrencias(Categoria c){
		if(c==Categoria.HAM)
			return numOcurrencias[0];
		else
			return numOcurrencias[1];
	}
	
	public double getCondProb(Categoria c){
		if(c==Categoria.HAM)
			return condprob[0];
		else
			return condprob[1];
	}
	
	//métodos SET
	
	public void setNumOcurrencias(int ocurrencias, Categoria c){
		if(c==Categoria.HAM)
			this.numOcurrencias[0] = ocurrencias;
		else
			this.numOcurrencias[1] = ocurrencias;
		
	}
	
	public void setCondProb(double value, Categoria c){
		if(c==Categoria.HAM)
			this.condprob[0] = value;
		else
			this.condprob[1] = value;
		
	}
}
