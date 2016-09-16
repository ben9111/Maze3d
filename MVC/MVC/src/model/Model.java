package model;

import algorithms.mazeGenerator.Maze3d;
import algorithms.mazeGenerator.Position;
import algorithms_search.Solution;
import controller.Controller;

public interface Model {

	void generateMaze(String name, int floors, int rows, int cols);

	Maze3d getMaze(String name);

	void exit();

	void setController(Controller controller);

	public int[][] model_cross_section(int index, String XYZ, String nameOfmaze);

	public void model_save_maze(String nameOfmaze, String nameofFile);

	void model_load_maze(String nameofFile, String nameOfmaze);

	void model_solve_maze(String nameOfMaze, String algorithms);

	Solution<Position> modelGetSolution(String name);

	String modelPath(String path);

}
