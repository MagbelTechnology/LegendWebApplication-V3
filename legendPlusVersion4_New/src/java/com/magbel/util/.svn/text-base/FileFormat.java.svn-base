/**
 * File name        :FileFormat.java
 *
 * CalendarDate.java
 * Created on December 2, 2004, 12:56 PM
 * @author  Jejelowo Festus
 * @author  MagBel Technologies LTD
 * @author  Joseph Street Eleganza House Lagos.
 * @version 1.0
 *
 *@author           :fjejelowo@empire-bsl.com
 *
 *Description       :To format data to be wriiten int the getpan1.txt file
 *                  :a comma separated value(csv) format to be uploaded
 *                  :from the eForm application.
 *                  The format are:
 *                  Name    :type text of 26 length which must be padded with space
 *                  if it is less than 26;
 *                  BRANCH also text that must be padded with space if less tahn 26
 *                  characters.
 *                  CUST_ID:a type text that must be 9 characters and should be padded
 *                  with space if less than the required length;
 *                  Account number: as many as thse that exeist must be 13 characters
 *                  in legth and should also be padded with space if it is less
 *                  than the required format.
 */

package com.magbel.util;

import java.io.File;

public class FileFormat {


    /** Creates a new instance of FileFormat */
    public FileFormat() {
        System.out.println("Formating inputText...");
    }

    public String formatText(int requiredLength, String inputText) {

        String space = " ";
        int textSize = inputText.length();
        int sizeDifference = requiredLength - textSize;

        if (textSize < requiredLength) {

            for (int i = 0; i < sizeDifference; i++) {

                inputText = inputText + space;
            }
        } else if (textSize > requiredLength) {

            inputText = inputText.substring(0, requiredLength);

        } else {}

        return inputText;

    }

    public String zeroFormatText(int requiredLength, String inputText) {

        String zero = "0";
        int textSize = inputText.length();
        int sizeDifference = requiredLength - textSize;

        if (textSize < requiredLength) {

            for (int i = 0; i < sizeDifference; i++) {

                inputText = zero + inputText;
            }
        } else if (textSize > requiredLength) {

            inputText = inputText.substring(0, requiredLength);

        } else {}

        return inputText;

    }

    public String formatTextRightJustified(int requiredLength, String inputText) {

        String space = " ";
        int textSize = inputText.length();
        int sizeDifference = requiredLength - textSize;

        if (textSize < requiredLength) {

            for (int i = 0; i < sizeDifference; i++) {

                inputText = space + inputText;
            }
        } else if (textSize > requiredLength) {

            inputText = inputText.substring(0, requiredLength);

        } else {}

        return inputText;

    }

    public String addSpace(int size) {
        String space = " ";
        space = formatText(size, space);
        return space;
    }

    public void fileData(File f) {
        System.out.println(
                "Absolute path: " + f.getAbsolutePath() +
                "\n Can read: " + f.canRead() +
                "\n Can write: " + f.canWrite() +
                "\n getName: " + f.getName() +
                "\n getParent: " + f.getParent() +
                "\n getPath: " + f.getPath() +
                "\n length: " + f.length() +
                "\n lastModified: " + f.lastModified());

        if (f.isFile()) {
            System.out.println("It's a file");

        } else if (f.isDirectory()) {
            System.out.println("It's a directory");
        } else {}
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        /**
         *
         * This part for testing purpose
         * as a stand-alone programe.
         *
         */

        FileFormat fm = new FileFormat();

        File file = new File("SecurityCheck/setup.txt");
        String user1 = fm.formatText(26, "Abegunde Paul");
        String branch1 = fm.formatText(26, "Apapa Branch");

        String user2 = fm.formatText(26, "Orosun iyabo");
        String branch2 = fm.formatText(26, "Airport Middle way Branch");

        String user3 = fm.formatText(26, "Dansaki Arogundade John");
        String branch3 = fm.formatText(26, "Marina");

        System.out.println(user1 + "," + branch1 + "," + "0000001");
        System.out.println(user2 + "," + branch2 + "," + "0000002");
        System.out.println(user3 + "," + branch3 + "," + "0000003");

        fm.printClass(fm);
        fm.fileData(file);
        System.out.println("1 space" + fm.addSpace(1) + "END:");
        System.out.println("3 space" + fm.addSpace(3) + "END:");
        System.out.println("5 space" + fm.addSpace(5) + "END!");

    }

    /*  public Object[] convertToArray(Vector v,Class) {
          ackRequests = new Request[uBound];
              for(int index = 0; index <uBound;index++){

                  ackRequests[index] = (Request)v.elementAt(index);
              }
      }

     */
    public void printClass(Object obj) {

        System.out.println("Object :" + obj + " name is " +
                           obj.getClass().getName());
    }


}
