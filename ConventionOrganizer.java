import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

//This program allows a user to create databases and tables, and insert, update
//and delete entries to organize a conventions' events' information

public class ConventionOrganizer {
    
    //The name of the database
    public static String fileName;
    

    /**
     * Create a new database
     */
    public static void createNewDatabase() {
 
        //The url with the local path to the database
        String url = "jdbc:sqlite:C:\\Users\\batma\\Downloads\\sqlite\\db\\" + fileName;
 
        //Create the database
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The name of the driver is " + meta.getDriverName());
                System.out.println("A new database has been created.\n");
            }
 
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Connect to a database
     */
    public static void connect(){
        
        Connection conn = null;
        
        try{
            
            String url = "jdbc:sqlite:C:\\Users\\batma\\Downloads\\sqlite\\db\\" + fileName;
            
            //Connect to the database
            conn = DriverManager.getConnection(url);
            
            System.out.println("Connection to the database is successful.\n");
            
        } catch (SQLException e){
            
            System.out.println(e.getMessage());
            
        } finally{
            
            try{
                //Close the connection
                if(conn != null){
                    conn.close();
                }
                
            } catch (SQLException ex){
                
                System.out.println(ex.getMessage());
            }
        }
    }
    

    /**
     * Create a new table
     * @param tableName 
     */
    public static void createNewTable(String tableName) {
        
        //Url for the database
        String url = "jdbc:sqlite:C:\\Users\\batma\\Downloads\\sqlite\\db\\" + fileName;
        
        //SQLite statement to create a new table
        String sql = "CREATE TABLE IF NOT EXISTS " + tableName + "("
                + "     Id INTEGER PRIMARY KEY,\n"
                + "	Name text ,\n"
                + "	Date text ,\n"
                + "	Time text ,\n"
                + "     Room text ,\n"
                + "     Price real,\n"
                + "     Notes text\n"
                + ");";
        
        //Connect to the database
        try (Connection conn = DriverManager.getConnection(url);
                Statement stmt = conn.createStatement()) {
            
            //Execute the statement to create the new table
            stmt.execute(sql);
            
            //Display the results
            System.out.println("A new table has been created.\n");
            
        } catch (SQLException e) {
            
            System.out.println(e.getMessage());
        }
    }


    /**
     * View the table
     * @param tableName 
     */
    public static void selectAll(String tableName){
        
        //The url of the database
        String url = "jdbc:sqlite:C:\\Users\\batma\\Downloads\\sqlite\\db\\" + fileName;
        
        //The SQL statement to get the data from the database
        String sql = "SELECT id, name, date, time, room, price, notes FROM " + tableName;
        
        //Connect to the database and display the data
        try (Connection conn = DriverManager.getConnection(url);
                
            Statement sm  = conn.createStatement();
                
            ResultSet rs    = sm.executeQuery(sql)){
            
            ResultSetMetaData md = rs.getMetaData();
            
            int count = md.getColumnCount();
            
            //Display the columns
            for(int i = 1; i <= count; i++){
                System.out.printf("%-20s", md.getColumnName(i));
            }
            
            System.out.println("");
            
            //Display the data
            while (rs.next()) {
                System.out.printf("%-20d%-20s%-20s%-20s%-20s%-20.2f%-20s\n",
                        rs.getInt("id"),
                        rs.getString("name") , rs.getString("date"), 
                        rs.getString("time"), rs.getString("room"),
                        rs.getDouble("price"), rs.getString("notes"));
            }
            
            System.out.println("\n\n");
            
        } catch (SQLException e) {
            
            System.out.println(e.getMessage());
        }
    }
    
    /**
     * Insert into the database table
     * @param tableName The name of the table
     * @param name The name
     * @param date The date of the event
     * @param time The time of the event
     * @param room The room of the event
     * @param price The price
     * @param notes Any notes
     */
    public static void insert(String tableName, String name, String date, String time, 
            String room, double price, String notes) {
        
        //Url for the database
        String url = "jdbc:sqlite:C:\\Users\\batma\\Downloads\\sqlite\\db\\" + fileName;
        
        //Insert into the database
        String sql = "INSERT INTO " + tableName + "(name, date, time, room, price, notes) VALUES(?,?,?,?,?,?)";
 
        //Connect to the database
        try (Connection conn = DriverManager.getConnection(url);
                PreparedStatement ps = conn.prepareStatement(sql)) {
            
            //Set the parameters
            ps.setString(1, name);
            ps.setString(2, date);
            ps.setString(3, time);
            ps.setString(4, room);
            ps.setDouble(5, price);
            ps.setString(6, notes);
            
            //Execute the update
            ps.executeUpdate();
            
            //Display the results
            System.out.println("Data successfully added.\n");
            
        } catch (SQLException e) {
            
            System.out.println(e.getMessage());
        }
    }
    
