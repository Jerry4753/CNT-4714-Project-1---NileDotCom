/* Name: Anh Vuong
Course: CNT 4714 – Spring 2022
Assignment title: Project 1 – Event-driven Enterprise Simulation
Date: Sunday January 30, 2022
*/

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;

import javax.swing.*;

public class NileDotComProject extends JFrame{
	
	// static variables to hold frame dimensions in pixels
	private static final int WIDTH = 700;
	private static final int HEIGHT = 230;
	
	// labels, fields, and buttons
	private JLabel blankLabel, idLabel, qtyLabel, itemLabel, totalLabel;
	private JTextField blankTextField, idTextField, qtyTextField, itemTextField, totalTextField;
	private JButton processBtn, confirmBtn, viewBtn, finishBtn, newBtn, exitBtn;
	
	// declare reference variables for event handlers
	private ProcessButtonHandler procBtnHandler;
	private ConfirmButtonHandler confBtnHandler;
	private ViewButtonHandler viewBtnHandler;
	private FinishButtonHandler finBtnHandler;
	private NewButtonHandler newBtnHandler;
	private ExitButtonHandler exitBtnHandler;
	
	// formatting variables
	static NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance();
	static NumberFormat percentFormatter = NumberFormat.getPercentInstance();
	static DecimalFormat decimalFormatter = (DecimalFormat) percentFormatter;
	
	//arrays to hold shopping cart items for later processing
	static String [] itemIDArray;
	static String[] itemTitleArray;
	static String[] itemInStockArray;
	static double[] itemPriceArray;
	static int[] itemQtyArray;
	static double[] itemDiscountArray;
	static double[] itemSubtotalArray;
	
	// initialize variables as needed
	static String itemID = "", itemTitle = "", outputStr = "", maxArraySizeStr = "",
			itemPriceStr = "", itemInStock = "", itemQtyStr = "", itemSubtotalStr = "", itemDiscountStr = "", orderSubtotalStr = "";
	static double itemPrice = 0, itemSubtotal = 0, orderSubtotal = 0, orderTotal = 0,
			itemDiscount = 0, orderTaxAmount = 0;
	
	private static int itemCount = 0, itemQuantity = 0, maxArraySize = 0;
	
	final static double TAX_RATE = 0.060,
						DISCOUNT_FOR_05 = .10,
						DISCOUNT_FOR_10 = .15,
						DISCOUNT_FOR_15 = .20;
	String fileName;
	
