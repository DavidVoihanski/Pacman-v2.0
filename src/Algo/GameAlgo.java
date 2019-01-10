package Algo;

import java.util.ArrayList;
import java.util.Iterator;

import Coords.LatLonAlt;
import Robot.Fruit;
import Utils.Ghost;
import Utils.MyCoords;
import Utils.MyPlayer;
import Utils.Positionts;
import Utils.Range;
import Utils.Rectangle;
import graph.Graph;
import graph.Graph_Algo;
import graph.Node;

public abstract class GameAlgo {

	public static double pathToColsestFruit(MyPlayer player,Fruit closest) {
		double angle=Range.GetAzi(player.getPosition(), closest.getLocation());
		return angle;
	}

	public static Fruit findClosestFruit(ArrayList<Fruit>fruits,MyPlayer player1) {
		Iterator<Fruit>it=fruits.iterator();
		if(!it.hasNext())return null;
		Fruit closest=it.next();
		double closestDistance=closest.getLocation().distance3D(player1.getPosition());
		while(it.hasNext()) {
			Fruit tempFruit=it.next();
			double tempDistance=tempFruit.getLocation().distance3D(player1.getPosition());
			if(tempDistance<closestDistance) {
				closestDistance=tempDistance;
				closest=tempFruit;
			}
		}
		return closest;
	}
	public static Ghost findClosestGhost(ArrayList<Ghost>ghosts,MyPlayer player1) {
		MyCoords converter=new MyCoords();
		Iterator<Ghost>it=ghosts.iterator();
		if(!it.hasNext())return null;
		Ghost closest=it.next();
		double closestDistance=converter.distance2d(closest.getPosition(), player1.getPosition());
		while(it.hasNext()) {
			Ghost tempGhost=it.next();
			double tempDistance=converter.distance2d(tempGhost.getPosition(), player1.getPosition());
			if(tempDistance<closestDistance) {
				closestDistance=tempDistance;
				closest=tempGhost;
			}
		}
		return closest;
	}
	public static boolean isValidPointOnMap(Positionts pos,LatLonAlt gps) {
		ArrayList<Rectangle>rects=pos.getRactCollection();
		Iterator<Rectangle>it=rects.iterator();
		while(it.hasNext()) {
			Rectangle currRect=it.next();
			double minLat=currRect.getBottomLeft().x();
			double maxLat=currRect.getTopLeft().x();
			double minLon=currRect.getBottomLeft().y();
			double maxLon=currRect.getBottomRight().y();
			double lat=gps.x();
			double lon=gps.y();
			if(lat>=minLat&&lat<=maxLat&&lon>=minLon&&lon<=maxLon)
				return false;
		}
		return true;
	}
	public static void removeInvalidPointsFromRects(Positionts pos) {
		ArrayList<Rectangle>rects=pos.getRactCollection();
		Iterator<Rectangle>rectIt=rects.iterator();
		while(rectIt.hasNext()) {
			ArrayList<LatLonAlt>toRemove=new ArrayList<>();
			Rectangle currRect=rectIt.next();
			ArrayList<LatLonAlt>points=currRect.getAcceciblePoints();
			Iterator<LatLonAlt>pointsIt=points.iterator();
			while(pointsIt.hasNext()) {
				LatLonAlt currPoint=pointsIt.next();
				if(!isValidPointOnMap(pos, currPoint))
					toRemove.add(currPoint);
			}
			Iterator<LatLonAlt>remover=toRemove.iterator();
			while(remover.hasNext()) {
				points.remove(remover.next());
			}
		}
	}
	
	public static boolean hasLineOfSight(LatLonAlt start,LatLonAlt target,Positionts pos) {
		ArrayList<Rectangle>rects=pos.getRactCollection();
		Iterator<Rectangle>it=rects.iterator();
		while(it.hasNext()) {
			Rectangle currRect=it.next();
			if(doIntersect(start, target, currRect.getBottomLeft(), currRect.getTopLeft()))return false;
			if(doIntersect(start, target, currRect.getBottomLeft(), currRect.getBottomRight()))return false;
			if(doIntersect(start, target, currRect.getTopLeft(), currRect.getTopRight()))return false;
			if(doIntersect(start, target, currRect.getBottomRight(), currRect.getTopRight()))return false;
		}
		return true;
	}
	

	
	public static ArrayList<LatLonAlt> runGame(Positionts pos) {
		return findNextBestFruit(pos);
	}
	
