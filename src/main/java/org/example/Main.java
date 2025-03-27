package org.example;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        System.out.println("Select a language: ");
        System.out.println("1.English");
        System.out.println("2.Finnish");
        System.out.println("3.Chinese");
        System.out.println("4.Vietnamese");
        Scanner scanner = new Scanner(System.in);
//        int choice = scanner.nextInt();
        int choice = Integer.parseInt(scanner.nextLine());

//      select language for localization

        Locale locale;
        switch (choice){
            case 1:
                locale = new Locale("en", "US");
                break;
            case 2:
                locale = new Locale("fi","FI");
                break;
            case 3:
                locale = new Locale("cn","CN");
                break;
            case 4:
                locale = new Locale("vn","VN");
                break;
            default:
                System.out.println("Invalid language selection");
                locale = new Locale( "en","US");
        }

        ResourceBundle rb;
//        in case there is no resource bundle for the selected language, it will default to English
        try {
            rb = ResourceBundle.getBundle("messages",locale);
        } catch (Exception e){
            System.out.println("Invalid, selected language is not available, translate to English");
            rb = ResourceBundle.getBundle("messages",new Locale("en","US"));
        }

        System.out.print("what is your name? ");
        String input = scanner.nextLine();

        System.out.println(MessageFormat.format(rb.getString("test"),String.format("%s",input)));



    }
}