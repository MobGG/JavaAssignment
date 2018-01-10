package javaassignment;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author MobGG
 */
public class JavaAssignment {

    public static void main(String[] args) {
        calculateBill();
    }

    public static String readFile(String filePath) {
        String phoneBill = "";
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        try {
            fileReader = new FileReader(filePath);
            bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                phoneBill += line + "\n";
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(JavaAssignment.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(JavaAssignment.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ex) {
                    Logger.getLogger(JavaAssignment.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (fileReader != null) {
                try {
                    fileReader.close();
                } catch (IOException ex) {
                    Logger.getLogger(JavaAssignment.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return phoneBill;
    }

    public static String combineDateAndTime(String date, String time) {
        String dateTime = "";
        dateTime = date + " " + time;
        return dateTime;
    }

    public static Date convertStringToDate(String strDateTime) {
        Date dateTime = new Date();
        final SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        try {
            dateTime = format.parse(strDateTime);
        } catch (ParseException ex) {
            Logger.getLogger(JavaAssignment.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dateTime;
    }

    public static void calculateBill() {
        String[] phoneBalance = readFile("promotion1.log").split("\n");

        int count = phoneBalance.length;
        String[][] billData = new String[count][];
        long diff, diffSeconds, diffMinutes, diffHours;
        JSONArray billList = new JSONArray();
        JSONObject phoneBill = new JSONObject();

        for (int i = 0; i < count; i++) {
            billData[i] = phoneBalance[i].split("\\|");
            String callStart = combineDateAndTime(billData[i][0], billData[i][1]);
            String callEnd = combineDateAndTime(billData[i][0], billData[i][2]);
            Date start, end;
            double payment = 0;

            start = convertStringToDate(callStart);
            end = convertStringToDate(callEnd);

            diff = end.getTime() - start.getTime();
            diffSeconds = diff / 1000 % 60;
            diffMinutes = diff / (60 * 1000) % 60;
            diffHours = diff / (60 * 60 * 1000) % 24;
            switch (billData[i][4]) {
                case "P1":
                    if (diffHours == 0 && diffMinutes == 0) {
                        payment = 3;
                    } else {
                        payment = (3 + (diffHours * 60) + (diffMinutes - 1) + (diffSeconds * (1 / 60)));
                    }
                    break;
                default:
                    break;
            }

            JSONObject bill = new JSONObject();
            bill.put("mobileNo", billData[i][3]);
            bill.put("payment", payment);

            billList.add(bill);
        }
        phoneBill.put("phoneBill", billList);
        exportJson(phoneBill);
    }

    public static void exportJson(JSONObject phoneBill) {
        try (FileWriter file = new FileWriter("phoneBill.json")) {
            file.write(phoneBill.toJSONString());
        } catch (IOException ex) {
            Logger.getLogger(JavaAssignment.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
