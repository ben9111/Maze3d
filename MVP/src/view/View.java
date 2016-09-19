package view;

import java.util.HashMap;

import algorithms.mazeGenerator.Maze3d;
import algorithms.mazeGenerator.Position;
import algorithms_search.Solution;
import presenter.Command;

public interface View {
	void notifySolutionIsReady(String name);
	void notifyMazeIsReady(String name);
	void displayMaze(Maze3d maze);
	void display_cross_section(int[][] maze2d);
	void setCommands(HashMap<String, Command> command);
	void display_solution(Solution<Position> solve);
	void view_path(String path);
	void exit();
	void start();
	public void displayMessage(String msg);

}
