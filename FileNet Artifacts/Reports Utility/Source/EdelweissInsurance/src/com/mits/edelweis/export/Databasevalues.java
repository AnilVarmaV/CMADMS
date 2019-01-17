package com.mits.edelweis.export;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ibm.json.java.JSONArray;
import com.ibm.json.java.JSONObject;

public class Databasevalues {

	/**
	 * @param args
	 * @throws ClassNotFoundException 
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub

		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver"); 		
		Connection con=DriverManager.getConnection("jdbc:sqlserver://172.16.8.125:1433;"+"databaseName=Test;user=sa;password=mits123$");


		System.out.println("connection is:::::::::::::::::::"+con);

		PreparedStatement prepareStatement = con.prepareStatement("SELECT * FROM [Test].[dbo].[EntityTable]"); 
		ResultSet resultSet = prepareStatement.executeQuery();
		JSONObject choiceList = new JSONObject();
		JSONArray choicess = new JSONArray();
		JSONObject choices = new JSONObject();
		JSONArray jsonPropertyData = new JSONArray();
		//System.out.println("resultSet::"+resultSet.getString("Entitly1"));
		while(resultSet.next()){

			System.out.println("resultSet::"+resultSet.getString("Entitly1"));

			JSONObject choice = new JSONObject();
			choice.put("value",resultSet.getString("Entitly1").trim());
			choice.put("displayName",resultSet.getString("Entitly1").trim());

	//		System.out.println("choice:value::"+choice);
			choicess.add(choice);

		}
	//	System.out.println("choices:value::"+choicess);

		choices.put("displayName", "Entitys");
		choices.put("choices",choicess);
	//	System.out.println("choices:value::"+choices);

		choiceList.put("choiceList", choices);
		choiceList.put("symbolicName","Entity");
		//System.out.println("choiceList:value::"+choiceList);

		jsonPropertyData.add(choiceList);
	//	System.out.println("jsonPropertyData:value::"+jsonPropertyData);
		PreparedStatement prepareStatement1 = con.prepareStatement("SELECT * FROM [Test].[dbo].[LOB]"); 
		ResultSet resultSet1 = prepareStatement1.executeQuery();
		JSONObject choiceList1 = new JSONObject();
		JSONArray choicess1 = new JSONArray();
		JSONObject choices1 = new JSONObject();
		//System.out.println("resultSet::"+resultSet.getString("Entitly1"));
		while(resultSet1.next()){

		//	System.out.println("resultSet1::"+resultSet1.getString("LOB"));

			JSONObject choice1 = new JSONObject();
			choice1.put("value",resultSet1.getString("LOB").trim());
			choice1.put("displayName",resultSet1.getString("LOB").trim());

			//System.out.println("choice:value::"+choice1);
			choicess1.add(choice1);

		}
		//System.out.println("choices:value::"+choicess1);

		choices1.put("displayName", "LOB");
		choices1.put("choices",choicess1);
	//	System.out.println("choices:value::"+choices1);

		choiceList1.put("choiceList", choices1);
		choiceList1.put("symbolicName","LOB");
		//System.out.println("choiceList:value::"+choiceList1);

		jsonPropertyData.add(choiceList1);
		System.out.println("jsonPropertyData:value::"+jsonPropertyData);
	}
}
