module com.tp_biblio.tp_biblio {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires java.sql;

    opens com.tp_biblio.tp_biblio to javafx.fxml;
    exports com.tp_biblio.tp_biblio;
}