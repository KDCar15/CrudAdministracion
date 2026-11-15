package ni.edu.uam.administracionfx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import ni.edu.uam.administracionfx.dao.IUsuarioDao;
import ni.edu.uam.administracionfx.dao.UsuarioDao;
import ni.edu.uam.administracionfx.model.Usuario;

import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {

    private final IUsuarioDao dao = new UsuarioDao();

    // Campos del CRUD
    @FXML
    private TextField tf_pnombre;

    @FXML
    private TextField tf_snombre;

    @FXML
    private TextField tf_papellido;

    @FXML
    private TextField tf_sapellido;

    @FXML
    private TextField tf_correo;

    @FXML
    private TextField tf_usuario;

    @FXML
    private TextField tf_password;

    // TableView y columnas
    @FXML
    private TableView<Usuario> tv_usuarios;

    @FXML
    private TableColumn<Usuario, String> tc_pnombre;

    @FXML
    private TableColumn<Usuario, String> tc_snombre;

    @FXML
    private TableColumn<Usuario, String> tc_papellido;

    @FXML
    private TableColumn<Usuario, String> tc_sapellido;

    @FXML
    private TableColumn<Usuario, String> tc_correo;

    @FXML
    private TableColumn<Usuario, String> tc_usuario;

    @FXML
    private TableColumn<Usuario, String> tc_password;

    private boolean verificarCampos() {
        return !tf_pnombre.getText().isBlank()
                && !tf_snombre.getText().isBlank()
                && !tf_papellido.getText().isBlank()
                && !tf_sapellido.getText().isBlank()
                && !tf_correo.getText().isBlank()
                && !tf_usuario.getText().isBlank()
                && !tf_password.getText().isBlank();
    }

    @FXML
    public void agregar(ActionEvent actionEvent) {

        if (!verificarCampos()) {
            mostrarAlerta("Campos incompletos",
                    "Completa los campos obligatorios.");
            return;
        }

        Usuario u = new Usuario();

        u.setPrimerNombre(tf_pnombre.getText());
        u.setSegundoNombre(tf_snombre.getText());
        u.setPrimerApellido(tf_papellido.getText());
        u.setSegundoApellido(tf_sapellido.getText());
        u.setCorreo(tf_correo.getText());
        u.setUsuario(tf_usuario.getText());
        u.setPassword(tf_password.getText());

        dao.save(u);

        refrescar();
        limpiar();
    }


    // Actualizar usuario seleccionado
    @FXML
    public void actualizar(ActionEvent actionEvent) {

        Usuario seleccionado = tv_usuarios.getSelectionModel()
                .getSelectedItem();

        if (seleccionado == null) {
            mostrarAlerta("Sin selección",
                    "Selecciona un usuario de la tabla para actualizarlo.");
            return;
        }

        if (!verificarCampos()) {
            mostrarAlerta("Campos incompletos",
                    "Completa los campos obligatorios.");
            return;
        }

        // Modificamos el objeto seleccionado, conservando su ID.
        seleccionado.setPrimerNombre(tf_pnombre.getText());
        seleccionado.setSegundoNombre(tf_snombre.getText());
        seleccionado.setPrimerApellido(tf_papellido.getText());
        seleccionado.setSegundoApellido(tf_sapellido.getText());
        seleccionado.setCorreo(tf_correo.getText());
        seleccionado.setUsuario(tf_usuario.getText());

        // Solo se cambia la contraseña si se introduce una nueva.
        if (!tf_password.getText().isBlank()) {
            seleccionado.setPassword(tf_password.getText());
        }

        Usuario actualizado = dao.update(seleccionado);

        if (actualizado != null) {
            refrescar();
            limpiar();
            mostrarAlerta("Actualización",
                    "El usuario se actualizó correctamente.");
        } else {
            mostrarAlerta("Error",
                    "No se pudo actualizar el usuario.");
        }
    }

    @FXML
    public void eliminar() {

        Usuario seleccionado = tv_usuarios.getSelectionModel()
                .getSelectedItem();

        if (seleccionado == null) {
            mostrarAlerta("Sin selección",
                    "Selecciona un usuario para eliminarlo.");
            return;
        }

        dao.delete(seleccionado);

        refrescar();
        limpiar();
    }

    @FXML
    private void refrescar() {

        ObservableList<Usuario> usuarios =
                FXCollections.observableArrayList(dao.listAll());

        tv_usuarios.setItems(usuarios);
    }

    @FXML
    private void limpiar() {

        tf_pnombre.clear();
        tf_snombre.clear();
        tf_papellido.clear();
        tf_sapellido.clear();
        tf_correo.clear();
        tf_usuario.clear();
        tf_password.clear();
        tv_usuarios.getSelectionModel().clearSelection();
    }

    private void cargarUsuario(Usuario usuario) {
        if (usuario == null) {
            return;
        }

        tf_pnombre.setText(usuario.getPrimerNombre());
        tf_snombre.setText(usuario.getSegundoNombre());
        tf_papellido.setText(usuario.getPrimerApellido());
        tf_sapellido.setText(usuario.getSegundoApellido());
        tf_correo.setText(usuario.getCorreo());
        tf_usuario.setText(usuario.getUsuario());
        tf_password.setText(usuario.getPassword());
    }

    private void mostrarAlerta(String titulo, String mensaje) {

        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        tc_pnombre.setCellValueFactory(
                new PropertyValueFactory<>("primerNombre"));

        tc_snombre.setCellValueFactory(
                new PropertyValueFactory<>("segundoNombre"));

        tc_papellido.setCellValueFactory(
                new PropertyValueFactory<>("primerApellido"));

        tc_sapellido.setCellValueFactory(
                new PropertyValueFactory<>("segundoApellido"));

        tc_correo.setCellValueFactory(
                new PropertyValueFactory<>("correo"));

        tc_usuario.setCellValueFactory(
                new PropertyValueFactory<>("usuario"));

        tc_password.setCellValueFactory(
                new PropertyValueFactory<>("password"));

        // Escuchar cambios en la selección del TableView
        tv_usuarios.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, anterior, seleccionado) -> {
                    if (seleccionado != null) {
                        cargarUsuario(seleccionado);
                    }
                });

        refrescar();
    }
}