	public NileDotComProject() {
		setTitle("Nile Dot Com - Spring 2022");
		setSize(WIDTH, HEIGHT);
		
		// instantiate JLabel objects
		blankLabel = new JLabel(" ", SwingConstants.RIGHT);
		idLabel = new JLabel("Enter item ID for Item #" + (itemCount + 1) + ":", SwingConstants.RIGHT);
		qtyLabel = new JLabel("Enter quantity for Item #"+ (itemCount + 1) + ":", SwingConstants.RIGHT);
		itemLabel = new JLabel("Details for Item #"+ (itemCount + 1) + ":", SwingConstants.RIGHT);
		totalLabel = new JLabel("Order subtotal for " + itemCount + " item(s):", SwingConstants.RIGHT);
		
		// instantiate JTextField objects
		blankTextField = new JTextField();
		idTextField = new JTextField();
		qtyTextField = new JTextField();
		itemTextField = new JTextField();
		totalTextField = new JTextField();
		
		// set the layout - use a border layout to hold a grid layout in the north panel and a flow layout in the south panel
		Container pane = getContentPane();
		
		// create a 6-by-2 grid layout with a horizontal gap of 8 and a vertical gap of 2
		GridLayout grid5by2 = new GridLayout(6,2,8,2);
		FlowLayout flowLayout = new FlowLayout();// flow layout for buttons
		
		// create panels
		JPanel northPanel = new JPanel();
		JPanel southPanel = new JPanel();
		
		// set layouts for panels
		northPanel.setLayout(grid5by2);
		southPanel.setLayout(flowLayout);
		
		// add labels to panel
		northPanel.add(blankLabel);
		northPanel.add(blankTextField);
		
		idLabel.setForeground(Color.yellow);
		northPanel.add(idLabel);
		northPanel.add(idTextField);
		
		northPanel.add(qtyLabel);
		northPanel.add(qtyTextField);	
		qtyLabel.setForeground(Color.yellow);
		
		northPanel.add(itemLabel);
		northPanel.add(itemTextField);	
		itemLabel.setForeground(Color.yellow);
		
		northPanel.add(totalLabel);
		northPanel.add(totalTextField);	
		totalLabel.setForeground(Color.yellow);
		

		
		
		// instantiate buttons and register handlers
		processBtn = new JButton("Process Item #" + (itemCount + 1));
		procBtnHandler = new ProcessButtonHandler();
		processBtn.addActionListener(procBtnHandler);
		
		confirmBtn = new JButton("Confirm Item #" + (itemCount + 1));
		confBtnHandler = new ConfirmButtonHandler();
		confirmBtn.addActionListener(confBtnHandler);
		
		viewBtn = new JButton("View Order");
		viewBtnHandler = new ViewButtonHandler();
		viewBtn.addActionListener(viewBtnHandler);
		
		finishBtn = new JButton("Finish Order");
		finBtnHandler = new FinishButtonHandler();
		finishBtn.addActionListener(finBtnHandler);

		newBtn = new JButton("New Order");
		newBtnHandler = new NewButtonHandler();
		newBtn.addActionListener(newBtnHandler);
		
		exitBtn = new JButton("Exit");
		exitBtnHandler = new ExitButtonHandler();
		exitBtn.addActionListener(exitBtnHandler);
		
		// initial visibility settings for buttons, fields
		confirmBtn.setEnabled(false);
		viewBtn.setEnabled(false);
		finishBtn.setEnabled(false);
		itemTextField.setEditable(false);
		totalTextField.setEditable(false);
		blankTextField.setEditable(false);
		blankTextField.setForeground(Color.black);
		blankTextField.setVisible(false);
		
		// add buttons to panel
		southPanel.add(processBtn);
		southPanel.add(confirmBtn);
		southPanel.add(viewBtn);
		southPanel.add(finishBtn);
		southPanel.add(newBtn);
		southPanel.add(exitBtn);
		
		// add panels to content pane using BorderLayout
		pane.add(northPanel, BorderLayout.NORTH);
		pane.add(southPanel, BorderLayout.SOUTH);
		
		centerFrame(WIDTH, HEIGHT);
		pane.setBackground(Color.black);
		northPanel.setBackground(Color.black);
		southPanel.setBackground(Color.blue);

	}
	
	public void centerFrame(int frameWidth, int frameHeight) {
		// create a Toolkit object
		Toolkit aToolkit = Toolkit.getDefaultToolkit();
		
		// create a Dimension object with user screen information
		Dimension screen = aToolkit.getScreenSize();
		
		// assign x, y position of upper-left corner of frame
		int xPositionOfFrame = (screen.width - frameWidth) / 2;
		int yPositionOfFrame = (screen.height - frameHeight) / 2;
		setBounds(xPositionOfFrame, yPositionOfFrame, frameWidth, frameHeight);
	}
	
