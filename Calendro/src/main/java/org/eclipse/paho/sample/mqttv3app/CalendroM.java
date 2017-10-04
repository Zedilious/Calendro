//package for mqtt version 3
package org.eclipse.paho.sample.mqttv3app;

//import necessary libraries
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import javax.swing.border.LineBorder;
import java.text.Format;
import java.text.SimpleDateFormat;

public class CalendroM
{
	//set up global objects, eclipse was having a fuss so they are all 'static'
    static JLabel lblMonth, lblYear, lblToday, lblWelcome, lblFullDate; 			// java Labels 
    static JButton btnPrev, btnNext, btnProfile, paneDisplay, btnPro;				// java Buttons
    static JTable tblCalendro;														// java Table
	static JComboBox<String> comYear;												// combo box to store a range of years
    static JFrame frmMain;															// main Java Frame
    static Container pnMain;														// main container
    static DefaultTableModel mtblCalendro;											// table Model for the table to adhere to
    static JScrollPane stblCalendro;												// scroll pane to hold the table, in case of size issues
    static JPanel pnlCalendro, pnlRight, pnlProfile;								// java panels
    static int realYear, realMonth, realDay, currentYear, currentMonth;				// normal integers
    static Connect con = new Connect();												// create object Connect to call and process mqtt stuff 
    static String uname = "\"user\"";												// head of display username
    static URL relResources = CalendroM.class.getResource("/images/");				// create a relaitve pathway to the resource folder
    static String relPathway = relResources.toString();								// turn pathway into a string
    
	//initialise name of file and name of folder to save file to, stops errors in the event a date is not received.
	static String fileYear = "0000";
	static String folderName = "0000";
	
