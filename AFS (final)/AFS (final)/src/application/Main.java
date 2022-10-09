package application;

import java.awt.event.ActionEvent;
import java.awt.geom.Rectangle2D;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import engine.*;
import exceptions.AbilityUseException;
import exceptions.ChampionDisarmedException;
import exceptions.GameActionException;
import exceptions.InvalidTargetException;
import exceptions.NotEnoughResourcesException;
import exceptions.UnallowedMovementException;
import model.abilities.*;
import model.effects.*;
import model.world.*;
import javafx.animation.*;
//import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.ImageCursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.stage.Popup;

import java.awt.Point;

import javax.jws.Oneway;


public class Main extends Application {

	private static Scene scene;
	private static Scene chooseChamps;
	private static Scene rlyChooseChamps;
	private static Scene play;
	private static Scene instruct;
	private static Player firstPlayer;
	private static Player secondPlayer;
	private static Game game;
	private boolean firstPlayerTurn;	
	private static Stage board;
	private static EventHandler<KeyEvent> key;
	private static Direction dir;
	private static Point cell;
	private static boolean can = true;
	private static Ability directionalAbility;
	private static Stage stage;
	private final String[] players = new String[2];
	private VBox informationScroll = new VBox();
	
	
	private Image bg00 = new Image("/resources/bg00.jpg");
	private Image bg02 = new Image("/resources/bg02.jpg");
	private Image bg04 = new Image("/resources/bg04.jpg");
	private Image bg05 = new Image("/resources/bg05.jpg");
	private Image bg06 = new Image("/resources/bg06.jpg");
	private Image bg07 = new Image("/resources/bg07.png");
	private Image scroll = (new Image("/resources/scroll.png"));
	//private Image castB = new Image("/resources/castB.jpg");
	private Image directional = new Image("/resources/directional.png");
	private Image cover = new Image("/resources/coverb.png");
	private Image abilityButton = new Image("/resources/abilityButton.png");
	private Image cardB = (new Image("/resources/cardB.jpg"));
	private Image cardL = (new Image("/resources/cardL.jpg"));
	private Image cardY = (new Image("/resources/cardY.jpg"));
	private ArrayList <Image> team1 = new ArrayList<>();
	private ArrayList <Image> team2 = new ArrayList<>();
	
	
	private ImageCursor cursor = (new ImageCursor(new Image("/resources/spidey Cursor.png")));
	
	
	private static Button castAbility = new Button("CAST ABILITY");
	private static Button useLeaderAbility = new Button("USE LEADER ABILITY");
	private boolean turned = false;

	public void instructions(Stage stage) {
		Button instructions = new Button("Instructions");
		instructions.setTranslateX(775);
		instructions.setTranslateY(575);
		instructions.setBackground(Background.EMPTY);
		instructions.setTextFill(Color.WHITE);
		instructions.setFont(Font.font("Stencil", FontWeight.BOLD, 45));

		instructions
				.setOnAction(e -> {
					sound("/resources/type.wav");
					Label msg = new Label(
							"*Move using keyboard arrows! \n *Attack using WASD keys! "
							+ "\n *Cast abilty using buttons and depending on the ability: "
							+ "\n you might need to set direction by clicking on an arrow "
							+ "\n if Directional  or by clicking on a target on the board "
							+ "\n itself if Single Target! "
							+ "\n *Click enter to end the champion's turn!");
					//msg.setTranslateX(30);
					//msg.setTranslateY(500);
					msg.setFont(new Font("Times New Roman", 30));
					msg.setTextAlignment(TextAlignment.JUSTIFY);
					msg.setTextFill(Color.WHITE);
					HBox insLayout = new HBox(msg);
					insLayout.setAlignment(Pos.CENTER);
					insLayout.setBackground(new Background(
			                new BackgroundFill(
			                        new ImagePattern(
			                                scroll
			                        ), CornerRadii.EMPTY, Insets.EMPTY
			                )));
					Scene insScene = new Scene(insLayout, 1050, 1200, new ImagePattern(scroll));
					Stage ins = new Stage();
					ins.setHeight(700);
					insScene.setCursor(cursor);
					ins.setScene(insScene);
					ins.show();

				});

		EventHandler<KeyEvent> eventHandlerTextField = new EventHandler<KeyEvent>() {

			public void handle(KeyEvent event) {

				sound("/resources/type.wav");
			}
		};

		Button startPlaying = new Button("Play");
		startPlaying.setTranslateX(775);
		startPlaying.setTranslateY(575);
		startPlaying.setBackground(Background.EMPTY);
		startPlaying.setTextFill(Color.WHITE);
		startPlaying.setFont(Font.font("Stencil", FontWeight.BOLD, 45));

		CheckBox commit = new CheckBox(
				"I've read the instructions and I commit to use my brain!");
		commit.setFont(Font.font("Stencil", 23));
		commit.setTextFill(Color.WHITE);
		commit.setAlignment(Pos.CENTER);
		commit.setLayoutX(615);
		commit.setLayoutY(800);
        

		VBox instructAndPlay = new VBox();
		instructAndPlay.getChildren().add(instructions);
		instructAndPlay.getChildren().add(startPlaying);
		instructAndPlay.setAlignment(Pos.CENTER);

		Label creators = new Label();
		creators.setBackground(Background.EMPTY);
		creators.setText("© Arwa Ghoneim, Farida Maheeb, Sara Alajmy");
		creators.setFont(Font.font("Great Vibes", FontWeight.BOLD, 20));
		creators.setTextFill(Color.WHITE);
		creators.setLayoutX(1390);
		creators.setLayoutY(925);
		Label credits = new Label();
		credits.setBackground(Background.EMPTY);
		//credits.setText("Disclaimer: the photos used in this game do not belong to us. Credits to the owners.");
		credits.setFont(Font.font("Great Vibes", FontWeight.BOLD, 30));
		credits.setTextFill(Color.WHITE);
		credits.setLayoutX(0);
		credits.setLayoutY(925);

		Label title = new Label();
		title.setBackground(Background.EMPTY);
		title.setText("MARVEL \nULTIMATE WAR");
		InputStream istream = getClass().getResourceAsStream(
				"/resources/CURE OF PAIN PERSONAL.ttf");
		Font myFont = Font.loadFont(istream, 120);
		title.setFont(myFont);
		title.setTextFill(Color.WHITE);
		title.setLayoutX(700);
		title.setLayoutY(190);
		title.setTextAlignment(TextAlignment.CENTER);

		DropShadow ds = new DropShadow();
		ds.setOffsetY(1.0f);
		ds.setColor(Color.color(0.0f, 0.0f, 0.0f));
		title.setEffect(ds);
		instructions.setEffect(ds);
		startPlaying.setEffect(ds);
		commit.setEffect(ds);

		Group gp = new Group(instructAndPlay, creators, title, commit, credits);
		
		AnchorPane pane = new AnchorPane(gp);
		
		pane.setBackground(new Background(
                new BackgroundFill(
                        new ImagePattern(
                                bg00
                        ), CornerRadii.EMPTY, Insets.EMPTY
                )));
		
		instruct = new Scene(pane, 1000, 700, new ImagePattern(
                bg02
        ));
		
		commit.setOnAction(e->sound("/resources/type.wav"));
		startPlaying.setOnAction(e -> {
			sound("/resources/type.wav");
			if (commit.isSelected())
			{
				FadeTransition fade = new FadeTransition (Duration.millis(2000), pane);
				//fade.setFromValue(1.0);
				fade.setToValue(0);
				
				fade.setOnFinished(ActionEvent -> {

					begin(stage);
				});
				
				fade.play();
				
				//begin(stage);
		    }
			else{
				//begin(stage);
				displayError4erira("You have to check the condition!");
			}

		});

	}

