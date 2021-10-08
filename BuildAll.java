import java.util.List;
import java.lang.ProcessBuilder;
import java.util.concurrent.TimeUnit;
import java.io.IOException;

// Look Ma, Written without IDE !

class BuildAll {


	public static void main( String... args) {
		var dirs = List.of("spring-gateway-java", "spring-gateway-kotlin",
			"seller-service", "supplier-service",
			"spring-discovery", "partner-service");
		dirs.stream()
		    .map(s -> BuildAll.mavenVerify(s, false))
		    .map(BuildAll::buildImage)
		    .peek(s -> System.out.println("verified & build image for " + s))
		    .toList();
		System.out.println("Finished");
	}

	static String mavenVerify(String dir, boolean verbose)  {
		try {
			var processBuilder = new ProcessBuilder();
			if (verbose) {
			    processBuilder.inheritIO(); 
			}
			processBuilder.command("zsh", "-c", "cd " + dir + " && mvn clean verify");
			System.out.println("Executing mvn clean verify in " + dir);
			var process = processBuilder.start();
			if (process.waitFor(5, TimeUnit.MINUTES)) {
				if (process.exitValue() == 0) {
					System.out.println("Process succeeded");
				} else {
					System.out.println("Process failed with exit code " + process.exitValue());
				}
			} else {
				System.out.println("The process did not finish in the given time (5 minutes)");
			}
		} catch (IOException|InterruptedException e) {
			throw new RuntimeException(e);
		}
		return dir;
	}

	static String buildImage(String dir) {

		return dir;
	}

}