	//private methods
	
	//Takes target nodes and returns an arraylist of coords representing a path to it
	private static ArrayList<LatLonAlt> parsePath(Node target,ArrayList<LatLonAlt>allPoints,Graph graph ) {
		ArrayList<String>shortestPath=target.getPath();
		ArrayList<LatLonAlt>pathInCoords=new ArrayList<>();
		Iterator<String>it=shortestPath.iterator();
		String nextTarget=it.next();  //the first target will always be our starting point so it should not be added to the list
		LatLonAlt nextTargetGps;
		while(it.hasNext()) {
			nextTarget=it.next();
			int indexOfNextTarget=graph.getNodeByName(nextTarget).get_id();
			nextTargetGps=allPoints.get(indexOfNextTarget);
			pathInCoords.add(nextTargetGps);
		}
		nextTargetGps=allPoints.get(target.get_id());
		pathInCoords.add(nextTargetGps);
		System.out.println("");
		return pathInCoords;
	}
	
	private static ArrayList<LatLonAlt> findNextBestFruit(Positionts pos) {
		
		ArrayList<LatLonAlt>allPoints=new ArrayList<>(); //will hold every point of interest on the map(corners and fruits)
		String source = "player";
		allPoints.add(pos.getPlayer().getPosition());
		Graph graph = new Graph();
		graph.add(new Node(source));
		int startOfFruits=addRectPoints(graph, pos, allPoints);//when we're done adding rects, we start adding fruits- important to know when the fruits started
		addFruits(graph, pos, allPoints);
		addReachablePoints(graph, allPoints, pos, startOfFruits);
		Graph_Algo.dijkstra(graph, source);
		double minDistance;
		if(startOfFruits<allPoints.size())minDistance=graph.getNodeByIndex(startOfFruits).getDist();
		else return null;
		String target=graph.getNodeByIndex(startOfFruits).get_name();
		int indexOfTarget=startOfFruits;
		for(int i=startOfFruits+1;i<allPoints.size();i++) { //finds the closest fruit
			double tempDist=graph.getNodeByIndex(i).getDist();
			if(tempDist<minDistance) {
				minDistance=tempDist;
				target=graph.getNodeByIndex(i).get_name();
				indexOfTarget=i;
			}
		}
		
		System.out.println("");
		Node targetNode=graph.getNodeByIndex(indexOfTarget);
		ArrayList<LatLonAlt>path=parsePath(targetNode,allPoints, graph);
		return path;
	}
	
	
	//adds all accessible points of the rects to the graph
	private static int addRectPoints(Graph graph,Positionts pos,ArrayList<LatLonAlt>allPoints) {
		ArrayList<Rectangle>rects=pos.getRactCollection();
		Iterator<Rectangle>rectIt=rects.iterator();
		int counter=1;
		while(rectIt.hasNext()) {
			Rectangle currRect=rectIt.next();
			ArrayList<LatLonAlt>acceciblePoints=currRect.getAcceciblePoints();
			Iterator<LatLonAlt>accecibleIt=acceciblePoints.iterator();
			while(accecibleIt.hasNext()) {
				Node d = new Node(""+counter);
				counter++;
				graph.add(d);
				allPoints.add(accecibleIt.next());
			}
		}
		return counter;
	}
	//adds the fruits to the graph
	private static void addFruits(Graph graph,Positionts pos,ArrayList<LatLonAlt>allPoints) {
		ArrayList<Fruit>fruits=pos.getFruitCollection();
		Iterator<Fruit>fruitIt=fruits.iterator();
		int counter=1;
		while(fruitIt.hasNext()) {
			Fruit currFruit=fruitIt.next();
			Node d = new Node("Fruit"+counter);
			graph.add(d);
			allPoints.add(currFruit.getLocation());
			counter++;
		}
	}
	//finds where you can get in a straight line from each point on the graph
	private static void addReachablePoints(Graph graph, ArrayList<LatLonAlt>allPoints,Positionts pos,int endOfRects) {
		Iterator<LatLonAlt>pointsIt=allPoints.iterator();
		MyCoords converter=new MyCoords();
		int counterForOuter=0;
		String source;
		String target;
		while(pointsIt.hasNext()) {
			Iterator<LatLonAlt>checker=allPoints.iterator();
			LatLonAlt currPoint=pointsIt.next();
			int counterForInner=0;
			while(checker.hasNext()) {
				LatLonAlt toBeChecked=checker.next();
				if(!toBeChecked.equals(currPoint)&&hasLineOfSight(currPoint, toBeChecked, pos)) {
					if(counterForOuter>=endOfRects) {
						int diff=counterForOuter-endOfRects+1;
						source="Fruit"+diff;
					}
					else source=""+counterForOuter;
					if(counterForInner>=endOfRects) {
						int diff=counterForInner-endOfRects+1;
						target="Fruit"+diff;
					}
					else target=""+counterForInner;
					if(counterForInner==0)target="player";
					if(counterForOuter==0)source="player";
					graph.addEdge(source, target, converter.distance2d(currPoint, toBeChecked));
				}
				counterForInner++;
			}
			counterForOuter++;
		}
	}
	
