package org;

import java.sql.SQLException;
import java.sql.PreparedStatement;

/**
 * Class builds sql querys.
 */
public class SQLQuerries {

	/**
	 * Returns a statement.
	 * @param c
	 * @param s
	 * @return statement
	 */
	private static PreparedStatement getPS(java.sql.Connection c, String s) {
		try {
			return (PreparedStatement) c.prepareStatement(s);
		} catch (SQLException e) {
			return null;
		}
	}

	/**
	 * Returns a statement that retrieves the solution of a question from the database.
	 * @param c
	 * @return statement
	 */
	public static PreparedStatement getCheckAnswer(java.sql.Connection c) {
		return getPS(c,
				"select solution from Question where Question.questiontext =?");
	}

	/**
	 * Returns a statement that checks if a question_data is already in the database.
	 * @param c
	 * @return statement
	 */
	public static PreparedStatement getCountForCheck(java.sql.Connection c) {
		return getPS(
				c,
				"select count(*) from Question_data where Question_data.QuestionID ="
						+ " (select id from Question where questiontext = ?) and Question_data.UserID = (select `User`.id from"
						+ " `User` where `User`.`name` = ?)");
	}

	/**
	 * Updates question_data in database.
	 * @param c
	 * @param isLite
	 * @return statement
	 */
	public static PreparedStatement get1ForCheck(java.sql.Connection c, boolean isLite) {
		if(isLite){
			return getPS(
					c,
					"update Question_data set falseCount= falsecount + ?, lastAnswered = date('now'), overallCount = overallCount +1"
							+ " where Question_data.QuestionID = (select id from Question where questiontext = ? "
							+ ") and Question_data.UserID = (select `User`.id from `User` where `User`.`name` = "
							+ "?)");
		}
		return getPS(
				c,
				"update Question_data set falseCount= falsecount + ?, lastAnswered = now(), overallCount = overallCount +1"
						+ " where Question_data.QuestionID = (select id from Question where questiontext = ? "
						+ ") and Question_data.UserID = (select `User`.id from `User` where `User`.`name` = "
						+ "?)");

	}

	/**
	 * Inserts question_data in database.
	 * @param c
	 * @param isLite
	 * @return statement
	 */
	public static PreparedStatement get2ForCheck(java.sql.Connection c, boolean isLite) {
		if(isLite){
			return getPS(
					c,
					"insert into Question_data(id,UserID,QuestionID,falseCount,lastAnswered,overallCount) values(?,(select `User`.id from `User` where `User`.`name` = ?) , (select id from Question where questiontext = ?), ? ,date('now'),1)");
		}
		return getPS(
				c,
				"insert into Question_data(UserID,QuestionID,falseCount,lastAnswered,overallCount) values((select `User`.id from `User` where `User`.`name` = ?) , (select id from Question where questiontext = ?), ? ,now(),1)");
	}

	/**
	 * Retrieves all available categories from the database.
	 * @param c
	 * @return statement
	 */
	public static PreparedStatement addCategories(java.sql.Connection c) {
		return getPS(c, "select id, title from Topic");
	}

	/**
	 * Retrieves all available levels from the database.
	 * @param c
	 * @return statement
	 */
	public static PreparedStatement addLevel(java.sql.Connection c) {
		return getPS(c, "SELECT title FROM Level");
	}
	
	/**
	 * Retrieves the id of a specific user from the database.
	 * @param c
	 * @return statement
	 */
	public static PreparedStatement getUser(java.sql.Connection c){
		return getPS(
				c,"select id from `User` where name = ?");
	}

