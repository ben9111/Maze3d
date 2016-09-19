package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import algorithem.Demo.MazeAdapter;
import algorithms.mazeGenerator.GrowingTreeGenerator;
import algorithms.mazeGenerator.Maze3d;
import algorithms.mazeGenerator.Position;
import algorithms_search.BFS;
import algorithms_search.DFS;
import algorithms_search.Searchable;
import algorithms_search.Searcher;
import algorithms_search.Solution;
import io.MyCompressorOutputStream;
import io.MyDecompressorInputStream;
import presenter.Presenter;

/**
 * 
 * All the model's methods that responsible of the commands coming from the
 * controller At the end of each method
 * 
 */

public class MyModel extends Observable implements Model {

	// private ExecutorService executor;

	private Presenter presenter;
	private HashMap<String, Maze3d> MazeNames;
	private HashMap<String, Solution> MazeSolutions;
	private ExecutorService threadPool;

	private Map<String, Maze3d> mazes = new ConcurrentHashMap<String, Maze3d>();

	private Map<String, Solution<Position>> solutions = new ConcurrentHashMap<String, Solution<Position>>();
	private Map<Maze3d, Solution<Position>> calculatedSolutions = new ConcurrentHashMap<Maze3d, Solution<Position>>();

	public MyModel() {
		this.MazeNames = new HashMap<String, Maze3d>();
		this.MazeSolutions = new HashMap<String, Solution>();
		threadPool = Executors.newFixedThreadPool(50);
	}

	@Override
	public int[][] model_cross_section(int index, String XYZ, String nameOfmaze) {

		int[][] maze2d = null;
		if (!mazes.containsKey(nameOfmaze)) {
			setChanged();

			System.out.println(nameOfmaze + " Not Found");
		}

		else {
			Maze3d maze = mazes.get(nameOfmaze);
			if ((XYZ.compareTo("X") == 0 || (XYZ.compareTo("x") == 0))) {
				maze2d = maze.getCrossSectionByX(index);
			} else if ((XYZ.compareTo("Y") == 0 || (XYZ.compareTo("y") == 0))) {
				maze2d = maze.getCrossSectionByY(index);
			} else if ((XYZ.compareTo("Z") == 0 || (XYZ.compareTo("z") == 0))) {
				maze2d = maze.getCrossSectionByZ(index);
			}

			else
				System.out.println("Wrong input please Enter X,Y,Z");

		}
		return maze2d;

	}

	@Override
	public void model_save_maze(String nameOfmaze, String nameofFile) {
		if (!mazes.containsKey(nameOfmaze)) {

			setChanged();
			notifyObservers("display_msg " + "maze name not found");
		}

		else {

			Maze3d maze = mazes.get(nameOfmaze);

			OutputStream out;

			try {

				out = new MyCompressorOutputStream(new FileOutputStream(nameofFile));
				out.write(maze.toByteArray());

				out.flush();

				out.close();
				setChanged();

				notifyObservers("display_msg  Maze " + nameOfmaze + " was saved succesfully in the file " + nameofFile);
				// System.out.println("Maze: " + nameOfmaze + " " + " was saved
				// successfully in file " + nameofFile);
			}

			catch (FileNotFoundException e) {

				System.out.println("File " + nameofFile + "not exist");
				// e.printStackTrace();

			}

			catch (IOException e) {
				// e.printStackTrace();

			}

		}

	}

