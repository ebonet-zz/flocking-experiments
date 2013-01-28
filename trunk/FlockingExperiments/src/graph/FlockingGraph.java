package graph;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

/**
 * TODO Put here a description of what this class does.
 * 
 * @author Balthazar. Created Jan 25, 2013.
 */
public class FlockingGraph extends TraditionalGraph {
	public static final int DEFAULT_SEGMENT_LENGTH = 1;
	public static final int DEFAULT_SEGMENT_CAPACITY = 3;

	double segmentLength;
	int[][] capacityMatrix; // same value for all edges unless flow problem
	Map<Edge, LinkedList<Segment>> edgeSegments;

	public FlockingGraph(int numberOfNodes) {
		this(numberOfNodes, DEFAULT_SEGMENT_LENGTH, DEFAULT_SEGMENT_CAPACITY);
	}

	/**
	 * Constructor of a graph for our flocking problem
	 * 
	 * @param numberOfNodes
	 * @param segmentLenght
	 *            - The length of each segment in the graph
	 * @param segmentCapacity
	 *            - The maximum number of agents that fit inside one segment
	 */
	public FlockingGraph(int numberOfNodes, double segmentLenght, int segmentCapacity) {
		super(numberOfNodes);
		this.segmentLength = segmentLenght;
		this.edgeSegments = new HashMap<Edge, LinkedList<Segment>>();
		fillAll(segmentCapacity, this.capacityMatrix);
	}

	public Edge getInverse(Edge edge) {
		return new Edge(edge.getTo(), edge.getFrom(), getEdgeLength(edge.getTo(), edge.getFrom()));
	}

	public int getEdgeCapacity(int nodeIndexA, int nodeIndexB) {
		return this.capacityMatrix[nodeIndexA][nodeIndexB];
	}

	public int getEdgeCapacity(Edge edge) {
		return getEdgeCapacity(edge.getFrom(), edge.getTo());
	}

	public int getEdgeCapacity(Position pos) {
		return getEdgeCapacity(pos.edge);
	}

	public int getEdgeCapacity(Segment seg) {
		return getEdgeCapacity(seg.startLocation);
	}

	public Segment getFarthestAvailableSegment(Segment start, Segment limit) {
		LinkedList<Segment> segmentsForEdge = this.edgeSegments.get(limit.startLocation.edge);
		Iterator<Segment> it = segmentsForEdge.descendingIterator();

		Segment currentSeg = it.next();
		while (!currentSeg.equals(limit))
			currentSeg = it.next();

		while (currentSeg.isFull() && !currentSeg.equals(start)) {
			currentSeg = it.next();
		}

		return currentSeg;
	}

	public Segment getSegmentForPosition(Position pos) {
		LinkedList<Segment> segmentsForEdge = this.edgeSegments.get(pos.edge);
		Iterator<Segment> it = segmentsForEdge.iterator();
		if (!it.hasNext()) {
			return null;
		}

		Segment seg = it.next();
		while (!seg.contains(pos)) {
			seg = it.next();
		}

		return seg;
	}

	public void buildAllSegments() {
		for (int from = 0; from < this.numberOfNodes; from++) {
			for (int to = 0; to < this.numberOfNodes; to++) {
				int edgeValue = getEdgeLength(from, to);
				if (edgeValue != INVALID_VALUE) {
					Edge edge = new Edge(from, to, edgeValue);
					buildSegmentsFor(edge);
				}
			}
		}
	}

	public void buildSegmentsFor(Edge edge) {
		if (this.edgeSegments.get(edge) == null) { // segments not yet created
			Edge symmetric = getInverse(edge); // find symmetric segment
			if (getEdgeLength(symmetric.getFrom(), symmetric.getTo()) == edge.getLength()) { // Both ways have same
																								// length
				// Same length for the reverse edge
				LinkedList<Segment> symmetricSegments = this.edgeSegments.get(symmetric);
				if (symmetricSegments != null) {
					// Reverse edge had segments already created, possible reuse of segments
					LinkedList<Segment> reversedSegments = new LinkedList<Segment>();
					Iterator<Segment> it = symmetricSegments.descendingIterator();
					while (it.hasNext()) {
						reversedSegments.offer(it.next());
					}

					// reuse the segments in the reverse order in this case
					this.edgeSegments.put(edge, reversedSegments);
					return;
				}
			}

			LinkedList<Segment> segmentList = new LinkedList<Segment>();
			Position segmentStart = new Position(edge, 0d);
			Position segmentEnd = new Position(edge, edge.getLength());
			Segment s = new Segment(segmentStart, this.segmentLength, this.getEdgeCapacity(edge));

			do {
				segmentList.offer(s);
				s = new Segment(s.exclusiveEndLocation, this.segmentLength, this.getEdgeCapacity(edge));
			} while (!s.contains(segmentEnd));

			this.edgeSegments.put(edge, segmentList);
		}
	}
}
