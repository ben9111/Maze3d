package presenter;

import java.util.Observable;
import java.util.Observer;

import model.Model;
import view.View;

public class Presenter implements Observer {

	private View view;
	private Model model;
	private CommandsManager commandsManager;

	/**
	 * Controller c'tor presenter
	 * 
	 * @param view
	 * @param model
	 */

	public Presenter(View view, Model model) {
		this.view = view;
		this.model = model;

		commandsManager = new CommandsManager(model, view);
		// view.setCommands(commandsManager.getCommandsMap());
	}

	public void displayMessage(String msg) {

		view.displayMessage(msg);

	}

	public void notifyMazeIsReady(String name) {
		view.notifyMazeIsReady(name);

	}

	@Override
	public void update(Observable o, Object arg) {

		if (o == view) {

			String commandLine = (String) arg;

			commandsManager.executCommand(commandLine);

		}

		if (o == model) {

			String commandLine = (String) arg;

			commandsManager.executCommand(commandLine);

		}

	}

}
