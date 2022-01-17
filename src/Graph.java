import java.util.*;

public class Graph {

        class Vertex {
            public int label; // to hold the name / number of the vertex
            Vertex[] neighbors; // array of neighbors, adjacency list
            int[] walls; //array of walls, -1 edge of maze, 0 broken wall, 1 intact wall, 4 entry/exit
            int color; // white 0, grey 1, black 2
            Vertex pi; // parent
            int startTime; // found time when it turns grey
            int endTime; // when it turns black
            int distance; // ADDED
            boolean inPath; //true if the Vertex is in the solution path and false if it is not
            int traverseOrder; //the order in which the vertex is traversed while solving


            public Vertex(int lab) {
                label = lab;
                /*
                 * index correspondence for neighbors 0 = up 1 = right 2 = down 3 = left
                 */
                neighbors = new Vertex[4];
                walls = new int[4];
                /*
                 * index correspondence for walls 0 = up wall;  1 = right wall;
                 * 2 = down wall;  3 = left wall
                 */
                setAllWallsIntact();
                startTime = Integer.MAX_VALUE; //start time = infinity
                endTime = Integer.MAX_VALUE; //end time = infinity
                pi = null;
                distance = 0;
                inPath = false;
                traverseOrder = 0;
            }

            /*
             * checks if all walls intact
             */
            public boolean allWallsIntact() {
                for (int i = 0; i < walls.length; i++) {
                    if (walls[i] == 0) {
                        return false;
                    }
                }
                return true;
            }

            /*
             * sets all walls intact
             */
            public void setAllWallsIntact() {
                for (int i = 0; i < walls.length; i++) {
                    walls[i] = 1;
                }
            }


            /*
             * Methods to break walls
             */
            public void breakUpWall() {
                if (walls[0] != -1)
                    walls[0] = 0;
            }

            public void breakRightWall() {
                if (walls[1] != -1)
                    walls[1] = 0;
            }

            public void breakDownWall() {
                if (walls[2] != -1)
                    walls[2] = 0;
            }

            public void breakLeftWall() {
                if (walls[3] != -1)
                    walls[3] = 0;
            }


            /*
             * Methods to set neighbors
             */
            public void setLeft(Vertex v) {
                neighbors[3] = v;
            }

            public void setRight(Vertex v) {
                neighbors[1] = v;
            }

            public void setUp(Vertex v) {
                neighbors[0] = v;
            }

            public void setDown(Vertex v) {
                neighbors[2] = v;
            }


            /*
             * Methods to get neighbors
             */
            public Vertex getLeft() {
                return this.neighbors[3];
            }

            public Vertex getRight() {
                return this.neighbors[1];
            }

            public Vertex getUp() {
                return this.neighbors[0];
            }

            public Vertex getDown() {
                return this.neighbors[2];
            }

            /*
             * finds the relationship between this vertex and vertex v
             */
            public int vertexRelationship(Vertex v) {
                if (getUp() != null && getUp().equals(v)) {
                    return 0;
                } else if (getRight() != null && getRight().equals(v)) {
                    return 1;
                } else if (getDown() != null && getDown().equals(v)) {
                    return 2;
                } else {
                    return 3;
                }
            }
        }

        Vertex vertexList[][];  //2d array of the vertices
        int amountVertices; //total vertices in maze
        int dimension; // dimension of maze
        private Random myRandGen; // random number generator
        private Vertex startVertex; //start of maze
        private Vertex endVertex; //end of maze
        int time; //time used for dfs
        int[] traversed; //array either 0 or 1. 0 means that order # hasn't been selected and 1 means it has been

        /**
         * Constructor for this program takes in the dimensions of the maze It also
         * makes the seed of the random generator 0 for easier testing
         *
         * *** calls method to fill the graph with n*n rooms
         *
         * @param dimension_in as number of rows and columns
         */

