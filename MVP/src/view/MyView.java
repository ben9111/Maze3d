package view;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import algorithms.mazeGenerator.Maze3d;
import algorithms.mazeGenerator.Position;
import algorithms_search.Solution;
import presenter.Command;

/**
 * @author ben & adam
 * 
 * @param the
 *            class implements the view interface and show in to the user
 * 
 */

public class MyView extends Observable implements View, Observer {

	private BufferedReader in;
	private PrintWriter out;
	private CLI cli;

	public MyView(BufferedReader in, PrintWriter out) {

		this.in = in;
		this.out = out;

		cli = new CLI(in, out);
		cli.addObserver(this);
	}

	@Override
	public void notifyMazeIsReady(String name) {
		/*
		 * System.out.println("Maze is ready!");
		 */
		out.println("maze " + name + " is ready");
		out.flush();
	}

	public void notifySolutionIsReady(String name) {
		out.println("Solution " + name + " is ready");
		out.flush();
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

	@Override
	public void displayMessage(String msg) {
		out.println(msg);
		out.flush();
	}

	@Override
	public void update(Observable o, Object arg) {
		if (o == cli)
			setChanged();
		notifyObservers(arg);
	}

}
