package org;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

public class SQLQuerries {

	private static PreparedStatement getPS(java.sql.Connection c, String s) {
		try {
			return (PreparedStatement) c.prepareStatement(s);
		} catch (SQLException e) {
			return null;
		}
	}

	public static PreparedStatement getCheckAnswer(java.sql.Connection c) {
		return getPS(c,
				"select solution from Question where Question.questiontext =?");
	}

	public static PreparedStatement getCountForCheck(java.sql.Connection c) {
		return getPS(
				c,
				"select count(*) from Question_data where Question_data.QuestionID ="
						+ " (select id from Question where questiontext = ?) and Question_data.UserID = (select `User`.id from"
						+ " `User` where `User`.`name` = ?)");
	}

	public static PreparedStatement get1ForCheck(java.sql.Connection c) {
		return getPS(
				c,
				"update Question_data set falseCount= falsecount + ?, lastAnswered = now(), overallCount = overallCount +1"
						+ " where Question_data.QuestionID = (select id from Question where questiontext = ? "
						+ ") and Question_data.UserID = (select `User`.id from `User` where `User`.`name` = "
						+ "?)");

	}

	public static PreparedStatement get2ForCheck(java.sql.Connection c) {
		return getPS(
				c,
				"insert into Question_data(UserID,QuestionID,falseCount,lastAnswered,overallCount) values((select `User`.id from `User` where `User`.`name` = ?) , (select id from Question where questiontext = ?), ? ,now(),1)");
	}

	public static PreparedStatement addCategories(java.sql.Connection c) {
		return getPS(c, "select title from Topic");
	}

	public static PreparedStatement addLevel(java.sql.Connection c) {
		return getPS(c, "SELECT title FROM Level");
	}

	public static PreparedStatement getFrage(java.sql.Connection c) {
		return getPS(
				c,
				"select Question.id, questiontext, answer1, answer2, answer3, answer4, image, video, audio from Topic join Question on Question.TopicID = Topic.id where Topic.title = ?"
						+ " ORDER BY RAND()");
	}

	public static PreparedStatement getLogin(java.sql.Connection c) {
		return getPS(c,
				"select name, admin from User where name = ? and password = ?");
	}

	public static PreparedStatement addAllUsers(java.sql.Connection c) {
		return getPS(c, "SELECT * from `User");
	}

	public static PreparedStatement changeUser(java.sql.Connection c) {
		return getPS(c, "update `User` set name = ?, password = ? where id = ?");
	}

	public static PreparedStatement removeUser(java.sql.Connection c) {
		return getPS(c, "DELETE FROM `User` WHERE ((`name` = ?))");
	}

	public static PreparedStatement addUser(java.sql.Connection c) {
		return getPS(c,
				"Insert into `User`(name, password, surname, email) VALUES(?,?,' ',' ')");
	}

	public static PreparedStatement udpateQuestion(java.sql.Connection c,
			String mediatype) {
			return getPS(
				c,
				"update `Question` set questiontext = ?, solution = ?, answer1 = ?, answer2 = ?, answer3 = ?, answer4 = ? "
						+ mediatype + " where id = ?");
	}

	public static PreparedStatement setHighScore(java.sql.Connection c) {
		return getPS(
				c,
				"select (sum(overallCount) - sum(falseCount))* level_value, User.`name` from Question_data join Question on QuestionID = Question.id join `User` on `User`.id = Question_data.UserID GROUP BY UserID");
	}
	

}
