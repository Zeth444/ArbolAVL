// Importa la clase Scanner para lectura de datos desde la consola
import java.util.Scanner;

// Clase que representa un nodo del árbol AVL
class NodoAVL {
    int valor;               // Valor almacenado en el nodo
    int altura;              // Altura del nodo (importante para el balanceo)
    NodoAVL izquierdo;       // Referencia al hijo izquierdo
    NodoAVL derecho;         // Referencia al hijo derecho

    // Constructor del nodo
    public NodoAVL(int valor) {
        this.valor = valor;
        this.altura = 1;     // Al crearse, un nodo tiene altura 1
    }
}

// Clase principal que contiene el árbol AVL
public class ArbolAVL {

    private NodoAVL raiz;    // Nodo raíz del árbol AVL

    // Códigos ANSI para colorear la salida en consola
    public static final String ANSI_RESET = "\u001B[0m";     // Reset de color
    public static final String ANSI_YELLOW = "\u001B[33m";   // Color amarillo (raíz)
    public static final String ANSI_GREEN = "\u001B[32m";    // Color verde (otros nodos)
    public static final String ANSI_CYAN = "\u001B[36m";     // Color cian (mensajes de rotación)

    // Método público para insertar un valor al árbol
    public void insertar(int valor) {
        raiz = insertar(raiz, valor);  // Llama al método recursivo
    }

    // Método recursivo de inserción con balanceo
    private NodoAVL insertar(NodoAVL nodo, int valor) {
        if (nodo == null) return new NodoAVL(valor);  // Caso base: insertar nodo nuevo

        // Insertar recursivamente según el valor
        if (valor < nodo.valor)
            nodo.izquierdo = insertar(nodo.izquierdo, valor);
        else if (valor > nodo.valor)
            nodo.derecho = insertar(nodo.derecho, valor);
        else
            return nodo; // No se permiten duplicados

        // Actualiza la altura del nodo actual
        actualizarAltura(nodo);

        // Calcula el factor de balance
        int balance = obtenerFactorBalance(nodo);

        // CASOS DE ROTACIÓN PARA MANTENER EL BALANCE

        // Rotación simple derecha (Izquierda Izquierda)
        if (balance > 1 && valor < nodo.izquierdo.valor) {
            System.out.println(ANSI_CYAN + "Rotación simple derecha (Izquierda Izquierda) en nodo " + nodo.valor + ANSI_RESET);
            return rotarDerecha(nodo);
        }

        // Rotación simple izquierda (Derecha Derecha)
        if (balance < -1 && valor > nodo.derecho.valor) {
            System.out.println(ANSI_CYAN + "Rotación simple izquierda (Derecha Derecha) en nodo " + nodo.valor + ANSI_RESET);
            return rotarIzquierda(nodo);
        }

        // Rotación doble izquierda-derecha (Izquierda Derecha)
        if (balance > 1 && valor > nodo.izquierdo.valor) {
            System.out.println(ANSI_CYAN + "Rotación doble izquierda-derecha (Izquierda Derecha) en nodo " + nodo.valor + ANSI_RESET);
            nodo.izquierdo = rotarIzquierda(nodo.izquierdo);
            return rotarDerecha(nodo);
        }

        // Rotación doble derecha-izquierda (Derecha Izquierda)
        if (balance < -1 && valor < nodo.derecho.valor) {
            System.out.println(ANSI_CYAN + "Rotación doble derecha-izquierda (Derecha Izquierda) en nodo " + nodo.valor + ANSI_RESET);
            nodo.derecho = rotarDerecha(nodo.derecho);
            return rotarIzquierda(nodo);
        }

        return nodo;  // Si no requiere rotación, retorna el nodo
    }

    // Método que actualiza la altura de un nodo
    private void actualizarAltura(NodoAVL nodo) {
        int altIzq = (nodo.izquierdo != null) ? nodo.izquierdo.altura : 0;
        int altDer = (nodo.derecho != null) ? nodo.derecho.altura : 0;
        nodo.altura = 1 + Math.max(altIzq, altDer);
    }

    // Método que obtiene el factor de balance de un nodo
    private int obtenerFactorBalance(NodoAVL nodo) {
        if (nodo == null) return 0;
        int altIzq = (nodo.izquierdo != null) ? nodo.izquierdo.altura : 0;
        int altDer = (nodo.derecho != null) ? nodo.derecho.altura : 0;
        return altIzq - altDer; // Diferencia entre alturas
    }

    // Rotación simple a la derecha
    private NodoAVL rotarDerecha(NodoAVL y) {
        NodoAVL x = y.izquierdo;
        NodoAVL T2 = x.derecho;

        // Realiza la rotación
        x.derecho = y;
        y.izquierdo = T2;

        // Actualiza alturas
        actualizarAltura(y);
        actualizarAltura(x);

        return x; // Nueva raíz del subárbol
    }

    // Rotación simple a la izquierda
    private NodoAVL rotarIzquierda(NodoAVL x) {
        NodoAVL y = x.derecho;
        NodoAVL T2 = y.izquierdo;

        // Realiza la rotación 
        y.izquierdo = x;
        x.derecho = T2;

        // Actualiza las alturas
        actualizarAltura(x);
        actualizarAltura(y);

        return y; // Nueva raíz del subárbol
    }

    // Método para imprimir el árbol de forma jerárquica (vertical)
    public void imprimirArbol() {
        if (raiz == null) {
            System.out.println("(árbol vacío)");
        } else {
            imprimirJerarquico(raiz, "", true, true); // Inicia desde la raíz
        }
    }

    // Método recursivo para impresión jerárquica con colores
    private void imprimirJerarquico(NodoAVL nodo, String prefijo, boolean esUltimo, boolean esRaiz) {
        if (nodo != null) {
            // Si es la raíz se imprime en amarillo, si no, en verde
            String color = esRaiz ? ANSI_YELLOW : ANSI_GREEN;
            System.out.println(prefijo + (esUltimo ? "└── " : "├── ") + color + nodo.valor + ANSI_RESET);
            // Llama recursivamente a los hijos
            imprimirJerarquico(nodo.izquierdo, prefijo + (esUltimo ? "    " : "│   "), false, false);
            imprimirJerarquico(nodo.derecho, prefijo + (esUltimo ? "    " : "│   "), true, false);
        }
    }

    // Método principal (main) para ejecución del programa
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in); // Scanner para leer entradas
        ArbolAVL arbol = new ArbolAVL();          // Crea un nuevo árbol AVL

        System.out.println("Ingrese números para insertar en el árbol AVL (escriba -1 o 'exit' para terminar):");

        while (true) {
            System.out.print("> ");
            String entrada = scanner.nextLine();

            // Condición de salida
            if (entrada.equalsIgnoreCase("exit") || entrada.equals("-1"))
                break;

            try {
                int numero = Integer.parseInt(entrada); // Convierte entrada a entero
                arbol.insertar(numero);                // Inserta al árbol
                System.out.println("\nÁrbol AVL actualizado:");
                arbol.imprimirArbol();                 // Imprime el árbol
                System.out.println();
            } catch (NumberFormatException e) {
                // Si la entrada no es número, muestra mensaje de error
                System.out.println("Entrada inválida. Por favor ingrese un número entero.");
            }
        }

        System.out.println("Finalizado.");
        scanner.close(); // Cierra el Scanner al finalizar
    }
}