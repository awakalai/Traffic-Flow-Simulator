
package trafficflowsimulator;

import java.util.*;

public class TrafficFlowSimulator {
    static final int V = 6; // ژمارەی خاڵەکان (چوارڕیانەکان)

    // ئەم بەشە تەنها دەپشکنێت ئایا ڕێگایەک ماوە لە نێوان سەرەتا و کۆتایی؟
    boolean hasPath(int rGraph[][], int s, int t, int parent[]) {
        boolean visited[] = new boolean[V];
        LinkedList<Integer> queue = new LinkedList<>();
        queue.add(s);
        visited[s] = true;

        while (!queue.isEmpty()) {
            int u = queue.poll();
            for (int v = 0; v < V; v++) {
                // ئەگەر خاڵەکە نەبینرابوو و جێگەی ترافیکی مابوو
                if (!visited[v] && rGraph[u][v] > 0) {
                    queue.add(v);
                    parent[v] = u;
                    visited[v] = true;
                }
            }
        }
        return visited[t]; // ئەگەر گەیشتینە خاڵی کۆتایی (t) واتە ڕێگا هەیە
    }

    int findMaxFlow(int graph[][], int source, int sink) {
        int rGraph[][] = new int[V][V]; // کۆپییەکی گرافەکە بۆ دەستکاریکردن
        for (int i = 0; i < V; i++) {
            for (int j = 0; j < V; j++) {
                rGraph[i][j] = graph[i][j];
            }
        }

        int parent[] = new int[V]; 
        int totalFlow = 0;

        // تا ڕێگایەک مابێت، ترافیکی تێدا دەنێرین
        while (hasPath(rGraph, source, sink, parent)) {
            int pathFlow = Integer.MAX_VALUE;

            // ١. دۆزینەوەی کەمترین شوێن (Bottleneck) لەو ڕێگایەی دۆزراوەتەوە
            for (int v = sink; v != source; v = parent[v]) {
                int u = parent[v];
                pathFlow = Math.min(pathFlow, rGraph[u][v]);
            }

            // ٢. کەمکردنەوەی ئەو بڕە لە توانای شەقامەکان
            for (int v = sink; v != source; v = parent[v]) {
                int u = parent[v];
                rGraph[u][v] -= pathFlow;
                rGraph[v][u] += pathFlow;
            }

            totalFlow += pathFlow; // زیادکردنی بۆ کۆی گشتی
        }
        return totalFlow;
    }

    public static void main(String[] args) {
        int trafficNetwork[][] = new int[][] {
            {0, 10, 10, 0, 0, 0},
            {0, 0, 2, 4, 8, 0},
            {0, 0, 0, 0, 9, 0},
            {0, 0, 0, 0, 0, 10},
            {0, 0, 0, 6, 0, 10},
            {0, 0, 0, 0, 0, 0}
        };

        TrafficFlowSimulator simulator = new TrafficFlowSimulator();
        int result = simulator.findMaxFlow(trafficNetwork, 0, 5);
        System.out.println("Zortrin Trafic: " + result);
    }
}

