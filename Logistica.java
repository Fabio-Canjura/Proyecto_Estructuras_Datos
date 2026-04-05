import java.util.*;

public class Logistica {
    private double[][] distancias; // 
    private Producto[] productos;  // 
    private int cantidadNodos;

    public Logistica(List<Producto> listaProductos) {
        this.cantidadNodos = listaProductos.size();
        this.distancias = new double[cantidadNodos][cantidadNodos];
        this.productos = listaProductos.toArray(new Producto[0]);

        
        for (int i = 0; i < cantidadNodos; i++) {
            for (int j = 0; j < cantidadNodos; j++) {
                if (i == j) {
                    distancias[i][j] = 0;
                } else {
                    
                    distancias[i][j] = calcularDistanciaFisica(productos[i], productos[j]);
                }
            }
        }
    }

    private double calcularDistanciaFisica(Producto p1, Producto p2) {
        return Math.sqrt(Math.pow(p2.getCoordX() - p1.getCoordX(), 2) + 
                         Math.pow(p2.getCoordY() - p1.getCoordY(), 2));
    }

    public void calcularRutaOptima(int indiceInicio) {
        double[] dist = new double[cantidadNodos];
        boolean[] visitados = new boolean[cantidadNodos];
        int[] padres = new int[cantidadNodos]; 

        // 1. Inicialización
        Arrays.fill(dist, Double.MAX_VALUE);
        Arrays.fill(visitados, false);
        Arrays.fill(padres, -1);
        dist[indiceInicio] = 0;

        System.out.println("\n--- [ALGORITMO DE DIJKSTRA: CALCULANDO RUTA LOGÍSTICA] ---");

        for (int i = 0; i < cantidadNodos - 1; i++) {
            int u = -1;
            double min = Double.MAX_VALUE;
            for (int v = 0; v < cantidadNodos; v++) {
                if (!visitados[v] && dist[v] <= min) {
                    min = dist[v];
                    u = v;
                }
            }

            if (u == -1) break;
            visitados[u] = true;


            for (int v = 0; v < cantidadNodos; v++) {
                if (!visitados[v] && distancias[u][v] != Double.MAX_VALUE 
                    && dist[u] + distancias[u][v] < dist[v]) {
                    
                    dist[v] = dist[u] + distancias[u][v];
                    padres[v] = u;
                }
            }
        }

        mostrarReporteRutas(dist, padres, indiceInicio);
    }   

    private void mostrarReporteRutas(double[] dist, int[] padres, int inicio) {
        System.out.println("Rutas de reabastecimiento desde: " + productos[inicio].getNombre());
        for (int i = 0; i < cantidadNodos; i++) {
            if (i != inicio) {
                System.out.print("Hacia " + productos[i].getNombre() + ": ");
                System.out.printf("Distancia: %.2f m | ", dist[i]);
                imprimirCamino(i, padres);
                System.out.println();
            }
        }
    }

    private void imprimirCamino(int actual, int[] padres) {
        if (actual == -1) return;
        imprimirCamino(padres[actual], padres);
        System.out.print(productos[actual].getId() + " ");
    }
}