	/**
	 * Retrieves a question from the database.
	 * @param c
	 * @param isLite
	 * @return statement
	 */
	public static PreparedStatement getQuestion(java.sql.Connection c, boolean isLite) {
		if (!isLite) {
			String querry = "SELECT	Question.id, Question.questiontext, Question.answer1,	Question.answer2,	Question.answer3,	Question.answer4,	image,	video,	audio, level_value FROM	Question "
					+ "LEFT JOIN Question_data ON Question.id = Question_data.QuestionID "
					+ "LEFT JOIN `User` ON `User`.id = Question_data.UserID "
					+ "LEFT JOIN Topic ON Question.TopicID = Topic.id "
					+ "LEFT join `Level` on Question.level_value = `Level`.`value` "
					+ "WHERE 	Topic.title = ? and `Level`.title = ?  "
					+ "AND (IFNULL(UserID, ?)) = ? AND IFNULL(lastAnswered, 0) < DATE_SUB(NOW(), INTERVAL 3 MINUTE) "
					+ "ORDER BY (falseCount / overallCount) DESC,	lastAnswered";

			return getPS(c, querry);
		} else {
			String querry = "SELECT	Question.id, Question.questiontext, Question.answer1,	Question.answer2,	Question.answer3,	Question.answer4,	image,	video,	audio, level_value FROM	Question "
					+ "LEFT JOIN Question_data ON Question.id = Question_data.QuestionID "
					+ "LEFT JOIN `User` ON `User`.id = Question_data.UserID "
					+ "LEFT JOIN Topic ON Question.TopicID = Topic.id " 
					+ "LEFT join `Level` on Question.level_value = `Level`.`value` "
					+ "WHERE 	Topic.title = ? and `Level`.title = ? "
					+ "AND (ifnull(UserID, ?)) = ? AND ifnull(lastAnswered, 0) < datetime('now','-3 minutes') "
					+ "ORDER BY (falseCount / overallCount) DESC,	lastAnswered";

			return getPS(c, querry);
		}
		
	}

	/**
	 * Retrieves the permissions of a specific user from the database.
	 * @param c
	 * @return statement
	 */
	public static PreparedStatement getLogin(java.sql.Connection c) {
		return getPS(c,
				"select name, admin from User where name = ? and password = ?");
	}

	/**
	 * Retrieves a list of all users from the database.
	 * @param c
	 * @return statement
	 */
	public static PreparedStatement addAllUsers(java.sql.Connection c) {
		return getPS(c, "SELECT * from `User");
	}

	/**
	 * Updates an user in the database (Sets password an name).
	 * @param c
	 * @return statement
	 */
	public static PreparedStatement changeUser(java.sql.Connection c) {
		return getPS(c, "update `User` set name = ?, password = ? where id = ?");
	}

	/**
	 * Removes an user from the database.
	 * @param c
	 * @return statement
	 */
	public static PreparedStatement removeUser(java.sql.Connection c) {
		return getPS(c, "DELETE FROM `User` WHERE ((`name` = ?))");
	}

	/**
	 * Inserts a new user into the database.
	 * @param c
	 * @return statement
	 */
	public static PreparedStatement addUser(java.sql.Connection c) {
		return getPS(c,
				"Insert into `User`(name, password, surname, email) VALUES(?,?,' ',' ')");
	}

	/**
	 * Updates a question in the database.
	 * @param c
	 * @param mediatype
	 * @return statement
	 */
	public static PreparedStatement udpateQuestion(java.sql.Connection c,
			String mediatype) {
			return getPS(
				c,
				"update `Question` set questiontext = ?, solution = ?, answer1 = ?, answer2 = ?, answer3 = ?, answer4 = ? "
						+ mediatype + " where id = ?");
	}

	/**
	 * Retrieves the highscore from the database.
	 * @param c
	 * @return statement
	 */
	public static PreparedStatement setHighScore(java.sql.Connection c) {
		return getPS(
				c,
				"select (sum(overallCount) - sum(falseCount))* level_value as blub, User.`name` from Question_data join Question on QuestionID = Question.id join `User` on `User`.id = Question_data.UserID GROUP BY UserID order by blub DESC");
	}
	
