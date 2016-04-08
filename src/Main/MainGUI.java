package Main;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JButton;
import javax.swing.DefaultComboBoxModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.HashMap;

//main gui class that handles user input and then calls main class to execute query
public class MainGUI {

	private JFrame frame;//the main frame of the gui
	private JTextField textField;//the text box which expects the user to input numbers
	private JComboBox<String> querySelector;//the drop down which holds all the different query types
	private JLabel lbl1;//the first label, is modified based on query type
	private JLabel lbl2;//the second label, is modified based on query type
	private JButton btnClear;//clears the gui button
	private JButton btnQuery;//exectues the query button
	private JTextPane resultsPane;//pane that holds the results when the query is executed
	private JComboBox<String> boxX;//the drop down of pages that corresponds in most querys to page A
	private JComboBox<String> boxY;//the drop down of pages that corresponds in most querys to page B
	private JLabel lblTitle;//title for the main window
	private HashMap<Integer,String> queryDict = new HashMap<Integer,String>();//HM that holds the page and the corresponding query number index

	/**
	 * Launch the application.
	 */
	public static void launch(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainGUI window = new MainGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application. Also adds important values to the HM for calculations and output later.
	 */
	public MainGUI() {
		initialize();
		queryDict.put(1, "QueryUsers");
		queryDict.put(2,"QueryPercentage");
		queryDict.put(3, "QueryMoreUsers");
		queryDict.put(4, "QueryCount");
		queryDict.put(5, "QueryPercentLooked");
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		//starts the frame
		frame = new JFrame();
		frame.setBounds(100, 100, 520, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		//adds the first combo box for page a
		boxX = new JComboBox<String>();
		boxX.setModel(new DefaultComboBoxModel<String>(new String[] {"", "Front Page", "News", "Technology", "Local", "Opinion", "On-Air", "Miscellaneous", "Weather", "MSN News", "Health", "Living", "Business", "MSN Sports", "Sports", "Summary", "BBS", "Travel"}));
		boxX.setBounds(240, 139, 140, 27);
		frame.getContentPane().add(boxX);
		boxX.setVisible(false);
		
		//adds the second combo box for page b
		boxY = new JComboBox<String>();
		boxY.setModel(new DefaultComboBoxModel<String>(new String[] {"", "Front Page", "News", "Technology", "Local", "Opinion", "On-Air", "Miscellaneous", "Weather", "MSN News", "Health", "Living", "Business", "MSN Sports", "Sports", "Summary", "BBS", "Travel"}));
		boxY.setBounds(168, 107, 140, 27);
		frame.getContentPane().add(boxY);
		boxY.setVisible(false);
		
		//adds the title
		lblTitle = new JLabel("MSNBC Data Query Tool");
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
		lblTitle.setBounds(6, 6, 488, 27);
		frame.getContentPane().add(lblTitle);
		
		//adds the input text box for inputting integers
		textField = new JTextField();
		textField.setBounds(304, 105, 72, 28);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		textField.setVisible(false);
		
		//adds teh results panel 
		resultsPane = new JTextPane();
		resultsPane.setEditable(false);
		resultsPane.setBounds(6, 283, 488, 68);
		frame.getContentPane().add(resultsPane);
		resultsPane.setVisible(false);
		
		//adds the query button
		btnQuery = new JButton("Query");
		btnQuery.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {//action listener for the button to execute a query
				//execute query from main and populate results in gui
				int queryNum = querySelector.getSelectedIndex();
				String queryType = queryDict.get(queryNum);
				byte x = (byte)boxX.getSelectedIndex();
				byte y = (byte)boxY.getSelectedIndex();
				int count = 0;
				if(textField.isVisible()) {
					count = Integer.parseInt(textField.getText());
				}
				if(queryType==null) {//checks that a query is selected
					resultsPane.setText("ERROR FINDING QUERY TYPE");
					return;
				}
				String queryResult = "";
				long startTime = 0, endTime = 0;//checks start and end time
				try {
					startTime = System.currentTimeMillis();
					queryResult = Main.executeQuery(queryType, x, y, count);
					endTime = System.currentTimeMillis();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				//main dispatch that determines what query needs to be run and passes data to the main class
				if(queryType=="QueryPercentage"){ //done
					resultsPane.setText(queryResult+"% of "+Data.getDataSetSize()+" users viewed the selected page.");
				}
				else if (queryType=="QueryUsers"){ //done
					//determines who viewed which thing more and outputs proper result
					int qr = Integer.parseInt(queryResult);
					if(qr>count) {
						resultsPane.setText("Yes. "+queryResult+" individuals viewed the selected page, which is more than "+count+".");
					}
					else if(count>qr){
						resultsPane.setText("No. "+queryResult+" individuals viewed the selected page, which is less than "+count+".");
					}
					else {
						resultsPane.setText("No. However, there were exactly "+queryResult+" individuals who viewed the selected page.");
					}
				}
				else if (queryType=="QueryMoreUsers"){//done
					resultsPane.setText(queryResult);
				}
				else if (queryType=="QueryCount"){//done
					resultsPane.setText(queryResult + " users viewed the " + boxX.getSelectedItem() +" "+ count + " times.");
				}
				else if (queryType=="QueryPercentLooked"){//done
					resultsPane.setText(queryResult+"% of users looked at "+boxX.getSelectedItem()+" more than "+boxY.getSelectedItem()+"." );
				}
				else { //should never get here
					resultsPane.setText("ERROR, UNKNOWN QUERY, PLEASE TRY AGAIN");
				}
				long exeTime = endTime-startTime;
				resultsPane.setText("                                    EXECUTING QUERY #"+queryNum+" NOW\n*********************************************************************************\n"+resultsPane.getText()+"\nExecution Time: "+exeTime+"ms");
				
			}
		});
		btnQuery.setBounds(168, 241, 117, 29);
		frame.getContentPane().add(btnQuery);
		btnQuery.setVisible(false);
		
		//adds the clear button
		btnClear = new JButton("Clear");
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				querySelector.setSelectedIndex(0);
			}
		});
		btnClear.setBounds(304, 241, 117, 29);
		frame.getContentPane().add(btnClear);
		btnClear.setVisible(false);
		
		//adds the query selector box 
		querySelector = new JComboBox<String>();
		querySelector.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { //action listener that is used to setup the whole gui once a query is selected
				int selected = querySelector.getSelectedIndex();
				resetUI();
				switch(selected) {
					case 1:
						//are there more than __ users who looked at X
						lbl1.setVisible(true);
						lbl1.setText("Are there more than");
						lbl1.setBounds(12, 111, 133, 16);
						lbl2.setVisible(true);
						lbl2.setText("users who looked at");
						lbl2.setBounds(223, 111, 133, 16);
						btnClear.setVisible(true);
						btnQuery.setVisible(true);
						textField.setVisible(true);
						textField.setBounds(145, 105, 72, 28);
						boxX.setVisible(true);
						boxX.setBounds(354, 107, 140, 27);
						boxY.setVisible(false);
						resultsPane.setVisible(true);
						break;
					case 2:
						//what percent of users looked at X
						lbl1.setVisible(true);
						lbl1.setText("What percent of users looked at");
						lbl1.setBounds(145, 111, 300, 16);
						lbl2.setVisible(false);
						btnClear.setVisible(true);
						btnQuery.setVisible(true);
						textField.setVisible(false);
						boxX.setVisible(true);
						boxX.setBounds(354, 107, 140, 27);
						boxY.setVisible(false);
						resultsPane.setVisible(true);
						break;
					case 3:
						//are there more users who looked at X than Y
						boxX.setVisible(true);
						boxY.setVisible(true);
						boxX.setBounds(240, 107, 140, 27);
						boxY.setBounds(240, 139, 140, 27);
						textField.setVisible(false);
						lbl1.setVisible(true);
						lbl2.setVisible(true);
						lbl1.setBounds(12, 111, 224, 16);
						lbl1.setText("Are there more users who looked at");
						lbl2.setBounds(208, 143, 41, 16);
						lbl2.setText("than");
						resultsPane.setVisible(true);
						resultsPane.setBounds(6, 283, 488, 68);
						btnClear.setVisible(true);
						btnQuery.setVisible(true);
						break;
					case 4:
						//How many users viewed X __ number of times
						lbl1.setVisible(true);
						lbl1.setText("How many users viewed");
						lbl1.setBounds(12, 111, 224, 16);
						lbl2.setVisible(true);
						lbl2.setText("number of times");
						lbl2.setBounds(380, 111, 114, 16);
						btnClear.setVisible(true);
						btnQuery.setVisible(true);
						textField.setVisible(true);
						textField.setBounds(304, 105, 72, 28);
						boxX.setVisible(true);
						boxX.setBounds(165, 105, 140, 27);
						boxY.setVisible(false);
						resultsPane.setVisible(true);
						break;
					case 5:
						//What percent of users looked at X more than Y
						boxX.setVisible(true);
						boxY.setVisible(true);
						boxX.setBounds(240, 107, 140, 27);
						boxY.setBounds(240, 139, 140, 27);
						textField.setVisible(false);
						lbl1.setVisible(true);
						lbl2.setVisible(true);
						lbl1.setBounds(36, 111, 224, 16);
						lbl1.setText("What percent of users looked at");
						lbl2.setBounds(173, 143, 100, 16);
						lbl2.setText("more than");
						resultsPane.setVisible(true);
						resultsPane.setBounds(6, 283, 488, 68);
						btnClear.setVisible(true);
						btnQuery.setVisible(true);
						break;
					default: //assumes no query is selected, clears the gui
						lbl1.setVisible(false);
						lbl2.setVisible(false);
						
						btnClear.setVisible(false);
						btnQuery.setVisible(false);
						textField.setVisible(false);
						boxX.setVisible(false);
						boxY.setVisible(false);
						resultsPane.setVisible(false);
						break;
				}
					
			}
		});
		querySelector.setModel(new DefaultComboBoxModel<String>(new String[] {"Select A Query", "Are there more than __ users who looked at X?", "What percent of users looked at X?", "Are there more users who looked at X than Y?", "How many users viewed X __ number of times?", "What percent of users looked at X more than Y?"}));
		querySelector.setBounds(6, 45, 488, 27);
		frame.getContentPane().add(querySelector);
		
		//initial value for one label
		lbl1 = new JLabel("How many users viewed");
		lbl1.setBounds(12, 111, 224, 16);
		frame.getContentPane().add(lbl1);
		lbl1.setVisible(false);
		
		//initial value for another label
		lbl2 = new JLabel("number of times");
		lbl2.setBounds(380, 111, 114, 16);
		frame.getContentPane().add(lbl2);
		lbl2.setVisible(false);
	}
	
	//resets the ui for use again
	private void resetUI() {
		textField.setText("");
		boxX.setSelectedIndex(0);
		boxY.setSelectedIndex(0);
		resultsPane.setText("");
	}	
}
