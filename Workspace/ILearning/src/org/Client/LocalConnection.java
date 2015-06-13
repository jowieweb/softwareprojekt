package org.Client;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.Packet;
import org.sqlite.SQLiteDataSource;
import org.sqlite.SQLiteJDBCLoader;

public class LocalConnection extends Client {
private SQLiteDataSource dataSource;
	public LocalConnection(ClientListener listener) {
		super(listener);
		connect();
		// TODO Auto-generated constructor stub
	}

	private void connect() {
		try {
			boolean initialize = SQLiteJDBCLoader.initialize();
			dataSource = new SQLiteDataSource();
			dataSource
					.setUrl("jdbc:sqlite:local.db");
			int i = 1;
		

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void sendPacket(Packet packet) throws TCPClientException {
		// TODO Auto-generated method stub

	}
	
	
	public void insert(Packet p){
		
		
		String[] all = p.getQuestion().split(";");
		for (String s : all) {
			if (s.length() > 2) {
				try {
					boolean b = dataSource.getConnection().createStatement()
							.execute(s);
					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
			}
		}
		try {
			ResultSet executeQuery = dataSource.getConnection()
					.createStatement().executeQuery("select * from Topic");
			while(executeQuery.next()){
				System.out.println(executeQuery.getString(1));
			}
		}catch(Exception e){
			
		}
	}

	private String[] getDump() {
		String blub = "DROP TABLE IF EXISTS `Level`;CREATE TABLE `Level` (  `value` int(3) NOT NULL ,  `title` varchar(50) NOT NULL,  PRIMARY KEY (`value`));;INSERT INTO Level VALUES('1','leicht');INSERT INTO Level VALUES('2','mittel');INSERT INTO Level VALUES('3','schwer');DROP TABLE IF EXISTS `Question`;CREATE TABLE `Question` (  `id` int(3) NOT NULL ,  `TopicID` int(3) NOT NULL,  `level_value` int(3) NOT NULL,  `image` varchar(100) DEFAULT NULL,  `video` varchar(100) DEFAULT NULL,  `audio` varchar(100) DEFAULT NULL,  `questiontext` varchar(1000) DEFAULT NULL,  `answer1` varchar(250) DEFAULT NULL,  `answer2` varchar(250) DEFAULT NULL,  `answer3` varchar(250) DEFAULT NULL,  `answer4` varchar(250) DEFAULT NULL,  `solution` int(1) DEFAULT NULL,  PRIMARY KEY (`id`));;INSERT INTO Question VALUES('1','1','1','url.jpg','','','Erläutern Sie den Begriff  Informatik !','Informatik ist eine Begriffsverschmelzung aus den beiden Wörtern Information und Automatik . Informatik umfasst allgemein die automatisierte Informationsverarbeitung in Natur, Technik und Gesellschaft.','Informatik ist eine Begriffsverschmelzung aus den beiden Wörtern Information und Mathematik .','Informatik ist eine Begriffsverschmelzung aus den beiden Wörtern infotainment und Automatik .','Informatik ist eine Begriffsverschmelzung aus den beiden Wörtern infotainment und Mathematik .','1');INSERT INTO Question VALUES('2','1','1','','','','Welche englischen Begriffe gelten für den Wissenschaftszweig  Informatik?','Engl: Computer Electronics Science, Information Electronics Systems','Engl: Science of Informations','Engl: Computer Science, Information Systems, Informatics','Engl: Computer Art, Information Systems, Informatics','3');INSERT INTO Question VALUES('3','1','1','','','','Definieren Sie den Begriff  Computer !','Ein Computer ist ein rechengerät.','Ein  Computer (dt. Rechner) ist ein technisches informationsverarbeitendes System, das aus Einheiten für die Eingabe, Verarbeitung, Speicherung und Ausgabe der Information besteht.','Ein Computer ist ein Kommunikationsgerät.','Ein Computer ist eine Maschine, die vieles kann.','2');INSERT INTO Question VALUES('4','1','1','','','','Definieren Sie den Begriff  Hardware !','Technische Geräte','Gesamtheit aller physischen, d.h. materiellen Komponenten eines Rechensystems – HW (Abkürzung)','Technische Komponenten eines Rechensystems','','2');INSERT INTO Question VALUES('5','1','1','','','','Definieren Sie den Begriff  Software !','Ideelle Ausrüstung des Rechensystems, d.h. Programme, deren zugrunde liegendeVerfahren (Algorithmen) und deren Dokumentation – SW (Abkürzung)','Ist ein Algorithmus.','Ist ein Programm.','','1');INSERT INTO Question VALUES('6','2','2','','','','Was ist ein Abakus?','Der Abakus ist ein Rechenbrett mit Kugeln, meist Holz- oder Glasperlen.Nutzung bereits vor mehr als 3000 Jahren in China','Der Abakus ist ein Mathematik Spielzeug','Der Abakus ist ein Rechenbrett mit Kugeln, meist Holz- oder Glasperlen.Nutzung bereits vor genau 2000 Jahren in Indien','Der Abakus ist ein Rechenbrett mit Kugeln, meist Holz- oder Glasperlen.Nutzung bereits vor genau 2000 Jahren in China','1');INSERT INTO Question VALUES('7','2','3','','','','Seit wann ist die Nutzung eines Abakus bekannt?','Nutzung bereits vor mehr als 2000 Jahren in Indien','Nutzung bereits vor mehr als 2000 Jahren in China','Nutzung bereits vor mehr als 3000 Jahren in China','Nutzung bereits vor mehr als 3000 Jahren in Indien','3');INSERT INTO Question VALUES('8','2','2','Eine URL fehlt','','','Welche Zahl wird auf dem Abakus Suanpan und Soroban dargestellt?','12345','654321','','','1');INSERT INTO Question VALUES('9','2','2','','','','Was ist ein Algorithmus?','Der Algorithmus ist eine Verarbeitungsvorschrift, die von einer Maschine oder auchvon einem Menschen durchgeführt werden kann.','Der Algorithmus ist ein mathematisches Verfahren um etwas exakt zuberechnen.','Ein Algorithmus ist eine mathematische Gleichung mit eigene zu definierenden Variablen.','','1');INSERT INTO Question VALUES('10','2','3','','','','In welchem Jahrhundert lebte Adam Riese? Welches waren seine größten Verdienstein Bezug auf die Informatik?','Adam Riese (1591-1636) veröffentlicht Rechengesetze zum Dezimalsystem inEuropa (stammen ursprünglich aus Indien)','Adam Riese (1588-1635) veröffentlicht Rechengesetze zum Dezimalsystem inEuropa (stammen ursprünglich aus China)','Adam Riese (1592-1645) veröffentlicht Rechengesetze zum Dezimalsystem inEuropa (stammen ursprünglich aus China)','Adam Riese (1592-1635) veröffentlicht Rechengesetze zum Dezimalsystem inEuropa (stammen ursprünglich aus Indien)','4');INSERT INTO Question VALUES('11','2','3','','','','In welchem Jahrhundert lebte Wilhelm Schickard? Welches waren seine größtenVerdienste in Bezug auf die Informatik?','Wilhelm Schickard (1591-1638) konstruiert 1624 erste Rechenmaschine für seinenFreund Keppler','Wilhelm Schickard (1592-1637) konstruiert 1625 erste Rechenmaschine für seinenFreund Keppler','Wilhelm Schickard (1592-1635) konstruiert 1623 erste Rechenmaschine für seinenFreund Keppler','Wilhelm Schickard (1591-1637) konstruiert 1622 erste Rechenmaschine für seinenFreund Keppler','3');INSERT INTO Question VALUES('12','2','3','','','','In welchem Jahrhundert lebte Blaise Pascal? Welches waren seinegrößten Verdienste in Bezug auf die Informatik?','Blaise Pascal (1623-1662): entwickelte die Programmiersprache PASCAL','Blaise Pascal (1593-1642): entwickelte die Programmiersprache C++','Blaise Pascal (1581-1642): entwickelte die Programmiersprache C','','1');INSERT INTO Question VALUES('13','2','3','','','','In welchem Jahrhundert lebte Gottfried Wilhelm Leibnitz? Welches waren seinegrößten Verdienste in Bezug auf die Informatik?','Gottfried Wilhelm Leibnitz (1646 - 1718):konstruierte 1672 Rechenmaschine mit Staffelwalzen für die vierGrundrechenarten (also auch Multiplikation und Division)','Gottfried Wilhelm Leibnitz (1648 - 1718):konstruierte 1670 Rechenmaschine mit Staffelwalzen für die vierGrundrechenarten (also auch Multiplikation und Division)','Gottfried Wilhelm Leibnitz (1646 - 1716):konstruierte 1674 Rechenmaschine mit Staffelwalzen für die vierGrundrechenarten (also auch Multiplikation und Division)','','3');INSERT INTO Question VALUES('14','2','3','','','','In welchem Jahrhundert lebte Philipp Matthäus Hahn? Welches waren seinegrößten Verdienste in Bezug auf die Informatik?','Philipp Matthäus Hahn (1720 - 1775):entwickelte 1753 erste mechanische Rechenmaschine mit bis zu 10 Stellen, die auch zuverlässig arbeitete','Philipp Matthäus Hahn (1739 - 1790):entwickelte 1774 erste mechanische Rechenmaschine mit bis zu 14 Stellen, die auch zuverlässig arbeitete','Philipp Matthäus Hahn (1728 - 1767):entwickelte 1744 erste mechanische Rechenmaschine mit bis zu 12 Stellen, die auch zuverlässig arbeitete','Philipp Matthäus Hahn (1752 - 1823):entwickelte 1787 erste mechanische Rechenmaschine mit bis zu 16 Stellen, die auch zuverlässig arbeitete','2');INSERT INTO Question VALUES('15','3','3','','','','Welche Art von Benutzerschnittstellen sind heute fast nur noch anzutreffen','Konsolen','graphikfreie Bedienoberfläche','graphische Bedienoberfla?chen','','3');INSERT INTO Question VALUES('16','3','2','','','','Charakterisieren Sie den Kernbereich „Theoretische Informatik“!','Es ist zum Teil eine Grundlage fu?r die Kernbereiche Technische und Praktische Informatik','Es ist eine Grundlage fu?r die Kernbereiche Technische und Praktische Informatik','Es ist eine Grundlage fu?r die Kernbereiche Angewandte und Theoretische Informatik','Es ist eine Grundlage fu?r die Kernbereiche Theoretische und Praktische Informatik','2');INSERT INTO Question VALUES('17','3','2','','','','Charakterisieren Sie den Kernbereich „Theoretische Informatik“!','Es ist zum Teil eine Grundlage fu?r die Kernbereiche Technische und Praktische Informatik','Es ist eine Grundlage fu?r die Kernbereiche Technische und Praktische Informatik','Es ist eine Grundlage fu?r die Kernbereiche Angewandte und Theoretische Informatik','Es ist eine Grundlage fu?r die Kernbereiche Theoretische und Praktische Informatik','2');INSERT INTO Question VALUES('18','3','3','','','','Charakterisieren Sie den Kernbereich „Praktische Informatik“!','bescha?ftigt sich mit den Programmen, die ein System steuern','bescha?ftigt sich mit den Programmen, die ein Prgramm wiederum steuert','bescha?ftigt sich mit den Prozessen, aus den ein System besteht','bescha?ftigt sich mit den Logik, die hinter einem System steckt','1');INSERT INTO Question VALUES('19','3','2','','','','In welchen Bereich untergliedert sich der Kernbereich „Praktische Informatik“ NICHT','Programmiersprachen, Compiler und Interpreter','Komplexitätstherie','Datenbanken','Betriebssysteme und Netzwerke','2');INSERT INTO Question VALUES('20','3','3','','','','Charakterisieren Sie den Kernbereich „Technische Informatik“!','bescha?ftigt sich mit den Grundlagen der Software','bescha?ftigt sich mit den Grundlagen der Programmiersprachen','bescha?ftigt sich mit den Grundlagen der Softwarearchitektur','bescha?ftigt sich mit den Grundlagen der Hardware','4');INSERT INTO Question VALUES('21','3','3','','','','In welche Bereiche untergliedert sich der Kernbereich „Technische Informatik“ NICHT','Mikroprozessortechnik','Rechnerkommunikation','Rechnerarchitektur','Komplexitätstherie','4');INSERT INTO Question VALUES('22','3','3','','','','Charakterisieren Sie den Kernbereich „Angewandte Informatik“!','Verwendung der Resultate aus den drei anderen Kerngebieten fu?r Hard- und Softwarerealisierungen','Verwendung der Resultate aus dem Kerngebiet der Praktischen Informatik fu?r Hard- und Softwarerealisierungen','','','1');INSERT INTO Question VALUES('23','3','3','','','','In welche 2 allgemeinen Anwendungsgebiete untergliedert sich der Kernbereich „Angewandte Informatik“?','Wirtschaftliche, kommerzielle Anwendungen','Theoretisch-wissenschaftliche Anwendungen','','','1');INSERT INTO Question VALUES('24','3','3','','','','Welche von den 4 Auswahlmöglichekiten sind nicht Teil der bedeutende interdisplina?re Gebiete der Informatik','Wirtschaftsinformatik','Ku?nstliche Intelligenz','Bioinformatik','Kognitive Informatik','4');INSERT INTO Question VALUES('25','3','3','','','','Unter welchem Thema steht das Wissenschaftsjahr 2006','Wissenschaftsjahr 2006','Informatikjahr 2006','Computerjahr 2006','Personal-Computing Jahr 2006','2');INSERT INTO Question VALUES('26','4','2','','','','Wo und wann entstand das Zehnersystem?','ca 500 n. Chr. in Südchina','ca 1000 n. Chr. in Nordindien','ca 700 n. Chr. in Südindien','','2');INSERT INTO Question VALUES('27','4','2','','','','Welche Ziffer des römischen Zahlensystems hat hier den falschen Wert?','X = 10','D = 500','V = 5','C = 50','4');INSERT INTO Question VALUES('28','4','2','','','','Welche römische Zahl bildet die Zahl 1374 korrekt ab?','MCCCLXXIV','MDCCLXXIV','MCCLLLXIV','MCCDXXIV','1');INSERT INTO Question VALUES('29','4','2','','','','Welche Zahl des Zehnersystems entspricht der römischen Zahl DCCLXXIX?','752','881','579','779','4');DROP TABLE IF EXISTS `Question_data`;CREATE TABLE `Question_data` (  `id` int(3) NOT NULL ,  `UserID` int(3) NOT NULL,  `QuestionID` int(3) NOT NULL,  `lastAnswered` date NOT NULL,  `falseCount` int(4) NOT NULL,  `overallCount` int(4) NOT NULL,  PRIMARY KEY (`id`));;INSERT INTO Question_data VALUES('1','1','1','2015-06-12','3','12');INSERT INTO Question_data VALUES('2','1','3','2015-06-12','4','30');INSERT INTO Question_data VALUES('3','1','4','2015-06-12','1','20');INSERT INTO Question_data VALUES('4','1','5','2015-06-12','2','17');INSERT INTO Question_data VALUES('5','1','2','2015-06-12','3','16');INSERT INTO Question_data VALUES('6','13','5','2015-06-09','0','1');INSERT INTO Question_data VALUES('7','13','2','2015-06-09','0','2');DROP TABLE IF EXISTS `Topic`;CREATE TABLE `Topic` (  `id` int(3) NOT NULL ,  `title` varchar(100) NOT NULL,  `description` varchar(250) DEFAULT NULL,  PRIMARY KEY (`id`));;INSERT INTO Topic VALUES('1','Einstiegsveranstaltung','Zum Aufwärmen');INSERT INTO Topic VALUES('2','Geschichte der Informatik','Die Geschichte');INSERT INTO Topic VALUES('3','Gesellschaft und Informatik','Allgemeine Fragen ?ber die Gesellschaft zum Thema Informatik');INSERT INTO Topic VALUES('4','Informationsspeicherung','Verschiedene Zahlensysteme');DROP TABLE IF EXISTS `User`;CREATE TABLE `User` (  `id` int(3) NOT NULL ,  `name` varchar(50) NOT NULL,  `surname` varchar(50) NOT NULL,  `email` varchar(50) NOT NULL,  `password` varchar(20) NOT NULL,  `admin` tinyint(1) DEFAULT '0',  PRIMARY KEY (`id`));;INSERT INTO User VALUES('1','klaus','hallo','asd@asd.de','1','true');INSERT INTO User VALUES('13','2',' ',' ','<asdfg','false');INSERT INTO User VALUES('16','blubber',' ',' ','2','false');";

		String as[] = blub.split(";");
		return as;

	}

}
