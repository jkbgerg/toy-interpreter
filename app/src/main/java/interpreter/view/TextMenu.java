package interpreter.view;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class TextMenu {
    private final Map<String, Command> commands = new LinkedHashMap<>();

    public void addCommand(Command c) {
        commands.put(c.getKey(), c);
    }

    private void printMenu() {
        System.out.println("\n--- TOY LANGUAGE INTERPRETER ---");
        for (Command c : commands.values()) {
            System.out.printf("%4s : %s%n", c.getKey(), c.getDescription());
        }
    }

    public void show() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            printMenu();
            System.out.print("Input option: ");
            String inputKey = scanner.nextLine().strip();
            Command c = commands.get(inputKey);
            if (c == null) {
                System.out.println("Invalid option! Please try again.");
                continue;
            }
            c.execute();
        }
    }
}