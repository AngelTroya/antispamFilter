import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

public class NaiveBayes {
	private Categoria cat;
	
	public void entrenaNB(Categoria[] c, ConjuntoEntrenamiento d) throws FileNotFoundException, IOException, ArchivoExistenteException{
		Map<String, TerminoVocabulario> vocabulario;
		String ruta = "generados"+File.separator+"entrenaNB.txt";
		String key;
		double[] prior = new double[2];
		TerminoVocabulario value;
		//Extraigo el vocabulario a partir de los documentos anteriores
		d.extraeVocabulario();
		//creo el archivo de salida
		File archivo = new File(ruta);
		//si el archivo de salida existe evolveremos error
		if(archivo.exists()) {
			throw new ArchivoExistenteException("El archivo de salida ya existe, "
					+ "debe eliminarlo primero");
		}
		//si el archivo de salida no existe continuaremos la ejecución normal
		else{
			Writer write = null; 
	        try {
	        	for(int i=0;i<c.length;i++){
					cat=c[i];
					int numDocs = d.numDocumentosCategoria(cat);
					prior[i] = d.calculaPrior(numDocs, cat);
					//obtengo los archivos unificados hallados 
					//con anterioridad en el vocabulario
					vocabulario = d.getVocabulario().getVocabulario();
					write = new BufferedWriter(new OutputStreamWriter(
	                    new FileOutputStream(ruta),"UTF8"));
					write.write("Matriz condprob"
	                    +System.getProperty("line.separator"));
					
					write.write("Termino \t\t\t\t Ham \t\t\t\t\t Spam \n");
					for (Entry<String, TerminoVocabulario> e: vocabulario.entrySet()) {
						//DEBO EXTRAER LA VARIABLE CONDPROB DE CADA ENTRY e
						key = e.getKey();
						value = e.getValue();		
						write.write(key + " :\t\t\t\t\t" 
						+ value.getCondProb(Categoria.HAM) 
						+ "\t\t\t\t" + value.getCondProb(Categoria.SPAM) 
						+ "\n");
					}
	        	}
				write.write("\nMatriz prior:\n");
				write.write("\t\t\tHam\t\t\t\t\t\tSpam\n");
				write.write("Num_docs\t" + prior[0]+"\t"+ prior[1]);
	        }
	        catch(Exception e){
	         e.printStackTrace();
	        }
	         
	        finally{
	         write.close();
	        }
		}
	}
	
	//clasifica el conjunto de evaluación 
	public void evaluaNB(Categoria[] c, ConjuntoEntrenamiento d) throws IOException{
		ArrayList<File> hamsEv = d.getHamsEv(); 
		ArrayList<File> spamsEv = d.getSpamsEv();
		Categoria cat;
		double total,totalh,totals;
		//0:Ham bien clasificado,1:Ham mal clasificado, 
		//2:Spam mal clasificado, 3:Spam bien clasificado
		double[] result = {0,0,0,0};
		//para cada documento ham del conjunto de evaluación
		for(File f: hamsEv){
			cat = clasificaNB(f,d);
			if(cat ==Categoria.HAM){
				result[0]++;
			}
			else{
				result[1]++;
			}
		}
		//para cada documento spam del conjunto de evaluación
		for(File f: spamsEv){
			cat = clasificaNB(f,d);
			if(cat ==Categoria.HAM){
				result[2]++;
			}
			else{
				result[3]++;
			}
		}
		String ruta= "generados"+File.separator+"evaluacion.txt";
		FileWriter fw = null;// la extension al archivo 
		try { 
		fw = new FileWriter(ruta); 
		BufferedWriter bw = new BufferedWriter(fw); 
		PrintWriter salArch = new PrintWriter(bw); 
		//total = result[0]+result[1]+result[2]+result[3];
		totalh = result[0]+result[1];
		totals = result[2]+result[3];
		salArch.print("Correctos clasificados correctamente (Ham -> Ham):"
		+ (result[0]*100)/totalh); 
		salArch.println(); 
		salArch.print("Correctos clasificados incorrectamente (Ham -> Spam:)"
		+ (result[1]*100)/totalh); 
		salArch.println(); 
		salArch.print("Incorrectos clasificados correctamente (Spam -> Ham:)"
		+ (result[2]*100)/totals); 
		salArch.println();
		salArch.print("Incorrectos clasificados incorrectamente (Spam -> Spam:)"
		+ (result[3]*100)/totals); 
		salArch.println();
		salArch.close(); 
		} 
		catch (IOException ex) { 
		} 
	}
	
	//clasifica un documento como HAM o SPAM
	public Categoria clasificaNB(File f, ConjuntoEntrenamiento d) throws IOException{
		Categoria c;
		String cadena;
		String[] palabras;
		double[] score = new double[2];
		score[0] = Math.log(d.getPrior(Categoria.HAM));
		score[1] = Math.log(d.getPrior(Categoria.SPAM));
		//separo documento en array de palabras
		FileReader fr = new FileReader(f);
		BufferedReader b = new BufferedReader(fr);
		// por cada línea que leo del fichero
		while ((cadena = b.readLine()) != null) {
				palabras = d.separaPalabras(cadena);
				//por cada palabra debo añadir condprob a HAM y SPAM en 
				//el caso de que existan.
				for(String palabra : palabras){
					//si la palabra existe en el conjunto de entrenamiento
					if(d.getVocabulario().getVocabulario().containsKey(palabra)){
						score[0]+=d.getVocabulario().getVocabulario().get(palabra).getCondProb(Categoria.HAM);
						score[1]+=d.getVocabulario().getVocabulario().get(palabra).getCondProb(Categoria.SPAM);
					}
				}
		}//termina la evaluación del documento
		b.close();
		if(score[0]>score[1]){
			c = Categoria.HAM;
		}
		else{
			c = Categoria.SPAM;
		}
		return c;
	}
}
