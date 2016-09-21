package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Observable;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

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
import properties.Properties;
import properties.PropertiesLoader;

/**
 * 
 * All the model's methods that responsible of the commands coming from the
 * controller At the end of each method
 * 
 */

public class MyModel extends Observable implements Model {

	// private ExecutorService executor;

	private Presenter presenter;
	// private HashMap<String, Maze3d> MazeNames;
	// private HashMap<String, Solution> MazeSolutions;
	// private HashMap<Maze3d, Solution> caculatedSol;
	private ExecutorService threadPool;
	private Properties properties;

	private Map<String, Maze3d> mazes = new ConcurrentHashMap<String, Maze3d>();
	private Map<String, Solution<Position>> solutions = new ConcurrentHashMap<String, Solution<Position>>();
	private Map<Maze3d, Solution<Position>> calculatedSolutions = new ConcurrentHashMap<Maze3d, Solution<Position>>();

	public MyModel() {

		// properties = PropertiesLoader.getInstance().getProperties();
		// threadPool =
		// Executors.newFixedThreadPool(properties.getNumOfThreads());
		loadSolutions();
		threadPool = Executors.newFixedThreadPool(50);

	}

	@Override
	public int[][] model_cross_section(int index, String XYZ, String nameOfmaze) {

		int[][] maze2d = null;
		if (!mazes.containsKey(nameOfmaze)) {
			setChanged();
			notifyObservers("display_msg " + nameOfmaze + " maze was not found");
			// System.out.println(nameOfmaze + " Not Found");
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
				setChanged();
			notifyObservers("display_msg " + " Wrong input please enter X,Y,Z");
			// System.out.println("Wrong input please Enter X,Y,Z");

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

		Maze3d theMaze = mazes.get(nameOfMaze);

		threadPool.submit(new Callable<Solution<Position>>() {

			@Override
			public Solution<Position> call() throws Exception {
				if (!mazes.containsKey(nameOfMaze)) {
					setChanged();
					notifyObservers("display_msg " + "maze name not found");
				}

				/*
				 * for (Maze3d s : calculatedSolutions.keySet()) { if
				 * (s.getMaze().equals(theMaze)) { System.out.println("MATCH");
				 * return null;
				 * 
				 * }
				 */

			/*	if (!calculatedSolutions.containsKey(theMaze)) {
					System.out.println("already existed");
				}
				if (solutions.containsKey(nameOfMaze)) {
					setChanged();
					notifyObservers("display_msg " + " solution is ready, and already existed");
				}*/

				else {
					Maze3d myMaze = mazes.get(nameOfMaze);
					Searchable<Position> adapter = new MazeAdapter(myMaze);
					Searcher<Position> my_algorithm;

					switch (algorithms) {

					case "BFS":
						my_algorithm = new BFS<Position>();
						setChanged();
						notifyObservers("solution_ready " + nameOfMaze);
						break;
					case "DFS":
						my_algorithm = new DFS<Position>();
						setChanged();
						notifyObservers("solution_ready " + nameOfMaze);

						break;
					default:
						setChanged();
						notifyObservers("display_msg " + "Wrong algorithm choose DFS/BFS");
						return null;

					}

					solutions.put(nameOfMaze, my_algorithm.search(adapter));
					calculatedSolutions.put(theMaze, my_algorithm.search(adapter));

					/*
					 * System.out.println(calculatedSolutions.keySet());
					 * System.out.println(calculatedSolutions.get(theMaze));
					 */

				}

				return (Solution<Position>) solutions;
			}

		});
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
		setChanged();
		notifyObservers("display_msg " + "Wrong name of maze not found solutions");
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

	public void SaveSolutions() {
		ObjectOutputStream save = null;

		try {

			save = new ObjectOutputStream(new GZIPOutputStream(new FileOutputStream("solutions.dat")));
			save.writeObject(mazes);
			save.writeObject(solutions);

		} catch (FileNotFoundException e) {
			setChanged();
			notifyObservers("display_msg Error while trying to save the solution into the file!");
		} catch (IOException e) {
			setChanged();
			notifyObservers("display_msg Error while trying to save the solution into the file!");

		} finally {

			try {
				save.close();
			} catch (IOException e) {
				setChanged();
				notifyObservers("display_msg Error while trying to close the file!");
			}

		}

	}

	@SuppressWarnings("unchecked")
	public void loadSolutions() {
		File file = new File("solutions.dat");
		if (!file.exists()) {
			setChanged();
			notifyObservers("display_msg File do not exist in the our files solutions!");
			return;
		}

		ObjectInputStream load = null;
		try {

			load = new ObjectInputStream(new GZIPInputStream(new FileInputStream("solutions.dat")));
			mazes = (Map<String, Maze3d>) load.readObject();
			solutions = (Map<String, Solution<Position>>) load.readObject();
		} catch (FileNotFoundException e) {
			setChanged();
			notifyObservers("display_msg Error while trying to load/read the solution into the file!");
		}

		catch (IOException e) {
			setChanged();
			notifyObservers("display_msg Error while trying to load the solution into the file!");
		}

		catch (ClassNotFoundException e) {
			setChanged();
			notifyObservers("display_msg Error while trying to load the solution into the file!");
		}

		finally {

			try {
				load.close();
			}

			catch (IOException e) {
				setChanged();
				notifyObservers("display_msg Error while trying to close the file!");
			}
		}

	}

	@Override
	public Maze3d getMaze(String name) {
		return mazes.get(name);
	}

	@Override
	public void exit() {
		threadPool.shutdown();
		SaveSolutions();
	}

	/*
	 * @Override public boolean equals(Maze3d obj) { //System.out.println(obj);
	 * return calculatedSolutions.equals(obj);
	 * 
	 * }
	 */

	@Override
	public void setPresetner(Presenter presenter) {
		this.presenter = presenter;

	}
}