	// private class for process item button
	private class ProcessButtonHandler implements ActionListener{
		public void actionPerformed(ActionEvent ae) {
			String outputMessage, priceData, itemIDFromFile;
			boolean foundID = false, isNumItemsOk = false, isItemQtyOk = false, isItemInStock = true;
			
			// define file, file reader and buffered readers, plus a scanner object
			File priceFile= new File("inventory.txt");
			FileReader priceFileReader = null;
			BufferedReader priceBuffReader = null;
			Scanner scnr = null;
			
			try {
				maxArraySize = 20;
				
				// check if quantity is in bounds?
				if(itemCount <= maxArraySize) {
					isNumItemsOk = true;
				}
				if(Integer.parseInt(qtyTextField.getText()) > 0){
					isItemQtyOk = true;
				}
				
				// if ok, create arrays to hold shopping cart items
				if(itemCount == 0) {
					// declare arrays
					itemIDArray = new String[maxArraySize];
					itemTitleArray = new String[maxArraySize];
					itemInStockArray = new String[maxArraySize];
					itemPriceArray = new double[maxArraySize];
					itemQtyArray = new int[maxArraySize];
					itemDiscountArray = new double[maxArraySize];
					itemSubtotalArray = new double[maxArraySize];
				}
				
				itemID = idTextField.getText();
				itemQuantity = Integer.parseInt(qtyTextField.getText());
				
				// read a line in the inventory file
				priceFileReader = new FileReader(priceFile);
				priceBuffReader = new BufferedReader(priceFileReader);
				priceData = priceBuffReader.readLine();
				
				// while loop
				while(priceData != null && isItemInStock) {
					scnr = new Scanner(priceData);
					scnr.useDelimiter(", ");
					itemIDFromFile = scnr.next();
					
					if(itemID.equals(itemIDFromFile)) {
						foundID = true;
						itemTitle = scnr.next();
						itemInStock = scnr.next();
						
						if(!itemInStock.equals("true")) {
							isItemInStock = false;
						}
						itemPriceStr = scnr.next();
						itemPrice = Double.parseDouble(itemPriceStr);
						break;
					}
//					itemTitle = scnr.next();
					priceData = priceBuffReader.readLine();
					
				}// end while
				
				if(foundID == false || isNumItemsOk == false || isItemQtyOk == false || isItemInStock == false) {
					if(foundID == false) {
						outputMessage = "Item ID " + itemID + " not in file";
						JOptionPane.showMessageDialog(null, outputMessage, "Nile Dot Com - ERROR", JOptionPane.ERROR_MESSAGE);
						idTextField.setText("");
						qtyTextField.setText("");
					}// end if
					
					if(isNumItemsOk == false || isItemQtyOk == false) {
						outputMessage = "Please enter positive numbers for number of items";
						JOptionPane.showMessageDialog(null, outputMessage, "Nile Dot Com - ERROR", JOptionPane.ERROR_MESSAGE);
					}
					
					if(isItemInStock == false) {
						outputMessage = "Sorry... That item is out of stock, please try another item";
						JOptionPane.showMessageDialog(null, outputMessage, "Nile Dot Com - ERROR", JOptionPane.ERROR_MESSAGE);
						idTextField.setText("");
						qtyTextField.setText("");
					}// end if
					
				}// end if
				else {
					itemQuantity = Integer.parseInt(qtyTextField.getText());
					itemQtyStr = String.valueOf(itemQuantity);
					if(itemQuantity > 0 && itemQuantity < 5) {
						itemDiscount = 0;
					}
					else if(itemQuantity >= 5 && itemQuantity < 10) {
						itemDiscount = DISCOUNT_FOR_05;
					}
					else if(itemQuantity >= 10 && itemQuantity < 15) {
						itemDiscount = DISCOUNT_FOR_10;
					}
					else {
						itemDiscount = DISCOUNT_FOR_15;
					}// end if
					
					// calculating subtotals
					itemSubtotal = itemPrice * (1 - itemDiscount) * itemQuantity;
					orderSubtotal += itemSubtotal;
					
					// set currency format and percentage format for output strings
					itemPriceStr = currencyFormatter.format(itemPrice);
					itemDiscountStr = decimalFormatter.format(itemDiscount);
					itemSubtotalStr = currencyFormatter.format(itemSubtotal);
					
					// build out message string
					outputMessage = itemID + " " + itemTitle + " " + itemPriceStr + " "
							+ itemQuantity + " " + itemDiscountStr + " " + itemSubtotalStr;
					// update labels
					idLabel.setText("Enter item ID for Item #" + (itemCount + 1) + ":");
					qtyLabel.setText("Enter quantity for Item #" + (itemCount + 1) + ":");
					itemLabel.setText("Details for Item #" + (itemCount + 1) + ":");
					
					itemTextField.setText(outputMessage);
					processBtn.setEnabled(false);
					confirmBtn.setEnabled(true);
				} // end else
				
			} catch(NumberFormatException e) {
				outputMessage = "Invalid input for number of line items or quantity of items";
				JOptionPane.showMessageDialog(null, outputMessage, "Nile Dot Com - ERROR", JOptionPane.ERROR_MESSAGE);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				System.err.println("File not Found");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.err.println("Error reading file");
			}
		}
		
	}
	
