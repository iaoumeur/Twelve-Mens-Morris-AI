package main;

import gui.Game;
import gui.MainMenu;

public class App {

	public static void main(String[] args) {
		MainMenu menu = new MainMenu();

		while(menu.isReady() == false){
		    try {
		       Thread.sleep(200);
		       System.out.println("waiting");
		    } catch(InterruptedException e) {
		    }
		}
		
		Game game = new Game(menu.getP1Name(), menu.getP2Name());
		

		if(Math.random() < 0.5) {
			game.switchTurn();
		}
		while(game.getState().getGameStage()!=5) {
			while(game.getState().getTurn()=="white") {
				game.getState().evaluateState("white");
				//System.out.println("White Evaluation: " + game.getState().evaluateState("white"));
				//System.out.println("Game Stage: " + game.getState().getGameStage());
				try {
				       Thread.sleep(1000);
				    } catch(InterruptedException e) {
				    }
			}
			while(game.getState().getTurn()=="black") {
				game.getState().evaluateState("black");
				//System.out.println("Black Evaluation: " + game.getState().evaluateState("black"));
				//System.out.println("Game Stage: " + game.getState().getGameStage());
				try {
				       Thread.sleep(1000);
				    } catch(InterruptedException e) {
				    }
			}
		}
		System.out.print("Game ended");
		
	}
	

}