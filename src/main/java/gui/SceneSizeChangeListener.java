package gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Scale;

/**
 * class aids the GUI
 * used when the scene size changes
 */
public class SceneSizeChangeListener implements ChangeListener<Number> {
  /**
   * a scene
   */
  private final Scene scene;

  /**
   * the ratio
   */
  private final double ratio;

  /**
   * the initial height of the scene
   */
  private final double initHeight;

  /**
   * the inital width of the scene
   */
  private final double initWidth;

  /**
   * the content pane
   */
  private final Pane contentPane;

  /**
   * creates a new SceneSizeChangeListener object
   *
   * @param scene       Scene
   * @param ratio       double
   * @param initHeight  double
   * @param initWidth   double
   * @param contentPane Pane
   */
  public SceneSizeChangeListener(Scene scene, double ratio, double initHeight, double initWidth, Pane contentPane) {
    this.scene = scene;
    this.ratio = ratio;
    this.initHeight = initHeight;
    this.initWidth = initWidth;
    this.contentPane = contentPane;
  }

  /**
   * waits for changes in the scene size
   *
   * @param observableValue The {@code ObservableValue} which value changed
   * @param oldValue        The old value
   * @param newValue        The new value
   */
  @Override
  public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
    final double newWidth = scene.getWidth();
    final double newHeight = scene.getHeight();

    double scaleFactor =
        newWidth / newHeight > ratio
            ? newHeight / initHeight
            : newWidth / initWidth;

    if (scaleFactor != 1) {
      Scale scale = new Scale(scaleFactor, scaleFactor);
      scale.setPivotX(0);
      scale.setPivotY(0);

      scene.getRoot().getTransforms().setAll(scale);

      contentPane.setPrefWidth(newWidth / scaleFactor);
      contentPane.setPrefHeight(newHeight / scaleFactor);
    } else {
      contentPane.setPrefWidth(Math.max(initWidth, newWidth));
      contentPane.setPrefHeight(Math.max(initHeight, newHeight));
    }
  }
}