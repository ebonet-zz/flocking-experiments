package controller;

import graph.EuclideanGraph;
import graph.TraditionalGraph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class GraphReaderTSPLIB {

	public static final String EIL_51 = "testdata/eil51.tsp";
	public static final String EIL_76 = "testdata/eil76.tsp";
	public static final String KROA_100 = "testdata/kroA100.tsp";
	public static final String D_198 = "testdata/d198.tsp";
	public static final String LIN_318 = "testdata/lin318.tsp";
	public static final String ATT_532 = "testdata/att532.tsp";
	public static final String RAT_783 = "testdata/rat783.tsp";
	
	private static final String DIMENSION_STRING = "DIMENSION";
	/**
	 * The main method, for testing only.
	 * 
	 * @param args
	 *            the arguments
	 */
	public static void main(String args[]) {

		int numberOfCities = 51;

		TraditionalGraph world = null ;
		try {
			world = generateGraphFromFile(EIL_51);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(world.toString());

		for (int i = 0; i < numberOfCities; i++) {
			Integer[] temp = world.getArrayOfNeighborsOf(i);
			int j = 0;
			while (j < temp.length) {
				System.out.println(i + " " + temp[j] + " " + world.getEdgeLength(i, temp[j]));
				j++;
			}
		}
		
		int[] path = new int[] {1, 22, 8, 26, 31, 28, 3, 36, 35, 20, 2, 29, 21, 16, 50, 34, 30, 9, 49, 10, 39, 33, 45, 15, 44, 42, 40, 19, 41, 13, 25, 14, 24, 43, 7, 23, 48, 6, 27, 51, 46, 12, 47, 18, 4, 17, 37, 5, 38, 11, 32};
		int length = 0;
		for (int i=0; i<path.length-1; i++) {
			length += world.getEdgeLength(path[i]-1, path[i+1]-1);
		}
		System.out.println("Path length: " + length + ", path size: " + path.length);
		System.out.println("Close cycle: " + world.getEdgeLength(path[path.length-1] - 1, path[0]-1) );
				
	}

	/**
	 * Generate graph from file.
	 * 
	 * @param filepath
	 *            path to the file containing graph data
	 * @return the traditional graph
	 * @throws IOException
	 * 				if there are any issues while reading the file
	 */
	public static EuclideanGraph generateGraphFromFile(String filepath) throws IOException {
		File graphFile = new File(filepath);
		EuclideanGraph graph = EuclideanGraph.loadGraph(graphFile);
		return graph;
	}
	
}