	@Override
	public void model_load_maze(String nameOfFile, String nameOfmaze) {

		Maze3d maze = mazes.get(nameOfmaze);

		InputStream in;

		try {

			in = new MyDecompressorInputStream(new FileInputStream(nameOfFile));

			byte b[] = new byte[maze.toByteArray().length];

			in.read(b);

			in.close();

			Maze3d loaded = new Maze3d(b);
			loaded.setStartPosition(maze.getStartPosition());
			loaded.setGoalPosition(maze.getGoalPosition());

			System.out.println("maze loaded from file:");
			// if (loaded.equals(myMaze))
			System.out.println(loaded.toString());
			setChanged();
			notifyObservers("display_msg  Maze" + nameOfmaze + " was loaded sucssucfully");
			// succesfully);
			// from file " + nameOfFile);
			// System.out.println("**ATTENTION** Maze: " + nameOfmaze + " was
			// loaded succesfully from file " + nameOfFile);
			// successfully from file " + nameOfFile);

		}

		catch (FileNotFoundException e) {
			e.printStackTrace();

		}

		catch (IOException e) {
			e.printStackTrace();

		}

	}

	@Override
	public void model_solve_maze(String nameOfMaze, String algorithms) {

		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {

				if (!mazes.containsKey(nameOfMaze)) {
					setChanged();
					// notifyObservers("maze_solution_ready maze name not
					// found");
					// System.out.println("Maze name not found");
				}

				else {
					Maze3d myMaze = mazes.get(nameOfMaze);
					Searchable<Position> adapter = new MazeAdapter(myMaze);
					Searcher<Position> my_algorithm;

					switch (algorithms) {

					case "BFS":
						my_algorithm = new BFS<Position>();
						setChanged();
						// System.out.println("maze solution ready BFS");
						notifyObservers("solution_ready " + nameOfMaze);
						break;
					case "DFS":
						my_algorithm = new DFS<Position>();
						setChanged();
						// System.out.println("maze solution ready DFS");

						notifyObservers("solution_ready " + nameOfMaze);

						break;
					default:
						setChanged();
						notifyObservers("display_msg " + "Wrong algorithm choose DFS/BFS");
						return;

					}

					solutions.put(nameOfMaze, my_algorithm.search(adapter));

					// System.out.println("Solution for " + nameOfMaze + " is
					// ready");

				}
			}

		});
		thread.start();
		threadPool.submit(thread);

	}

	class GenerateMazeRunnable implements Runnable {

		private int floors, rows, cols;
		private String name;
		private GrowingTreeGenerator generator;

		public GenerateMazeRunnable(String name, int floors, int rows, int cols) {

			this.name = name;
			this.floors = floors;
			this.rows = rows;
			this.cols = cols;

		}

		@Override
		public void run() {
			generator = new GrowingTreeGenerator();
			Maze3d maze = generator.generate(floors, rows, cols);
			mazes.put(name, maze);
			setChanged();
			notifyObservers("maze_ready" + name);

		}

		public void terminate() {
			generator.setDone(true);
		}

	}

	@Override
	public void generateMaze(String name, int floors, int rows, int cols) {
		threadPool.submit(new Callable<Maze3d>() {

			@Override

			public Maze3d call() throws Exception {
				GrowingTreeGenerator Generator = new GrowingTreeGenerator();
				Maze3d maze = Generator.generate(floors, rows, cols);
				mazes.put(name, maze);
				setChanged();
				notifyObservers("maze_ready " + name);
				return maze;

			}

		});

	}

	public Presenter getPresenter() {
		return presenter;
	}

	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public Solution<Position> modelGetSolution(String name) {

		if (solutions.containsKey(name)) {
			Solution<Position> mySolution = solutions.get(name);

			return mySolution;
		}

		return null;
	}

	@Override
	public String modelPath(String path) {

		StringBuilder sb = new StringBuilder();
		File folder = null;
		File[] listOfFiles = null;

		try {

			folder = new File(path);
			listOfFiles = folder.listFiles();

			for (File f : listOfFiles) {
				sb.append(f.toString()).append("\n");

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return sb.toString();

	}

	@Override
	public Maze3d getMaze(String name) {
		return mazes.get(name);
	}

	@Override
	public void exit() {
		threadPool.shutdown();

	}

	@Override
	public void setPresetner(Presenter presenter) {
		this.presenter = presenter;

	}
}
