package uk.ac.bristol.util;

import org.eclipse.jgit.lib.ProgressMonitor;

import javafx.scene.control.ProgressBar;

public class ProgressBarMonitor implements ProgressMonitor {

  private ProgressBar progressBar;
  private int tasks;
  private int tasksComplete;

  public ProgressBarMonitor(final ProgressBar progressBar) {
    this.progressBar = progressBar;
    tasksComplete = 0;
  }

  @Override
  public void start(final int totalTasks) {
    tasks = totalTasks;
    progressBar.setProgress(0F);
  }

  @Override
  public void beginTask(String title, int totalWork) {
  }

  @Override
  public void update(int completed) {
    tasksComplete += completed;
    if (tasks == 0) {
      progressBar.setProgress(1);
      return;
    }
    progressBar.setProgress(tasksComplete / tasks);
  }

  @Override
  public void endTask() {
  }

  @Override
  public boolean isCancelled() {
    return false;
  }
  
}
