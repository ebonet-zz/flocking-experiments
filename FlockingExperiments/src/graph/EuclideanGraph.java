package graph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class EuclideanGraph extends TraditionalGraph {

	private static final String DIMENSION_STRING = "DIMENSION";
	
	private double[][] nodeCoords;
	
	private EuclideanGraph(int numberOfNodes) {
		super(numberOfNodes);
		nodeCoords = new double[numberOfNodes][2];
	}

	public static EuclideanGraph loadGraph(File graphData) throws IOException {
		BufferedReader reader = null;
		try {
			FileReader fileReader = new FileReader(graphData);
			reader = new BufferedReader(fileReader);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}

		int numberOfCities = readGraphDimensions(reader);
		EuclideanGraph graph = new EuclideanGraph(numberOfCities);
		
		// go through the rest of the header
		String line = reader.readLine();
		while(!line.trim().startsWith("1 ")) {
			line = reader.readLine();
		}
		
		int currentCity = 0;
		while(!line.equals("EOF") && currentCity < numberOfCities) {
			line = line.trim();
			int firstCoordIdx = line.indexOf(' ') + 1;
			int secondCoordIdx = line.lastIndexOf(' ') + 1;
			double cityX = Double.parseDouble(line.substring(firstCoordIdx, secondCoordIdx-1));
			double cityY = Double.parseDouble(line.substring(secondCoordIdx));
			graph.addCity(currentCity, cityX, cityY);
			
			line = reader.readLine();
			currentCity++;
		}
		
		return graph;
	}

	private void addCity(int cityId, double cityX, double cityY) {
		nodeCoords[cityId][0] = cityX;
		nodeCoords[cityId][1] = cityY;
		for (int i=0; i<cityId; i++) {
			// add edges from the new city to all the old ones
			int distance = Math.round(((getDistance(nodeCoords[cityId], nodeCoords[i]))));
			this.setEdgeLength(i, cityId, distance);
		}	
	}

	public double[] getCityCoords(int city) {
		return nodeCoords[city];
	}
	
	public double[][] getAllCitiesCoords() {
		return nodeCoords;
	}
	
	private static int readGraphDimensions(BufferedReader reader)
			throws IOException {
		int numberOfCities = 0;
		String line = "";
		while (!line.contains(DIMENSION_STRING)) {
			// keep reading header
			line = reader.readLine();
		}
		
		// read number of cities
		int numberStart = line.lastIndexOf(' ');
		numberOfCities = Integer.parseInt(line.substring(numberStart+1));
		return numberOfCities;
	}
	
	private static float getDistance(double[] coords1, double[] coords2) {
		return (float)Math.sqrt((coords1[0] - coords2[0])*(coords1[0] - coords2[0]) + (coords1[1] - coords2[1])*(coords1[1] - coords2[1])); 
	}
}
