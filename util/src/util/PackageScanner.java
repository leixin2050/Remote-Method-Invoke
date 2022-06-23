package util;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public abstract class PackageScanner {

	public PackageScanner() {
	}
	
	public abstract void dealClass(Class<?> klass);


	public void scanPackage(String packageName) 
			throws URISyntaxException, ClassNotFoundException, IOException {
		String pathName = packageName.replace('.', '/');
		URL url = Thread.currentThread().getContextClassLoader().getResource(pathName);
		
		String protocol = url.getProtocol();
		if (protocol.equals("jar")) {
			dealJar(packageName, url);
		} else {
			File root = new File(url.toURI());
			dealDir(packageName, root);
		}
	}

	//循环递归处理jar包
	private void dealJar(String packageName, URL url) throws IOException, ClassNotFoundException {
		JarURLConnection connection = (JarURLConnection) url.openConnection();
		JarFile jarFile = connection.getJarFile();
		Enumeration<JarEntry> entries = jarFile.entries();
		while (entries.hasMoreElements()) {
			JarEntry entry = entries.nextElement();
			String entryName = entry.getName();
			if (!entryName.endsWith(".class")) {
				continue;
			}
			entryName = entryName.replace(".class", "");
			String className = entryName.replace("/", ".");
			if (!className.startsWith(packageName)) {
				continue;
			}
			Class<?> klass = Class.forName(className);
			dealClass(klass);
		}
	}

	//循环处理package内的所有类
	private void dealDir(String packageName, File curDir) throws ClassNotFoundException {
		File[] files = curDir.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				String dirName = file.getName();
				dealDir(packageName + "." + dirName, file);
			} else {
				String fileName = file.getName();
				if (fileName.endsWith(".class")) {
					String className = fileName.replace(".class", "");
					className = packageName + "." + className;
					Class<?> klass = Class.forName(className);
					dealClass(klass);
				}
			}
		}
	}
	
}
