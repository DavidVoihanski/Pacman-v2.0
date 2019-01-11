package MySql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * This class is used to compare your score to all other scores in the data base
 * @author David&Evgeny
 *
 */
public abstract class Statistics {

	/**
	 * Takes a map id and extracts the results for this map from the DB
	 * @param mapNumber map number
	 * @return Returns your place compared to the DB
	 */
	public static double compareScore(int mapId,double myScore) {
		ArrayList<Double> scores = extractScores(mapId);
		double myPlace = compareResultToDB(scores, myScore,mapId);
		return myPlace;
	}

	//private methods*************************************************************************************
	//compares your %of top
	private static double compareResultToDB(ArrayList<Double>scores,double myScore,int mapId) {
		Iterator<Double>scoreIt=scores.iterator();
		long countLowerScores=0;
		while(scoreIt.hasNext()) {
			double currScore=scoreIt.next();
			if(myScore>=currScore) 
				countLowerScores++;
		}
		double percentOfTop = 100.0-(countLowerScores/(double)scores.size()*100);
		return percentOfTop;
	}
	
	//connects to the DB and extracts the scores of a certain map
	private static ArrayList<Double> extractScores(int mapId) {
		ArrayList<Double>scores = new ArrayList<Double>();
		String jdbcUrl="jdbc:mysql://ariel-oop.xyz:3306/oop"; //?useUnicode=yes&characterEncoding=UTF-8&useSSL=false";
		String jdbcUser="student";
		String jdbcPassword="student";
		int currentMap=0;

		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = 
					DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword);


			Statement statement = connection.createStatement();

			//select data
			String allCustomersQuery = "SELECT * FROM logs;";
			ResultSet resultSet = statement.executeQuery(allCustomersQuery);
			while(resultSet.next())
			{
				currentMap = resultSet.getInt("SomeDouble");
				if(currentMap == mapId)
					scores.add(resultSet.getDouble("Point"));
			}

			resultSet.close();		
			statement.close();		
			connection.close();	
		}

		catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle.getMessage());
			System.out.println("Vendor Error: " + sqle.getErrorCode());
		}

		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return scores;
	}

}