        public Graph(int dimension_in) {
            vertexList = new Vertex[dimension_in][dimension_in];
            // for simplicity of naming vertices, r keeps track of what "row" the
            // vertex is being created in
            int r = 1;
            for (int i = 0; i < dimension_in; i++) {
                for (int j = 0; j < dimension_in; j++) {
                    vertexList[i][j] = new Vertex(i + r + j);
                }
                // r increases by a function of the length of the column
                r += dimension_in - 1;
            }

            dimension = dimension_in;
            amountVertices = dimension * dimension;
            myRandGen = new java.util.Random(0); // seed is 0
            startVertex = vertexList[0][0]; // set startVertex to top left
            endVertex = vertexList[dimension - 1][dimension - 1]; // set endVertex to bottom right
            traversed = new int[dimension*dimension];
        }


        /*
         * sets the path of the solution using the parent and working backwards
         * useful for printing the DFS and BFS
         */
        public void setPath(){
            Vertex current = vertexList[dimension-1][dimension-1];
            while (current != null){
                current.inPath = true;
                current = current.pi;
            }
        }


        /*
         * resets graph
         * makes all vertices in vertex list to white, changes start and end times back to infinity, distance from source to 0
         */
        public void graphReset() {
            for (int i = 0; i < dimension; i++) {
                for (int j = 0; j < dimension; j++) {
                    vertexList[i][j].color = 0;
                    vertexList[i][j].pi = null;
                    vertexList[i][j].startTime = Integer.MAX_VALUE; //infinity
                    vertexList[i][j].endTime = Integer.MAX_VALUE;
                    vertexList[i][j].distance = 0;
                }
            }
            startVertex.walls[0] = 4; //sets startVertex up wall as entry point
            endVertex.walls[2] = 4; //sets endVertex down wall as exit point
        }


        /**
         * DFS Solution
         * Solves the maze using Depth-first search, and uses a stack.
         * @param s - the starting vertex - the root
         */
        public void DFS(Vertex s) {
            int traverseO = 1;
            Stack<Vertex> q = new Stack<>();
            q.push(s);
            while (!q.isEmpty() && !q.peek().equals(endVertex)) { //while we aren't at the end
                Vertex u = q.pop();
                for (int i = 0; i < u.neighbors.length; i++) {
                    Vertex v = u.neighbors[i];
                    int direction = u.vertexRelationship(v);
                    if ((u.walls[direction] == 0) && v != null && v.color == 0) { //if the wall is broken and it's not null and it hasn't been visited
                        v.color = 1; //color = grey
                        if (v.traverseOrder == 0){ //if this is the first time it has been visited
                            if (traversed[traverseO] == 0){ //if this number hasn't been used
                                v.traverseOrder = traverseO;
                                traversed[traverseO] = 1;	//set it to used
                            } else {		//if the number has been used
                                traverseO++;
                                v.traverseOrder = traverseO;
                                traversed[traverseO] = 1; //set it to used
                            }
                        }
                        v.distance = u.distance + 1;
                        v.pi = u;
                        q.push(v);
                    }
                }
                u.color = 2; //color = black
            }
        }


        /**
         * BFS: Breadth-first Search solution to the maze
         * uses a queue
         *
         * @param s - the starting vertex - the root
         */
        public void BFS(Vertex s) {
            int traverseO = 1;
            Queue<Vertex> q = new LinkedList<>();
            q.add(s);
            while (!q.isEmpty() && !q.peek().equals(endVertex)) { //while we aren't at the end
                Vertex u = q.remove();
                for (int i = 0; i < u.neighbors.length; i++) {
                    Vertex v = u.neighbors[i];
                    int direction = u.vertexRelationship(v);
                    if ((u.walls[direction] == 0) && v != null && v.color == 0) { //if the wall is broken and it's not null and it hasn't been visited
                        v.color = 1; //color = grey
                        if (v.traverseOrder == 0){ //if this is the first time it has been visited
                            if (traversed[traverseO] == 0){ //if this number hasn't been used
                                v.traverseOrder = traverseO;
                                traversed[traverseO] = 1;	//set it to used
                            } else {		//if the number has been used
                                traverseO++;
                                v.traverseOrder = traverseO;
                                traversed[traverseO] = 1; //set it to used
                            }
                        }
                        v.distance = u.distance + 1;
                        v.pi = u;
                        q.add(v);
                    }
                }
                u.color = 2; //color = black
            }
        }

