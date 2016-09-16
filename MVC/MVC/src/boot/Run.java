package boot;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import controller.Controller;
import controller.MyController;
import model.Model;
import model.MyModel;
import view.MyView;
import view.View;

public class Run {

	/**
	 * Run function creating the View,Model,and Controller
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter out = new PrintWriter(System.out);

		View view = new MyView(in, out);
		Model model = new MyModel();

		Controller controller = new MyController(view, model);
		view.setController(controller);
		model.setController(controller);

		view.start();
	}

}
