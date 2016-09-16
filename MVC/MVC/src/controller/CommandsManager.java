package controller;

import java.util.HashMap;

import algorithms.mazeGenerator.Maze3d;
import algorithms.mazeGenerator.Position;
import algorithms_search.Solution;
import model.Model;
import view.View;

/**
 * @author ben & adam
 * @category 
 * @param view , model
 */
public class CommandsManager {

	private Model model;
	private View view;

	public CommandsManager(Model model, View view) {
		this.model = model;
		this.view = view;
	}

	public HashMap<String, Command> getCommandsMap() {
		HashMap<String, Command> commands = new HashMap<String, Command>();
		commands.put("dir", new displayPath());
		commands.put("generate_maze", new GenerateMazeCommand());
		commands.put("display", new DisplayMazeCommand());
		commands.put("display_cross_section", new CrossSection());
		commands.put("save_maze", new saveMaze());
		commands.put("load_maze", new loadMaze());
		commands.put("solve", new solveMaze());
		commands.put("display_solution", new display_solution());
		commands.put("exit", new exit());
		return commands;
	}

	public class displayPath implements Command {

		@Override
		public void doCommand(String[] args) {

			String path = args[0];
			String file = model.modelPath(path);
			view.view_path(file);
		}

	}

	public class exit implements Command {
		@Override
		public void doCommand(String[] args) {

			model.exit();
			view.exit();

		}
	}

	public class display_solution implements Command {

		@Override
		public void doCommand(String[] args) {

			String nameOfMaze = args[0];

			Solution<Position> s = model.modelGetSolution(nameOfMaze);
			view.display_solution(s);
		}

	}

	public class solveMaze implements Command {

		@Override
		public void doCommand(String[] args) {
			String nameOfMaze = args[0];
			String algorithm = args[1];

			model.model_solve_maze(nameOfMaze, algorithm);

		}
	}

	public class loadMaze implements Command {
		@Override
		public void doCommand(String[] args) {
			String nameofFile = args[0];
			String nameOfmaze = args[1];
			model.model_load_maze(nameofFile, nameOfmaze);

		}
	}

	public class saveMaze implements Command {

		@Override
		public void doCommand(String[] args) {
			String nameOfMaze = args[0];
			String nameofFile = args[1];

			model.model_save_maze(nameOfMaze, nameofFile);
		}

	}

	public class CrossSection implements Command {

		@Override
		public void doCommand(String[] args) {
			int index = Integer.parseInt(args[0]);
			String XYZ = args[1];
			String nameOfmaze = args[2];
			int[][] maze2d = model.model_cross_section(index, XYZ, nameOfmaze);
			view.display_cross_section(maze2d);

		}

	}

	public class GenerateMazeCommand implements Command {
		@Override
		public void doCommand(String[] args) {
			String name = args[0];
			int floors = Integer.parseInt(args[1]);
			int rows = Integer.parseInt(args[2]);
			int cols = Integer.parseInt(args[3]);
			model.generateMaze(name, floors, rows, cols);
		}
	}

	public class DisplayMazeCommand implements Command {
		@Override
		public void doCommand(String[] args) {
			String name = args[0];
			Maze3d maze = model.getMaze(name);
			view.displayMaze(maze);
		}

	}

}
