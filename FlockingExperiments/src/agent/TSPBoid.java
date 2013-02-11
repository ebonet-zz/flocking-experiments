package agent;

import graph.Edge;
import graph.Position;

import java.util.ArrayList;
import java.util.List;

import controller.Environment;

public class TSPBoid extends Boid {

	public TSPBoid(Position position, Double speed, Double visionRange,
			Double distanceChoiceWeight, Double occupancyChoiceWeight,
			Environment enviroment, GoalEvaluator goalEvaluator) {
		super(position, speed, visionRange, distanceChoiceWeight,
				occupancyChoiceWeight, enviroment, goalEvaluator);
	}

	@Override
	protected List<Edge> generatePossibleEdges() {
		if (this.pathTaken.size() < this.getGraph().getNumberOfNodes()) {
			return super.generatePossibleEdges();
		}
		ArrayList<Integer> closestNeighbors = getGraph().getClosestNeighborsSortedByDistance(this.pos.edge.getTo());
		ArrayList<Integer> result = new ArrayList<Integer>();
		Integer startNode = this.pathTaken.firstLocation();
		if (closestNeighbors.contains(startNode)) {
			result.add(startNode);
		}
		List<Edge> possibleEdges = generateEdges(result);
		return possibleEdges;
	}

}