	public void begin(Stage stage) {
		
		AnchorPane pane = new AnchorPane();

		TextField textField1 = new TextField();
		TextField textField2 = new TextField();
		textField1.setPromptText("Write here");
		textField2.setPromptText("Write here");

		textField1.setAlignment(Pos.CENTER);
		textField1.setFont(Font.font(25));

		textField2.setAlignment(Pos.CENTER);
		textField2.setFont(Font.font(25));

		Button submit = new Button("Submit");
		submit.setBackground(Background.EMPTY);
		submit.setTextFill(Color.WHITE);
		submit.setFont(Font.font("Stencil", FontWeight.BOLD, 45));

		Label label1 = new Label("PLAYER ONE NAME:");
		label1.setFont(Font.font("Cabin Sketch", FontWeight.BOLD, 40));
		label1.setTextFill(Color.WHITE);
		label1.setAlignment(Pos.BASELINE_CENTER);

		Label label2 = new Label("PLAYER TWO NAME:");
		label2.setFont(Font.font("Cabin Sketch", FontWeight.BOLD, 40));
		label2.setTextFill(Color.WHITE);
		label2.setAlignment(Pos.BASELINE_CENTER);

		EventHandler<KeyEvent> eventHandlerTextField = new EventHandler<KeyEvent>() {

			public void handle(KeyEvent event) {

				sound("/resources/type.wav");
			}
		};

		textField2.addEventHandler(KeyEvent.KEY_TYPED, eventHandlerTextField);
		textField1.addEventHandler(KeyEvent.KEY_TYPED, eventHandlerTextField);
		

		VBox box = new VBox();
		box.setAlignment(Pos.BASELINE_CENTER);
		box.getChildren().addAll(label1, textField1, label2, textField2);
		box.setSpacing(10);
		box.setLayoutX(805);
		box.setLayoutY(300);

		DropShadow ds = new DropShadow();
		ds.setOffsetY(1.0f);
		ds.setColor(Color.color(0.0f, 0.0f, 0.0f));
		submit.setLayoutX(865);
		submit.setLayoutY(650);
		submit.setEffect(ds);
		label1.setEffect(ds);
		label2.setEffect(ds);

		Group root = new Group(box, submit);

		
		pane.getChildren().add(root);
//        AnchorPane pane = new AnchorPane(root);
		
		pane.setBackground(new Background(
                new BackgroundFill(
                        new ImagePattern(
                                bg02
                        ), CornerRadii.EMPTY, Insets.EMPTY
                )));
		
		
		scene = new Scene(pane, 1000, 700, new ImagePattern(bg04));
		
		scene.setCursor(cursor);
		stage.setScene(scene);
		
		submit.setOnAction(e -> {
			// Retrieving data
			players[0] = textField1.getText();
			players[1] = textField2.getText();
			firstPlayer = new Player(players[0]);
			secondPlayer = new Player(players[1]);

			try {
				Game.loadAbilities("Abilities.csv");
				Game.loadChampions("Champions.csv");
				Game.loadIcons("Icons.csv");
				Game.loadImages("Heroes.csv");
				submit.setDisable(true);
			} catch (Exception e1) {
				//System.out.println("Loading csv file error");
			}
			
			FadeTransition fade = new FadeTransition (Duration.millis(2000), pane);
			fade.setToValue(0f);
			
			fade.setOnFinished(ActionEvent -> {

				choosingChampsPage1(players, stage);
				chooseChamps.setCursor(cursor);
				stage.setScene(chooseChamps);
				
			});
			
			fade.play();
			
			sound("/resources/type.wav");
		});
		
	}

	public void choosingChampsPage1(String[] players, Stage stage) {
		choosingChampsPage2(stage);

		DropShadow ds = new DropShadow();
		ds.setOffsetY(1.5f);
		ds.setColor(Color.color(0.0f, 0.0f, 0.0f));

		Label text = new Label("Welcome " + players[0] + " and " + players[1]
				+ "\n Proceed to choose your champions!! :)");
		
		InputStream istream = getClass().getResourceAsStream(
				"/resources/HelloAudrey.ttf");
		Font myFont = Font.loadFont(istream, 60);
		
		text.setFont(myFont);
		text.setTextFill(Color.WHITE);
//		text.setTranslateX(0);
		text.setTranslateY(-75);
		text.setEffect(ds);
		text.setTextAlignment(TextAlignment.CENTER);

		Image button = new Image("/resources/button.png");
		button.isPreserveRatio();

		Label chooseChamps1 = new Label();
		Label chooseChamps2 = new Label();
		chooseChamps1.prefWidth(150);
		chooseChamps2.prefWidth(150);
		chooseChamps1.setGraphic(new ImageView(button));
		chooseChamps2.setGraphic(new ImageView(button));
		chooseChamps1.setBackground(Background.EMPTY);
		chooseChamps2.setBackground(Background.EMPTY);

		Button choose1 = new Button();
		choose1.setBackground(Background.EMPTY);
		choose1.setText(players[0] + " champions");
		choose1.setFont(Font.font("Cabin Sketch", FontWeight.BOLD, 25));
		choose1.setTextFill(Color.WHITE);
		choose1.setAlignment(Pos.CENTER);

		Button choose2 = new Button();
		choose2.setBackground(Background.EMPTY);
		choose2.setText(players[1] + " champions");
		choose2.setFont(Font.font("Cabin Sketch", FontWeight.BOLD, 25));
		choose2.setTextFill(Color.WHITE);
		choose2.setAlignment(Pos.CENTER);

		StackPane champ1 = new StackPane(chooseChamps1, choose1);
		StackPane champ2 = new StackPane(chooseChamps2, choose2);

		rlyChooseChamps.setCursor(cursor);

		VBox pane = new VBox();
		
		choose1.setOnAction(e -> {
			sound("/resources/type.wav");
			if (firstPlayer.getTeam().size() == 3)
				displayErrorTayeba("You already chose your champs");
			else {
				firstPlayerTurn = true;
				FadeTransition fade = new FadeTransition (Duration.millis(2000), pane);
				fade.setToValue(0f);
				
				fade.setOnFinished(ActionEvent -> {
					
					StackPane scroll = (StackPane) rlyChooseChamps.getRoot();
					
					FadeTransition fade1 = new FadeTransition (Duration.millis(1), scroll);
					fade1.setToValue(10);
					fade1.play();
					
					stage.setScene(rlyChooseChamps);
				});
				
				fade.play();
			}
		});

		choose2.setOnAction(e -> {
			sound("/resources/type.wav");
			if (secondPlayer.getTeam().size() == 3)
				displayErrorTayeba("You already chose your champs");
			else {
				firstPlayerTurn = false;
				
				FadeTransition fade = new FadeTransition (Duration.millis(2000), pane);
				fade.setToValue(0f);
				
				fade.setOnFinished(ActionEvent -> {
					
					StackPane scroll = (StackPane) rlyChooseChamps.getRoot();
					
					FadeTransition fade1 = new FadeTransition (Duration.millis(1), scroll);
					fade1.setToValue(10);
					fade1.play();
					
					stage.setScene(rlyChooseChamps);
				});
				
				fade.play();
			}
		});

		HBox layout2 = new HBox(50,champ1,champ2);
		
//		Button leaders = choosingLeaders(stage,players);
////		leaders.setLayoutX(-leaders.getLayoutX());
////		leaders.setLayoutY(1000-leaders.getLayoutY());
//		
//		leaders.setAlignment(Pos.CENTER);
		

		pane.getChildren().addAll(text, layout2, 
				choosingLeaders(stage,players));
//		text.setAlignment(Pos.CENTER);
		layout2.setAlignment(Pos.CENTER);
		
		pane.setSpacing(30);
		
		pane.setAlignment(Pos.CENTER);
		//pane.setCenterShape(true);
		
		pane.setBackground(new Background(
                new BackgroundFill(
                        new ImagePattern(
                                bg04
                        ), CornerRadii.EMPTY, Insets.EMPTY
                )));

		chooseChamps = new Scene(pane, 700, 400, new ImagePattern(
				bg05));

	}

