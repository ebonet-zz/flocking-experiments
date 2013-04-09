package log;

import graph.Tour;

import java.io.IOException;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import util.SortableKeyValue;

public class GraphLogger {

	private static Logger logger = createLogger();
	
	private static Logger createLogger() {
		Logger logger = Logger.getLogger("GraphLogger");
		FileHandler fh;

		try {

			// This block configure the logger with handler and formatter
			fh = new FileHandler("logs/FlockingLogFile.log");
			logger.addHandler(fh);
			//logger.setLevel(Level.ALL);
			SimpleFormatter formatter = new SimpleFormatter();
			fh.setFormatter(formatter);

		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return logger;

	}

	public static void logFlocksMap(List<SortableKeyValue<Tour, Integer>> pathsList) {
		logger.info("Tours registered:");
		for (SortableKeyValue<Tour, Integer> tour : pathsList) {
			logger.info(tour.keyObject.toString() + "\nDensity: " + tour.valueToUseOnSorting);
		}
	}
	
	public static void logConverged(boolean converged, int iterations) {
		if(converged)
			logger.info("Optimal tour found in " + iterations + " iterations.");		
		else
			logger.info("Optimal tour could not be found in " + iterations + " iterations.");
	}
	
	public static void logMessage(String message) {
		logger.info(message);
	}

	public static void logBoidSpawnSkipped() {
		logger.info("Skept a boid creation because initial paths are too crowded.\n" +
				"This indicates a possible 'clogged graph' deadlock.\n" +
				"Consider using less agents for this problem config.\n");		
	}
	
}