    public static void main (String args[])
    {
        //try getting look and feel class, catch all them errors so Eclipse doesn't bitch at me
        try 
        {
        	UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (ClassNotFoundException e) {}
        catch (InstantiationException e) {}
        catch (IllegalAccessException e) {}
        catch (UnsupportedLookAndFeelException e) {}
        
        //shorten pathway to remove invalid characters
        relPathway = relPathway.substring(6);
        
        //if (try connecting = true) print ....
        if(con.tryCon()){System.out.println("----- Connected -----");}
        
        //prepare the main frame
        frmMain = new JFrame ("Calendro Main Board");
        frmMain.setTitle("Calendro");
        frmMain.setSize(750, 450); 
        frmMain.getContentPane().setLayout(null);
        pnMain = frmMain.getContentPane();
        frmMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //create welcome banner, top left of main frame
        lblWelcome = new JLabel("Welcome " + uname + " - ");
        lblWelcome.setBounds(10, 11, 312, 36);
        lblWelcome.setFont(new Font("Arial", Font.BOLD, 14));
        lblFullDate = new JLabel("Full Date");
        lblFullDate.setBounds(142, 11, 165, 36);
        lblFullDate.setFont(new Font("Arial", Font.BOLD, 14));
        
        //add to main frame
        frmMain.getContentPane().add(lblWelcome);
        frmMain.getContentPane().add(lblFullDate);
        
        //create labels and buttons for the Calendro Panel, Set Font where necessary
        btnPrev = new JButton ("Previous");
        btnPrev.setFont(new Font("Arial", Font.BOLD, 12));
        btnNext = new JButton ("Next");
        btnNext.setFont(new Font("Arial", Font.BOLD, 12));
        lblMonth = new JLabel ("");
        lblMonth.setFont(new Font("Arial", Font.BOLD, 12));
        lblYear = new JLabel ("Change year:");
        lblYear.setFont(new Font("Arial", Font.BOLD, 12));
        
        //create table model, create table with the model, set font, need that consistency.
        mtblCalendro = new DefaultTableModel();
        tblCalendro = new JTable(mtblCalendro);
        tblCalendro.setFont(new Font("Arial", Font.BOLD, 12));
        
        //can click on cells
        tblCalendro.setCellSelectionEnabled(true);
        
        //needs scroll pane in case of sizing error.
        stblCalendro = new JScrollPane(tblCalendro);
        pnlCalendro = new JPanel(null);
        pnlCalendro.setBounds(10, 58, 533, 352);
        
        //comboBox for range of years
        comYear = new JComboBox<String>();
        comYear.setFont(new Font("Arial", Font.BOLD, 12));
        
        //border is set
        pnlCalendro.setBorder(new LineBorder(new Color(0, 0, 0)));       
        
        //set bounds of objects
        lblYear.setBounds(353, 315, 80, 20);
        comYear.setBounds(443, 315, 80, 20);
        btnPrev.setBounds(10, 11, 95, 25);
        btnNext.setBounds(428, 11, 95, 25);
        stblCalendro.setBounds(10, 44, 513, 255);
        
        //add main Panel to main pane, add all buttons and tables to main panel
        pnMain.add(pnlCalendro);
        pnlCalendro.add(btnPrev);
        pnlCalendro.add(btnNext);
        pnlCalendro.add(lblMonth);
        pnlCalendro.add(lblYear);
        pnlCalendro.add(comYear);
        pnlCalendro.add(stblCalendro);
        
        //create new side Panel (Right hand side)
        pnlRight = new JPanel();
        pnlRight.setBounds(548, 58, 186, 352);
        pnlRight.setLayout(null);
        
        //create label for side panel
        lblToday = new JLabel();
        lblToday.setFont(new Font("Arial", Font.BOLD, 18));
        lblToday.setHorizontalAlignment(SwingConstants.CENTER);
        lblToday.setBounds(127, 11, 49, 26);
        
        //add label to side panel
        pnlRight.add(lblToday);
        
        //add new panel to main frame
        frmMain.getContentPane().add(pnlRight);
        
        //new JButton for displaying the image retrieved from the mqtt server
        paneDisplay = new JButton();
        paneDisplay.setBounds(10, 48, 166, 300);
        paneDisplay.setEnabled(true);
        pnlRight.add(paneDisplay);
        
        //new panel for the profile icon
        pnlProfile = new JPanel();
        pnlProfile.setBounds(687, 11, 47, 36);
        pnlProfile.setLayout(null);
        frmMain.getContentPane().add(pnlProfile);
        
        //create button to hold the profile icon
        btnPro = new JButton();
        btnPro.setBounds(0, 0, 47, 36);
	    pnlProfile.add(btnPro);
	    
        //try getting and setting the profile icon
        try 
        {
        	BufferedImage imgProfile = ImageIO.read(CalendroM.class.getResource("/images/users.png")); 
        	
        	//change type to ImageIcon to be set on button
        	ImageIcon iconPro = new ImageIcon(imgProfile);
        	btnPro.setIcon(iconPro);
		} 
        catch (IOException e) 
        {
			e.printStackTrace();
		}
       
        //make the frame visible
        frmMain.setResizable(false);
        frmMain.setVisible(true);
        
        //create new gregorian calendar 
        GregorianCalendar cal = new GregorianCalendar();
        
        //get the real day with String version
        realDay = cal.get(GregorianCalendar.DAY_OF_MONTH);
        String day = Integer.toString(realDay);
        
        //get the real month
        realMonth = cal.get(GregorianCalendar.MONTH);
        String month = Integer.toString(realMonth + 1);
        
        //get the real year with String version
        realYear = cal.get(GregorianCalendar.YEAR);
        String year = Integer.toString(realYear);
        
        //set the current month/year variable to the real month/year retrieved  
        currentMonth = realMonth; 
        currentYear = realYear;
        
        //set today label as current day
        lblToday.setText(day);

        //load the formatter with the specified format
        Format formatter = new SimpleDateFormat("MMMM");
        //turns the month from a number to a word, was difficult to use the same method used in refreshCalendro()
        String s = formatter.format(new Date());
        //set current day in top welcome banner 
        lblFullDate.setText(realDay + " " + s + " " + realYear);
        
        //add days of week
        String[] days = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        for (int i=0; i<7; i++)
        {
            mtblCalendro.addColumn(days[i]);
        }
        
        //don't allow the table to be resized or reordered
        tblCalendro.getTableHeader().setResizingAllowed(false);
        tblCalendro.getTableHeader().setReorderingAllowed(false);
        
        //allow the selection of single cells and not whole rows and columns
        tblCalendro.setColumnSelectionAllowed(true);
        tblCalendro.setRowSelectionAllowed(true);
        tblCalendro.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        //set the height of the row and the amount of columns and rows
        tblCalendro.setRowHeight(38);
        mtblCalendro.setColumnCount(7);
        mtblCalendro.setRowCount(6);
        
        //populate the combo box with a range of 100 years, 50 before and 50 after the current year
        for (int i=realYear-50; i<=realYear+50; i++)
        {
            comYear.addItem(String.valueOf(i));
        }
        
        //refresh the calendar
        refreshCalendro (realMonth, realYear); 
        
        
        //load todays image
        if(day.length() < 2)
        {
        	day = "0" + day;
        }
    	if(month.length() < 2)
    	{
    		month = "0" + month;
    	}
    	
    	//create folder name
        String folder = month + day;
        
        //publish todays image if there is one
        publishImage(folder, year);
        
        //assign listeners to buttons and comBox
        btnPrev.addActionListener(new btnPrev_Action());
        btnNext.addActionListener(new btnNext_Action());
        comYear.addActionListener(new comYear_Action());
        tblCalendro.addMouseListener(new lblToday_Action());
    }
    
//--------------------------------------------------------- Public and Private Methods ---------------------------------------------------------
    
    
//--------------------------------------------------------- New Message ---------------------------------------------------------
    /*
     * receive new message of type String .
     * convert the received Base 64 string into a byte array
     * bitmap is saved as a png for later retrieval  
    */
    public static void newMessage(String message)
    {	
    	
    	//if the first character in the message is :, then it is the username being sent
    	if(message.charAt(0) == 58)
    	{
    		String newMessage = message.replace(":", "");
    		setUser(newMessage);
    	}
    	
    	//else if message received is equal to a length of 9 then it is the date
    	//dd_mm_yyyy indexed dd(0,1)_(2)mm(3,4)_(5)yyyy(6,7,8,9)
    	else if(message.length() == 10)
    	{
    		System.out.println(message);
    		folderName = (message.charAt(3) + "" + message.charAt(4) + "" + message.charAt(0) + "" + message.charAt(1));
    		createFolder(folderName);
    		fileYear = (message.charAt(6) + "" + message.charAt(7) + "" + message.charAt(8) + "" + message.charAt(9));
    	}
    	
    	//else its the base 64 string and we got some stuff to do to it
    	else if(message.length() > 100)
    	{
    		System.out.println("Received.");
    				
    		//create new byte array to store the string after its been decoded
    		byte[] decodedImg = Base64.getMimeDecoder().decode(message.getBytes(StandardCharsets.UTF_8));
    		
    		//create path object, where to store the final bitmap image and what name to be stored under
    		Path destinationFile = Paths.get(relPathway.toString() + folderName, fileYear + ".png");
    		
    		//old pathway code -- Path destinationFile = Paths.get("Resources\\images\\" + folderName, fileYear + ".png");
    		
    		//try writing the byte array to the path specified
    		try 
    		{
    			Files.write(destinationFile, decodedImg);
    			System.out.println("Saved.");
    		} 
    		catch(Exception e) //gotta catch'em all!
    		{
    			e.printStackTrace();
    			System.out.println("Error 1");
    		}
    	}
    }
	   
//--------------------------------------------------------- Create Folder ---------------------------------------------------------
	/*
	 * create a new folder for the new image to be stored under
	 */
	private static void createFolder(String name)
	{
		Path newFolder = Paths.get(relPathway.toString() + name);
		//old pathway code -- Path newFolder = Paths.get("Resources\\images\\" + name);
		if(!Files.exists(newFolder))
		{
			try 
			{
				Files.createDirectory(newFolder);
			}
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
	}
    
//--------------------------------------------------------- Publish Image ---------------------------------------------------------
    /*
     * publish received image to the right hand side panel
     */
	private static void publishImage(String folder, String imageYear) 
	{
		Path filePath = Paths.get(relPathway.toString() + folder + "/" + imageYear + ".png");
		//Path filePath = Paths.get("Resources\\images\\" + folder + "\\" + imageYear + ".png");
		if(Files.exists(filePath))
		{
			System.out.println("found: " + filePath);
			try
			{				
				//try loading the image as a buffered image
				BufferedImage myPic = ImageIO.read(CalendroM.class.getResource("/images/" + folder + "/" + imageYear + ".png"));
				//BufferedImage myPic = ImageIO.read(CalendroM.class.getResource("/images/" + folder + "/" + imageYear + ".png"));
				System.out.println("Buffered.");
				
				//set the retrieved image to an ImageIcon so that it can be set to a display
				ImageIcon icon = new ImageIcon(myPic);
				System.out.println("Loaded.");
				
				//set the paneDisplays icon as the retrieved image
				paneDisplay.setIcon(icon);
				
				////resize the image to fit the button so that no part of the image is lost
				int offset = paneDisplay.getInsets().left;
				paneDisplay.setIcon(resizeIcon(icon, paneDisplay.getWidth() - offset, paneDisplay.getHeight() - offset));
				System.out.println("Displayed.");
			}
			catch(Exception e)
			{
				e.printStackTrace();
				System.out.println("Error 2");
			}
		}
		else
		{
			paneDisplay.setIcon(null);
		}
		
	}
    
//--------------------------------------------------------- Resize Icon ---------------------------------------------------------
	/*
	 * resize image icon to the specified parameters
	 */
	private static Icon resizeIcon(ImageIcon icon, int resizedWidth, int resizedHeight) 
	{
	    Image img = icon.getImage();  
	    Image resizedImage = img.getScaledInstance(resizedWidth, resizedHeight,  java.awt.Image.SCALE_SMOOTH);  
	    return new ImageIcon(resizedImage);
	}
    
//--------------------------------------------------------- Refresh Calendro ---------------------------------------------------------
    /*
     * refresh the calendar table  to retrieve the correct days and month
     * receive month and year as parameters
     */
    private static void refreshCalendro(int month, int year)
    {
        //assign months to array
        String[] months =  {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        
        //number Of Days, Start Of Month
        int ndays, smonth; 
        
        //enable buttons in case they were disabled on the last refresh
        btnPrev.setEnabled(true);
        btnNext.setEnabled(true);
        
        //disable buttons if the previous year limit is reached, 
        if (month == 0 && year <= realYear-50)
        {
        	btnPrev.setEnabled(false);
        }
        
        if (month == 11 && year >= realYear+50)
        {
        	btnNext.setEnabled(false);
        }
        
        //update the month label and re align as the length might have changed
        lblMonth.setText(months[month]); 
        lblMonth.setBounds(230, 13, 180, 25); 
        
        //update the year in the combo box
        comYear.setSelectedItem(String.valueOf(year)); 
        
        //set all table cells to null
        for (int i=0; i<6; i++)
        {
            for (int j=0; j<7; j++)
            {
                mtblCalendro.setValueAt(null, i, j);
            }
        }
        
        //use the gregorian calendar to get number of days in the month and the day of the week that the first day starts
        GregorianCalendar cal = new GregorianCalendar(year, month, 1);
        ndays = cal.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
        smonth = cal.get(GregorianCalendar.DAY_OF_WEEK);
        
        
        int check = 3;
        
        //throw in a check to see if the start of the month is a sunday, and alter the check variable if it is
        //this was because I was getting an error whenever a month started on a sunday
        if(smonth == 1)
        {
        	check = -4;
        }
        
        //assign an integer to each day of the week
        for (int i=1; i <= ndays; i++)
        {
            int row = new Integer((i + smonth - check) / 7);
            int column  =  (i + smonth - check) % 7;
            mtblCalendro.setValueAt(i, row, column);
        }
        
        
        //apply the table renderer. assign blue to current day and red to weekends
        tblCalendro.setDefaultRenderer(tblCalendro.getColumnClass(0), new renderCalendro());
    }
    
//--------------------------------------------------------- Set Username ---------------------------------------------------------
  	/*
  	 * set the username at the top of the UI
  	 */
  	private static void setUser(String username)
  	{
  		lblWelcome.setText("Welcome " + username + " - ");
  	}

//--------------------------------------------------------- Render Calendro ---------------------------------------------------------
    /*
     * paints the table certain colours depending on the columns, such that the weekends are red and weekdays are null/white
     */
    static class renderCalendro extends DefaultTableCellRenderer
    {
    	//eclipse was having a hissy fit that this method didn't have a serial version id, this appeased it, no idea what it does, so don't ask 
		private static final long serialVersionUID = 1L;

		public Component getTableCellRendererComponent (JTable table, Object value, boolean selected, boolean focused, int row, int column)
		{
            super.getTableCellRendererComponent(table, value, selected, focused, row, column);
            
            //column 5 and 6 are pre set as the weekend
            if (column == 5 || column == 6) 
            { 
                setBackground(new Color(255, 220, 220));
            }
            else
            { 
                setBackground(new Color(255, 255, 255));
            }
            
            if (value != null)
            {
            	//if the parsed value is the same as the real day, and the month is this month, etc...
                if (Integer.parseInt(value.toString()) == realDay && currentMonth == realMonth && currentYear == realYear)
                {
                	//todays background is blue
                    setBackground(new Color(220, 220, 255)); 
                }
            }
            
            //remove border, set the foreground
            setBorder(null);
            setForeground(Color.black);
            return this;
        }
    }
   
//--------------------------------------------------------- Previous Month Button ---------------------------------------------------------    
    //decrement the month, and year if january
    static class btnPrev_Action implements ActionListener
    {
        public void actionPerformed (ActionEvent e)
        {
            if (currentMonth == 0)
            { 
                currentMonth = 11;
                currentYear -= 1;
            }
            else
            { 
                currentMonth -= 1;
            }
            //redraw the days in the table to match the year and month selected
            refreshCalendro(currentMonth, currentYear);
        }
    }
    
//--------------------------------------------------------- Next Month Button ---------------------------------------------------------
    //increment the month, and year if december
    static class btnNext_Action implements ActionListener
    {
        public void actionPerformed (ActionEvent e)
        {
            if (currentMonth == 11)
            { 
                currentMonth = 0;
                currentYear += 1;
            }
            else
            { 
                currentMonth += 1;
            }
            //redraw the days in the table to match the year and month selected
            refreshCalendro(currentMonth, currentYear);
        }
    }
    
//--------------------------------------------------------- Update Label ---------------------------------------------------------    
    /*
     * update the right hand panels label with whatever day was clicked and load any image linked to the day selected
     */
    static class lblToday_Action implements MouseListener
    {
    	public void mouseClicked(MouseEvent e) 
    	{
    		if (e.getClickCount() == 1) 
        	{
    			//get the source id of the cell that was clicked
        	    JTable cell = (JTable)e.getSource();
        	    int row = cell.getSelectedRow();
        	    int column = cell.getSelectedColumn();

        	    //return the integer that was in the cell as an object
        	    Object checkValue = tblCalendro.getValueAt(row, column);
        	    
        	    //turn value to string
        	    String cellValue = checkValue.toString();
        	    
        	    if(checkValue != null)
        	    {
        	    	//publish any image linked to that cell
        	    	if(cellValue.length() < 2)
        	    	{
        	    		cellValue = "0" + cellValue;
        	    	}
        	    	int curMonth = currentMonth + 1;
        	    	String currMonth = Integer.toString(curMonth);
        	    	if(currMonth.length() < 2)
        	    	{
        	    		currMonth = "0" + currMonth;
        	    	}
        	    	String currYear = Integer.toString(currentYear);
        	    		publishImage(currMonth + cellValue, currYear);
        	    		
        	    	//update label with object
        	    	lblToday.setText(cellValue);
        	    	
        	    }
        	}
        }

    	//eclipse wasn't happy that the listener didn't implement these processes so i left them blank here.
		public void mouseEntered(MouseEvent e) {
		}
		public void mouseExited(MouseEvent e) {
		}
		public void mousePressed(MouseEvent e) {
		}
		public void mouseReleased(MouseEvent e) {
		}
    }

//--------------------------------------------------------- Combo Box Selection ---------------------------------------------------------
    /*
     * when a year is selected in the combo box. 
     */
    static class comYear_Action implements ActionListener
    {
        public void actionPerformed (ActionEvent e)
        {
            if (comYear.getSelectedItem() != null)
            {
                String b = comYear.getSelectedItem().toString();
                currentYear = Integer.parseInt(b);
                
                //update calendro to match selected year
                refreshCalendro(currentMonth, currentYear);
            }
        }
    }
}