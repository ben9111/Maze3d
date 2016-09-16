package controller;

import java.util.HashMap;

import model.Model;
import view.View;

public class MyController implements Controller {

	private View view;
	private Model model;
	private CommandsManager commandsManager;

	/**
	 * Controller c'tor
	 * 
	 * @param view
	 * @param model
	 */

	public MyController(View view, Model model) {
		this.view = view;
		this.model = model;

		commandsManager = new CommandsManager(model, view);
		view.setCommands(commandsManager.getCommandsMap());
	}

	@Override
	public void notifyMazeIsReady(String name) {
		view.notifyMazeIsReady(name);

	}

}
