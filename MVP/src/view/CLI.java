package view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Observable;

import presenter.Command;

/**
 * @author ben & adam CLI
 */
public class CLI extends Observable {

	private BufferedReader in;
	private PrintWriter out;
	private HashMap<String, Command> commands;

	public CLI(BufferedReader in, PrintWriter out) {

		this.in = new BufferedReader(new InputStreamReader(System.in));
		this.out = new PrintWriter(System.out);
		// this.commands = commandMap;

	}

	private void printMenu() {
		out.println("Choose command:");
		out.println("1) dir");
		out.println("2) generate_maze");
		out.println("3) display");
		out.println("4) display_cross_section");
		out.println("5) save_maze");
		out.println("6) load_maze");
		out.println("7) solve");
		out.println("8) display_solution ");
		out.println("9) exit");
		out.flush();

		/*
		 * for (String command : commands.keySet()) { out.print(command + ",");
		 * } out.println(")"); out.flush();
		 */
	}

	/*
	 * public void Instructions() {
	 * out.println("Please enter your command according to the following:");
	 * out.println("To display files pn a directory: dir <path> "); out.
	 * println("To generate maze3D: generate <name> <#floors> <#rows> <#cols>");
	 * out.flush(); }
	 */
	public void start() {

		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {

					printMenu();
					try {
						String commandLine = in.readLine();
						// System.out.println(commandLine);
						setChanged();
						notifyObservers(commandLine);

						if (commandLine.equals("exit")) {

							setChanged();
							notifyObservers("display_msg " + " bye");

							// System.out.println("Bye bye,exit");
							break;
						}

					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		thread.start();
	}

	public void setCommands(HashMap<String, Command> command) {
		this.commands = command;

	}

}
