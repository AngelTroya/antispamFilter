import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class ConjuntoEntrenamiento {
	private String ruta;
	private int numDocumentosTotales;
	private ArrayList<File> hams;
	private ArrayList<File> spams;
	private double[] prior;
	private Vocabulario vocabulario;
	private static final double porcentaje = 0.75;
	private File[] filesUnificados;
	//hams y spams del conjunto de evaluación aleatorio
	private ArrayList<File>hamsEv;
	private ArrayList<File>spamsEv;
	private String[] texto_c;

	// constructor sin documentos
	public ConjuntoEntrenamiento(String ruta) {
		this.setRuta(ruta);
		this.setnumDocumentosTotales(0);
		hams = new ArrayList<File>();
		spams = new ArrayList<File>();
		hamsEv = new ArrayList<File>();
		spamsEv = new ArrayList<File>();
		texto_c = new String[2];
		prior = new double[2];
				
	}

	// métodos set y get de ruta
	public String getRuta() {
		return this.ruta;
	}

	public ArrayList<File> getHams() {
		return hams;
	}

	public ArrayList<File> getSpams() {
		return spams;
	}

	public double getPrior(Categoria c) {
		if (c == Categoria.HAM) {
			return prior[0];
		} else {
			return prior[1];
		}
	}

	public int getnumDocumentosTotales() {
		return this.numDocumentosTotales;
	}

	public Vocabulario getVocabulario() {
		return this.vocabulario;
	}
	
	public ArrayList<File> getHamsEv(){
		return hamsEv;
	}
	public ArrayList<File> getSpamsEv(){
		return spamsEv;
	}
	
	//TODO
	public String getFilesUnificados(Categoria c){
		if(c == Categoria.HAM){
			//obtengo el texto el documento unificado HAM
			return this.texto_c[0];
		}
		else{
			//obtengo el texto del documento unificado SPAM
			return this.texto_c[1];
		}
	}

	public void setRuta(String r) {
		this.ruta = r;
	}

	public void setnumDocumentosTotales(int i) {
		this.numDocumentosTotales = i;
	}

	// actualiza el número de documentos totales del conjunto de entrenamiento
	// se debe usar tras añadir los documentos al conjunto de entrenamiento
	public void setNumDocumentosTotales() {
		this.numDocumentosTotales = spams.size() + hams.size();
	}

	// calculamos el prior en base al número de documentos de una
	// categoría(entrada)
	public double calculaPrior(int numDocs, Categoria c) {
		if (c == Categoria.HAM) {
			this.prior[0] = ((double)numDocs) / numDocumentosTotales;
			return prior[0];
		} else {
			this.prior[1] = ((double)numDocs) / numDocumentosTotales;
			return prior[1];
		}
	}

	public void obtieneDocumentosEnron15() {
		String ruta_interna;
		String ruta_ham;
		String ruta_spam;
		File dir_ham;
		File dir_spam;
		File[] ficheros_ham;
		File[] ficheros_spam;
		// analizar desde enron1 a enron5
		for (int i = 1; i <= 5; i++) {
			ruta_interna = ruta + "/enron" + i;
			ruta_ham = ruta_interna + "/ham/";
			ruta_spam = ruta_interna + "/spam/";
			// Directorios ham y spam de cada carpeta enron
			dir_ham = new File(ruta_ham);
			dir_spam = new File(ruta_spam);
			// meto en un arrayList los ficheros ham
			if (dir_ham.exists()) {
				ficheros_ham = dir_ham.listFiles();
				for(File f:ficheros_ham){
					this.hams.add(f);
				}
			}
			if (dir_spam.exists()) {
				ficheros_spam = dir_spam.listFiles();
				for(File f:ficheros_spam){
					this.spams.add(f);
				}
			}
		}
		//añado los documentos al conjunto de evaluación
		ruta_interna = ruta + "/enron6";
		ruta_ham = ruta_interna + "/ham/";
		ruta_spam = ruta_interna + "/spam/";
		dir_ham = new File(ruta_ham);
		dir_spam = new File(ruta_spam);
		// meto en un arrayList los ficheros ham
		if (dir_ham.exists()) {
			ficheros_ham = dir_ham.listFiles();
			for(File f:ficheros_ham){
				this.hamsEv.add(f);
			}
		}
		if (dir_spam.exists()) {
			ficheros_spam = dir_spam.listFiles();
			for(File f:ficheros_spam){
				this.spamsEv.add(f);
			}
		}
		
		setNumDocumentosTotales();
	}

	// TODO debe crear también un conjunto de evaluación asociado con los documentos que no son seleccionados
	public void obtieneDocumentosEnronAleatorio() {
		String ruta_interna;
		String ruta_ham;
		String ruta_spam;
		File[] ficheros_ham;
		File[] ficheros_spam;
		int numDocumentosEH, numDocumentosES;
		//ConjuntoEvaluacion cev = new ConjuntoEvaluacion();
		int rem;

		// añadimos a hams y spams el total de documentos
		for (int i = 1; i <= 6; i++) {
			ruta_interna = ruta + "/enron" + i;
			ruta_ham = ruta_interna + "/ham/";
			ruta_spam = ruta_interna + "/spam/";
			// Directorios ham y spam de cada carpeta enron
			File dir_ham;
			File dir_spam;
			dir_ham = new File(ruta_ham);
			dir_spam = new File(ruta_spam);
			//añado a hams y spams todos los documentos y a posteriori eliminaremos al azar el número de hams y spams
			//que formarán parte del conjunto de evaluación
			if (dir_ham.exists()) {
				ficheros_ham = dir_ham.listFiles();
				for(File f:ficheros_ham){
					this.hams.add(f);
				}
			}
			if (dir_spam.exists()) {
				ficheros_spam = dir_spam.listFiles();
				for(File f:ficheros_spam){
					this.spams.add(f);
				}
			}
			setNumDocumentosTotales();

		}
		// calculamos el # de documentos que debe tener el conjunto de
		// evaluación
		numDocumentosEH = (int) (getHams().size() * (1 - ConjuntoEntrenamiento.porcentaje));
		numDocumentosES = (int) (getSpams().size() * (1 - ConjuntoEntrenamiento.porcentaje));
		// elimino ese numero de elementos aleatorios de spam y ham y los
		// meto en evaluación
		while(numDocumentosEH>0){
			//selecciono un elemento aleatorio a eliminar del conjunto de entrenamiento
			//y lo añado en el de evaluación
			rem = (int)Math.random()*numDocumentosEH+1;
			hamsEv.add(this.hams.remove(rem));
			numDocumentosEH--;
		}
		while(numDocumentosES>0){
			//selecciono un elemento aleatorio a eliminar del conjunto de entrenamiento
			//y lo añado en el de evaluación
			rem = (int)Math.random()*numDocumentosES+1;
			spamsEv.add(this.spams.remove(rem));
			numDocumentosES--;
		}
	}

	// separa las palabras de un String
	public String[] separaPalabras(String s) {
		String delimitadores = "[ .,\'\"\\[\\]]+";
		String[] palabrasSeparadas = s.split(delimitadores);
		return palabrasSeparadas;
	}

	// extrae el vocabulario del conjunto de entrenamiento
	public void extraeVocabulario() throws FileNotFoundException, IOException {
		String cadena;
		String[] palabras;
		vocabulario = new Vocabulario();
		// creo los archivos HAM y SPAM concatenados
		this.filesUnificados = concatenaDocumentos();
		// recorro ambos archivos línea a línea
		for (int i = 0; i < filesUnificados.length; i++) {
			File file = filesUnificados[i];
			FileReader f = new FileReader(file);
			BufferedReader b = new BufferedReader(f);
			if (i == 0) {// si es el vocabulario de HAM
				// por cada línea que leo
				while ((cadena = b.readLine()) != null) {
					palabras = separaPalabras(cadena);
					// por cada palabra
					for (String palabra : palabras) {
						// si la palabra no está en el vocabulario
						if (!(vocabulario.getVocabulario().containsKey(palabra))) {
							TerminoVocabulario tv = new TerminoVocabulario();
							tv.setNumOcurrencias(1, Categoria.HAM);
							tv.setCondProb(2.0, Categoria.HAM);// tb debo añadir
																// condprob
							vocabulario.getVocabulario().put(palabra, tv);
						} else {
							// si la palabra ya existe
							TerminoVocabulario tv = (TerminoVocabulario) vocabulario.getVocabulario().get(palabra);
							// actualizo numOcurrencias y condprob
							int numOcurrenciasHam = tv.getNumOcurrencias(Categoria.HAM);
							int numOcurrenciasSpam = tv.getNumOcurrencias(Categoria.SPAM);
							numOcurrenciasHam++;
							double numer = (((double) numOcurrenciasHam)+(double)1.0);
							double denom = ((double)(numOcurrenciasHam + numOcurrenciasSpam + 1.0));
							double condprob = numer / (double)denom;
											
							tv.setNumOcurrencias(numOcurrenciasHam, Categoria.HAM);
							tv.setCondProb(condprob, Categoria.HAM);
							// Actualizo el TerminoVocabulario del Map con el
							// que se acaba de calcular
							vocabulario.getVocabulario().put(palabra, tv);
						}
					}
				}
			} else {// si es el vocabulario de SPAM
					// por cada línea que leo
				while ((cadena = b.readLine()) != null) {
					palabras = separaPalabras(cadena);
					// por cada palabra
					for (String palabra : palabras) {
						// si la palabra no está en el vocabulario
						if (!(vocabulario.getVocabulario().containsKey(palabra))) {
							TerminoVocabulario tv = new TerminoVocabulario();
							tv.setNumOcurrencias(1, Categoria.SPAM);
							tv.setCondProb(2.0, Categoria.SPAM);// tb debo
																// añadir
																// condprob
							vocabulario.getVocabulario().put(palabra, tv);
						} else {
							// si la palabra ya existe
							TerminoVocabulario tv = (TerminoVocabulario) vocabulario.getVocabulario().get(palabra);
							// actualizo numOcurrencias y condprob
							int numOcurrenciasHam = tv.getNumOcurrencias(Categoria.HAM);
							int numOcurrenciasSpam = tv.getNumOcurrencias(Categoria.SPAM);
							numOcurrenciasHam++;
							double numer = (((double) numOcurrenciasSpam)+(double)1.0);
							double denom = ((double)(numOcurrenciasHam + numOcurrenciasSpam + 1.0));
							double condprob = numer/(double)denom;	
							
							tv.setNumOcurrencias(numOcurrenciasSpam, Categoria.SPAM);
							tv.setCondProb(condprob, Categoria.SPAM);
							//Actualizo condprob de HAM con el nuevo valor
							numer = (((double) tv.getCondProb(Categoria.HAM))+(double)1.0);
							condprob = numer/(double)denom;
							tv.setCondProb(condprob, Categoria.HAM);
							// Actualizo el TerminoVocabulario del Map con el
							// que se acaba de calcular
							vocabulario.getVocabulario().put(palabra, tv);
						}
					}
				}
			}
			b.close();
		}
	}

	// devuelve el número de documentos de la Categoría pasada por parámetro(HAM
	// o SPAM)
	public int numDocumentosCategoria(Categoria c) {
		if (c == Categoria.HAM)
			return hams.size();
		else
			return spams.size();
	}
	
	public File[] concatenaDocumentos(){
		String ruta_aux= "generados"+File.separator+"hamsunificados.txt"; // Aqui se le asigna el nombre y 
		String cadena;
		File[] files = new File[Categoria.values().length];
		FileWriter fw = null;	// la extension al archivo
		File fexist = new File("generados"+File.separator+"hamsunificados.txt"); 
		try {
			//si el fichero no existe lo creo
			if(!fexist.exists()){
				fw = new FileWriter(ruta_aux);
				BufferedWriter bw = new BufferedWriter(fw); 
				PrintWriter salArch = new PrintWriter(bw); 
				int i =0;
				System.out.println(i);
				//añado el contenido del archivo
				// recorro todos los ficheros hams
				for (File doc : this.hams) {
					FileReader f = new FileReader(doc);
					BufferedReader b = new BufferedReader(f);
					i++;
					System.out.println(i);
					// cada linea de cada fichero la añado al fichero único
					while ((cadena = b.readLine()) != null) {
						// realizo la escritura de los archivos HAM en uno nuevo
						salArch.println(cadena);
						this.texto_c[0] = this.texto_c[0] + cadena;
					}
					b.close();
					f.close();
					
				}
				salArch.close();
				files[0]=new File("generados"+File.separator+"hamsunificados.txt");
			}else{
				files[0]=new File("generados"+File.separator+"hamsunificados.txt");
			}
			
		}
		catch (IOException ex) { 
		}
		//hago lo mismo para los SPAM
		this.texto_c[1]="";
		ruta_aux= "generados"+File.separator+"spamsunificados.txt";
		fexist = new File("generados"+File.separator+"spamsunificados.txt");
		try { 
			if(!fexist.exists()){
				fw = new FileWriter(ruta_aux);
				BufferedWriter bw = new BufferedWriter(fw); 
				PrintWriter salArch = new PrintWriter(bw); 
				int i =0;
				System.out.println(i);
				//añado el contenido del archivo
				// recorro todos los ficheros hams
				for (File doc : this.spams) {
					FileReader f = new FileReader(doc);
					BufferedReader b = new BufferedReader(f);
					i++;
					System.out.println(i);
					// cada linea de cada fichero la añado al fichero único
					while ((cadena = b.readLine()) != null) {
						// realizo la escritura de los archivos HAM en uno nuevo
						salArch.println(cadena);
						this.texto_c[1] = this.texto_c[1] + cadena;
					}
					b.close();
					f.close();					
				}
				salArch.close();
				files[1]=new File("generados"+File.separator+"spamsunificados.txt");
			}
			else{
				files[1]=new File("generados"+File.separator+"spamsunificados.txt");
			}		
		}
		catch (IOException ex) { 
		}
		return files;
	}
}
