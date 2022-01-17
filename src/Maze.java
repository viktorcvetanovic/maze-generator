import java.util.Arrays;

public class Maze {

    private int mazeWidth;
    private int mazeHeight;
    private boolean draw;
    private boolean initialize;
    private final int FIRST_X = 0;
    private final int FIRST_Y = 0;
    private int lastX;
    private int lastY;
    private int beginX;
    private int beginY;
    private boolean[][] maze;
    private boolean[][] vertical;
    private boolean[][] horizontal;
    private boolean[][] mazeSolution;
    private boolean north;
    private boolean east;
    private boolean south;
    private boolean west;
    private boolean solved;

    public Maze(int width, int height) {
        mazeWidth = width;
        mazeHeight = height;
        maze = new boolean[mazeWidth * 2 + 1][mazeHeight * 2 + 1];
        vertical = new boolean[mazeWidth * 2 + 1][mazeHeight * 2 + 1];
        horizontal = new boolean[mazeWidth * 2 + 1][mazeHeight * 2 + 1];
        mazeSolution = new boolean[mazeWidth * 2 + 1][mazeHeight * 2 + 1];
        solved = false;
        initialize = false;
        init();
        if (mazeWidth != 1 && mazeHeight != 1) {
            generateMaze(FIRST_X, mazeWidth * 2, FIRST_Y, mazeHeight * 2, mazeWidth, mazeHeight);
        }
    }

    private void init() {
        for (int x = 0; x < mazeWidth * 2; x++) {
            maze[x][0] = true;
            maze[x][mazeHeight * 2] = true;
        }
        for (int y = 0; y < mazeHeight * 2; y++) {
            maze[0][y] = true;
            maze[mazeWidth * 2][y] = true;
        }
    }




    public void showMaze() {
        for (int y = 0; y < mazeHeight * 2 + 1; y++) {
            for (int x = 0; x < mazeWidth * 2 + 1; x++) {
                if (maze[x][y]) {
                    if (y == 0 || y == (mazeHeight * 2)) {
                        if (x == 0) {
                            System.out.print("+");
                        }
                        if (x % 2 == 0 && (x != mazeWidth * 2)) {
                            System.out.print("---");
                        } else {
                            if (x != mazeWidth * 2) {
                                System.out.print("+");
                            }
                        }
                    } else if (x == 0 || x == (mazeWidth * 2)) {
                        if (y % 2 == 0) {
                            if (horizontal[x][y] && x == (mazeWidth * 2)) {
                                System.out.print("---");
                            }
                            System.out.print("+");
                        } else {
                            System.out.print("|");
                        }
                    } else if (vertical[x][y]) {
                        if (y % 2 == 0) {
                            System.out.print("+");
                        } else {
                            System.out.print("|");
                        }
                    } else if (horizontal[x][y]) {
                        if (x % 2 == 0) {
                            System.out.print("+");
                        } else {
                            System.out.print("---");
                        }
                    }
                } else if (mazeSolution[x][y]) {
                    if (x == beginX && y == beginY) {
                        if (mazeSolution[x + 1][y]) {
                            System.out.print(" **");
                        }
                        else if(mazeSolution[x-1][y]) {
                            System.out.print("** ");
                        }
                        else {
                            System.out.print(" * ");
                        }

                    } else if (x == lastX && y == lastY && (mazeSolution[x - 1][y] || mazeSolution[x + 1][y])) {
                        if (mazeSolution[x - 1][y]) {
                            System.out.print("** ");
                        }
                        if (mazeSolution[x + 1][y]) {
                            System.out.print(" **");
                        }
                    } else if (vertical[x - 1][y] && vertical[x + 1][y]) {
                        System.out.print(" * ");
                    } else if (x % 2 == 1 && mazeSolution[x][y - 1] && mazeSolution[x - 1][y] && !mazeSolution[x + 1][y]) {
                        System.out.print("** ");
                    } else if (x % 2 == 1 && mazeSolution[x][y + 1] && mazeSolution[x - 1][y] && !mazeSolution[x + 1][y]) {
                        System.out.print("** ");
                    } else if (x % 2 == 1 && mazeSolution[x][y + 1] && !mazeSolution[x - 1][y] && mazeSolution[x + 1][y]) {
                        System.out.print(" **");
                    } else if (x % 2 == 1 && mazeSolution[x][y - 1] && !mazeSolution[x - 1][y] && mazeSolution[x + 1][y]) {
                        System.out.print(" **");
                    } else if (x % 2 == 1 && (mazeSolution[x + 1][y] || mazeSolution[x - 1][y])) {
                        if (vertical[x - 1][y]) {
                            System.out.print(" **");
                        } else if (vertical[x + 1][y]) {
                            System.out.print("** ");
                        } else {
                            System.out.print("***");
                        }
                    } else if (x % 2 == 0 && mazeSolution[x + 1][y] || mazeSolution[x - 1][y]) {
                        System.out.print("*");
                    } else {
                        System.out.print(" * ");
                    }
                } else if (x % 2 == 1) {
                    System.out.print("   ");
                } else {
                    System.out.print(" ");
                }
                if (x == mazeWidth * 2) {
                    System.out.println("");
                }
            }
        }
    }