	// private class for process item button
	private class ConfirmButtonHandler implements ActionListener{
		public void actionPerformed(ActionEvent ae) {
			String confirmStr = "Item #" + (itemCount+1) + " accepted. Added to your cart.";
			
			// set array values with items from inventory file user has confirmed - added to cart
			itemIDArray[itemCount] = itemID;
			itemTitleArray[itemCount] = itemTitle;
			itemPriceArray[itemCount] = itemPrice;
			itemQtyArray[itemCount] = itemQuantity;
			itemDiscountArray[itemCount] = itemDiscount;
			itemSubtotalArray[itemCount] = itemSubtotal;
			
			// increment item count in order
			itemCount++; // count the number of items in order
			assert itemCount > 0: "Item count is <= 0";
			
			// dump item confirmed message
			orderSubtotalStr = currencyFormatter.format(orderSubtotal);
			totalTextField.setText(orderSubtotalStr);
			JOptionPane.showMessageDialog(null, confirmStr, "Nile Dot Com - Item Confirmed", JOptionPane.INFORMATION_MESSAGE);
			
			// reset labels, text fields, and buttons
			idLabel.setText("Enter item ID for Item #" + (itemCount + 1) + ":");
			qtyLabel.setText("Enter quantity for Item #" + (itemCount + 1) + ":");
			itemLabel.setText("Details for Item #" + itemCount + ":");
			totalLabel.setText("Order subtotal for " + itemCount + " item(s)" + ":");
			
			
			finishBtn.setEnabled(true);	
			viewBtn.setEnabled(true);
			processBtn.setEnabled(true);
			processBtn.setText("Process item #" + (itemCount + 1));
			confirmBtn.setEnabled(false);
			confirmBtn.setText("Confirm Item #" + (itemCount + 1));
			
			idTextField.setText("");
			qtyTextField.setText("");
		}
	}
	
	// private class for view order button
	private class ViewButtonHandler implements ActionListener{
		public void actionPerformed(ActionEvent ae) {
			outputStr = "";
			
			//iterate through current arrays and dump view of shopping cart
			for (int i = 0 ; i < itemCount; i++) {
				outputStr = outputStr.concat((i + 1) + ". " 
						+ itemIDArray[i] + " " 
						+ itemTitleArray[i] + " "
						+ currencyFormatter.format(itemPriceArray[i]) + " "
						+ itemQtyArray[i] + " " 
						+ decimalFormatter.format(itemDiscountArray[i]) + " " 
						+ currencyFormatter.format(itemSubtotalArray[i]) + "\n");
			}
			JOptionPane.showMessageDialog(null, outputStr, "Nile Dot Com - Current Shopping Cart Status", JOptionPane.INFORMATION_MESSAGE);

		}
	}
	
	// private class for view order button
	private class FinishButtonHandler implements ActionListener{
		public void actionPerformed(ActionEvent ae) {
			String totalMessage = "";
			
			// generate date and time of invoice and timestamp permutaion
			Date today = new Date(System.currentTimeMillis());
			DateFormat usaDateTime = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.LONG);
			DateFormat frenchDateTime = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.LONG, Locale.FRANCE);
			FileWriter transactionFile;
			PrintWriter aPrintWriter = null;
			