	public void choosingChampsPage2(Stage stage) {

		GridPane grid = new GridPane();
		//grid.getRowConstraints().add(new RowConstraints(5));
		grid.setHgap(20);
		grid.setPadding(new Insets(5));
		grid.autosize();
		grid.setMinHeight(stage.getHeight());
		grid.setMinWidth(stage.getWidth());
		//grid.setGridLinesVisible(true);
		int i = 1;
		int j = 201;
		ArrayList<CheckBox> clickedButtons = new ArrayList<CheckBox>();
		
		//pane2.setBackground(Background.EMPTY);
		
		StackPane stack = new StackPane(grid);
		stack.setBackground(Background.EMPTY);
		
		Label empty = new Label (" ");
		grid.add(empty, 2, 200);
		
		for (Champion c : Game.getAvailableChampions()) {
			
			Image img = new Image("/resources/" + c.getName() + ".png");
			StackPane bt = new StackPane(displayingChamp(c, cardB, img));
			CheckBox bt2 = new CheckBox();
			StackPane info = displayInfo(c);
			stack.getChildren().add(info);
			stack.setAlignment(info, Pos.TOP_RIGHT);
			info.setTranslateX(-245);
			
			bt.setOnMouseEntered(ee -> 
			{
				
				info.setVisible(true);
			});
			bt.setOnMouseExited(ee -> 
			{
				
				info.setVisible(false);
			});

			grid.add(bt2, i, j);
			grid.add(bt, i++, j + 1);
			if (i == 6) {
				j += 2;
				i = 1;
			}
			
			bt2.setOnAction(ee -> {
				sound("/resources/type.wav");
				if (firstPlayer.getTeam().contains(c)
						|| secondPlayer.getTeam().contains(c)) {
					clickedButtons.remove(bt2);
					firstPlayer.getImages().remove(img);
					secondPlayer.getImages().remove(img);
					firstPlayer.getTeam().remove(c);
					secondPlayer.getTeam().remove(c);
				} else if (firstPlayerTurn) {
					firstPlayer.getTeam().add(c);
					clickedButtons.add(bt2);
					firstPlayer.getImages().add(img);
					if (firstPlayer.getTeam().size() > 3) {
						CheckBox old = clickedButtons.remove(0);
						old.setSelected(false);
						firstPlayer.getTeam().remove(0);
						firstPlayer.getImages().remove(0);

					}

				} else {
					secondPlayer.getTeam().add(c);
					clickedButtons.add(bt2);
					secondPlayer.getImages().add(img);
					if (secondPlayer.getTeam().size() > 3) {
						CheckBox old = clickedButtons.remove(0);
						old.setSelected(false);
						secondPlayer.getTeam().remove(0);
						secondPlayer.getImages().remove(0);

					}
				}
			});

			

		}

		Button dn = new Button();
		dn.setBackground(Background.EMPTY);
		Image button = new Image("/resources/BlueBut.png");
		dn.setGraphic(new ImageView(button));
		dn.setPrefWidth(75);
		Label done = new Label ("Done Choosing");
		done.setTextFill(Color.WHITE);
		done.setFont(Font.font("Stencil", FontWeight.BOLD, 20));
		
		StackPane donee = new StackPane (dn, done);
		donee.setAlignment(Pos.CENTER);
		
		DropShadow borderGlow= new DropShadow();
		borderGlow.setOffsetY(0f);
		borderGlow.setOffsetX(0f);
		borderGlow.setColor(Color.BLACK);
		borderGlow.setWidth(5);
		borderGlow.setHeight(5);
		done.setEffect(borderGlow);

		grid.add(donee, 19, 207);
		grid.setValignment(donee, VPos.BOTTOM);
		

		BackgroundImage bImg = new BackgroundImage(bg05,
				BackgroundRepeat.NO_REPEAT, BackgroundRepeat.REPEAT,
				BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
		Background bGround = new Background(bImg);
		grid.setBackground(bGround);
		
		dn.setOnAction(ee -> {
			sound("/resources/type.wav");
			if (clickedButtons.size() < 3)
				displayErrorTayeba("You need to choose 3 champions");
			else {
				while (!clickedButtons.isEmpty()) {
					CheckBox cb = clickedButtons.remove(0);
					cb.setDisable(true);
				}
				
//				for (Champion c: firstPlayer.getTeam())
//				{
//					Image img = new Image("/resources/" + c.getName() + ".png");
//					team1.add(img);
//				}
//				
//				for (Champion c: secondPlayer.getTeam())
//				{
//					Image img = new Image("/resources/" + c.getName() + ".png");
//					team2.add(img);
//				}
				
				FadeTransition fade = new FadeTransition (Duration.millis(2000), stack);
				fade.setToValue(0f);
				
				fade.setOnFinished(ActionEvent -> {
					VBox pane = (VBox) chooseChamps.getRoot();
					
					FadeTransition fade1 = new FadeTransition (Duration.millis(1), pane);
					fade1.setToValue(10);
					fade1.play();
					
					stage.setScene(chooseChamps);
				});
				
				fade.play();
				
			}
		});

		
		
		
		rlyChooseChamps = new Scene(stack, 700, 400, new ImagePattern(
                bg04));

	}

	public Button choosingLeaders(Stage stage, String[] players) {

		DropShadow ds = new DropShadow();
		ds.setOffsetY(1.0f);
		ds.setColor(Color.color(0.0f, 0.0f, 0.0f));

		GridPane grid = new GridPane();
		grid.setMinSize(stage.getWidth(), stage.getHeight());
		grid.setPadding(new Insets(10, 10, 10, 10));
		grid.setVgap(5);
		grid.setHgap(5);
		grid.setAlignment(Pos.CENTER);
		Button chooseLeaders = new Button("Choose leaders");
		chooseLeaders.setBackground(Background.EMPTY);
		chooseLeaders.setTextFill(Color.WHITE);
		chooseLeaders.setFont(Font.font("Stencil", FontWeight.BOLD, 45));
		chooseLeaders.setEffect(ds);

		chooseLeaders.setOnAction(e -> {
			sound("/resources/type.wav");
			if (firstPlayer.getLeader() == null
					|| secondPlayer.getLeader() == null) {

				if ((firstPlayer.getTeam().size() != 3)
						|| (secondPlayer.getTeam().size() != 3)) {

					displayErrorTayeba("Please choose ur whole team first");
					stage.setScene(chooseChamps);

				} else {
					Label command1 = new Label(players[0]
							+ ", choose your leader:");
					grid.add(command1, 3, 1);
					Label command2 = new Label(players[1]
							+ ", choose your leader:");
					grid.add(command2, 3, 5);
					
					command1.setFont(Font.font("Great Vibes", FontWeight.BOLD, 40));
					command1.setTextFill(Color.WHITE);
					command1.setEffect(ds);
					command2.setFont(Font.font("Great Vibes", FontWeight.BOLD, 40));
					command2.setTextFill(Color.WHITE);
					command2.setEffect(ds);
					
					ToggleGroup tg1 = new ToggleGroup();
					ToggleGroup tg2 = new ToggleGroup();

					for (int i = 0; i < 3; i++) {
						Champion curr1 = firstPlayer.getTeam().get(i);
						Champion curr2 = secondPlayer.getTeam().get(i);
						
						RadioButton champ1 = new RadioButton(curr1.getName());
						champ1.setFont(Font.font("Great Vibes", FontWeight.BOLD, 30));
						champ1.setTextFill(Color.WHITE);
						ImageView img = new ImageView(curr1.getIcon());
						img.setPreserveRatio(true);
						img.setFitHeight(100);
						champ1.setGraphic(img);
						champ1.setPickOnBounds(true);
						champ1.setToggleGroup(tg1);
						grid.add(champ1, (i + 2), 2);
						
						RadioButton champ2 = new RadioButton(curr2.getName());
						champ2.setFont(Font.font("Great Vibes", FontWeight.BOLD, 30));
						champ2.setTextFill(Color.WHITE);
						champ2.setToggleGroup(tg2);
						champ2.setPickOnBounds(true);
						ImageView img2 = new ImageView(curr2.getIcon());
						img2.setPreserveRatio(true);
						img2.setFitHeight(100);
						champ2.setGraphic(img2);
						grid.add(champ2, (i + 2), 6);
						
						champ1.setOnAction(ee -> {
							sound("/resources/type.wav");
							firstPlayer.setLeader(curr1);
						});
						champ2.setOnAction(ee -> {
							sound("/resources/type.wav");
							secondPlayer.setLeader(curr2);
						});

					}

					Scene chooseLead = new Scene(grid, 1200, 700,
							new ImagePattern(bg06));

					grid.setBackground(null);
					
					chooseLead.setCursor(cursor);

					stage.setScene(chooseLead);

					Button dn = new Button("Done Choosing");
					dn.setAlignment(Pos.CENTER);

					dn.setBackground(Background.EMPTY);
					dn.setTextFill(Color.WHITE);
					dn.setFont(Font.font("Stencil", FontWeight.BOLD, 30));
					dn.setEffect(ds);

					grid.add(dn, 3, 10);
					grid.setAlignment(Pos.CENTER);

					dn.setOnAction(ee -> {
						sound("/resources/type.wav");
						if (tg1.getSelectedToggle() == null
								|| tg2.getSelectedToggle() == null)
							displayErrorTayeba("You need to choose a leader");
						else {
							createBoard(stage);
							// stage.setScene(chooseChamps);
						}
					});
				}

			} else
				displayErrorTayeba("You already chose ur leaders");

		});

		return chooseLeaders;
	}

	public void move() {
		try {
			game.move(dir);
			 refresh(stage);

		} catch (NotEnoughResourcesException | UnallowedMovementException e) {
			can = false;

			displayError4erira(e.getMessage());
		}
	}

	public void createBoard(Stage stage) {
		game = new Game(firstPlayer, secondPlayer);
		refresh(stage);
		board = stage;
		key = new EventHandler<KeyEvent>() {
			public void handle(KeyEvent k) {
				if (k.getCode() == KeyCode.LEFT || k.getCode() == KeyCode.A)
					dir = Direction.LEFT;

				else if (k.getCode() == KeyCode.RIGHT
						|| k.getCode() == KeyCode.D)
					dir = Direction.RIGHT;
				else if (k.getCode() == KeyCode.UP || k.getCode() == KeyCode.W)
					dir = Direction.UP;
				else if (k.getCode() == KeyCode.DOWN
						|| k.getCode() == KeyCode.S)
					dir = Direction.DOWN;
				if (k.getCode() == KeyCode.A || k.getCode() == KeyCode.S
						|| k.getCode() == KeyCode.D || k.getCode() == KeyCode.W) {

					try {
						game.attack(dir);
						refresh(stage);
					} catch (NotEnoughResourcesException
							| ChampionDisarmedException e) {

						can = false;
						displayError4erira(e.getMessage());
					}
				}

				else if (k.getCode() == KeyCode.ENTER) {
					game.endTurn();
					if (!(game.checkGameOver() == null)){
						endGame(stage);
						return;
					}
					refresh(stage);
				}
				
				else if (k.getCode() == KeyCode.LEFT || k.getCode() == KeyCode.RIGHT
						|| k.getCode() == KeyCode.UP || k.getCode() == KeyCode.DOWN) {
					move();
				}

				
				play.setOnKeyPressed(key);
			}
		};

		play.setOnKeyPressed(key);
		stage.setScene(play);
	}

	public  void refresh(Stage stage) {
        if(game.checkGameOver()!=null){
        	endGame(stage);
        	return;
        }
		BorderPane border = new BorderPane();
		HBox top = new HBox();
		GridPane gameBoard = new GridPane();
		
		gameBoard.setMinSize(750, 750);
		gameBoard.setAlignment(Pos.CENTER);
		StackPane qAndButtons = new StackPane();
		for (int i = 0; i < game.getBoardheight(); i++) {
			for (int j = 0; j < game.getBoardwidth(); j++) {
				Rectangle tile = new Rectangle(135, 135);
				tile.setFill(Color.TRANSPARENT);
				tile.setStroke(Color.BLACK);
				tile.setStrokeWidth(2.5);
				final int x = i;
				final int y = j;
				if (game.getBoard()[i][j] instanceof Cover) {
					tile.setFill(new ImagePattern(cover));
					Cover c = (Cover) game.getBoard()[i][j];
					
					DropShadow ds = new DropShadow();
					ds.setOffsetY(1.0f);
					ds.setColor(Color.color(0.0f, 0.0f, 0.0f));
					
					Label hp = new Label ("Current HP: " + c.getCurrentHP());
					hp.setFont(Font.font("Cabin Sketch", FontWeight.BOLD, 30));
					hp.setTextFill(Color.WHITE);
					hp.setVisible(false);
					hp.setEffect(ds);
					
					qAndButtons.getChildren().add(hp);
					qAndButtons.setAlignment(hp, Pos.CENTER_RIGHT);
					
			        tile.setOnMouseEntered(e->{
			        	hp.setVisible(true);
			        });					
			        tile.setOnMouseExited(e->{
			        	hp.setVisible(false);
			        });					
				}
			
				if (game.getBoard()[i][j] instanceof Champion)
					tile.setFill(new ImagePattern(
							((Champion) game.getBoard()[i][j]).getIcon()));
					tile.setOnMouseClicked(e -> {
						if (directionalAbility != null) {
							try {
								game.castAbility(directionalAbility, x, y);
								//System.out.println("ability cast");
								directionalAbility = null;
							} catch (Exception e1) {
								displayError4erira(e1.getMessage());
							}
						}
					});
//					tile.setOnMouseEntered(e->{
//						informationScroll = displayInfoOnBoard((Damageable) game.getBoard()[x][y]);
//						refresh(stage);
//					});
				GridPane.setRowIndex(tile, 4 - i);
				GridPane.setColumnIndex(tile, j);
				gameBoard.getChildren().addAll(tile);
			}
		}
		Champion[] array = new Champion[game.getTurnOrder().size()];
		for (int i = 0; i < array.length; i++) {
			Rectangle tile = new Rectangle(100, 100);
			tile.setStroke(Color.BLUE);
			array[i] = (Champion) game.getTurnOrder().remove();
			tile.setFill(new ImagePattern(array[i].getIcon()));
			top.getChildren().add(tile);
		}
		for (int i = 0; i < array.length; i++) {
			game.getTurnOrder().insert(array[i]);
		}
//
		//border.setLeft(informationScroll);
//		border.setRight(scrollRight);
		border.setCenter(gameBoard);
		
		useLeaderAbility.setDisable(false);
		if ((game.getFirstPlayer().getTeam()
				.contains(game.getCurrentChampion()) && game
				.isFirstLeaderAbilityUsed())
				|| (game.getSecondPlayer().getTeam()
						.contains(game.getCurrentChampion()) && game
						.isSecondLeaderAbilityUsed()))
			useLeaderAbility.setDisable(true);
		useLeaderAbility
				.setOnAction(e -> {
					sound("/resources/type.wav");
					try {
						game.useLeaderAbility();
						useLeaderAbility.setDisable(true);
//						useLeaderAbility
//								.setStyle("-fx-border-color: blue; -fx-background-color: transparent; -fx-text-fill: #666666;");
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						displayError4erira(e1.getMessage());
					}
				});
		useLeaderAbility.setAlignment(Pos.CENTER);
		useLeaderAbility.setLayoutX(175);
		Champion current = game.getCurrentChampion();
		castAbility.setOnAction(e -> {
			sound("/resources/type.wav");
			Stage ca = new Stage();
			ca.setHeight(1100);
			ca.setWidth(500);
			VBox abilities = new VBox();
//			abilities.setTranslateX(300);
//			abilities.setTranslateY(350);
//			abilities.setCenterShape(true);
			abilities.setAlignment(Pos.BASELINE_CENTER);
			abilities.setBackground(new Background(
	                new BackgroundFill(
	                        new ImagePattern(
	                                directional
	                        ), CornerRadii.EMPTY, Insets.EMPTY
	                )));

			//abilities.setPadding(new Insets(150));
			//abilities.setSpacing(50);
			
			abilityButton.isPreserveRatio();
			

			for (int i = 0; i < current.getAbilities().size(); i++) {
				Ability a = current.getAbilities().get(i);
				Label aBack = new Label();
				aBack.setGraphic(new ImageView(abilityButton));
				aBack.setPrefWidth(150);
				aBack.setBackground(Background.EMPTY);
				Button abilityb = new Button();
				abilityb.setBackground(Background.EMPTY);
				abilityb.setText(a.getName());
				abilityb.setFont(Font.font("Stencil", FontWeight.BOLD, 30));
				abilityb.setTextFill(Color.WHITE);
				abilityb.setAlignment(Pos.CENTER);
				StackPane ability = new StackPane(aBack, abilityb);
				abilities.getChildren().add(ability);
				abilityb.setOnAction(event -> {
					sound("/resources/type.wav");
					
					if (a.getCastArea() == AreaOfEffect.DIRECTIONAL) {
						
						play.setCursor(cursor);
						Button topp = new Button();
						topp.setGraphic(new ImageView (new Image ("/resources/arrowU.png")));
						topp.setPrefHeight(10);
						topp.setBackground(Background.EMPTY);
						topp.setOnAction(eventd -> {
							sound("/resources/type.wav");
							try {
								game.castAbility(a, Direction.UP);
								//System.out.println("ability cast U");
								refresh(stage);
							} catch (Exception e1) {
								displayError4erira(e1.getMessage());
							}
							ca.close();
						});
						
						Button right = new Button();
						right.setGraphic(new ImageView (
								new Image ("/resources/arrowR.png")));
						right.setPrefHeight(10);
						right.setBackground(Background.EMPTY);
						right.setOnAction(eventd -> {
							sound("/resources/type.wav");
							try {
								//System.out.println("ability cast R");
								game.castAbility(a, Direction.RIGHT);

								refresh(stage);
								ca.close();

							} catch (Exception e1) {
								//System.out.println("wrong");
								displayError4erira(e1.getMessage());
							}
						});
						Button left = new Button();
						left.setGraphic(new ImageView (
								new Image ("/resources/arrowL.png")));
						left.setPrefHeight(10);
						left.setBackground(Background.EMPTY);
						left.setOnAction(eventd -> {
							sound("/resources/type.wav");
							try {
								
								game.castAbility(a, Direction.LEFT);
								//System.out.println("ability cast L");

								refresh(stage);

								ca.close();
							} catch (Exception e1) {
								displayError4erira(e1.getMessage());
							}
						});
						Button bottom = new Button();
						bottom.setGraphic(new ImageView 
								(new Image ("/resources/arrowD.png")));
						bottom.setPrefHeight(10);
						bottom.setBackground(Background.EMPTY);
						bottom.setOnAction(eventd -> {
							sound("/resources/type.wav");
							try {
								game.castAbility(a, Direction.DOWN);
								//System.out.println("ability cast D");
								ca.close();

								refresh(stage);
							} catch (Exception e1) {
								displayError4erira(e1.getMessage());
							}
						});
						VBox arrows = new VBox();
						arrows.setAlignment(Pos.BASELINE_CENTER);
						arrows.setBackground(new Background(
				                new BackgroundFill(
				                        new ImagePattern(
				                                directional
				                        ), CornerRadii.EMPTY, Insets.EMPTY
				                )));
						HBox lr = new HBox (left, right);
						lr.setAlignment(Pos.CENTER);
						//arrows.setTranslateX(300);
						//arrows.setTranslateY(350);
						//arrows.setCenterShape(true);
						//arrows.setAlignment(Pos.CENTER);
						arrows.getChildren().addAll(topp, lr, bottom);
						arrows.setAlignment(Pos.TOP_CENTER);
						Scene castArrows = new Scene(arrows, stage.getWidth(), stage.getHeight());
						
						castArrows.setCursor(cursor);
						ca.setScene(castArrows);
					} else if (a.getCastArea() == AreaOfEffect.SINGLETARGET) {
						directionalAbility = a;
						ca.close();

						refresh(stage);

					} else {
						try {
							game.castAbility(a);
							//System.out.println("ability cast");
							ca.close();
							refresh(stage);
						} catch (Exception e1) {
							displayError4erira(e1.getMessage());
						}
					}

				}

				);
			}
			Scene chooseAbility = new Scene(abilities, stage.getWidth(), stage.getHeight());
			chooseAbility.setCursor(cursor);
			ca.setScene(chooseAbility);
			ca.show();
			ca.setOnCloseRequest(ee -> refresh(stage));
			ca.setOnHidden(eee -> refresh(stage));
			ca.getIcons().add(new Image("/resources/castIcon.png"));

		});
		
		
		
		VBox Buttons = new VBox (castAbility, useLeaderAbility);
		Buttons.setAlignment(Pos.CENTER);
		Buttons.setSpacing(15);
		
		qAndButtons.getChildren().addAll(top, Buttons);
		qAndButtons.setAlignment(top, Pos.CENTER_LEFT);
		qAndButtons.setAlignment(Buttons, Pos.CENTER);
		
		border.setTop(qAndButtons);
		border.setBackground(new Background(
                new BackgroundFill(
                        new ImagePattern(
                                bg07
                        ), CornerRadii.EMPTY, Insets.EMPTY
                )));
		
		border.setLeft(displayChampOnBoard(firstPlayer));
		border.setRight(displayChampOnBoard(secondPlayer));

		HBox abil = new HBox();
		
		Label avail = new Label ("Available Abilities: ");
		avail.setTextFill(Color.WHITE);
		avail.setFont(Font.font("Stencil", FontWeight.BOLD, 30));
		avail.setBackground(Background.EMPTY);
		
		DropShadow borderGlow= new DropShadow();
		borderGlow.setOffsetY(0f);
		borderGlow.setOffsetX(0f);
		borderGlow.setColor(Color.BLACK);
		borderGlow.setWidth(5);
		borderGlow.setHeight(5);
		avail.setEffect(borderGlow);
		
		abil.getChildren().add(avail);
		abil.setSpacing(50);
		abil.setAlignment(Pos.CENTER);
		
		Champion curr = game.getCurrentChampion();
		
		for (Ability a : curr.getAbilities())
		{
			DropShadow ds = new DropShadow();
			ds.setOffsetY(1.0f);
			ds.setColor(Color.color(1.0f, 1.0f, 1.0f));
			
			Line line = new Line();
			line.setStartX(0.0f);
			line.setStartY(0.0f);
			line.setEndX(0.0f);
			line.setEndY(100.0f);
			line.setEffect(ds);
			line.setStroke(Color.WHITE);
			line.setStrokeWidth(3.0);
			abil.getChildren().add(line);
			
			Label ab = new Label (a.toString1());
			ab.setTextFill(Color.WHITE);
			ab.setFont(Font.font("Times New Roman", FontWeight.BOLD, 15));
			ab.setBackground(Background.EMPTY);
			ab.setEffect(borderGlow);
			abil.getChildren().add(ab);
			
			Label ab2 = new Label (a.toString2());
			ab2.setTextFill(Color.WHITE);
			ab2.setFont(Font.font("Times New Roman", FontWeight.BOLD, 15));
			ab2.setBackground(Background.EMPTY);
			ab2.setEffect(borderGlow);
			abil.getChildren().add(ab2);
		}
		
		border.setBottom(abil);
		border.autosize();
		
		play = new Scene(border, 1050, 1200, new ImagePattern(bg07));
		play.setCursor(cursor);
		play.setOnKeyPressed(key);
		stage.setScene(play);

	}

	public void start(Stage stagee) throws IOException {
		stage = stagee;
		sound("/resources/mission.wav");
		stagee.getIcons().add(new Image("/resources/spidey.png"));
		stagee.setTitle("Ultimate War");
		instructions(stagee);
		stagee.centerOnScreen();
		stagee.setHeight(700);
		stagee.setWidth(2000);
		stagee.setMaximized(true);
		instruct.setCursor(cursor);
		stagee.setScene(instruct);
		stagee.show();
	}

	public static void displayErrorTayeba(String s) {
		Image img = new Image("/resources/groot.png");
		ImageView groot = new ImageView(img);
		groot.setFitHeight(250);
		groot.setPreserveRatio(true);
		groot.setTranslateX(200);
		groot.setTranslateY(150);
		Label grootmsg = new Label("I am groot!");
		grootmsg.setTranslateX(430);
		grootmsg.setTranslateY(50);
		grootmsg.setFont(new Font("Harrington", 40));
		HBox errorLayout = new HBox(grootmsg);
		Label msg = new Label("Translation:");
		msg.setTranslateX(0);
		msg.setTranslateY(500);
		msg.setFont(new Font("Stylus BT", 30));
		Label error = new Label(s);
		error.setTranslateX(-300);
		error.setTranslateY(550);
		error.setFont(new Font("Stylus BT", 30));
		errorLayout.getChildren().add(groot);
		errorLayout.getChildren().add(msg);
		errorLayout.getChildren().add(error);
		Scene errorScene = new Scene(errorLayout, 1050, 1200);
		Stage err = new Stage();

		err.setHeight(700);
		err.getIcons().add(img);
		Image cursor = new Image("/resources/spiderman.png");
		errorScene.setCursor(new ImageCursor(cursor));
		err.setScene(errorScene);
		err.show();
	
	}

	public static Stage displayError4erira(String s) {
		Image img = new Image("/resources/groot 4erir.png");
		ImageView groot = new ImageView(img);
		groot.setFitHeight(300);
		groot.setPreserveRatio(true);
		groot.setTranslateX(200);
		groot.setTranslateY(150);
		Label grootmsg = new Label("I. AM. GROOT!");
		grootmsg.setTranslateX(430);
		grootmsg.setTranslateY(50);
		grootmsg.setFont(new Font("Mistral", 40));
		grootmsg.setTextAlignment(TextAlignment.CENTER);

		HBox errorLayout = new HBox(grootmsg);
		Label msg = new Label("Translation:");
		msg.setTranslateX(0);
		msg.setTranslateY(500);
		msg.setFont(new Font("Stylus BT", 30));
		Label error = new Label(s);
		error.setTranslateX(-200);
		error.setTranslateY(550);
		error.setFont(new Font("Stylus BT", 30));
		errorLayout.getChildren().add(groot);
		errorLayout.getChildren().add(msg);
		errorLayout.getChildren().add(error);
		Scene errorScene = new Scene(errorLayout, 1050, 1200);
		Stage err = new Stage();
    //    err.setOnHidden(value);(e->err.close());
	    //err.setAlwaysOnTop(true);
		err.setHeight(700);
		err.getIcons().add(img);
		Image cursor = new Image("/resources/spiderman.png");
		errorScene.setCursor(new ImageCursor(cursor));
		err.setScene(errorScene);
		err.show();

		return err;
	}

	public void endGame(Stage stage) {
		Label gameOver = new Label("GAME OVER");
		Label winner = new Label(game.checkGameOver().getName()
				+ " is the WINNER!");
		Label thanks = new Label("Thank you " + players[0] + " and " + players[1]
				+ " for playing <3");
		
		InputStream istream = getClass().getResourceAsStream(
				"/resources/Agreloy.otf");
		Font myFont = Font.loadFont(istream, 80);
		DropShadow ds = new DropShadow();
		ds.setOffsetY(1.0f);
		ds.setColor(Color.color(0.0f, 0.0f, 0.0f));
		
		gameOver.setFont(myFont);
		gameOver.setTextFill(Color.WHITE);
		gameOver.setEffect(ds);
		winner.setFont(myFont);
		winner.setTextFill(Color.WHITE);
		winner.setEffect(ds);
		thanks.setFont(myFont);
		thanks.setTextFill(Color.WHITE);
		thanks.setEffect(ds);
		
		VBox gameEnd = new VBox();
		gameEnd.getChildren().add(gameOver);
		gameEnd.getChildren().addAll(winner, thanks);
		gameEnd.setAlignment(Pos.CENTER);

		gameEnd.setBackground(Background.EMPTY);
		Scene ending = new Scene(gameEnd, 1000, 700, new ImagePattern(
				new Image ("/resources/gameOver.png")));
		stage.setScene(ending);
	}

	public void sound(String s) {
		AudioClip a = new AudioClip(this.getClass().getResource(s).toString());
		a.play();

	}
	
	public StackPane displayingChamp (Champion c, Image card, Image champ)
	{
		StackPane sp = new StackPane();
		sp.setPadding(new Insets (5));
		
		ImageView photo = new ImageView(champ);
		photo.setPreserveRatio(true);
		photo.setFitHeight(200);
		photo.setFitWidth(150);
		
		Label name = new Label (c.getName());
		name.setFont(Font.font("Stencil", FontWeight.BOLD, 15));
		name.setTextFill(Color.WHITE);
		name.setTextAlignment(TextAlignment.CENTER);
		name.setAlignment(Pos.TOP_LEFT);
		name.setTranslateY(10);
		
		Label hp = new Label (c.getCurrentHP()+"");
		hp.setFont(Font.font("Stencil", FontWeight.BOLD, 15));
		hp.setTextFill(Color.BLACK);
		hp.setTextAlignment(TextAlignment.LEFT);
		hp.setAlignment(Pos.TOP_RIGHT);
		hp.setTranslateY(30);
		hp.setTranslateX(-20);
		
		
		
		ImageView bg = new ImageView (card);
		sp.getChildren().add(bg);
		sp.getChildren().add(name);
		sp.setAlignment(name, Pos.TOP_LEFT);
		//sp.setLayoutY(-25);
		sp.getChildren().add(hp);
		sp.setAlignment(hp, Pos.TOP_RIGHT);
		//sp.setLayoutY(-75);
		sp.getChildren().add(photo);
		sp.setAlignment(photo, Pos.CENTER);
		
		if (game != null && game.getCurrentChampion().getName().equals(c.getName()))
		{
			Label ap = new Label ("AP:\n"+ c.getCurrentActionPoints());
			ap.setFont(Font.font("Stencil", FontWeight.BOLD, 15));
			ap.setTextFill(Color.WHITE);
			ap.setTextAlignment(TextAlignment.CENTER);
			sp.getChildren().add(ap);
			sp.setAlignment(Pos.BOTTOM_LEFT);
		}
		
//		if (firstPlayer != null && secondPlayer != null 
//				&& (firstPlayer.getLeader()==c || secondPlayer.getLeader()==c))
//		{
//			name.setTranslateY(155);
//			hp.setTranslateY(175);
//		}
		
		return sp;
	}
	
//	public StackPane displayingChampOnBoard (Champion c, Image card, Image champ)
//	{
//		StackPane sp = new StackPane();
//		sp.setPadding(new Insets (5));
//		
//		DropShadow ds = new DropShadow();
//		ds.setOffsetY(0f);
//		ds.setColor(Color.color(0.0f, 0.0f, 0.0f));
//		
//		ImageView photo = new ImageView(champ);
//		photo.setPreserveRatio(true);
//		photo.setFitHeight(185);
//		photo.setFitWidth(135);
//		
//		Label name = new Label (c.getName());
//		name.setFont(Font.font("Stencil", FontWeight.BOLD, 15));
//		name.setTextFill(Color.WHITE);
//		name.setTextAlignment(TextAlignment.CENTER);
//		name.setTranslateY(8);
//		name.setTranslateX(3);
//		name.setEffect(ds);
//		
//		Label hp = new Label (c.getCurrentHP()+"");
//		hp.setFont(Font.font("Stencil", FontWeight.BOLD, 15));
//		hp.setTextFill(Color.BLACK);
//		hp.setTextAlignment(TextAlignment.LEFT);
//		hp.setTranslateY(27);
//		hp.setTranslateX(-27);
//		
//		
//		ImageView bg = new ImageView (card);
//		sp.getChildren().add(bg);
//		sp.getChildren().add(name);
//		sp.setAlignment(name, Pos.TOP_LEFT);
//		//sp.setLayoutY(-25);
//		sp.getChildren().add(hp);
//		sp.setAlignment(hp, Pos.TOP_RIGHT);
//		//sp.setLayoutY(-75);
//		sp.getChildren().add(photo);
//		sp.setAlignment(photo, Pos.CENTER);
//		
//		if (game != null && game.getCurrentChampion().getName().equals(c.getName()))
//		{
//			Label ap = new Label ("AP:\n"+ c.getCurrentActionPoints());
//			ap.setFont(Font.font("Stencil", FontWeight.BOLD, 15));
//			ap.setTextFill(Color.WHITE);
//			ap.setTextAlignment(TextAlignment.CENTER);
//			ap.setEffect(ds);
//			sp.getChildren().add(ap);
//			sp.setAlignment(Pos.BOTTOM_LEFT);
//		}
//		
//		return sp;
//	}
//	
	public StackPane displayInfo(Champion c)
	{
		StackPane tile = new StackPane();
		
		DropShadow ds = new DropShadow();
		ds.setOffsetY(1.0f);
		ds.setColor(Color.color(0.0f, 0.0f, 0.0f));
		
		tile.setBackground(new Background(
                new BackgroundFill(
                        new ImagePattern(
                                scroll
                        ), CornerRadii.EMPTY, Insets.EMPTY
                )));
		
		tile.setMaxHeight(925);
		tile.setMaxWidth(375);
		
		Label name = new Label (c.getName());
		name.setFont(Font.font("Stencil", FontWeight.BOLD, 20));
		name.setTextFill(Color.WHITE);
		name.setTextAlignment(TextAlignment.CENTER);
		name.setEffect(ds);
		tile.getChildren().add(name);
		tile.setAlignment(name, Pos.TOP_CENTER);
		name.setTranslateY(8);
		
		
		Label info = new Label (c.toString());
		info.setFont(Font.font("Times New Roman", FontWeight.BOLD, 15));
		info.setTextFill(Color.WHITE);
		info.setTextAlignment(TextAlignment.CENTER);
		
		tile.getChildren().addAll(info);
		tile.setVisible(false);
		
		return tile;
	}
	
	public VBox displayChampOnBoard(Player p)
	{
		VBox champions = new VBox();
		Label name = new Label (p.getName());
		name.setTextFill(Color.WHITE);
		InputStream istream = getClass().getResourceAsStream(
				"/resources/Agreloy.otf");
		Font myFont = Font.loadFont(istream, 35);
		DropShadow ds = new DropShadow();
		ds.setOffsetY(1.0f);
		ds.setColor(Color.color(0.0f, 0.0f, 0.0f));
		name.setFont(myFont);
		name.setAlignment(Pos.TOP_CENTER);
		name.setBackground(Background.EMPTY);
		name.setEffect(ds);
		
		champions.setAlignment(Pos.CENTER);
		champions.getChildren().add(name);
		
		int counter = 0;
		
		
		VBox normal = new VBox();
		normal.setAlignment(Pos.CENTER);
		VBox leader = new VBox();
		HBox all = new HBox();
		leader.setAlignment(Pos.CENTER);
		
		for (Champion c : p.getTeam())
		{
			StackPane front;
			StackPane back;
			
			Image img = p.getImages().get(counter++);
			
			StackPane sp = new StackPane();
			if (p.getLeader()==c)
			{
				front = displayingChamp(c, cardL, img);
				back = cardBack(c, cardL);
				sp.getChildren().addAll(back, front);
				leader.getChildren().add(sp);
				
			}
			else
			{
				front = displayingChamp(c, cardY, img);
				back = cardBack(c, cardY);
				sp.getChildren().addAll(back, front);
				normal.getChildren().add(sp);
			}
			
			sp.setOnMouseClicked(ee -> {
				if(front.getScaleX()==0)
				{
					turningCard(back, front);
					
				}
				else
				{
					turningCard(front, back);
				}
				//turnedCard = !turnedCard;
			});
		}
		all.setAlignment(Pos.CENTER);
		if (p == firstPlayer)
			all.getChildren().addAll(normal, leader);
		else
			all.getChildren().addAll(leader, normal);
		
		champions.getChildren().add(all);
		
		return champions;
	}
	

	public void turningCard (StackPane front, StackPane back)
	{
		
		ScaleTransition stHideFront = new ScaleTransition(Duration.millis(750), front);
		stHideFront.setFromX(1);
		stHideFront.setToX(0);

		back.setScaleX(0);

		ScaleTransition stShowBack = new ScaleTransition(Duration.millis(750), back);
		stShowBack.setFromX(0);
		stShowBack.setToX(1);

		stHideFront.setOnFinished(ee -> {
		        stShowBack.play();
		});

		stHideFront.play();
		
	}
	
	
	

	
	private static void setborder(Button bt) {

        Color[] colors = Stream.of("deeppink", "blueviolet", "steelblue", "cornflowerblue", "lightseagreen", "#6fba82", "crimson")
                .map(Color::web)
                .toArray(Color[]::new);
        
        //List<Border> list = new ArrayList<>();
        
        int mills[] = {-250};
        KeyFrame keyFrames[]  = Stream.iterate(0, i -> i+1)
                .limit(100)
                .map(i -> new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE, new Stop[]{new Stop(0, colors[i%colors.length]), new Stop(1, colors[(i+1)%colors.length])}))
                .map(lg -> new Border(new BorderStroke(lg, BorderStrokeStyle.SOLID, new CornerRadii(5), new BorderWidths(2))))
                .map(b -> new KeyFrame(Duration.millis(mills[0]+=250), new KeyValue(bt.borderProperty(), b, Interpolator.EASE_IN)))
                .toArray(KeyFrame[]::new);
        
        Timeline timeline = new Timeline(keyFrames);
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
	
	public StackPane cardBack (Champion c, Image cardI)
	{
		StackPane card = new StackPane();
		card.setPadding(new Insets (5));
		
		DropShadow ds = new DropShadow();
		ds.setOffsetY(0f);
		ds.setColor(Color.color(0.0f, 0.0f, 0.0f));
		
		Label name = new Label (c.getName());
		name.setFont(Font.font("Stencil", FontWeight.BOLD, 15));
		name.setTextFill(Color.WHITE);
		name.setTextAlignment(TextAlignment.CENTER);
		name.setTranslateY(8);
		name.setTranslateX(3);
		name.setEffect(ds);
		
		Label hp = new Label (c.getCurrentHP()+"");
		hp.setFont(Font.font("Stencil", FontWeight.BOLD, 15));
		hp.setTextFill(Color.BLACK);
		hp.setTextAlignment(TextAlignment.LEFT);
		hp.setTranslateY(30);
		hp.setTranslateX(-20);
		
		ImageView bg = new ImageView (cardI);
		card.getChildren().add(bg);
		card.getChildren().add(name);
		card.setAlignment(name, Pos.TOP_LEFT);
		//card.setLayoutY(-25);
		card.getChildren().add(hp);
		card.setAlignment(hp, Pos.TOP_RIGHT);
		//card.setLayoutY(-75);
		
		Label info = new Label ();
		if (c == game.getCurrentChampion())
			{info.setText(c.displayPlaying());
			}
		else
		{
			info.setText(c.displayMe4Playing());
		}
		info.setFont(Font.font("Times New Roman", FontWeight.BOLD, 15));
		info.setTextFill(Color.BLACK);
		info.setTextAlignment(TextAlignment.CENTER);
		
//		if (firstPlayer != null && secondPlayer != null 
//				&& (firstPlayer.getLeader()==c || secondPlayer.getLeader()==c))
//		{
//			name.setTranslateY(155);
//			hp.setTranslateY(175);
//		}
		
		card.getChildren().add(info);
		card.setAlignment(info, Pos.CENTER);
		
		return card;
	}
	
	
	public static void main(String args[]) {
		castAbility.setBackground(Background.EMPTY);
		setborder(castAbility);
		castAbility.setFont(Font.font("Stencil", FontWeight.BOLD, 20));
		castAbility.setTextFill(Color.WHITE);
		setborder(useLeaderAbility);
		useLeaderAbility.setBackground(Background.EMPTY);
		useLeaderAbility.setFont(Font.font("Stencil", FontWeight.BOLD, 20));
		useLeaderAbility.setTextFill(Color.WHITE);
		launch(args);
	}
}