    private void generateMaze(int startX, int finishX, int startY, int finishY, int width, int height) {
        int midX;
        int midY;

        midX = (startX + finishX) / 2;
        midY = (startY + finishY) / 2;

        if (midX % 2 == 1) {
            midX++;
        }
        if (midY % 2 == 1) {
            midY++;
        }

        for (int x = startX; x < finishX; x++) {
            maze[x][midY] = true;
            horizontal[x][midY] = true;
        }
        for (int y = startY; y < finishY; y++) {
            maze[midX][y] = true;
            vertical[midX][y] = true;
        }

        double r = Math.random();
        System.out.println("Generisan broj je: " + r);

        if (r <= .25) {
            north = false;
            east = true;
            west = true;
            south = true;
        } else if (r <= .5) {
            north = true;
            east = false;
            west = true;
            south = true;
        } else if (r <= .75) {
            north = true;
            east = true;
            west = false;
            south = true;
        } else {
            north = true;
            east = true;
            west = true;
            south = false;
        }

        int oddRandom;
        int max;
        int min;

        if (north) {

            max = midY;
            min = startY;

            if (max % 2 == 0) {
                max--;
            }
            if (min == 0) {
                min++;
            }

            oddRandom = min + 2 * (int) (Math.random() * ((max - min) / 2 + 1));
            if (oddRandom % 2 == 0) {
                oddRandom++;
            }
            System.out.println("Ovo je odd Random za North: " + oddRandom);
            maze[midX][oddRandom] = false;
            vertical[midX][oddRandom] = false;
        }
        if (south) {

            max = finishY;
            min = midY;

            if (max % 2 == 0) {
                max--;
            }
            if (min % 2 == 0) {
                min++;
            }

            oddRandom = min + 2 * (int) (Math.random() * ((max - min) / 2 + 1));
            if (oddRandom == height || oddRandom == height - 1) {
                oddRandom = oddRandom + 2;
            }
            System.out.println("Ovo je odd Random za South: " + oddRandom);
            maze[midX][oddRandom] = false;
            vertical[midX][oddRandom] = false;
        }
        if (west) {

            max = midX;
            min = startX;

            if (max % 2 == 0) {
                max--;
            }
            if (min == 0) {
                min++;
            }

            oddRandom = min + 2 * (int) (Math.random() * ((max - min) / 2 + 1));
            if (oddRandom % 2 == 0) {
                oddRandom++;
            }
            System.out.println("Ovo je odd Random za West: " + oddRandom);
            maze[oddRandom][midY] = false;
        }
        if (east) {

            max = finishX;
            min = midX;

            if (max % 2 == 0) {
                max--;
            }
            if (min == 0) {
                min++;
            }

            oddRandom = min + 2 * (int) (Math.random() * ((max - min) / 2 + 1));
            if (oddRandom % 2 == 0) {
                oddRandom++;
            }
            System.out.println("Ovo je odd Random za East: " + oddRandom);
            maze[oddRandom][midY] = false;
        }

        if (width == 3 || width / 2 >= 1 || height == 3 || height / 2 >= 1) {

            if (midX - startX > 2 && midY - startY > 2) {
                generateMaze(startX, midX, startY, midY, width / 2, height / 2);
            }
            if (midX - startX > 2 && finishY - midY > 2) {
                generateMaze(startX, midX, midY, finishY, width / 2, height / 2);
            }
            if (finishX - midX > 2 && finishY - midY > 2) {
                generateMaze(midX, finishX, midY, finishY, width / 2, height / 2);
            }
            if (finishX - midX > 2 && midY - startY > 2) {
                generateMaze(midX, finishX, startY, midY, width / 2, height / 2);
            }
        }
    }

    public boolean solveMaze(int sx, int sy, int ex, int ey, int px, int py) {
        if (!initialize) {
            mazeSolution[sx][sy] = true;
            beginX = sx;
            beginY = sy;
            lastX = ex;
            lastY = ey;
            initialize = true;
        }
        if (sx == px && sy == py) {
            mazeSolution[sx][sy] = false;
        }
        if (sx == ex && sy == ey) {
            solved = true;
            return true;
        } else {
            if (sy - 1 > 0) {
                if (!maze[sx][sy - 1] && !mazeSolution[sx][sy - 1] && !(sx == px && sy == py)) {
                    mazeSolution[sx][sy - 1] = true;
                    draw = solveMaze(sx, sy - 1, ex, ey, sx, sy);
                    mazeSolution[sx][sy - 1] = draw;
                    if (solved) {
                        return true;
                    }
                }
            }
            if (sx + 1 < mazeWidth * 2) {
                if (!maze[sx + 1][sy] && !mazeSolution[sx + 1][sy] && !(sx == px && sy == py)) {

                    mazeSolution[sx + 1][sy] = true;
                    draw = solveMaze(sx + 1, sy, ex, ey, sx, sy);
                    mazeSolution[sx + 1][sy] = draw;
                    if (solved) {
                        return true;
                    }
                }
            }
            if (sy + 1 < mazeHeight * 2) {
                if (!maze[sx][sy + 1] && !mazeSolution[sx][sy + 1] && !(sx == px && sy == py)) {

                    mazeSolution[sx][sy + 1] = true;
                    draw = solveMaze(sx, sy + 1, ex, ey, sx, sy);
                    mazeSolution[sx][sy + 1] = draw;
                    if (solved) {
                        return true;
                    }
                }
            }
            if (sx - 1 > 0) {
                if (!maze[sx - 1][sy] && !mazeSolution[sx - 1][sy] && !(sx == px && sy == py)) {

                    mazeSolution[sx - 1][sy] = true;
                    draw = solveMaze(sx - 1, sy, ex, ey, sx, sy);
                    mazeSolution[sx - 1][sy] = draw;
                    if (solved) {
                        return true;
                    }
                }
            }
        }
        mazeSolution[sx][sy] = false;
        return false;
    }

}