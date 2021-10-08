import java.util.List;
import java.lang.ProcessBuilder;
import java.util.concurrent.TimeUnit;
import java.io.IOException;
import java.util.Arrays;

// Look Ma, Written without IDE !

class BuildAll {


	public static void main( String... args) {
		var dirs = List.of(
			"spring-gateway-java", "spring-gateway-kotlin",
			"seller-service", "supplier-service",
			"spring-discovery", "partner-service"
			);
		
		var verbose = Arrays.stream(args).anyMatch("-v"::equals);
		if (Arrays.stream(args).anyMatch("-h"::equals)) {
			System.out.println("Options:\n-v verbose\n-h Help\n--dont-clean do not clean when running mvn verify");
		}
		var clean = !Arrays.stream(args).anyMatch("--dont-clean"::equals);

		var builder = new Builder(verbose, clean);
		dirs.stream()
		    .map(builder::mavenVerify)
		    .map(builder::buildImage)
			.map(s -> "verified & build image for " + s)
		    .forEach(System.out::println);
		System.out.println("Finished");
	}
}
class Builder {
	private final boolean verbose;
	private final boolean clean;
	private static final String MAVEN_CLEAN_VERIFY="mvn clean verify";
	private static final String MAVEN_VERIFY="mvn clean verify";
	private static final String MAVEN_BUILD_IMAGE="mvn spring-boot:build-image";

	Builder(boolean verbose, boolean clean) { this.verbose = verbose; this.clean = clean; }

	String mavenVerify(String dir)  {
		try {
			if (clean) {
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
		var processBuilder = new ProcessBuilder();
		if (verbose) {
			processBuilder.inheritIO(); 
		}
		// processBuilder.command("zsh", "-c", "cd " + dir + " && " command);
		System.out.println("cmd is " + "cd %s  %s".formatted(dir, command));
		processBuilder.command("zsh", "-c", "cd %s && %s".formatted(dir, command));
		System.out.println("Executing %s in %s".formatted(command, dir));
		var process = processBuilder.start();
		if (process.waitFor(5, TimeUnit.MINUTES)) {
			if (process.exitValue() == 0) {
				System.out.println("Process succeeded");
			} else {
				System.out.println("Process failed with exit code " + process.exitValue() + ". Try again with the -v option.");
			}
		} else {
			System.out.println("The process did not finish in the given time (5 minutes)" + ". Try again with the -v option.");
		}
	}

}