        /*
         * populates graph to the dimension provided in the constructor of graph
         * also sets the neighbors and walls of the vertices
         */
        public void populateGraph() {
            for (int i = 0; i < dimension; i++) {
                for (int j = 0; j < dimension; j++) {
                    Vertex mine = vertexList[i][j];

                    if (i == 0) {
                        mine.setUp(null);
                        mine.walls[0] = -1; // edge wall
                    }

                    else {
                        mine.setUp(vertexList[i - 1][j]); //setting the up wall
                        mine.neighbors[0] = vertexList[i - 1][j]; //setting up neighbor
                    }

                    if (j == 0) {
                        mine.setLeft(null);
                        mine.walls[3] = -1;
                    }

                    else {
                        mine.setLeft(vertexList[i][j - 1]);
                        mine.neighbors[3] = vertexList[i][j - 1];
                    }

                    if (i == dimension - 1) {
                        mine.setDown(null);
                        mine.walls[2] = -1;
                    }

                    else {
                        mine.setDown(vertexList[i + 1][j]);
                        mine.neighbors[2] = vertexList[i + 1][j];
                    }

                    if (j == dimension - 1) {
                        mine.setRight(null);
                        mine.walls[1] = -1;
                    }

                    else {
                        mine.setRight(vertexList[i][j + 1]);
                        mine.neighbors[1] = vertexList[i][j + 1];
                    }
                }
            }
        }

        /**
         * Get a random number
         *
         * @return random double between 0 and 1
         */
        double myRandom() {
            return myRandGen.nextDouble();
        }


        /**
         * Generates a pseudo-random perfect maze.
         */
        void generateMaze() {
            //the general algorithm
            /*
             * create a CellStack (LIFO) to hold a list of cell locations set
             * TotalCells= number of cells in grid choose the starting cell and call
             * it CurrentCell set VisitedCells = 1 while VisitedCells < TotalCells
             * find all neighbors of CurrentCell with all walls intact if one or
             * more found choose one at random knock down the wall between it and
             * CurrentCell push CurrentCell location on the CellStack make the new
             * cell CurrentCell add 1 to VisitedCells else pop the most recent cell
             * entry off the CellStack make it CurrentCell
             */

            graphReset(); //first reset the graph
            populateGraph();

            //4 is a special designation for starting and ending vertices
            startVertex.walls[0] = 4;
            endVertex.walls[2] = 4;

            var cellStack = new Stack<Vertex>();
            int totalCells = amountVertices;
            Vertex currentCell = vertexList[0][0];
            int visitedCells = 1;
            while (visitedCells < totalCells) {
                ArrayList<Vertex> neighborsIntact = new ArrayList<Vertex>();
                for (int i = 0; i < currentCell.neighbors.length; i++) {
                    Vertex neighbor = currentCell.neighbors[i];
                    if (neighbor != null) {
                        if (neighbor.allWallsIntact()) {
                            neighborsIntact.add(neighbor);
                        }
                    }
                }
                // if one or more walls
                if (neighborsIntact.size() != 0) {
                    // rand tells you which vertex you are knocking down a wall between
                    int rand = (int) (myRandom() * neighborsIntact.size());
                    Vertex knockDown = neighborsIntact.get(rand);
                    int relationship = currentCell.vertexRelationship(knockDown); // finds relationship between current and knockDown
                    if (relationship == 0) {// knockDown is above currentCell
                        currentCell.breakUpWall();
                        knockDown.breakDownWall();
                    } else if (relationship == 1) { // knockDown is to the right of currentCell
                        currentCell.breakRightWall();
                        knockDown.breakLeftWall();
                    } else if (relationship == 2) { // knockDown is below currentCell
                        currentCell.breakDownWall();
                        knockDown.breakUpWall();
                    } else { // knockDown is to the left of currentCell
                        currentCell.breakLeftWall();
                        knockDown.breakRightWall();
                    }
                    // push CurrentCell location on the CellStack
                    cellStack.push(currentCell);
                    // make the new cell CurrentCell
                    currentCell = knockDown;
                    // add 1 to VisitedCells
                    visitedCells++;

                } else {
                    currentCell = cellStack.pop();
                }
            }

        }

