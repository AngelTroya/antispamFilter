import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class Ejecutable {

	public static void main(String[] args) throws FileNotFoundException, IOException, ArchivoExistenteException {
		Scanner sc = new Scanner(System.in);
		String ruta, s;
		NaiveBayes nb;
		ConjuntoEntrenamiento ce;
		//ConjuntoEvaluacion cev = null;
		//creo el array con las dos categorías
		Categoria[] categorias = new Categoria[]{Categoria.HAM, Categoria.SPAM};
		
		System.out.println("Por favor,introduzca la ruta "
				+ "del conjunto de entrenamiento\n");
		ruta = sc.nextLine();
		//supondremos que la ruta se introduce correctamente
		nb = new NaiveBayes();
		ce = new ConjuntoEntrenamiento(ruta);

		//inicio el entrenamiento
		
		System.out.println("Pulse 1 si desea que el conjunto "
				+ "de entrenamiento los formen desde enron1 a enron5\n");
		System.out.println("Pulse 2 si desea que el conjunto"
				+ " de entrenamiento se forme aleatoriamente "
				+ "con el 75% de las muestras");
		s = sc.nextLine();
		
		if(s.equals("1")){
			ce.obtieneDocumentosEnron15();
			ce.concatenaDocumentos();
			nb.entrenaNB(categorias, ce);
			System.out.println("Evaluando emails");
			nb.evaluaNB(categorias, ce);
			System.out.println("Emails evaluados, los resultados"
					+ " se la evaluación se encuentran "
					+ "en la carpeta generados");
		}
		else if(s.equals("2")){
			ce.obtieneDocumentosEnronAleatorio();
			ce.concatenaDocumentos();
			nb.entrenaNB(categorias, ce);
			System.out.println("Evaluando emails");
			nb.evaluaNB(categorias, ce);
			System.out.println("Emails evaluados, los "
					+ "resultados se la evaluación se "
					+ "encuentran en la carpeta generados");
		}
		sc.close();
	}
}
