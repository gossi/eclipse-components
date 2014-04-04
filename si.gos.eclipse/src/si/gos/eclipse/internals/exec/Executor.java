package si.gos.eclipse.internals.exec;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.LogOutputStream;
import org.apache.commons.exec.PumpStreamHandler;

import si.gos.eclipse.exec.Environment;
import si.gos.eclipse.exec.ExecutionResponseListener;

/**
 * Executes a script using a specific {@link Environment}.
 * 
 * 
 */
public class Executor {

	private DefaultExecutor executor;
	private ExecuteWatchdog watchdog;
	
	private PumpStreamHandler streamHandler;
	
	private LogOutputStream outStream;
	private LogOutputStream errStream;
	
	private StringBuilder outBuilder;
	private StringBuilder errBuilder;

	private int timeout = 60000;

	private Set<ExecutionResponseListener> listeners = new HashSet<ExecutionResponseListener>();
	
	private DefaultExecuteResultHandler handler = new DefaultExecuteResultHandler() {
		
		public void onProcessComplete(int exitValue) {
			super.onProcessComplete(exitValue);
			for (ExecutionResponseListener handler : listeners) {
				handler.executionFinished(outBuilder.toString(), exitValue);
			}
		}

		public void onProcessFailed(ExecuteException e) {
			String response = errBuilder.toString();
			String stdOutResponse = outBuilder.toString();
			
			super.onProcessFailed(e);
			
			for (ExecutionResponseListener handler : listeners) {
				
				if (stdOutResponse.length() > 0) {
					response = stdOutResponse;
				}
				
				handler.executionFailed(response, e);
			}
		}
	};

	public Executor() {
		
		outBuilder = new StringBuilder();
		errBuilder = new StringBuilder();
		
		errStream = new LogOutputStream() {
			@Override
			protected void processLine(String line, int level) {
				if (!line.isEmpty()) {
					errBuilder.append(line + "\n");
					for (ExecutionResponseListener listener : listeners) {
						listener.executionMessage(line);
					}
				}
			}
		};
		
		outStream = new LogOutputStream() {
			@Override
			protected void processLine(String line, int level) {
				if (!line.isEmpty()) {
					outBuilder.append(line + "\n");
					for (ExecutionResponseListener listener : listeners) {
						listener.executionMessage(line);
					}
				}
			}
		};
		
		streamHandler = new PumpStreamHandler(outStream, errStream);
		
		executor = new DefaultExecutor();
		executor.setStreamHandler(streamHandler);

	}

	public void addResponseListener(ExecutionResponseListener listener) {
		listeners.add(listener);
	}

	public void removeResponseListener(ExecutionResponseListener listener) {
		listeners.remove(listener);
	}

	public void setTimeout(long timeout) {
		watchdog = new ExecuteWatchdog(timeout);
		executor.setWatchdog(watchdog);
	}
	
	public void setExitValue(int exitValue) {
		executor.setExitValue(exitValue);
	}

	public void execute(String cmd) throws ExecuteException, IOException, InterruptedException {
		execute(CommandLine.parse(cmd));
	}

	public void execute(CommandLine cmd) {
		
		try {
			for (ExecutionResponseListener handler : listeners) {
				handler.executionAboutToStart();
			}
			
//			Logger.debug("executing command using executable: " + cmd.getExecutable());
			executor.setExitValue(0);
			executor.execute(cmd, handler);
			
			for (ExecutionResponseListener handler : listeners) {
				handler.executionStarted();
			}
			
			handler.waitFor();
		} catch (Exception e) {
			for (ExecutionResponseListener handler : listeners) {
				handler.executionFailed("", e);
			}
		}
	}
	
	public void abort() {
		if (watchdog != null) {
			executor.setExitValues(new int[]{0, 143}); // we abort, so it's ok with 1 as exit value
            watchdog.destroyProcess();
        }
	}
	
	public void setWorkingDirectory(File dir) {
		executor.setWorkingDirectory(dir);
	}
	
	public File getWorkingDirectory() {
		return executor.getWorkingDirectory();
	}
	
	public void setTimeout(int timeout) {
		
		if (timeout < 0) {
			throw new IllegalArgumentException("Timeout cannot be negative");
		}
		
		this.timeout = timeout;
		
		watchdog = new ExecuteWatchdog(timeout);
		executor.setWatchdog(watchdog);
	}
	
	public int getTimeout() {
		return timeout;
	}
}