        /*
         * Prints empty grid
         * Prints all the top walls, the left and right, and the bottom row for an empty Grid
         */
        public String printGrid() {
            String grid = "";
            // first print the top layer

            int n = 2;
            for (int i = 0; i < dimension; i++) {
                if (i == dimension - 1)
                    n = 3;
                for (int layer = 1; layer <= n; layer++) {
                    // layer represents the layers of a cell: up, left/right, and down
                    // top layer already printed; for the rest, print sides and bottom
                    // 1 = top
                    // 2 = left and right
                    // 3 = bottom

                    if (layer == 1) {
                        grid += "+"; //first symbol of layer 1
                    }

                    if (layer == 2) {
                        grid += "|"; //first symbol of layer 2
                    }

                    if ((layer == 3) && (i == dimension - 1)) {
                        grid += "+"; //first symbol of layer three
                    }

                    for (int j = 0; j < dimension; j++) {
                        Vertex v = vertexList[i][j];

                        // prints according to the layer
                        // layer one --> print up
                        if (layer == 1) {
                            if ((v.walls[0] != 0) && (v.walls[0] != 4)) // if -1, edge wall; if 1, inner wall, 0 is broken wall
                                grid += "-";
                            else
                                grid += " ";

                            grid += "+";
                        }

                        // layer two --> print left/right and label
                        else if (layer == 2) {
                            grid += " ";

                            if (v.walls[1] != 0) // right wall is 1
                                grid += "|";
                            else
                                grid += " ";
                        }

                        // layer three --> print bottom layer
                        else if ((layer == 3) && (i == dimension - 1)) {
                            // down wall
                            // if there is an down wall, include symbol

                            if ((v.walls[2] != 0) && (v.walls[2] != 4)) // if -1, edge wall; if 1, inner  wall, 0 is broken wall, 4 is entrance/exit
                                grid += "-";
                            else
                                grid += " ";

                            grid += "+";

                        }
                    }
                    grid += "\n";
                }
            }
            return grid;
        }

        /*
         * Prints the grid for BFS and DFS - displays the numbers and the path
         *
         */
        public String printGrid1() {
            String grid = "";
            // first print the top layer

            int n = 2;
            for (int i = 0; i < dimension; i++) {
                if (i == dimension - 1)
                    n = 3;
                for (int layer = 1; layer <= n; layer++) {
                    // layer represents the layers of a cell: up, left/right, and
                    // down
                    // top layer already printed; for the rest, print sides and
                    // bottom
                    // 1 = top
                    // 2 = left and right
                    // 3 = bottom

                    if (layer == 1) {
                        grid += "+";
                    }

                    if (layer == 2) {
                        grid += "|";
                    }

                    if ((layer == 3) && (i == dimension - 1)) {
                        grid += "+";
                    }

                    for (int j = 0; j < dimension; j++) {
                        Vertex v = vertexList[i][j];

                        // prints according to the layer
                        // layer one --> print up
                        if (layer == 1) {
                            if ((v.walls[0] != 0) && (v.walls[0] != 4)) // if -1, edge wall; if 1, inner wall, 0 is broken
                                grid += "-";
                            else
                                grid += " ";

                            grid += "+";
                        }

                        // layer two --> print left/right and label
                        else if (layer == 2) {

                            // print label

                            if ((v != null) && v == (vertexList[0][0])){
                                grid += "0";
                            } else if (v.pi == null) { // don't print label
                                grid += " ";
                            } else {
                                grid += ((v.traverseOrder)%10);
                            }


                            if (v.walls[1] != 0) // right wall is 1
                                grid += "|";
                            else
                                grid += " ";
                        }

                        // layer three --> print bottom layer
                        else if ((layer == 3) && (i == dimension - 1)) {
                            // down wall
                            // if there is an down wall, include symbol
                            if ((v.walls[2] != 0) && (v.walls[2] != 4)) // if -1, edge wall; if 1, inner wall, 0 is broken wall, 4 is entry/exit
                                grid += "-";
                            else
                                grid += " ";

                            grid += "+";

                        }
                    }
                    grid += "\n";
                }
            }

            return grid;
        }


