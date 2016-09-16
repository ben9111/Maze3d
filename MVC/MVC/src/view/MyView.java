package view;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.HashMap;

import algorithms.mazeGenerator.Maze3d;
import algorithms.mazeGenerator.Position;
import algorithms_search.Solution;
import controller.Command;
import controller.Controller;

/**
 * @author ben & adam
 * 
 * @param the
 *            class implements the view interface and show in to the user
 * 
 */

public class MyView implements View {

	private BufferedReader in;
	private PrintWriter out;
	private CLI cli;
	private Controller controller;

	public MyView(BufferedReader in, PrintWriter out) {

		this.in = in;
		this.out = out;

		cli = new CLI(in, out);
	}

	@Override
	public void notifyMazeIsReady(String name) {
		/*
		 * System.out.println("Maze is ready!");
		 */
		out.println("maze " + name + " is ready");
		out.flush();
	}

	@Override
	public void setController(Controller controller) {
		this.controller = controller;
	}

	@Override
	public void displayMaze(Maze3d maze) {
		out.println(maze.toString());
		out.flush();
	}

	@Override
	public void setCommands(HashMap<String, Command> command) {
		cli.setCommands(command);
	}

	@Override
	public void start() {
		cli.start();
	}

	@Override
	public void display_cross_section(int[][] maze2d) {
		for (int i = 0; i < maze2d.length; i++) {

			for (int j = 0; j < maze2d[0].length; j++) {

				out.print(maze2d[i][j]);

			}
			out.println();
		}
		out.flush();

	}

	@Override
	public void display_solution(Solution<Position> solve) {
		out.println(solve);
		out.flush();
	}

	@Override
	public void exit() {
		out.print("Program has Been Terminted,Bye");
		out.flush();
	}

	@Override
	public void view_path(String path) {
		out.println(path);
		out.flush();
	}

}
