import java.util.List;
import java.lang.ProcessBuilder;
import java.util.concurrent.TimeUnit;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

// Look Ma, Written without IDE !

class BuildAll {


	public static void main( String... args) {
		var dirs = List.of(
			"spring-gateway-java", "spring-gateway-kotlin",
			"seller-service", "supplier-service",
			"spring-discovery", "partner-service"
			);
		
	
		var opts = Options.parse(args);
		var builder = new Builder(opts);

		System.out.println("Current options: " + opts);
		
		dirs.stream()
		    .map(builder::mavenVerify)
		    .map(builder::buildImage)
			.map(s -> "verified & build image for " + s)
		    .forEach(System.out::println);
		System.out.println("Finished");
	}
}

record Options(boolean verbose, boolean quitOnError, boolean clean) {
	private static final Map<String, String> opts = Map.of(
		"-h", "Help",
		"-v", "Verbose - print maven output",
		"-q", "Quit on error - Stop the build when something fails",
		"--dont-clean", "Run maven without clean"
	);

	static Options parse(String ... args) {
		if (Arrays.stream(args).anyMatch("-h"::equals)) {
			System.out.println("Options:");
			opts.keySet().stream()
				.forEach( (k) -> System.out.println("%s : %s".formatted(k, opts.get(k))));
		}

		var _verbose = Arrays.stream(args).anyMatch("-v"::equals);
		var _quitOnError = Arrays.stream(args).anyMatch("-q"::equals);
		var _clean = !Arrays.stream(args).anyMatch("--dont-clean"::equals);

		return new Options(_verbose,  _quitOnError, _clean);
	}
}


class Builder {
	private Options options;
	private static final String MAVEN_CLEAN_VERIFY="mvn clean verify";
	private static final String MAVEN_VERIFY="mvn verify";
	private static final String MAVEN_BUILD_IMAGE="mvn spring-boot:build-image";
	private static File NULL_FILE = new File(
          (System.getProperty("os.name")
                     .startsWith("Windows") ? "NUL" : "/dev/null")
   );

	Builder(Options options) {this.options = options; }

	String mavenVerify(String dir)  {
		try {
			if (options.clean()) {
				runProcess(dir, MAVEN_CLEAN_VERIFY);
			} else {
				runProcess(dir, MAVEN_VERIFY);
			}
		} catch (IOException|InterruptedException e) {
			throw new RuntimeException(e);
		}
		return dir;
	}

	String buildImage(String dir) {
		try {
			runProcess(dir, MAVEN_BUILD_IMAGE);
		} catch (IOException|InterruptedException e) {
			throw new RuntimeException(e);
		}
		return dir;
	}

	private void runProcess(String dir, String command) throws IOException, InterruptedException {
		var process = createProcess(dir, command);

		if (process.waitFor(5, TimeUnit.MINUTES)) {
			if (process.exitValue() == 0) {
				System.out.println("Process succeeded");
			} else {
				if (options.quitOnError()) {
						System.exit(1);
				} 
				System.out.println("Process failed with exit code " + process.exitValue() + ". Try again with the -v option.");
			}
		} else {
			System.out.println("The process '" + command + "' in '" + dir + "' did not finish in the given time (5 minutes)" + ". Try again with the -v option.");
			if (options.quitOnError()) {
				System.exit(1);
			}
		}
	}
	private void createProcess(String dir, String command) {
		var processBuilder = new ProcessBuilder();		
		if (options.verbose()) {
			processBuilder.inheritIO(); 
		} else {
			processBuilder.redirectErrorStream(true);
			processBuilder.redirectOutput(NULL_FILE);
		}
		processBuilder.command("zsh", "-c", "cd %s && %s".formatted(dir, command));
		return processBuilder.start();
	}
}