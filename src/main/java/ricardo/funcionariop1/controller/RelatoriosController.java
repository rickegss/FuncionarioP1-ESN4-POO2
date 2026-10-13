
package ricardo.funcionariop1.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

public class RelatoriosController {

    @FXML
    private ComboBox<String> cbTipoRelatorio;

    @FXML
    private VBox areaFiltros;

    @FXML
    private Button btnGerarRelatorio;

    @FXML
    private TextArea txtResultado;

    @FXML
    private Label lblResultado;
}
