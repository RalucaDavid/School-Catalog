package org.school;

import org.school.server.ServerApplication;

public class App {
    public static void main(String[] args) {
        try {
            new ServerApplication().run("server", "configuration.yml");
        }
        catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }
}
