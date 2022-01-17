import java.util.Scanner;

public class Main {

    private static Scanner keyboard;
    private static Maze testMaze;

    public static void main(String[] args) {
        keyboard = new Scanner(System.in);
        int w;
        int h;
        String answer;


        System.out.print("Enter the width of the maze: ");
        w = keyboard.nextInt();

        System.out.print("Enter the height of the maze: ");
        h = keyboard.nextInt();

        testMaze = new Maze(w, h);

        testMaze.showMaze();

        System.out.println("");
        System.out.print("Solve maze? (y/n): ");
        answer = keyboard.next();

        if (answer.equalsIgnoreCase("y")){

            testMaze.solveMaze(1, 1, (w-1) * 2 + 1, (h-1) * 2 + 1, -1, -1);
            testMaze.showMaze();
        }

    }
}