package gravitysandbox.gui;

import gravitysandbox.physics.BodyContainer;
import gravitysandbox.util.XMLEngine;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.IOException;

/**
 * Manages the menu bar at the top of the {@link MainFrame}.
 *
 * @author Christoph Bruckner
 * @version 1.0
 * @since 1.0
 */
public class MainMenuBar extends JMenuBar {

    /**
     * Represents the menu "File".
     */
    private JMenu fileMenu;

    /**
     * Represents the menu "Edit".
     */
    private JMenu editMenu;

    /**
     * Represents the menu "Simulation".
     */
    private JMenu simulationMenu;

    /**
     * Represents the menu item "Start/Stop Simulation" within the menu "Simulation".
     */
    private JMenuItem simMenuStart;

    /**
     * The parent window.
     */
    private MainFrame parent;

    /**
     * Creates a new menu bar for the parent window.
     * @param parent The parent window.
     */
    public MainMenuBar(MainFrame parent) {

        this.parent = parent;

        fileMenu = new JMenu("File");

        JMenuItem fileMenuNew = new JMenuItem("New system");
        fileMenuNew.addActionListener(e -> {
            BodyContainer.getInstance().clear();
            parent.resetView();
            parent.update();
        });
        fileMenu.add(fileMenuNew);

        JMenuItem filMenuOpen = new JMenuItem("Open");
        filMenuOpen.addActionListener(e -> {
            if (parent.isSimualtionRunnig()) {
                parent.toogleSimulation();
            }
            JFileChooser chooser = new JFileChooser();
            FileNameExtensionFilter extensionFilter = new FileNameExtensionFilter("XML files", "xml");
            chooser.setFileFilter(extensionFilter);
            chooser.setDialogTitle("Open");

            int returnVal = chooser.showOpenDialog(parent);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                try {
                    XMLEngine.load(chooser.getSelectedFile().getAbsolutePath());
                    parent.resetView();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null,
                            "Could not load system from file \"" + chooser.getSelectedFile().getName() + "\"!\r\nError message: " + ex.getMessage(),
                            "Error loading system",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
            parent.update();
        });
        fileMenu.add(filMenuOpen);

        JMenuItem filMenuSaveAs = new JMenuItem("Save as");
        filMenuSaveAs.addActionListener(e -> {
            if (parent.isSimualtionRunnig()) {
                parent.toogleSimulation();
            }
            JFileChooser chooser = new JFileChooser();
            FileNameExtensionFilter extensionFilter = new FileNameExtensionFilter("XML files", "xml");
            chooser.setFileFilter(extensionFilter);
            chooser.setDialogTitle("Save as");

            int returnVal = chooser.showOpenDialog(parent);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                try {
                    XMLEngine.save(chooser.getSelectedFile().getAbsolutePath());
                    parent.resetView();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null,
                            "Could not save system to file \"" + chooser.getSelectedFile().getName() + "\"!\r\nError message: " + ex.getMessage(),
                            "Error saving system",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
            parent.update();
        });
        fileMenu.add(filMenuSaveAs);

        fileMenu.addSeparator();

        JMenuItem fileMenuClose = new JMenuItem("Exit");
        fileMenuClose.addActionListener(e -> parent.close());
        fileMenu.add(fileMenuClose);

        editMenu = new JMenu("Edit");

        JMenuItem addBody = new JMenuItem("Add body");
        addBody.addActionListener(e -> new AddBodyDialog(parent));
        editMenu.add(addBody);

        JMenuItem editBody = new JMenuItem("Edit body");
        editBody.addActionListener(e -> new EditBodyDialog(parent, parent.getSelectedBody()));
        editMenu.add(editBody);

        JMenuItem removeBody = new JMenuItem("Remove body");
        removeBody.addActionListener(e-> new RemoveBodyDialog(parent));
        editMenu.add(removeBody);

        simulationMenu = new JMenu("Simulation");

        simMenuStart = new JMenuItem("Start Simulation");
        simMenuStart.addActionListener(e -> {
            updateMenuItems();
            parent.toogleSimulation();
        });

        simulationMenu.add(simMenuStart);

        this.add(fileMenu);
        this.add(editMenu);
        this.add(simulationMenu);
    }

    /**
     * Updates the text for simMenuStart.
     */
    void updateMenuItems() {
        if (!parent.isSimualtionRunnig())
            simMenuStart.setText("Stopp Simulation");
        else
            simMenuStart.setText("Start Simulation");
    }
}
