package com.example.demo.db;

import com.example.demo.kafka.Article;
import com.example.demo.user.UserSignupPayload;
import com.example.demo.user.WriterProfile;
import lombok.extern.log4j.Log4j2;

import java.sql.*;

@Log4j2
public class DBConnection {

    String dbName = "contentplatformdb";
    String userName = "root";
    String password = "liweraroot";
    String hostname = "contentplatformdb.cqwrvgf9ovof.ap-south-1.rds.amazonaws.com";
    String port = "3306";
    Connection conn = getRemoteConnection();

    private   Connection getRemoteConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String jdbcUrl = "jdbc:mysql://" + hostname + ":" + port + "/" + dbName + "?user=" + userName + "&password=" + password;
            Connection con = DriverManager.getConnection(jdbcUrl);
            return con;
        }
        catch (ClassNotFoundException | SQLException e) { log.warn(e.toString());}

        log.info("connection not established ");
        return null;
    }

    public boolean AddToDataBase(Article article){
        try {
            Statement stmt = conn.createStatement();
            String sql = "";
            String QUERY = "SELECT  article_id from articles where article_id = '"+ article.getArticleId() +"'";
            ResultSet rs = stmt.executeQuery(QUERY);


            if(rs.next() && rs.getString("article_id").toString().equals(article.getArticleId().toString())){

                if(article.getStatus().toString().equals("Update"))
                {
                    sql = "UPDATE articles SET body = '" + article.getBody() + "' , category = '" + article.getCategory() + "'  , title = '" + article.getTitle() + "'  , record_time_stamp = '" + article.getRecordTimeStamp() + "', status = '" + article.getStatus() + "' , title = '" + article.getTitle() + "' where article_id = '" + article.getArticleId() + "' and writer_name = '" + article.getWriterName() + "' ";

                } else if (article.getStatus().toString().equals("DELETE")) {
                    sql = "DELETE from articles where article_id = '"+ article.getArticleId() +"'";
                }
                else{
                    return false;
                }
            }
            else{

                if(article.getStatus().toString().equals("Update")||article.getStatus().toString().equals("Create")) {

                    sql = "INSERT INTO articles (article_id, body, category, record_date, record_time_stamp, status, title, writer_name)  VALUES " +
                            "('" + article.getArticleId() + "', '" + article.getBody() + "', '" + article.getCategory() + "', '" + article.getRecordDate() + "', '" +
                            article.getRecordTimeStamp() + "', '" + article.getStatus() + "', '" + article.getTitle() + "', '" + article.getWriterName() + "')";
                }
                else
                {
                    return false;
                }
            }

            log.info(sql);
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            log.warn(e.toString());
            return false;
        }

        return true;
    }

    public boolean AddUserToDB(UserSignupPayload userSignupPayload){

        try {
            Statement stmt = conn.createStatement();
            String sql = "INSERT INTO Users(email, userName, subscribedCategories, userRole) VALUES('"+userSignupPayload.getEmail()+"', '"+userSignupPayload.getUserName()+
                    "', '"+userSignupPayload.getSubscribedCategories()+"', '"+userSignupPayload.getUserRole()+"');";
            stmt.executeUpdate(sql);

        } catch (SQLException e) {
            log.warn(e.toString());
            return false;
        }

        return true;
    }

    public boolean AddWriterToDB(WriterProfile writerProfile){

        try {
            Statement stmt = conn.createStatement();
            String sql = "";
            String QUERY = "SELECT email from Users where email = '"+ writerProfile.getEmail() +"'";
            log.info(QUERY);
            ResultSet rs = stmt.executeQuery(QUERY);


            if(rs.next() && rs.getString("email").toString().equals(writerProfile.getEmail().toString())) {

                sql = "INSERT INTO Writer (email, writerName, country, description) VALUES('"+writerProfile.getEmail()+"', '"+writerProfile.getWriterName()
                        +"', '"+writerProfile.getCountry()+"', '"+writerProfile.getDescription()+"')";
                stmt.executeUpdate(sql);
                return true;
            }else{
                log.warn("not a logged user");
                return false;
            }

        } catch (SQLException e) {
            log.warn(e.toString());
            return false;
        }
    }

}
