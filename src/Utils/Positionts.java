package Utils;

import java.util.ArrayList;

import Coords.LatLonAlt;
import Robot.Fruit;
import Robot.Packman;

public class Positionts {
	private LatLonAlt topLeftP=new LatLonAlt(32.10574, 35.20228, 0);
	private ArrayList<Packman>packCollection;
	private ArrayList<Ghost>ghostCollection;
	private ArrayList<Rectangle>ractCollection;
	private ArrayList<Fruit>fruitCollection;
	private MyPlayer player;
	public Positionts() {
		this.packCollection=new ArrayList<>();
		this.fruitCollection=new ArrayList<>();
		this.ghostCollection=new ArrayList<>();
		this.ractCollection=new ArrayList<>();
	}
	public ArrayList<Packman> getPackCollection() {
		return packCollection;
	}
	public void setPackCollection(ArrayList<Packman> packCollection) {
		this.packCollection = packCollection;
	}
	public ArrayList<Ghost> getGhostCollection() {
		return ghostCollection;
	}
	public void setGhostCollection(ArrayList<Ghost> ghostCollection) {
		this.ghostCollection = ghostCollection;
	}
	public ArrayList<Rectangle> getRactCollection() {
		return ractCollection;
	}
	public void setRactCollection(ArrayList<Rectangle> ractCollection) {
		this.ractCollection = ractCollection;
	}
	
	public ArrayList<Fruit> getFruitCollection() {
		return fruitCollection;
	}
	public void setFruitCollection(ArrayList<Fruit> fruitCollection) {
		this.fruitCollection = fruitCollection;
	}
	public MyPlayer getPlayer() {
		return player;
	}
	public void setPlayer(MyPlayer player) {
		this.player = player;
	}
	public void addPackman(Packman p) {
		this.packCollection.add(p);
	}
	public void addFruit(Fruit f) {
		this.fruitCollection.add(f);
	}
	public void addRectangle(Rectangle r) {
		this.ractCollection.add(r);
	}
	public void addGhost(Ghost g) {
		this.ghostCollection.add(g);
	}
	public LatLonAlt getTopLeftP() {
		return this.topLeftP;
	}
	
}
