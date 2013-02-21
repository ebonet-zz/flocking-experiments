package graph;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

/**
 * Extension of a TraditionalGraph with all the complex attributes we need to run a flocking algorithm on top of it.
 * 
 * @author Balthazar. Created Jan 25, 2013.
 */
public class FlockingGraph extends TraditionalGraph {

	/** The Constant MINIMUM_DISTANCE_MARGIN. */
	public static final double MINIMUM_DISTANCE_MARGIN = 0.01d;

	/** The Constant DEFAULT_SEGMENT_LENGTH. */
	public static final int DEFAULT_SEGMENT_LENGTH = 1;

	/** The Constant DEFAULT_SEGMENT_CAPACITY. */
	public static final int DEFAULT_SEGMENT_CAPACITY = 3;

	/** The segment length. */
	double segmentLength;

	/** The capacity matrix. */
	int[][] capacityMatrix; // same value for all edges unless flow problem

	/** The edge segments. */
	Map<Edge, LinkedList<Segment>> edgeSegments;

	/**
	 * Constructor of a graph for our flocking problem.
	 * 
	 * @param numberOfNodes
	 *            the number of nodes
	 * @param segmentLenght
	 *            - The length of each segment in the graph
	 * @param segmentCapacity
	 *            - The maximum number of agents that fit inside one segment
	 */
	public FlockingGraph(int numberOfNodes, double segmentLenght, int segmentCapacity) {
		super(numberOfNodes);
		this.segmentLength = segmentLenght;
		this.edgeSegments = new HashMap<Edge, LinkedList<Segment>>();
		this.capacityMatrix = new int[this.numberOfNodes][this.numberOfNodes];
		fillAll(segmentCapacity, this.capacityMatrix);
	}

	/**
	 * Instantiates a new flocking graph.
	 * 
	 * @param tGraph
	 *            the t graph
	 */
	public FlockingGraph(TraditionalGraph tGraph) {
		this(tGraph, DEFAULT_SEGMENT_LENGTH, DEFAULT_SEGMENT_CAPACITY);
	}

	/**
	 * Instantiates a new flocking graph.
	 * 
	 * @param tGraph
	 *            the t graph
	 * @param segmentLenght
	 *            the segment lenght
	 * @param segmentCapacity
	 *            the segment capacity
	 */
	public FlockingGraph(TraditionalGraph tGraph, double segmentLenght, int segmentCapacity) {
		this(tGraph.numberOfNodes, segmentLenght, segmentCapacity);
		this.distanceMatrix = tGraph.distanceMatrix;
	}

	/**
	 * Builds the all segments.
	 */
	public void buildAllSegments() {
		for (int from = 0; from < this.numberOfNodes; from++) {
			for (int to = 0; to < this.numberOfNodes; to++) {
				int edgeValue = getEdgeLength(from, to);
				if (edgeValue != INVALID_VALUE) {
					Edge edge = getEdge(from, to);
					buildSegmentsFor(edge);
				}
			}
		}
	}

	/**
	 * Builds the segments for an edge.
	 * 
	 * @param edge
	 *            the edge
	 */
	public void buildSegmentsFor(Edge edge) {
		if (this.edgeSegments.get(edge) == null) { // segments not yet created
			LinkedList<Segment> segmentList = new LinkedList<Segment>();
			Position segmentStart = new Position(edge, 0d);
			Position segmentEnd = new Position(edge, edge.getLength());
			Segment s = new Segment(segmentStart, this.segmentLength, this.getEdgeCapacity(edge));

			do {
				segmentList.offer(s);
				s = new Segment(s.exclusiveEndLocation, this.segmentLength, this.getEdgeCapacity(edge));
			} while (!s.contains(segmentEnd));

			s = new Segment(s.startLocation, MINIMUM_DISTANCE_MARGIN, this.getEdgeCapacity(edge));
			segmentList.offer(s);

			this.edgeSegments.put(edge, segmentList);
		}
	}

	/**
	 * Gets the edge.
	 * 
	 * @param from
	 *            the from
	 * @param to
	 *            the to
	 * @return the edge
	 */
	public Edge getEdge(int from, int to) {
		return new Edge(from, to, getEdgeLength(from, to));
	}

	/**
	 * Gets the edge capacity.
	 * 
	 * @param edge
	 *            the edge
	 * @return the edge capacity
	 */
	public int getEdgeCapacity(Edge edge) {
		return getEdgeCapacity(edge.getFrom(), edge.getTo());
	}

	/**
	 * Gets the edge capacity.
	 * 
	 * @param nodeIndexA
	 *            the node index a
	 * @param nodeIndexB
	 *            the node index b
	 * @return the edge capacity
	 */
	public int getEdgeCapacity(int nodeIndexA, int nodeIndexB) {
		return this.capacityMatrix[nodeIndexA][nodeIndexB];
	}

	/**
	 * Gets the farthest available segment (occupation-wise).
	 * 
	 * @param start
	 *            the start
	 * @param limit
	 *            the limit
	 * @return the farthest available segment
	 */
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

	/**
	 * Gets the segment for position.
	 * 
	 * @param pos
	 *            the pos
	 * @return the segment for position
	 */
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

	/**
	 * Gets the segments up to position.
	 * 
	 * @param pos
	 *            the pos
	 * @return the segments up to position
	 */
	public LinkedList<Segment> getSegmentsUpToPosition(Position pos) {
		LinkedList<Segment> segmentsForEdge = this.edgeSegments.get(pos.edge);
		Iterator<Segment> it = segmentsForEdge.iterator();
		if (!it.hasNext()) {
			return null;
		}

		LinkedList<Segment> result = new LinkedList<>();

		Segment seg = it.next();
		while (!seg.contains(pos)) {
			result.add(seg);
			seg = it.next();
		}

		result.add(seg);

		return result;
	}

	/**
	 * Gets the segments for edge.
	 * 
	 * @param e
	 *            the e
	 * @return the segments for edge
	 */
	public LinkedList<Segment> getSegmentsForEdge(Edge e) {
		return this.edgeSegments.get(e);
	}

	/**
	 * Checks if edge is full.
	 * 
	 * @param e
	 *            the e
	 * @return true, if is edge full
	 */
	public boolean isEdgeFull(Edge e) {
		LinkedList<Segment> segmentsForEdge = new LinkedList<>();
		segmentsForEdge.addAll(getSegmentsForEdge(e));

		segmentsForEdge.removeLast();

		for (Segment s : segmentsForEdge) {
			if (!s.isFull()) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Reset and build segments.
	 */
	public void resetAndBuildSegments() {
		if (!this.edgeSegments.isEmpty()) {
			this.edgeSegments.clear();
		}
		buildAllSegments();
	}

}
