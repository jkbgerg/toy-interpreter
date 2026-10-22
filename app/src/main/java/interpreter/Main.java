package interpreter;

import interpreter.view.Examples;
import interpreter.view.RunExample;
import interpreter.view.TextMenu;
import interpreter.view.Command;

public class Main {
    public static void main(String[] args) {
        TextMenu menu = new TextMenu();

        menu.addCommand(new Command("0", "Exit") {
            @Override
            public void execute() {
                System.exit(0);
            }
        });

        menu.addCommand(new RunExample("1", "Run: int v; v = 2; print(v)", Examples.EX1));
        menu.addCommand(new RunExample("2", "Run: int a; int b; a = 2 + 3 * 5; b = a + 1; print(b)", Examples.EX2));
        menu.addCommand(new RunExample("3", "Run: bool a; int v; a = true; (if a then v = 2 else v = 3); print(v)", Examples.EX3));

        menu.show();
    }
}