package controller;

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
	public static TraditionalGraph generateGraphFromFile(String filepath) throws IOException {

		BufferedReader reader = null;
		try {
			FileReader fileReader = new FileReader(new File(filepath));
			reader = new BufferedReader(fileReader);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}

		int numberOfCities = 0;
		String line = "";
		while (!line.contains(DIMENSION_STRING)) {
			// keep reading header
			line = reader.readLine();
		}
		
		// read number of cities
		int numberStart = line.lastIndexOf(' ');
		numberOfCities = Integer.parseInt(line.substring(numberStart+1));
		
		// go through the rest of the header
		while(!line.startsWith("1 ")) {
			line = reader.readLine();
		}
		
		TraditionalGraph world = new TraditionalGraph(numberOfCities);
		double[][] citiesCoords = new double[numberOfCities][2];
		
		int currentCity = 0;
		while(!line.equals("EOF") && currentCity < numberOfCities) {
			line = line.trim();
			int firstCoordIdx = line.indexOf(' ') + 1;
			int secondCoordIdx = line.lastIndexOf(' ') + 1;
			citiesCoords[currentCity][0] = Double.parseDouble(line.substring(firstCoordIdx, secondCoordIdx-1));
			citiesCoords[currentCity][1] = Double.parseDouble(line.substring(secondCoordIdx));
			for (int i=0; i<currentCity; i++) {
				// add edges from the new city to all the old ones
				int distance = Math.round(((getDistance(citiesCoords[currentCity], citiesCoords[i]))));
				world.setEdgeLength(i, currentCity, distance);
			}
			
			line = reader.readLine();
			currentCity++;
		}

		return world;
	}
	
	private static float getDistance(double[] coords1, double[] coords2) {
		return (float)Math.sqrt((coords1[0] - coords2[0])*(coords1[0] - coords2[0]) + (coords1[1] - coords2[1])*(coords1[1] - coords2[1])); 
	}
}
