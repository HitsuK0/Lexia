package be.hers.info.ProjetIntegree.DAO;

import be.hers.info.ProjetIntegree.POJO.TimeSlotBase;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class DAOTimeSlotBase  extends DAO<TimeSlotBase> {




    public void closeStatement(PreparedStatement statement){
        if(statement != null){
            try{
                statement.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
    }

    public void closeResultSet(ResultSet resultSet){
        if(resultSet != null){
            try{
                resultSet.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
    }



    public TimeSlotBase find(int objectToSearchInDB)throws SQLException {
        TimeSlotBase timeSlotBase = null;
        String query = "SELECT * FROM TimeSlotBase WHERE numTimeSlot = ?";
        PreparedStatement prStat = null;
        ResultSet resultSet = null;

        try{
            prStat = connect.prepareStatement(query);
            prStat.setInt(1, objectToSearchInDB);
            resultSet = prStat.executeQuery();

            /*int numTimeSlotBase, LocalTime startTime, LocalTime duration, int dayNumber*/
            if(resultSet.next()){
                LocalTime start = resultSet.getTime("startTime").toLocalTime();
                LocalTime duration = resultSet.getTime("duration").toLocalTime();

                timeSlotBase = new TimeSlotBase(objectToSearchInDB,start, duration, resultSet.getInt("dayNumber"));
            }

        }finally{
           closeStatement(prStat);
           closeResultSet(resultSet);
        }

        return timeSlotBase;

    }


    public List<TimeSlotBase> findAll() throws SQLException {
        List<TimeSlotBase> listTimeSlotBase = new ArrayList<>();
        String query = "SELECT * FROM TimeSlotBase";
        PreparedStatement prStat = null;
        ResultSet resultSet = null;
        try{
            prStat = connect.prepareStatement(query);
            resultSet = prStat.executeQuery();

            while(resultSet.next()){
                LocalTime start = resultSet.getTimestamp("startTime").toLocalDateTime().toLocalTime();
                LocalTime duration = resultSet.getTimestamp("duration").toLocalDateTime().toLocalTime();

                TimeSlotBase timeSlotBase = new TimeSlotBase(resultSet.getInt("numTimeSlot"),start,duration, resultSet.getInt("dayNumber"));
                listTimeSlotBase.add(timeSlotBase);
            }

        }finally{
            closeStatement(prStat);
            closeResultSet(resultSet);
        }
        return listTimeSlotBase;

    }


    public boolean create(TimeSlotBase timeSlotBase) throws SQLException {
        boolean estInseree = false;
        String query = "INSERT INTO TimeSlotBase(numTimeSlotBase,startTime,duration,dayNumber)  VALUES (?,?,?,?)";
        PreparedStatement prStat = null;

        try{
            prStat = connect.prepareStatement(query);


            LocalDate dateDuJour = LocalDate.now();


            prStat.setInt(1, timeSlotBase.getNumTimeSlot());
            prStat.setTime(2, java.sql.Time.valueOf(timeSlotBase.getStartTime()));
            prStat.setTime(3,  java.sql.Time.valueOf(timeSlotBase.getStartTime()));
            prStat.setInt(4, timeSlotBase.getDayNumber());

            int nbrLigneInsert = prStat.executeUpdate();
            if(nbrLigneInsert > 0){
                estInseree = true;
            }
        } finally {
            closeStatement(prStat);
        }

        return estInseree ;
    }
/*
ici j'ai :

            prStat.setTime(2, java.sql.Date.valueOf(timeSlotBase.getStartTime().atDate(dateDuJour).toLocalDate()));
            prStat.setTime(3, java.sql.Date.valueOf(timeSlotBase.getDuration().atDate(dateDuJour).toLocalDate()));


         public class Date
extends java.util.Date

A thin wrapper around a millisecond value that allows JDBC to identify this as an SQL DATE value. A milliseconds value represents the number of milliseconds that have passed since January 1, 1970 00:00:00.000 GMT.
To conform with the definition of SQL DATE, the millisecond values wrapped by a java.sql.Date instance must be 'normalized' by setting the hours, minutes, seconds, and milliseconds to zero in the particular time zone with which the instance is associated.
Since:
1.1

 */
    public boolean update(TimeSlotBase objectToUpdateInDB) throws SQLException {

        boolean estModifier = false;
        String query = "UPDATE objectToUpdateInDB SET  startTime = ?, duration = ?, dayNumber = ? where numTimeSlot = ?";
        PreparedStatement prStat = null;

        try {
            prStat = connect.prepareStatement(query);


            prStat.setTime(1, java.sql.Time.valueOf(objectToUpdateInDB.getStartTime()));
            prStat.setTime(2,  java.sql.Time.valueOf(objectToUpdateInDB.getStartTime()));
            prStat.setInt(3, objectToUpdateInDB.getDayNumber());
            prStat.setInt(4, objectToUpdateInDB.getNumTimeSlot());

            int nbrLigneInsert = prStat.executeUpdate();
            if(nbrLigneInsert > 0){
                estModifier = true;
            }
        }finally{
            closeStatement(prStat);
        }

        return estModifier;
    }


    public boolean delete(TimeSlotBase objectToDeleteFormDB) {

        boolean estSupprime = false;
        String query = "DELETE FROM TimeSlotBase WHERE numTimeSlotBase = ?";
        PreparedStatement prStat = null;

        try {
            prStat = connect.prepareStatement(query);
            prStat.setInt(1, objectToDeleteFormDB.getNumTimeSlot());

            int nbrLigneDelete = prStat.executeUpdate();

            if (nbrLigneDelete > 0) {
                estSupprime = true;
            }

        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            closeStatement(prStat);
        }

        return estSupprime;
    }

}