	/**
	 * Inserts a new category into the database.
	 * @param c
	 * @return statement
	 */
	public static PreparedStatement addCategory(java.sql.Connection c) {
		if (c != null) {
			return getPS(c, "INSERT INTO `Topic`(title, description) VALUES(?, ' ')");
		} else {
			return null;
		}
	}

	/**
	 * Removes a category from the database.
	 * @param c
	 * @return statement
	 */
	public static PreparedStatement removeCategory(java.sql.Connection c) {
		if (c != null) {
			return getPS(c, "DELETE FROM `Topic` WHERE ((`title` = ?))");
		} else {
			return null;
		}
	}
	
	/**
	 * Updates a category in the database.
	 * @param c
	 * @return statement
	 */
	public static PreparedStatement updateCategory(java.sql.Connection c) {
		if (c != null) {
			return getPS(c, "UPDATE `Topic` SET title = ? WHERE id = ?");
		} else {
			return null;
		}
	}
	
	/**
	 * Retrieves a question from the database in random mode.
	 * @param c
	 * @param isLight
	 * @return statement
	 */
	public static PreparedStatement getQuestionAllCategories(java.sql.Connection c, boolean isLight){
		if( c != null){
			if(isLight){
				return getPS(c,"select Question.id, Question.questiontext, Question.answer1,	Question.answer2,	Question.answer3,	Question.answer4,	image,	video,	audio, level_value FROM	Question  ORDER BY RANDOM() limit 1");
			}
			return getPS(c,"select Question.id, Question.questiontext, Question.answer1,	Question.answer2,	Question.answer3,	Question.answer4,	image,	video,	audio, level_value FROM	Question  ORDER BY rand() limit 1"); 
		}
		return null;
	}
	
	/**
	 * Retrieves a question from the database in wrong mode.
	 * @param c
	 * @param isLight
	 * @return statement
	 */
	public static PreparedStatement getWrongQuestion(java.sql.Connection c, boolean isLight){
		if( c != null){
			if(isLight){
				return getPS(c,"select Question.id, Question.questiontext, Question.answer1,	Question.answer2,	Question.answer3,	Question.answer4,	image,	video,	audio, level_value FROM	Question join Question_data on Question.id = Question_data.QuestionID where UserID = ? and falseCount > 0 ORDER BY RANDOM() limit 1");
			}
			return getPS(c,"select Question.id, Question.questiontext, Question.answer1,	Question.answer2,	Question.answer3,	Question.answer4,	image,	video,	audio, level_value FROM	Question join Question_data on Question.id = Question_data.QuestionID where UserID = ? and falseCount > 0 ORDER BY RAND() limit 1"); 
		}
		return null;
	}
	
	/**
	 * Retrieves a random question from a given category.
	 * @param c
	 * @param isLight
	 * @return statement
	 */
	public static PreparedStatement getRandomIfEmpty(java.sql.Connection c, boolean isLight){
		if( c != null){
			if(isLight){
				return getPS(c,"select Question.id, Question.questiontext, Question.answer1,	Question.answer2,	Question.answer3,	Question.answer4,	image,	video,	audio, level_value FROM	Question join Topic on Question.TopicID = Topic.id where Topic.title = ? ORDER BY RANDOM() limit 1");
			}
			return getPS(c,"select Question.id, Question.questiontext, Question.answer1,	Question.answer2,	Question.answer3,	Question.answer4,	image,	video,	audio, level_value FROM	Question join Topic on Question.TopicID = Topic.id where Topic.title = ? ORDER BY rand() limit 1"); 
		}
		return null;
	}
	
	/**
	 * Inserts a new question into the database.
	 * @param c
	 * @return statement
	 */
	public static PreparedStatement insertQuestion(java.sql.Connection c){
		if( c != null){
			return getPS(c,"insert into Question(TopicId,level_value,image,video,audio,questiontext,answer1,answer2,answer3,answer4,solution) VALUES((select id from Topic where Topic.title = ?),?,?,?,?,?,?,?,?,?,?)");
		}
		return null;
	}
}