			try {
				transactionFile = new FileWriter("transaction.txt", true);
				aPrintWriter = new PrintWriter(transactionFile);
				
				String todayStr = usaDateTime.format(today);
				String stringForID = frenchDateTime.format(today);
				StringBuilder orderIDSB = new StringBuilder(stringForID);
				StringBuilder outputSB = new StringBuilder();
				
				// calculate total and format to two decimals
				orderTaxAmount = orderSubtotal * TAX_RATE;
				orderTotal = orderSubtotal + orderTaxAmount;
				
				// generate timestamp permutation
			    DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("ddMMyyyyHHmm");
			    LocalDateTime myDateObj = LocalDateTime.now();
			    String formattedDate = myDateObj.format(myFormatObj);
			    
				// total Message header
				totalMessage = "Date: " + todayStr
						+ "\n\nNumber of line item(s): " + itemCount
						+"\n\nItem # / ID / Title / Price / Qty / Disc% / Subtotal: \n\n";
				
				// iterate through shopping cart items and add to invoice message
				for (int i = 0 ; i < itemCount; i++) {
					// print to the screen
					totalMessage += ((i + 1) + ". " 
							+ itemIDArray[i] + " " 
							+ itemTitleArray[i] + " "
							+ currencyFormatter.format(itemPriceArray[i]) + " "
							+ itemQtyArray[i] + " " 
							+ decimalFormatter.format(itemDiscountArray[i]) + " " 
							+ currencyFormatter.format(itemSubtotalArray[i]) + "\n");
					
					// write to transaction file
					outputSB.append( formattedDate + ", "
							+ itemIDArray[i] +", "
							+ itemTitleArray[i] + ", "
							+ itemPriceArray[i] + ", "
							+ itemQtyArray[i] + ", "
							+ itemDiscountArray[i] + ", "
							+ currencyFormatter.format(itemSubtotalArray[i]) + ", "
							+ todayStr + "\n");
					
				}
				aPrintWriter.print(outputSB.toString());
			} catch(IOException ioException) {
				JOptionPane.showMessageDialog(null, "Error: Problem writing to file", "Nile Dot Com - ERROR", JOptionPane.ERROR_MESSAGE);
			}
			finally {
				aPrintWriter.close();
			}
			
			// total message footer
			totalMessage +=
					"\n\nOrder Subtotal: " + currencyFormatter.format(orderSubtotal)
					+"\n\nTax Rate:      " + percentFormatter.format(TAX_RATE)
					+"\n\nTax Amount:    " + currencyFormatter.format(orderTaxAmount)
					+"\n\nOrder Total:   " + currencyFormatter.format(orderTotal)
					+"\n\nThank you for shopping at Nile Dot Com";
			JOptionPane.showMessageDialog(null, totalMessage, "Nile Dot Com - Final Invoice", JOptionPane.INFORMATION_MESSAGE);
			
			// set text fields
			idTextField.setEditable(false);
			qtyTextField.setEditable(false);
			
			// set buttons
			viewBtn.setEnabled(true);
			processBtn.setEnabled(false);
			finishBtn.setEnabled(false);
			
		}
	}
	
	// private class for new order button
	private class NewButtonHandler implements ActionListener{
		public void actionPerformed(ActionEvent ae) {
			// reset arrays
			itemIDArray = new String[200];
			itemTitleArray = new String[200];
			itemPriceArray  = new double[200];
			itemQtyArray = new int[200];
			itemDiscountArray = new double[200];
			itemSubtotalArray = new double[200];
			
			// reset variables
			itemID = ""; itemTitle = ""; outputStr = ""; maxArraySizeStr = "";
					itemPriceStr = ""; itemInStock = ""; itemQtyStr = ""; itemSubtotalStr = ""; itemDiscountStr = ""; orderSubtotalStr = "";
			itemPrice = 0; itemSubtotal = 0; orderSubtotal = 0; orderTotal = 0;
					itemDiscount = 0; orderTaxAmount = 0;
			
			itemCount = 0; itemQuantity = 0; maxArraySize = 0;
			
			// reset labels
			idLabel.setText("Enter item ID for Item #" + (itemCount + 1) + ":");
			qtyLabel.setText("Enter quantity for Item #"+ (itemCount + 1) + ":");
			itemLabel.setText("Details for Item #"+ (itemCount + 1) + ":");
			totalLabel.setText("Order subtotal for " + itemCount + " item(s):");
			
			// reset fields
			idTextField.setText("");
			idTextField.setEditable(true);
			qtyTextField.setText("");
			qtyTextField.setEditable(true);
			itemTextField.setText("");
			totalTextField.setText("");
			
			// reset buttons
			processBtn.setEnabled(true);
			processBtn.setText("Process Item #" + (itemCount + 1));
			confirmBtn.setText("Confirm Item #" + (itemCount + 1));
			confirmBtn.setEnabled(false);
			viewBtn.setEnabled(false);
			finishBtn.setEnabled(false);
		}
	}
	
	// private class for exit button
	private class ExitButtonHandler implements ActionListener{
		public void actionPerformed(ActionEvent ae) {
			System.exit(0);
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		NileDotComProject project = new NileDotComProject();
		project.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		project.setVisible(true);
	}

}
