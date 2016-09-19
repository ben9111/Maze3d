package boot;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import model.MyModel;
import presenter.Presenter;
import view.MyView;

public class Run {

	/**
	 * Run function creating the View,Model,and presenter
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter out = new PrintWriter(System.out);

		MyView ui = new MyView(in, out); // ui its like Game2048View
		MyModel m = new MyModel(); // m its like Game2048 Model

		Presenter p = new Presenter(ui, m);

		ui.addObserver(p);
		m.addObserver(p);

		ui.start();

	}

}