        /*
         * Prints the maze with the '#'s and not numbers - for BFS and DFS
         */
        public String printGrid2() {
            String grid = "";
            // first print the top layer

            int n = 2;
            for (int i = 0; i < dimension; i++) {
                if (i == dimension - 1)
                    n = 3;
                for (int layer = 1; layer <= n; layer++) {
                    // layer represents the layers of a cell: up, left/right, and
                    // down
                    // top layer already printed; for the rest, print sides and
                    // bottom
                    // 1 = top
                    // 2 = left and right
                    // 3 = bottom

                    if (layer == 1) {
                        grid += "+";
                    }

                    if (layer == 2) {
                        grid += "|";
                    }

                    if ((layer == 3) && (i == dimension - 1)) {
                        grid += "+";
                    }

                    for (int j = 0; j < dimension; j++) {
                        Vertex v = vertexList[i][j];

                        // prints according to the layer
                        // layer one --> print up
                        if (layer == 1) {
                            if ((v.walls[0] != 0) && (v.walls[0] != 4)) // if -1, edge wall; if 1 inner wall if 0 broken wall														// wall
                                grid += "-";
                            else if (v.equals(startVertex)){
                                grid += "#";
                            }
                            else if (v.inPath && v.getUp() != null && v.getUp().inPath) //makes sure the one above is in path too
                                grid += "#";
                            else {
                                grid += " ";
                            }

                            grid += "+";
                        }

                        // layer two --> print left/right and label
                        else if (layer == 2) {
                            if (v == null) { // don't print label
                                grid += " ";
                            } else if (v.inPath){
                                grid += (("#"));
                            } else {
                                grid += " ";
                            }

                            if (v.walls[1] != 0) // right wall is 1
                                grid += "|";
                            else
                                grid += " ";
                        }

                        // layer three --> print bottom layer
                        else if ((layer == 3) && (i == dimension - 1)) {
                            // down wall
                            // if there is an down wall, include symbol

                            if ((v.walls[2] != 0) && (v.walls[2] != 4)) // if -1, edge wall; if 1, inner wall, 0 is broken wall, 4 is entry/exit
                                grid += "-";
                            else if (v.inPath)
                                grid += "#";
                            else
                                grid += " ";

                            grid += "+";

                        }
                    }
                    grid += "\n";
                }
            }

            return grid;
        }


        public static void main(String[] args) {
            //Runs the program - generate a maze, and solve it with BFS and DFS for a maze of size 4
            Graph g = new Graph(4);
            g.generateMaze();
            System.out.println("Grid: ");
            String grid = g.printGrid();
            System.out.println(grid);
            g.DFS(g.vertexList[0][0]);
            String aGrid = g.printGrid1();
            System.out.println();
            System.out.println("DFS");
            System.out.println(aGrid);
            g.setPath();
            String dGrid = g.printGrid2();
            System.out.println();
            System.out.println(dGrid);

            Graph g1 = new Graph(4);
            g1.generateMaze();
            g1.BFS(g1.vertexList[0][0]);
            String bGrid = g1.printGrid1();
            System.out.println();
            System.out.println("BFS");
            System.out.println(bGrid);
            g1.setPath();
            String cGrid = g1.printGrid2();
            System.out.println();
            System.out.println(cGrid);
        }
}
