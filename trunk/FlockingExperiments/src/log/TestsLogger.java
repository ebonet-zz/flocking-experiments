package log;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class TestsLogger {

	private static Logger logger = createLogger();
	
	private static Logger createLogger() {
		Logger logger = Logger.getLogger("TestsLogger");
		FileHandler fh;

		try {

			// This block configure the logger with handler and formatter
			fh = new FileHandler("logs/TestsLogFile.log");
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
	
	public static void logSpeedTest(double speed, double successPercentage, double averagePathLength) {
		logger.info("For speed = " + speed + " " + successPercentage + "% of the problems had a solution\n" +
				"For speed = " + speed + " the average path was " + averagePathLength +'\n');
	}

	public static void logMessage(String message) {
		logger.info(message);		
	}
}
