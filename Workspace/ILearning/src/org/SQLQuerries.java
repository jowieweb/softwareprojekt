package org;

import java.sql.SQLException;
import java.sql.PreparedStatement;

/**
 * Class builds sql querys.
 */
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

	public static PreparedStatement addCategories(java.sql.Connection c) {
		return getPS(c, "select id, title from Topic");
	}

	public static PreparedStatement addLevel(java.sql.Connection c) {
		return getPS(c, "SELECT title FROM Level");
	}
	
	
	public static PreparedStatement getUser(java.sql.Connection c){
		return getPS(
				c,"select id from `User` where name = ?");
	}

	public static PreparedStatement getFrage(java.sql.Connection c, boolean isLite) {
		if (!isLite) {
			String querry = "SELECT	Question.id, Question.questiontext, Question.answer1,	Question.answer2,	Question.answer3,	Question.answer4,	image,	video,	audio, level_value FROM	Question "
					+ "LEFT JOIN Question_data ON Question.id = Question_data.QuestionID "
					+ "LEFT JOIN `User` ON `User`.id = Question_data.UserID "
					+ "LEFT JOIN Topic ON Question.TopicID = Topic.id "
					+ "WHERE 	Topic.title = ? "
					+ "AND (IFNULL(UserID, ?)) = ? AND IFNULL(lastAnswered, 0) < DATE_SUB(NOW(), INTERVAL 3 MINUTE) "
					+ "ORDER BY (falseCount / overallCount) DESC,	lastAnswered";

			return getPS(c, querry);
		} else {
			String querry = "SELECT	Question.id, Question.questiontext, Question.answer1,	Question.answer2,	Question.answer3,	Question.answer4,	image,	video,	audio, level_value FROM	Question "
					+ "LEFT JOIN Question_data ON Question.id = Question_data.QuestionID "
					+ "LEFT JOIN `User` ON `User`.id = Question_data.UserID "
					+ "LEFT JOIN Topic ON Question.TopicID = Topic.id "
					+ "WHERE 	Topic.title = ? "
					+ "AND (ifnull(UserID, ?)) = ? AND ifnull(lastAnswered, 0) < datetime('now','-3 minutes') "
					+ "ORDER BY (falseCount / overallCount) DESC,	lastAnswered";

			return getPS(c, querry);
		}
		
		
//		select * from (select Question.id, Question.questiontext, Question.answer1, Question.answer2, Question.answer3, Question.answer4, (IFNULL(lastAnswered,0)) as realLast from Question 
//		LEFT JOIN Question_data on Question.id = Question_data.QuestionID left join `User` on `User`.id = Question_data.UserID 
//		LEFT JOIN Topic on Question.TopicID = Topic.id
//		where Topic.title = ?
//		ORDER BY (falseCount / overallCount) DESC, lastAnswered) as asd  where 
//		 asd.realLast != (select max(lastAnswered) from Question_data)
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
				"select (sum(overallCount) - sum(falseCount))* level_value as blub, User.`name` from Question_data join Question on QuestionID = Question.id join `User` on `User`.id = Question_data.UserID GROUP BY UserID order by blub DESC");
	}
	
	public static PreparedStatement addCategory(java.sql.Connection c) {
		if (c != null) {
			return getPS(c, "INSERT INTO `Topic`(title, description) VALUES(?, ' ')");
		} else {
			return null;
		}
	}

	public static PreparedStatement removeCategory(java.sql.Connection c) {
		if (c != null) {
			return getPS(c, "DELETE FROM `Topic` WHERE ((`title` = ?))");
		} else {
			return null;
		}
	}
	
	public static PreparedStatement updateCategory(java.sql.Connection c) {
		if (c != null) {
			return getPS(c, "UPDATE `Topic` SET title = ? WHERE id = ?");
		} else {
			return null;
		}
	}
	public static PreparedStatement getFrageAllCategories(java.sql.Connection c, boolean isLight){
		if( c != null){
			if(isLight){
				return getPS(c,"select Question.id, Question.questiontext, Question.answer1,	Question.answer2,	Question.answer3,	Question.answer4,	image,	video,	audio, level_value FROM	Question  ORDER BY RANDOM() limit 1");
			}
			return getPS(c,"select Question.id, Question.questiontext, Question.answer1,	Question.answer2,	Question.answer3,	Question.answer4,	image,	video,	audio, level_value FROM	Question  ORDER BY rand() limit 1"); 
		}
		return null;
	}
	
	
	public static PreparedStatement getFrageFromWrong(java.sql.Connection c, boolean isLight){
		if( c != null){
			if(isLight){
				return getPS(c,"select Question.id, Question.questiontext, Question.answer1,	Question.answer2,	Question.answer3,	Question.answer4,	image,	video,	audio, level_value FROM	Question join Question_data on Question.id = Question_data.QuestionID where UserID = ? and falseCount > 0 ORDER BY RANDOM() limit 1");
			}
			return getPS(c,"select Question.id, Question.questiontext, Question.answer1,	Question.answer2,	Question.answer3,	Question.answer4,	image,	video,	audio, level_value FROM	Question join Question_data on Question.id = Question_data.QuestionID where UserID = ? and falseCount > 0 ORDER BY RAND() limit 1"); 
		}
		return null;
	}
	
	public static PreparedStatement getRandomIfEmpty(java.sql.Connection c, boolean isLight){
		if( c != null){
			if(isLight){
				return getPS(c,"select Question.id, Question.questiontext, Question.answer1,	Question.answer2,	Question.answer3,	Question.answer4,	image,	video,	audio, level_value FROM	Question join Topic on Question.TopicID = Topic.id where Topic.title = ? ORDER BY RANDOM() limit 1");
			}
			return getPS(c,"select Question.id, Question.questiontext, Question.answer1,	Question.answer2,	Question.answer3,	Question.answer4,	image,	video,	audio, level_value FROM	Question join Topic on Question.TopicID = Topic.id where Topic.title = ? ORDER BY rand() limit 1"); 
		}
		return null;
	}
	public static PreparedStatement insertQuerry(java.sql.Connection c){
		if( c != null){
			return getPS(c,"insert into Question(TopicId,level_value,image,video,audio,questiontext,answer1,answer2,answer3,answer4,solution) VALUES(?,?,?,?,?,?,?,?,?,?,?)");
		}
		return null;
	}
}