    /**
     * Update an entry in the database table
     * @param tableName
     * @param id
     * @param name
     * @param date
     * @param time
     * @param room
     * @param price
     * @param notes 
     */
    public static void update(String tableName, int id, String name, String date, String time, 
            String room, double price, String notes) {
        
        //Url for the database
        String url = "jdbc:sqlite:C:\\Users\\batma\\Downloads\\sqlite\\db\\" + fileName;
        
        //SQL statement
        String sql = "UPDATE " + tableName + " SET name = ? , "
                + "date = ? ,"
                + "time = ? ,"
                + "room = ? ,"
                + "price = ? ,"
                + "notes = ? "
                + "WHERE id = ?";
 
        //Connect to the database
        try (Connection conn = DriverManager.getConnection(url);
                PreparedStatement ps = conn.prepareStatement(sql)) {
 
            //Set the parameters
            ps.setString(1, name);
            ps.setString(2, date);
            ps.setString(3, time);
            ps.setString(4, room);
            ps.setDouble(5, price);
            ps.setString(6, notes);
            ps.setInt(7, id);
            
            //Execute the update
            ps.executeUpdate();
            
            //Display the results
            System.out.println("Update successful.\n");
            
        } catch (SQLException e) {
            
            System.out.println(e.getMessage());
        }
    }
    
    /**
     * Delete an entry using the id
     * @param tableName
     * @param id 
     */
    public static void delete(String tableName, int id) {
        
        //Url for the database
        String url = "jdbc:sqlite:C:\\Users\\batma\\Downloads\\sqlite\\db\\" + fileName;
        
        //SQL statement
        String sql = "DELETE FROM " + tableName + " WHERE id = ?";
 
        //Connect to the database
        try (Connection conn = DriverManager.getConnection(url);
                PreparedStatement ps = conn.prepareStatement(sql)) {
 
            //Set the parameter
            ps.setInt(1, id);
            
            //Delete the entry
            ps.executeUpdate();
            
            //Display the results
            System.out.println("Delete successful.\n");
 
        } catch (SQLException e) {
            
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        
        //Create scanner object and declare variables
        Scanner input = new Scanner(System.in);
        int choice;
        String tableName;
        
        //Options for the user to choose
        String options = "Options:\n"
                + "0: Exit\n"
                + "1: Create new Convention Event\n"
                + "2: Open Convention Event\n"
                + "3: Create new table for Autograph, PhotoOp, Panel, or Other\n"
                + "4: View Table\n"
                + "5: Insert into table\n"
                + "6: Update an item in a table\n"
                + "7: Delete an item in a table\n"
                + "Enter option number: ";
        
        //Get the user input
        System.out.println(options);
        choice = input.nextInt();
        input.nextLine();

        //Process the user input until the user would like to exit
        while(choice != 0){
            switch(choice){
                case 0:
                    break;
                case 1:
                    System.out.print("Enter new file name for event (Ex: NYCC.db): ");                   
                    fileName = input.nextLine();
                    createNewDatabase();
                    break;
                case 2:
                    System.out.print("Enter file name to open for event: (EX: NYCC.db): ");
                    fileName = input.nextLine();
                    connect();
                    break;
                case 3:
                    System.out.print("Enter file name to open for event: (EX: NYCC.db): ");
                    fileName = input.nextLine();
                    System.out.print("Enter a table name: ");
                    tableName = input.nextLine();
                    createNewTable(tableName);
                    break;  
                case 4:
                    System.out.print("Enter file name to open for event (EX: NYCC.db): ");
                    fileName = input.nextLine();
                    System.out.print("Enter a table name: ");
                    tableName = input.nextLine();
                    System.out.println("");
                    selectAll(tableName);
                    break;
                 case 5:
                    System.out.print("Enter file name to open for event (EX: NYCC.db): ");
                    fileName = input.nextLine();
                    System.out.print("Enter a table name: ");
                    tableName = input.nextLine();
                    System.out.print("Enter Name: "); 
                    String name = input.nextLine();
                    System.out.println("Enter Date: ");
                    String date = input.nextLine();
                    System.out.print("Enter Time: "); 
                    String time = input.nextLine();
                    System.out.println("Enter Room #: ");
                    String room = input.nextLine();
                    System.out.println("Enter Price: ");
                    Double price = input.nextDouble();
                    input.nextLine();
                    System.out.println("Enter any Notes: ");
                    String notes = input.nextLine();
                    
                    insert(tableName, name, date, time, room, price, notes);
                    break;  
                 case 6:
                    System.out.print("Enter file name to open for event (EX: NYCC.db): ");
                    fileName = input.nextLine();
                    System.out.print("Enter a table name: ");
                    tableName = input.nextLine();
                    selectAll(tableName);
                    System.out.println("Enter the Id you want to update: ");
                    int id = input.nextInt();
                    input.nextLine();
                    System.out.print("Enter Name: "); 
                    name = input.nextLine();
                    System.out.println("Enter Date: ");
                    date = input.nextLine();
                    System.out.print("Enter Time: "); 
                    time = input.nextLine();
                    System.out.println("Enter Room #: ");
                    room = input.nextLine();
                    System.out.println("Enter Price: ");
                    price = input.nextDouble();
                    input.nextLine();
                    System.out.println("Enter any Notes: ");
                    notes = input.nextLine();
                    
                    update(tableName, id, name, date, time, room, price, notes);
                    break;
                 case 7:
                    System.out.print("Enter file name to open for event (EX: NYCC.db): ");
                    fileName = input.nextLine();
                    System.out.print("Enter a table name: ");
                    tableName = input.nextLine();
                    selectAll(tableName);
                    System.out.println("Enter the Id you want to delete: ");
                    id = input.nextInt();
                    input.nextLine();
                    delete(tableName, id);
                    break;

            }
            //Display the options and get the new choice
            System.out.println(options);
            choice = input.nextInt();
            input.nextLine();
        }
        
    }
    
}
