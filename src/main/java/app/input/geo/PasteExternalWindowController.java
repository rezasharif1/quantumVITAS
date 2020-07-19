/*******************************************************************************
 * Copyright (c) 2020 Haonan Huang.
 *
 *     This file is part of QuantumVITAS (Quantum Visualization Interactive 
 *     Toolkit for Ab-initio Simulations).
 *
 *     QuantumVITAS is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or any 
 *     later version.
 *
 *     QuantumVITAS is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with QuantumVITAS.  If not, see <https://www.gnu.org/licenses/gpl-3.0.txt>.
 *******************************************************************************/
package app.input.geo;

import java.net.URL;
import java.util.ResourceBundle;
import agent.InputAgentGeo;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import main.MainClass;

public class PasteExternalWindowController implements Initializable{
	@FXML
    private BorderPane borderPaneMain;

    @FXML
    private Button buttonCancel,
    buttonSave,
    buttonClearAll;

    @FXML
    private TabPane tabPanePaste;

    @FXML
    private Tab tabInput;

    @FXML
    private TextArea textAreaInput;

    @FXML
    private Tab tabPreview;

    @FXML
    private TextArea textAreaPreview;
    
    @FXML
    private Label labelStatus;
    
    private MainClass mainClass;
    
    private boolean boolSave = false;
    
    private InputAgentGeo iGeoPaste = null;//***check possibility of ram leak
    
    public PasteExternalWindowController(MainClass mc) {
    	mainClass = mc;
	}
    public InputAgentGeo getGeoAgent() {
    	return iGeoPaste;
    }
    public void initializeConversion() {
    	iGeoPaste = (InputAgentGeo) mainClass.projectManager.getCurrentGeoAgent().deepCopy();//should be safe to assume geoAgent exists
    	textAreaPreview.setText("Nothing recognized yet.");
    	textAreaInput.setText("");
    	boolSave = false;
    }
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		labelStatus.setText("");
		textAreaPreview.setEditable(false);
		buttonSave.setOnAction((event) -> {	
			boolSave = true;
			closeStage();
		});
		buttonCancel.setOnAction((event) -> {	
			boolSave = false;
			closeStage();
		});
		buttonClearAll.setOnAction((event) -> {	
			iGeoPaste = new InputAgentGeo();
			textAreaPreview.setText("Everything cleared.");
	    	textAreaInput.setText("");
		});
		textAreaInput.textProperty().addListener((obs,oldText,newText)->{
			if(!checkText(newText)) {return;}
			updatePreview();
		});
	}
	private boolean checkText(String textInput) {
		if(textInput==null || textInput.isEmpty()) {return false;}

		return this.iGeoPaste.convertInfoFromInput(textInput);
	}
	private void updatePreview() {
		
		String msg = "Information read from the input:\n";
		
		msg += this.iGeoPaste.genAgentSummary();
		
		//update preview
		textAreaPreview.setText(msg);
	}
	private void closeStage() {
		textAreaInput.setText("");
		textAreaPreview.setText("");
        Stage stage  = (Stage) this.borderPaneMain.getScene().getWindow();
        stage.close();
    }
	public boolean isBoolSave() {
		return boolSave;
	}
}