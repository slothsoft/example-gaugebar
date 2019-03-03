package de.slothsoft.gaugebar;

import java.util.concurrent.CountDownLatch;

import javax.swing.SwingUtilities;

import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;

/**
 * A JUnit {@link Rule} for running tests on the JavaFX thread and performing JavaFX
 * initialisation. To include in your test case, add the following code:
 *
 * <pre>
 * {@literal @}Rule
 * public JavaFXThreadingRule jfxRule = new JavaFXThreadingRule();
 * </pre>
 *
 * copied from http://andrewtill.blogspot.de/2012/10/junit-rule-for-javafx-controller
 * -testing.html
 *
 * @author Andy Till
 */

public class JavaFXThreadingRule implements TestRule {
	/**
	 * Flag for setting up the JavaFX, we only need to do this once for all tests.
	 */
	private static boolean jfxIsSetup;
	private static boolean jfxIsForbidden;

	@Override
	public Statement apply(Statement statement, Description description) {
		return new OnJFXThreadStatement(statement);
	}

	private static class OnJFXThreadStatement extends Statement {
		private final Statement statement;

		public OnJFXThreadStatement(Statement aStatement) {
			this.statement = aStatement;
		}

		private Throwable rethrownException = null;

		@Override
		public void evaluate() throws Throwable {
			if (!jfxIsSetup) {
				setupJavaFX();
				jfxIsSetup = true;
			}
			if (jfxIsForbidden) return;

			final CountDownLatch countDownLatch = new CountDownLatch(1);
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					try {
						OnJFXThreadStatement.this.statement.evaluate();
					} catch (final Throwable e) {
						OnJFXThreadStatement.this.rethrownException = e;
					}
					countDownLatch.countDown();
				}
			});
			countDownLatch.await();

			// if an exception was thrown by the statement during evaluation,
			// then re-throw it to fail the test
			if (this.rethrownException != null) throw this.rethrownException;
		}

		protected void setupJavaFX() throws InterruptedException {
			final long timeMillis = System.currentTimeMillis();
			final CountDownLatch latch = new CountDownLatch(1);
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					try {
						// initializes JavaFX environment
						new JFXPanel();
					} catch (final UnsupportedOperationException e) {
						// we are on a headless system (-> travis), so we are not allowed
						// to execute JavaFX
						jfxIsForbidden = true;
					}
					latch.countDown();
				}
			});
			System.out.println("javafx initialising...");
			latch.await();
			System.out.println("javafx is initialised in " + (System.currentTimeMillis() - timeMillis) + "ms");
		}
	}
}