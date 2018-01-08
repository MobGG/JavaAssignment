package javaassignment;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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
//        System.out.printf("test\n");
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
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fileReader != null) {
                try {
                    fileReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return phoneBill;
    }

    public static void calculateBill() {
        String[] phoneBalance = readFile("promotion1.log").split("\n");
//        if (phoneBalance != null) {
//            System.out.printf("data not null");
//        }
        int count = phoneBalance.length;
        String[][] billData = new String[count][];
        long diff, diffSeconds, diffMinutes, diffHours;
        JSONArray billList = new JSONArray();
        JSONObject bill = new JSONObject();

        for (int i = 0; i < count; i++) {
            billData[i] = phoneBalance[i].split("\\|");
            String callStart = billData[i][0] + " " + billData[i][1];
            String callEnd = billData[i][0] + " " + billData[i][2];
            Date start, end;
            double payment = 0;
            try {
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                start = format.parse(callStart);
                end = format.parse(callEnd);

                diff = end.getTime() - start.getTime();
                diffSeconds = diff / 1000 % 60;
                diffMinutes = diff / (60 * 1000) % 60;
                diffHours = diff / (60 * 60 * 1000) % 24;
                switch (billData[i][4]) {
                    case "P1":
                        payment = (3 + (diffHours * 60) + (diffMinutes - 1) + (diffSeconds * (1 / 60)));
                        break;
                    default:
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

//            bill.put("date", billData[i][0]);
//            bill.put("startTime", billData[i][1]);
//            bill.put("endTime", billData[i][2]);
            bill.put("mobileNo", billData[i][3]);
//            bill.put("promotion", billData[i][4]);
            bill.put("payment", payment);

            billList.add(bill);

            try (FileWriter file = new FileWriter("phoneBill.json")) {
                file.write(billList.toJSONString());
                System.out.println("Successfully Copied JSON Object to File...");
                System.out.println("\nJSON Object: " + billList);
            } catch (IOException ex) {
                Logger.getLogger(JavaAssignment.class.getName()).log(Level.SEVERE, null, ex);
            }
//            System.out.printf(" วันที่ " + billData[i][0] + " เริ่มโทรเวลา " + billData[i][1] + " ถึง " + billData[i][2] + " หมายเลข " + billData[i][3] + " โปรโมชั่น " + billData[i][4] + " ค่าบริการ %.2f\n", payment);
        }
    }

    public static void exportJson(JSONArray billList) {
        try (FileWriter file = new FileWriter("phoneBill.json")) {
            file.write(billList.toJSONString());
            System.out.println("Successfully Copied JSON Object to File...");
            System.out.println("\nJSON Object: " + billList);
        } catch (IOException ex) {
            Logger.getLogger(JavaAssignment.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
