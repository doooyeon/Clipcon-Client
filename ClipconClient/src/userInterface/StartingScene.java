package userInterface;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.websocket.EncodeException;

import controller.ClipboardController;
import controller.Endpoint;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import model.Message;

@Getter
@Setter
public class StartingScene implements Initializable {
	
	private UserInterface ui = UserInterface.getIntance();
	
	@FXML private Button createBtn;
	@FXML private Button joinBtn;
	
	private static ActionEvent event;
	
	private Endpoint endpoint = Endpoint.getIntance();
	
	/**
	 * flag variable for checking it is initialize (success about login)
	 */
	private boolean createGroupSuccessFlag;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ui.setStartingScene(this);
		createGroupSuccessFlag = false;
		
		createBtn.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event) {
				StartingScene.event = event;
				
				System.out.println("�׷����");
				
				//showMainView();//////////////////////////////////////�ӽ�
				
				// ������ REQUEST_REQUEST_CREATE_GROUP Messgae ����
				Message createGroupMsg = new Message().setType(Message.REQUEST_CREATE_GROUP);
				try {
					endpoint.sendMessage(createGroupMsg);
				} catch (IOException | EncodeException e) {
					e.printStackTrace();
				}
				
				// run scheduler for checking
				final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

				scheduler.scheduleAtFixedRate(new Runnable() {
					@Override
					public void run() {
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								// if flag turn on then client login game
								if (createGroupSuccessFlag) {
									createGroupSuccessFlag = false;
									showMainView();
									return;
								}
							}
						});

					}
				}, 50, 50, TimeUnit.MILLISECONDS);
			}
		});
		
		joinBtn.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event) {
				StartingScene.event = event;
				
				showGroupJoinView();
			}
		});
	}
	
	public void showMainView() {
		try {				
			Parent toMain = FXMLLoader.load(getClass().getResource("/view/MainView.fxml"));
			Scene mainScene = new Scene(toMain);
			Stage mainStage = (Stage) ((Node) StartingScene.event.getSource()).getScene().getWindow();
			
			mainStage.hide();
			mainStage.setScene(mainScene);
			mainStage.show();
			
			startHookProcess(); // Ű���� ��ŷ ����
					
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void showGroupJoinView() {
		try {
			Parent groupJoin = FXMLLoader.load(getClass().getResource("/view/GroupJoinView.fxml"));
			Scene groupJoinScene = new Scene(groupJoin);
			Stage tempStage = new Stage();
			tempStage.setScene(groupJoinScene);
			tempStage.setResizable(false);
			tempStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void startHookProcess() {
		hookManager.GlobalKeyboardHook hook = new hookManager.GlobalKeyboardHook();
		int vitrualKey = KeyEvent.VK_H;
        boolean CTRL_Key = true;
        boolean ALT_Key = true;
        boolean SHIFT_Key = false;
        boolean WIN_Key = false;
		
        hook.setHotKey(vitrualKey, ALT_Key, CTRL_Key, SHIFT_Key, WIN_Key);
        hook.startHook();
        // waiting for the event
        hook.addGlobalKeyboardListener(new hookManager.GlobalKeyboardListener() {
            public void onGlobalHotkeysPressed() {
                System.out.println("CTRL + ALT + H was pressed");
                System.out.println(ClipboardController.readClipboard());
            }
        });
	}
}