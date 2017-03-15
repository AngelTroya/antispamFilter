import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class ConjuntoEvaluacion {
	private int numDocumentosTotales;
	private ArrayList<File> hams;
	private ArrayList<File> spams;
	private String ruta;

	public ConjuntoEvaluacion() {
		super();
	}

	// GET
	public int getnumDocumentosTotales() {
		return numDocumentosTotales;
	}

	public ArrayList<File> getHams() {
		return hams;
	}

	public ArrayList<File> getSpams() {
		return hams;
	}
	
	public void setnumDocumentosTotales(int i) {
		this.numDocumentosTotales = i;
	}

	// actualiza el número de documentos totales del conjunto de evaluación
	// se debe usar tras añadir los documentos al conjunto de evaluación
	public void setNumDocumentosTotales() {
		this.numDocumentosTotales = spams.size() + hams.size();
	}

	public void obtieneDocumentosEnron6() {
		String ruta_interna;
		String ruta_ham;
		String ruta_spam;
		File dir_ham;
		File dir_spam;
		File[] ficheros_ham;
		File[] ficheros_spam;
		// analizar de enron6
			ruta_interna = ruta + "/enron6";
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
		setNumDocumentosTotales();
	}
	
	// devuelve dos documentos, uno con los SPAM y otro con los HAM
	public File[] concatenaDocumentos() throws FileNotFoundException, IOException {
		String cadena, ruta_aux;
		// lista_doc = new ArrayList<File>();
		ruta_aux = "../generados/hamsunificadoseval.txt";
		File hamsunificados = new File(ruta_aux);
		File[] files = new File[Categoria.values().length];
		BufferedWriter bw;
		// si se ha creado bien el fichero para unificar los demás
		if (hamsunificados.exists()) {
			bw = new BufferedWriter(new FileWriter(hamsunificados));
			// recorro todos los ficheros hams
			for (File doc : this.hams) {
				FileReader f = new FileReader(doc);
				BufferedReader b = new BufferedReader(f);
				// cada linea de cada fichero la añado al fichero único
				while ((cadena = b.readLine()) != null) {
					// realizo la escritura de los archivos HAM en uno nuevo
					bw.write(cadena);
				}
				bw.write("\n");
				b.close();
				f.close();
			}
			files[0] = hamsunificados;
		}
		// hacer los mismo para los SPAM
		ruta_aux = "../generados/spamsunificadoseval.txt";
		File spamsunificados = new File(ruta_aux);
		if (spamsunificados.exists()) {
			bw = new BufferedWriter(new FileWriter(spamsunificados));
			// recorro todos los ficheros hams
			for (File doc : this.spams) {
				FileReader f = new FileReader(doc);
				BufferedReader b = new BufferedReader(f);
				// cada linea de cada fichero la añado al fichero único
				while ((cadena = b.readLine()) != null) {
					// realizo la escritura de los archivos SPAM en uno nuevo
					bw.write(cadena);
				}
				bw.write("\n");
				b.close();
				f.close();
			}
			files[1] = spamsunificados;
		}
		return files;
	}
	
	public void obtieneDocumentosEvalAleatorio(ConjuntoEntrenamiento ce){
		//obtiene la variable del conjunto de entrenamiento que los guarda
		this.spams = ce.getSpamsEv();
		this.hams = ce.getHamsEv();
		
	}
}