	// The main function that returns true if line segment 'p1q1' 
	// and 'p2q2' intersect. 
	//Taken from https://www.geeksforgeeks.org/check-if-two-given-line-segments-intersect/
	private static boolean doIntersect(LatLonAlt p1, LatLonAlt q1, LatLonAlt p2, LatLonAlt q2) 
	{ 
	    // Find the four orientations needed for general and 
	    // special cases 
	    int o1 = orientation(p1, q1, p2); 
	    int o2 = orientation(p1, q1, q2); 
	    int o3 = orientation(p2, q2, p1); 
	    int o4 = orientation(p2, q2, q1); 
	  
	    // General case 
	    if (o1 != o2 && o3 != o4) 
	        return true; 
	  
	    // Special Cases 
	    // p1, q1 and p2 are colinear and p2 lies on segment p1q1 
	    if (o1 == 0 && onSegment(p1, p2, q1)) return true; 
	  
	    // p1, q1 and q2 are colinear and q2 lies on segment p1q1 
	    if (o2 == 0 && onSegment(p1, q2, q1)) return true; 
	  
	    // p2, q2 and p1 are colinear and p1 lies on segment p2q2 
	    if (o3 == 0 && onSegment(p2, p1, q2)) return true; 
	  
	     // p2, q2 and q1 are colinear and q1 lies on segment p2q2 
	    if (o4 == 0 && onSegment(p2, q1, q2)) return true; 
	  
	    return false; // Doesn't fall in any of the above cases 
	} 
	
	private static boolean onSegment(LatLonAlt p, LatLonAlt q, LatLonAlt r) 
	{ 
	    if (q.y() <= Math.max(p.y(), r.y()) && q.y() >= Math.min(p.y(), r.y()) && 
	        q.x() <= Math.max(p.x(), r.x()) && q.x() >= Math.min(p.x(), r.x())) 
	       return true; 
	  
	    return false; 
	}
	// To find orientation of ordered triplet (p, q, r). 
	// The function returns following values 
	// 0 --> p, q and r are colinear 
	// 1 --> Clockwise 
	// 2 --> Counterclockwise 
	private static int orientation(LatLonAlt p, LatLonAlt q, LatLonAlt r) 
	{ 
	    // See https://www.geeksforgeeks.org/orientation-3-ordered-points/ 
	    // for details of below formula. 
	    double val = (q.x() - p.x()) * (r.y() - q.y()) - 
	              (q.y() - p.y()) * (r.x() - q.x()); 
	  
	    if (val == 0) return 0;  // colinear 
	  
	    return (val > 0)? 1: 2; // clock or counterclock wise 
	} 
	
}
