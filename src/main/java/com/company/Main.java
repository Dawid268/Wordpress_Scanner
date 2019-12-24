package com.company;

import com.company.working.Service;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Scanner;
import java.util.logging.Logger;

public class Main
{
    public static void main(String[] args)
    {
        Logger log = Logger.getLogger(Main.class.getName());
        File programPath;
        String path = "/";
        try
        {
            programPath = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
            path = programPath.toString().split("Praca_Magisterska-jar-with-dependencies.jar")[0];
            System.out.println(path);
        }
        catch(URISyntaxException e)
        {
            log.info("Wystąpił problem z podanym adresem URL: " + e);
        }
        System.out.println("Set url example (example.com): ");
        Scanner scannerUrl = new Scanner(System.in);
        Scanner scannerPortFrom = new Scanner(System.in);
        Scanner scannerPortTo = new Scanner(System.in);
        String url = scannerUrl.nextLine();
        System.out.println("Start port: ");
        int startPort = scannerPortFrom.nextInt();
        System.out.println("End port: ");
        int endPort = scannerPortTo.nextInt();
        System.out.println("Start testing.....");
        Service service = new Service(url, startPort, endPort, path);
        service.run();
        System.out.println("Finish");
    }
}
